package com.lge.warehouse.common.test;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;

public class WarehouseManagerStub extends WarehouseRunnable{
	private static WarehouseManagerStub sInstance = null;
	
	private WarehouseManagerStub(){
		super(WComponentType.WM_MSG_HANDLER);
	}
	
	public static WarehouseManagerStub getInstance(){
		if(sInstance == null){
			sInstance = new WarehouseManagerStub();
		}	
		return sInstance;
	}

	@Override
	protected void threadStart() {
		// TODO Auto-generated method stub
		super.threadStart();
		
	}

	@Override
	protected void initBus() {
		// TODO Auto-generated method stub
		addBus(WComponentType.WAREHOUSE_SUPERVISOR);
	}

	@Override
	protected void eventHandle(EventMessage event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ping() {
		// TODO Auto-generated method stub
		sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.COMPONENT_HELLO, null);
	}


}
