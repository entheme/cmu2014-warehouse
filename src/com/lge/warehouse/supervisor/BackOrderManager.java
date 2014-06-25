/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.supervisor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.common.bus.p2p.P2PSender;
import com.lge.warehouse.util.ObjectCloner;
import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.OrderStatusInfo;
import com.lge.warehouse.util.QuantifiedWidget;

/**
 *
 * @author seuki77
 */
public final class BackOrderManager extends WarehouseRunnable{
	static Logger logger = Logger.getLogger(BackOrderManager.class);
	private static BackOrderManager sInstance;
	private P2PSender mSenderToSupervisor;
	private BackOrderManager(){
		super(WComponentType.BACKORDER_MANAGER, false);
	}

	public static BackOrderManager getInstance() {
		if (sInstance == null) {
			sInstance = new BackOrderManager();
		}
		return sInstance;
	}

	@Override
	protected void eventHandle(EventMessage event) {
		switch(event.getType()){
		case SYSTEM_READY:
			break;
		case REQUEST_BACK_ORDER:
			handleRequestBackOrder();
			break;
		case REQUEST_ORDER_STATUS:
			reportOrderStatus();
			break;
		default:
			logger.info("unhandled event :"+event);
			break;
		}
	}
	private void handleRequestBackOrder(){
		try {
			WarehouseInventoryInfo warehouseInventoryInfo = (WarehouseInventoryInfo)ObjectCloner.deepCopy(WarehouseInventoryInfoRepository.getInstance().getWarehouseInventoryInfo());
			List<Order> orderList = new ArrayList<Order>();
			for(Order order : BackOrderQueue.getInstance().getBackOrderList()){
				logger.info("Order : "+order);
				logger.info(warehouseInventoryInfo);
				if(warehouseInventoryInfo.hasInventory(order)){
					for(QuantifiedWidget qw : order.getItemList()){
						warehouseInventoryInfo.reduceInventoryWidget(qw.getWidget(), qw.getQuantity());
					}
					orderList.add(order);
				}
				
			}
			logger.info("backorder ->pendingorder : "+orderList.size());
			for(Order order : orderList){
				BackOrderQueue.getInstance().removeOrder(order);
				sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.NEW_ORDER, order);
			}
			reportOrderStatus();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void reportOrderStatus(){
		OrderStatusInfo orderStatusInfo = new OrderStatusInfo();
		for(Order backOrder : BackOrderQueue.getInstance().getBackOrderList())
			orderStatusInfo.addBackOrder(backOrder);
		sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.RESPONSE_ORDER_STATUS,orderStatusInfo);
	}
	public static void start() {
		logger.info("BackOrderManager start");
		new Thread(BackOrderManager.getInstance()).start();
	}

	@Override
	protected void initBus() {
		addBus(WComponentType.WAREHOUSE_SUPERVISOR);
		addBus(WComponentType.CUSTOMER_SERVICE_MANAGER);
	}

	@Override
	public void ping() {
		sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.COMPONENT_HELLO, null);
	}
}
