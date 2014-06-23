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
import static com.lge.warehouse.manager.WmMsgHandler.logger;
import com.lge.warehouse.util.InventoryName;
import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.QuantifiedWidget;
import com.lge.warehouse.util.WarehouseInventoryInfo;

/**
 *
 * @author kihyung2.lee
 */
public class WarehouseManagerController extends WarehouseRunnable {
    private static WarehouseManagerController sInstance = null;
    static Logger logger = Logger.getLogger(WarehouseManagerController.class);

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
				WarehouseInventoryInfo inventoryInfo = (WarehouseInventoryInfo)event.getBody();
				logger.info("WAREHOUSE_INVENTORY_INFO: WarehouseId =" + inventoryInfo.getWarehouseId());
			 	for(InventoryName inventoryName : InventoryName.values()){
                                    logger.info("WAREHOUSE_INVENTORY_INFO: inventoryName =" + inventoryName);
                                   if(inventoryInfo.hasInventoryStation(inventoryName)){
                                        for(QuantifiedWidget qw : inventoryInfo.getInventoryInfo(inventoryName)){
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

    @Override
    public void ping() {
        sendMsg(WComponentType.WM_MSG_HANDLER, EventMessageType.COMPONENT_HELLO,null);
        sendMsg(WComponentType.ROBOT_INPUT_MGR, EventMessageType.COMPONENT_HELLO,null);
        sendMsg(WComponentType.ROBOT_OUTPUT_MGR, EventMessageType.COMPONENT_HELLO,null);
        sendMsg(WComponentType.WAREHOUSE_INPUT_MGR, EventMessageType.COMPONENT_HELLO,null);
        sendMsg(WComponentType.WAREHOUSE_OUTPUT_MGR, EventMessageType.COMPONENT_HELLO,null);
    }
}
