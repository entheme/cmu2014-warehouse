/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.manager;

import java.util.List;

import org.apache.log4j.Logger;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.manager.OrderStatemachine.WahouseStateMachine;
import com.lge.warehouse.manager.OrderStatemachine.WMorderStatemachineState;
import com.lge.warehouse.util.InventoryName;
import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.QuantifiedWidget;
import com.lge.warehouse.util.WarehouseInventoryInfo;
import com.lge.warehouse.util.WarehouseStatus;

/**
 *
 * @author kihyung2.lee
 */
public class WarehouseManagerController extends WarehouseRunnable {
    private static WarehouseManagerController sInstance = null;
    static Logger logger = Logger.getLogger(WarehouseManagerController.class);
    WarehouseInventoryInfo minventoryInfo;
    WahouseStateMachine warehouseStatemachine = new WahouseStateMachine();
    NavigationPathSelector PathSelector = new NavigationPathSelector(minventoryInfo);

    private WarehouseManagerController() {
        super(WComponentType.WAREHOUSE_MANAGER_CONTROLLER);
    }

    public static WarehouseManagerController getInstance() {
        if (sInstance == null) {
            sInstance = new WarehouseManagerController();
        }
        return sInstance;
    }

    @Override
    protected void eventHandle(EventMessage event) {
		switch(event.getType()){
		case SYSTEM_READY:
			break;
		case WAREHOUSE_INVENTORY_INFO:
			if(event.getBody() instanceof WarehouseInventoryInfo){
				minventoryInfo = (WarehouseInventoryInfo)event.getBody();
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
				logger.info("FILL_ORDER order id = "+order.getOrderId());
				System.out.println("FILL_ORDER order id = "+order.getOrderId());
				for(QuantifiedWidget qw : order.getItemList()){
					logger.info(qw.getWidget()+" : "+qw.getQuantity());
					System.out.println(qw.getWidget()+" : "+qw.getQuantity());
			 	}
				
				List<WMorderStatemachineState> newOrderPath = PathSelector.MakeNewNavigationPath(order);
				warehouseStatemachine.Evt_NewOrder(newOrderPath);
				
				
				try {
					//To Do: deliver the order to Navigator instead of thread sleep
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                                //Send processed order's information to WM_MSG_HANDLER
				sendMsg(WComponentType.WM_MSG_HANDLER, EventMessageType.FINISH_FILL_ORDER, order);
				//For Test [END]
			}else {
                                handleBodyError(event);
			}
			break;
		case UPDATE_WAREHOUSE_STATUS:
			if(event.getBody() instanceof WarehouseStatus) {
                            WarehouseStatus warehouseStatus = (WarehouseStatus)event.getBody();
                            sendWarehouseStatus(warehouseStatus);
			}else {
                            handleBodyError(event);
			} 
			break;
		default:
			logger.info("unhandled event :"+event);
			break;
		}
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
