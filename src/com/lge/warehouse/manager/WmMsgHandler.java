/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.manager;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseContext;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.common.bus.p2p.P2PConnection;
import com.lge.warehouse.common.bus.p2p.P2PReceiver;
import com.lge.warehouse.common.bus.p2p.P2PSender;
import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.QuantifiedWidget;
import com.lge.warehouse.util.WidgetInfo;

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
			sendingInventoryInfo();
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
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.FINISH_FILL_ORDER, order);
				//For Test [END]
				
			}else {
				String errLog = "FILL_ORDER has wrong body";
				if(WarehouseContext.DEBUG_WITH_RUNTIME_EXCEPTION){
					throw new RuntimeException(errLog);
				}else{
					logger.info(errLog);
				}
			}
			break;
		default:
			logger.info("unhandled event :"+event);
			break;
		}
	}
	public void sendingInventoryInfo(){
		//For test [START]
		HashMap<WidgetInfo, Integer> inventoryMap = new HashMap<WidgetInfo, Integer>();
		inventoryMap.put(new WidgetInfo(0,"Item1",100), 50);
		inventoryMap.put(new WidgetInfo(1,"Item2",200), 50);
		inventoryMap.put(new WidgetInfo(2,"Item3",300), 50);
		inventoryMap.put(new WidgetInfo(3,"Item4",400), 50);
		inventoryMap.put(new WidgetInfo(4,"Item5",500), 50);
		sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.WAREHOUSE_INVENTORY_INFO, inventoryMap);
		//For test [END]
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

	@Override
	public void ping() {
		sendMsg(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, EventMessageType.COMPONENT_HELLO,null);
	}
}
