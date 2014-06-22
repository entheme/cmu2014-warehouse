/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.ordersys;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.common.bus.p2p.P2PSender;
import org.apache.log4j.Logger;

/**
 *
 * @author seuki77
 */
public final class PendingOrderManager extends WarehouseRunnable{
	static Logger logger = Logger.getLogger(PendingOrderManager.class);
	private static PendingOrderManager sInstance;
	private P2PSender mSenderToSupervisor;
	private PendingOrderHandler mPendingOrderHandler;
	private PendingOrderManager(){
		super(WComponentType.PENDING_ORDER_MANAGER);
		mPendingOrderHandler = new PendingOrderHandler(this);
	}

	public static PendingOrderManager getInstance() {
		if (sInstance == null) {
			sInstance = new PendingOrderManager();
		}
		return sInstance;
	}

	@Override
	protected void eventHandle(EventMessage event) {
		switch(event.getType()){
		case SYSTEM_READY:
			break;
		case REQUEST_PENDING_ORDER:
			mPendingOrderHandler.requestPendingOrder();
			break;
		case PENDING_ORDER_READY:
			mPendingOrderHandler.pendingOrderReady();
			break;
		default:
			logger.info("unhandled event :"+event);
			break;
		}
	}

	public static void start() {
		logger.info("PendingOrderManager start");
		new Thread(PendingOrderManager.getInstance()).start();
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
