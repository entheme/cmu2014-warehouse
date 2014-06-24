/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.manager;

import org.apache.log4j.Logger;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
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
				for(QuantifiedWidget qw : order.getItemList()){
					logger.info(qw.getWidget()+" : "+qw.getQuantity());
			 	}
				try {
					//To Do: deliver the order to Navigator instead of thread sleep
                                        
                                        //Follwing code is just for test.
                                        //sendMsg(WComponentType.WAREHOUSE_OUTPUT_MGR, EventMessageType.INIT_WAREHOUSE, null);
                                        //sendMsg(WComponentType.ROBOT_OUTPUT_MGR, EventMessageType.MOVE_NEXT_INV, null);
                      
                                        Thread.sleep(5000);
                                        
                                        //sendMsg(WComponentType.WAREHOUSE_OUTPUT_MGR, EventMessageType.REQUEST_LOAD_STATUS, null);
                                        //sendMsg(WComponentType.WAREHOUSE_OUTPUT_MGR, EventMessageType.REQUST_POS_STATUS, null);
                                        //sendMsg(WComponentType.WAREHOUSE_OUTPUT_MGR, EventMessageType.REQUEST_WAREHOUSE_RECOVERY, null);
                      
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
                case SEND_ROBOT_ERROR:
			if(event.getBody() instanceof String) {
                            String value = (String)event.getBody();
                            logger.info("SEND_ROBOT_ERROR :" + value);
			}else {
                            handleBodyError(event);
                        } 
                        break;
                case SEND_LOAD_STATUS:
			if(event.getBody() instanceof String) {
                             String strVal = (String)event.getBody();
                             int value = Integer.parseInt(strVal);
                             logger.info("SEND_LOAD_STATUS :" + value);
                             
			}else {
                            handleBodyError(event);
                        } 
                        break;
                case SEND_POS_STATUS:
			if(event.getBody() instanceof String) {
                             String strVal = (String)event.getBody();
                             int value = Integer.parseInt(strVal);
                             logger.info("SEND_LOAD_STATUS :" + value);
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
