/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.manager.OrderStatemachine.CmdToOther;
import com.lge.warehouse.manager.OrderStatemachine.RobotAtX;
import com.lge.warehouse.manager.OrderStatemachine.WahouseStateMachine;
import com.lge.warehouse.manager.OrderStatemachine.WMorderStatemachineState;
import com.lge.warehouse.util.InventoryName;
import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.QuantifiedWidget;
import com.lge.warehouse.util.RobotConnectionStatus;
import com.lge.warehouse.util.WarehouseConnectionStatus;
import com.lge.warehouse.supervisor.WarehouseInventoryInfo;
import com.lge.warehouse.util.WarehouseStatus;

/**
 *
 * @author kihyung2.lee
 */
public class WarehouseManagerController extends WarehouseRunnable {
    private static WarehouseManagerController sInstance = null;
    static Logger logger = Logger.getLogger(WarehouseManagerController.class);
    WarehouseInventoryInfo minventoryInfo;
    WarehouseStatus warehouseStatus = new WarehouseStatus();
    Order Handlingorder = new Order();
    private WarehouseConnectionStatus mWarehouseStatus;
    private RobotConnectionStatus mRobotStatus;
    
    WahouseStateMachine warehouseStatemachine = new WahouseStateMachine();
    NavigationPathSelector PathSelector = new NavigationPathSelector(minventoryInfo);

    static ArduinoConnector mArduinoConForWarehouse = new ArduinoConnector();
    static ArduinoConnector mArduinoConForRobot = new ArduinoConnector();
    
    private WarehouseManagerController() {
        super(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, false);
        mArduinoConForWarehouse.setSocketTimeout(3000);
        WarehouseInputMgr.start(mArduinoConForWarehouse);
        WarehouseOutputMgr.start(mArduinoConForWarehouse);
        mArduinoConForRobot.setSocketTimeout(7000);
        RobotInputMgr.start(mArduinoConForRobot);
        RobotOutputMgr.start(mArduinoConForRobot);
        mWarehouseStatus = WarehouseConnectionStatus.CONNECTION_OFF;
        mRobotStatus = RobotConnectionStatus.CONNECTION_OFF;
    }

    public static WarehouseManagerController getInstance() {
        if (sInstance == null) {
            sInstance = new WarehouseManagerController();
        }
        return sInstance;
    }
    
    
    //for SupervisorUI and Inventory update.
    public void SendWarehouseStatus() // have some problem to navigation path selector.
    {
    	
    	String StringFormat = "";
    	List<QuantifiedWidget> inventoryListOnBot = new ArrayList<QuantifiedWidget>();
    	List<WMorderStatemachineState> PathList = new ArrayList<WMorderStatemachineState>();
    	List<String> VisitedStationList = new ArrayList<String>();
    	
    	PathList = warehouseStatemachine.getCurrentState().getPassedNavigationPath();
    	//visited Station information
    	for(WMorderStatemachineState pa : PathList)
		{
    		VisitedStationList.add(pa.toString());
    		if(pa instanceof RobotAtX) // for robot holding widget lsit.
    		{
    			for(QuantifiedWidget qw : ((RobotAtX) pa).getQwOrderList())
    			{
    				inventoryListOnBot.add(qw);
    			}
    		}
	 	}
    	    	
    	warehouseStatus.setVisitedStationListOfBot(VisitedStationList);
    	warehouseStatus.setInventoryListOfBot(inventoryListOnBot);
		warehouseStatus.setLocationOfBot(warehouseStatemachine.getCurrentState().toString());
		
		// for remain path list.
		PathList = warehouseStatemachine.getCurrentState().getNavigationPath();
		StringFormat = "NULL";
		for(WMorderStatemachineState pa : PathList)
		{
			if(pa instanceof RobotAtX)
			{
				StringFormat = pa.toString();
				break;
			}
		}
		
		warehouseStatus.setNextStop(StringFormat);
		warehouseStatus.setWarehouseInventoryInfo(minventoryInfo);
		warehouseStatus.setRobotStatus(mRobotStatus);
		warehouseStatus.setWarehouseStatus(mWarehouseStatus);
				
		sendWarehouseStatus(warehouseStatus);
		
    }
    

    
    

    @Override
    protected void eventHandle(EventMessage event) {
    	CmdToOther Cmd = CmdToOther.CMD_NONE;
		switch(event.getType()){
		case SYSTEM_READY:
			break;
		case WAREHOUSE_INVENTORY_INFO:
			if(event.getBody() instanceof WarehouseInventoryInfo){
				minventoryInfo = (WarehouseInventoryInfo)event.getBody();
				PathSelector.SetNewInventory(minventoryInfo);
				
				logger.info("WAREHOUSE_INVENTORY_INFO: WarehouseId =" + minventoryInfo.getWarehouseId());
			 	for(InventoryName inventoryName : InventoryName.values()){
                                     //Note: Now, The InventoryName means the name of inventory stataion.  
                                    logger.info("WAREHOUSE_INVENTORY_INFO: inventoryName =" + inventoryName);
                                   if(minventoryInfo.hasInventoryStation(inventoryName)){
                                        for(QuantifiedWidget qw : minventoryInfo.getInventoryInfo(inventoryName)){
                                            logger.info(qw.getWidget()+" : "+qw.getQuantity());
                                        }   
                                    }
                                }
				//To Do : deliver the inventory information to InveontoryInfoRepository object
			}else{
				handleBodyError(event);
			}
			break;
		case FILL_ORDER:
			if(event.getBody() instanceof Order){
				//For Test [START]
				Order order = (Order)event.getBody();
				Handlingorder = order;
				logger.info("FILL_ORDER order id = "+order.getOrderId());
				for(QuantifiedWidget qw : order.getItemList()){
					logger.info(qw.getWidget()+" : "+qw.getQuantity());
			 	}
				
				// need to check Warehouse and Robot is alive.
				sendMsg(WComponentType.WAREHOUSE_OUTPUT_MGR, EventMessageType.INIT_WAREHOUSE, null);
				
				
				List<WMorderStatemachineState> newOrderPath = PathSelector.MakeNewNavigationPath(order);
				Cmd = warehouseStatemachine.Evt_NewOrder(newOrderPath);
				HandleStateMachineCmd(Cmd);
				
				/*
				// for 1 order clear without robot.
				Cmd = warehouseStatemachine.Evt_WareHouseSensorIsOn(1);
				HandleStateMachineCmd(Cmd);
				Cmd = warehouseStatemachine.Evt_WareHouseButtonIsOn(1);
				HandleStateMachineCmd(Cmd);
				Cmd = warehouseStatemachine.Evt_WareHouseSensorIsOn(2);
				HandleStateMachineCmd(Cmd);
				Cmd = warehouseStatemachine.Evt_WareHouseButtonIsOn(2);
				HandleStateMachineCmd(Cmd);
				Cmd = warehouseStatemachine.Evt_WareHouseSensorIsOn(3);
				HandleStateMachineCmd(Cmd);
				Cmd = warehouseStatemachine.Evt_WareHouseButtonIsOn(3);
				HandleStateMachineCmd(Cmd);
				Cmd = warehouseStatemachine.Evt_WareHouseSensorIsOn(4);
				HandleStateMachineCmd(Cmd);
				Cmd = warehouseStatemachine.Evt_WareHouseButtonIsOn(4);
				HandleStateMachineCmd(Cmd);
				*/
				
			}else {
                                handleBodyError(event);
			}
			break;

		case UPDATE_WAREHOUSE_STATUS:
			if(event.getBody() instanceof WarehouseStatus) 
			{
                            WarehouseStatus warehouseStatus = (WarehouseStatus)event.getBody();
                            sendWarehouseStatus(warehouseStatus);
			}
			break;
		case ROBOT_ERROR_STATUS:
			if(event.getBody() instanceof String) 
			{
				String value = (String)event.getBody();
				logger.info("ROBOT_ERROR_STATUS :" + value);
			}
			else 
			{
		        handleBodyError(event);
		    } 
		    break;
        case WAREHOUSE_LOAD_STATUS:
            if(event.getBody() instanceof String) 
            {
                String strVal = (String)event.getBody();
                int value = Integer.parseInt(strVal);
                logger.info("WAREHOUSE_LOAD_STATUS :" + value);
                //Note: if order is not processing, ignore this event    
                if(value == 0)
                {
                	//do nothing.
                }
                else
                {
                	
                	int StationNum = 0;
                	RobotAtX forInventoryUpdate;
                	List<QuantifiedWidget> QuantifiListAtBot = new ArrayList<QuantifiedWidget>();
                	
                	List<WMorderStatemachineState> stateMachineList = new ArrayList<WMorderStatemachineState>();
                	
                	List<WMorderStatemachineState> stateMachineList2 = warehouseStatemachine.getCurrentState().getPassedNavigationPath();
                	
              
                	stateMachineList.add(stateMachineList2.get(stateMachineList2.size()-1));
            		
                	Cmd = warehouseStatemachine.Evt_WareHouseButtonIsOn(value);
                	
                	if(Cmd == CmdToOther.ROBOT_MOVE_TONEXT || Cmd == CmdToOther.ORDER_COMPLETE)
                 	{
                		forInventoryUpdate = (RobotAtX)stateMachineList.get(0);
                		
                		StationNum = forInventoryUpdate.getID() - 1;
                		
                		for(QuantifiedWidget qw : forInventoryUpdate.getQwOrderList())
            			{
                			QuantifiListAtBot.add(qw);
            			}
                		
                		for(QuantifiedWidget qw : QuantifiListAtBot){
                			minventoryInfo.reduceInventoryWidget(InventoryName.fromInteger(StationNum), qw.getWidget(), qw.getQuantity());
                		}
                 	}
                	
                	
                	                	
                }
                
                HandleStateMachineCmd(Cmd);
            }
            else 
            {
                handleBodyError(event);
            } 
            break;
		case ROBOT_POSITION_STATUS:
			if(event.getBody() instanceof String)
			{
				String strVal = (String)event.getBody();
				int value = Integer.parseInt(strVal);
				logger.info("ROBOT_POSITION_STATUS :" + value);
				//Note: if order is not processing, ignore this event
				//Note: if order is not processing, ignore this event    
                if(value == 0)
                {
                	// do nothing.
                }
                else
                {
                	Cmd = warehouseStatemachine.Evt_WareHouseSensorIsOn(value);
                }
                HandleStateMachineCmd(Cmd);
			}
			else 
			{
				handleBodyError(event);
			} 
			break;
        case ROBOT_IS_CONNECTED:
        	logger.info("ROBOT_IS_CONNECTED");
        	mRobotStatus = RobotConnectionStatus.CONNECTION_ON;
        	
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	Cmd = warehouseStatemachine.Evt_RobotErrorStateChange(0);
			HandleStateMachineCmd(Cmd);
        	//SendWarehouseStatus();
        	break;
        	
        case ROBOT_IS_DISCONNECTED:
        	logger.info("ROBOT_IS_DISCONNECTED");
        	mRobotStatus = RobotConnectionStatus.CONNECTION_OFF;
        	
        	Cmd = warehouseStatemachine.Evt_RobotErrorStateChange(1);
			HandleStateMachineCmd(Cmd);
			 
        	//SendWarehouseStatus();
        	break;
        	
        case ROBOT_IS_ARRIVE:
                //Now, robot is arrived at some inventory station. Now, robot can receive the message such as moving.
                logger.info("ROBOT_IS_ARRIVE");
                
                Cmd = warehouseStatemachine.Evt_ReadyTogo();
                HandleStateMachineCmd(Cmd);

                break;
                
        case WAREHOUSE_IS_CONNECTED:
			 logger.info("WAREHOUSE_IS_CONNECTED");
			 mWarehouseStatus = WarehouseConnectionStatus.CONNECTION_ON;
			 
			 try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 
			 Cmd = warehouseStatemachine.Evt_WareHouseErrorStateChange(0);
			 HandleStateMachineCmd(Cmd);
			 
			 sendMsg(WComponentType.WAREHOUSE_OUTPUT_MGR, EventMessageType.REQUEST_LOAD_STATUS,null);
			 sendMsg(WComponentType.WAREHOUSE_OUTPUT_MGR, EventMessageType.REQUST_POS_STATUS,null);
			 
			 
			 
			 
			 //SendWarehouseStatus();
			 break;
			 
        case WAREHOUSE_IS_DISCONNECTED:
			logger.info("WAREHOUSE_IS_DISCONNECTED");
			mWarehouseStatus = WarehouseConnectionStatus.CONNECTION_OFF;
			
			Cmd = warehouseStatemachine.Evt_WareHouseErrorStateChange(1);
			HandleStateMachineCmd(Cmd);
			//SendWarehouseStatus();
	        
			break;
        case FILL_INVENTORY_WIDGET:
        	if(event.getBody() instanceof WarehouseInventoryInfo){
				WarehouseInventoryInfo warehouseInventoryInfo = (WarehouseInventoryInfo) event.getBody();
				minventoryInfo.fillInventoryInfo(warehouseInventoryInfo);
			}else{
				handleBodyError(event);
			}
			break;
		default:
			logger.info("unhandled event :"+event);
			break;
		}
    }
    
    protected void HandleStateMachineCmd(CmdToOther Cmd)
    {
    	switch(Cmd)
    	{
    		case CMD_NONE:
    			logger.info("Nothing To do");
    			break;
    		
    		case ROBOT_MOVE_TONEXT:
    			logger.info("Go robot next destanation");
    			sendMsg(WComponentType.ROBOT_OUTPUT_MGR, EventMessageType.MOVE_NEXT_INV,null);
    			break;
    			
    		case ROBOT_STOP:
    			logger.info("Stop robot by Error");
    			sendMsg(WComponentType.ROBOT_OUTPUT_MGR, EventMessageType.REQUEST_ROBOT_STOP,null);
    			
    			//Need to Stop command
    			break;
    			
    		case ORDER_COMPLETE:
    			logger.info("Order is complete");
    			
    			// inventory....
    			
    			sendMsg(WComponentType.WM_MSG_HANDLER, EventMessageType.FINISH_FILL_ORDER, Handlingorder);
    			
    			break;
    			
    	}
    	
    	SendWarehouseStatus();
    }
    

    public static void start() {
        logger.info("Controller start");
        new Thread(getInstance()).start();
    }

    @Override
    protected void initBus() {
        addBus(WComponentType.WM_MSG_HANDLER);
        addBus(WComponentType.ROBOT_INPUT_MGR);
        addBus(WComponentType.ROBOT_OUTPUT_MGR);
        addBus(WComponentType.WAREHOUSE_INPUT_MGR);
        addBus(WComponentType.WAREHOUSE_OUTPUT_MGR);
    }
    
    private void sendWarehouseStatus(WarehouseStatus warehouseStatus){
        sendMsg(WComponentType.WM_MSG_HANDLER, EventMessageType.UPDATE_WAREHOUSE_STATUS, warehouseStatus);
    }
    
    private void sendWarehouseInventoryInfo(WComponentType dest, WarehouseInventoryInfo warehouseInventoryInfo){
        sendMsg(dest, EventMessageType.WAREHOUSE_INVENTORY_INFO, warehouseInventoryInfo);
    }
    
    @Override
    public void ping() {
        sendMsg(WComponentType.WM_MSG_HANDLER, EventMessageType.COMPONENT_HELLO,null);
        sendMsg(WComponentType.ROBOT_INPUT_MGR, EventMessageType.COMPONENT_HELLO,null);
        sendMsg(WComponentType.ROBOT_OUTPUT_MGR, EventMessageType.COMPONENT_HELLO,null);
        sendMsg(WComponentType.WAREHOUSE_INPUT_MGR, EventMessageType.COMPONENT_HELLO,null);
        sendMsg(WComponentType.WAREHOUSE_OUTPUT_MGR, EventMessageType.COMPONENT_HELLO,null);
    }
}
