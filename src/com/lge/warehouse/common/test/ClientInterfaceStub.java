/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.common.test;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.util.Order;

/**
 *
 * @author seuki77
 */
public class ClientInterfaceStub extends WarehouseRunnable{
	private static ClientInterfaceStub sInstance = null;

	private ClientInterfaceStub(){
		super(WComponentType.CUSTOMER_INF, false);
	}

	public static ClientInterfaceStub getInstance() {
		if(sInstance == null)
			sInstance = new ClientInterfaceStub();
		return sInstance;
	}

	@Override
	protected void initBus() {
		addBus(WComponentType.CUSTOMER_SERVICE_MANAGER);
	}

	@Override
	protected void eventHandle(EventMessage event) {
		switch(event.getType()){
		case SYSTEM_READY:
			//Thread.currentThread().setName(getId().name());

			break;
		}
	}

	@Override
	public void ping() {
		sendMsg(WComponentType.CUSTOMER_SERVICE_MANAGER, EventMessageType.COMPONENT_HELLO, null);
	}

	public void placeOrder(){
		
		for(int i = 0; i<5; i++){
			Order order = new Order();
//			WidgetInfo item1 = new WidgetInfo(0,"Item1", 100);
//			WidgetInfo item2 = new WidgetInfo(1,"Item2", 200);
//			WidgetInfo item3 = new WidgetInfo(2,"Item3", 300);
//			WidgetInfo item4 = new WidgetInfo(3,"Item4", 400);
//			WidgetInfo item5 = new WidgetInfo(4,"Item5", 500);
//
//			order.addItem(item1, 5);
//			order.addItem(item2, 6);
//			order.addItem(item3, 7);
//			order.addItem(item4, 8);
//			order.addItem(item5, 9);
//
//			sendMsg(WComponentType.CUSTOMER_SERVICE_MANAGER, EventMessageType.PLACE_ORDER, order);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void start() {
		new Thread(ClientInterfaceStub.getInstance()).start();
	}
}
