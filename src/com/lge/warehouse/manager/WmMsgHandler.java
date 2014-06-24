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
import com.lge.warehouse.common.bus.p2p.P2PConnection;
import com.lge.warehouse.common.bus.p2p.P2PReceiver;
import com.lge.warehouse.common.bus.p2p.P2PSender;
import com.lge.warehouse.util.InventoryName;
import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.QuantifiedWidget;
import com.lge.warehouse.util.WarehouseInventoryInfo;
import com.lge.warehouse.util.WarehouseStatus;
/**
 *
 * @author kihyung2.lee
 */
public final class WmMsgHandler extends WarehouseRunnable  {
	private static WmMsgHandler sInstance = null;
	static Logger logger = Logger.getLogger(WmMsgHandler.class);

	private WmMsgHandler() {
		super(WComponentType.WM_MSG_HANDLER);
	}

	public static WmMsgHandler getInstance() {
		if (sInstance == null) {
			sInstance = new WmMsgHandler();
		}
		return sInstance;
	}

	@Override
	protected void eventHandle(EventMessage event) {
		switch(event.getType()){
		case SYSTEM_READY:
			sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.WAREHOUSE_ADD_REQUEST, null);
			break;
		case WAREHOUSE_ADD_ACCEPT:
			removeBus(WComponentType.WAREHOUSE_SUPERVISOR);
			int id= (Integer)event.getBody();
			setAlias(getId().name()+id);
			handleAddAccept(id);
			break;
		case WAREHOUSE_INVENTORY_INFO:
			if(event.getBody() instanceof WarehouseInventoryInfo){
				WarehouseInventoryInfo inventoryInfo = (WarehouseInventoryInfo)event.getBody();
				logger.info("WAREHOUSE_INVENTORY_INFO: WarehouseId =" + inventoryInfo.getWarehouseId());
			 	for(InventoryName inventoryName : InventoryName.values()){
                                    //Note: Now, The InventoryName means the name of inventory stataion. 
                                    logger.info("WAREHOUSE_INVENTORY_INFO: inventoryName =" + inventoryName);
                                    if(inventoryInfo.hasInventoryStation(inventoryName)){
                                        for(QuantifiedWidget qw : inventoryInfo.getInventoryInfo(inventoryName)){
                                            logger.info(qw.getWidget()+" : "+qw.getQuantity());
                                        }
                                    }
                                }
				sendMsg(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, EventMessageType.WAREHOUSE_INVENTORY_INFO, inventoryInfo);
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
				sendMsg(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, EventMessageType.FILL_ORDER, order);
				//For Test [END]
			}else {
				handleBodyError(event);
			}
			break;
		case FINISH_FILL_ORDER:
			if(event.getBody() instanceof Order){
				//For Test [START]
				Order order = (Order)event.getBody();
				logger.info("FINISH_FILL_ORDER order id = "+order.getOrderId());
				for(QuantifiedWidget qw : order.getItemList()){
					logger.info(qw.getWidget()+" : "+qw.getQuantity());
				}
                                //Send processed order's information to WM_MSG_HANDLER
				sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.FINISH_FILL_ORDER, order);
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

	public void handleAddAccept(int id){
		String src = WComponentType.WM_MSG_HANDLER.name()+id;
		String dest = WComponentType.WAREHOUSE_SUPERVISOR.name();
		P2PSender sender = P2PConnection.createSender(src+"_"+dest);
		mP2PSenderMap.put(dest, sender);
		P2PReceiver receiver = P2PConnection.createReceiver(dest+"_"+src);
		receiver.setMessageListener(this);
		mP2PReceiverMap.put(dest, receiver);
	}
	public static void start() {
		logger.info("WmMsgHandler start");
		new Thread(getInstance()).start();
	}

	@Override
	protected void threadStart(){
		super.threadStart();
	}
	@Override
	protected void initBus() {
		addBus(WComponentType.WAREHOUSE_SUPERVISOR);
		addBus(WComponentType.WAREHOUSE_MANAGER_CONTROLLER);
	}
	private void sendWarehouseStatus(WarehouseStatus warehouseStatus){
		sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.UPDATE_WAREHOUSE_STATUS, warehouseStatus);
	}
	@Override
	public void ping() {
		sendMsg(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, EventMessageType.COMPONENT_HELLO,null);
	}
}
