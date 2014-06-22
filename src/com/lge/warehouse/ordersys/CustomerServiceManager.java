/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.ordersys;

import org.apache.log4j.Logger;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.WidgetCatalog;

/**
 *
 * @author seuki77
 */
public final class CustomerServiceManager extends WarehouseRunnable{
	private static CustomerServiceManager sInstance;
	static Logger logger = Logger.getLogger(CustomerServiceManager.class);
	private PlaceOrderHandler mPlaceOrderHandler;

	private CustomerServiceManager() {
		super(WComponentType.CUSTOMER_SERVICE_MANAGER);
		mPlaceOrderHandler = new PlaceOrderHandler(this);
	}
	public static CustomerServiceManager getInstance() {
		if(sInstance == null) {
			sInstance = new CustomerServiceManager();
		}
		return sInstance;
	}
	@Override
	protected void eventHandle(EventMessage event) {
		switch(event.getType()){
		case SYSTEM_READY:
			break;
		case PLACE_ORDER:
			if (event.getBody() instanceof Order){
				mPlaceOrderHandler.handleOrder((Order)event.getBody());
			}else {
				logger.info("PLACE_ORDER Wrongtype"+event);
			}
			break;
		case REQUEST_CATAGORY_FROM_CUSTOMER_IF:
			sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.REQUEST_CATAGORY_FROM_CUSTOMER_SERVICE_MANAGER, null);
			break;
		case RESPONSE_CATAGORY_TO_CUSTOMER_SERVICE_MANAGER:
			if (event.getBody() instanceof WidgetCatalog){
				sendMsg(WComponentType.CUSTOMER_INF, EventMessageType.RESPONSE_CATAGORY_TO_CUSTOMER_IF, event.getBody());
			}
			break;
		default:
			logger.info("unhandled event :"+event);
			break;
		}
	}

	@Override
	protected void initBus() {
		addBus(WComponentType.CUSTOMER_INF);
		addBus(WComponentType.WAREHOUSE_SUPERVISOR);
		addBus(WComponentType.PENDING_ORDER_MANAGER);
	}

	public static void start() {
		logger.info("CustomerServiceManager start");
		new Thread(getInstance()).start();
	}

	@Override
	public void ping() {
		sendMsg(WComponentType.CUSTOMER_INF, EventMessageType.COMPONENT_HELLO, null);
		sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.COMPONENT_HELLO, null);
		sendMsg(WComponentType.PENDING_ORDER_MANAGER, EventMessageType.COMPONENT_HELLO, null);
	}
}
