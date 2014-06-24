/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.common.app;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.common.test.WarehouseTestSystem;
import com.lge.warehouse.manager.Manager;
import com.lge.warehouse.ordersys.OrderingSystem;
import com.lge.warehouse.supervisor.Supervisor;

/**
 *
 * @author seuki77
 */
public class WarehouseMain extends WarehouseRunnable{
	WComponentType mId = WComponentType.SYSTEM;
	static HashMap<WComponentType, Boolean> mSystemReadyMap = new HashMap<WComponentType, Boolean>();
	private BlockingQueue<EventMessage> mQueue;
	boolean mIsSystemReady;
	public WarehouseMain(){
		super(WComponentType.SYSTEM);
	}

	@Override
	protected void initBus() {
		// TODO Auto-generated method stub
		if(WarehouseContext.TEST_MODE){
			readyForMonitor(WComponentType.WM_MSG_HANDLER);
		}
		readyForMonitor(WComponentType.CUSTOMER_SERVICE_MANAGER);
		readyForMonitor(WComponentType.PENDING_ORDER_MANAGER);
		readyForMonitor(WComponentType.WAREHOUSE_SUPERVISOR);
		//readyForMonitor(WComponentType.WM_MSG_HANDLER);
		//readyForMonitor(WComponentType.WAREHOUSE_MANAGER_CONTROLLER);
		//readyForMonitor(WComponentType.ROBOT_INPUT_MGR);
		//readyForMonitor(WComponentType.ROBOT_OUTPUT_MGR);
		//readyForMonitor(WComponentType.WAREHOUSE_INPUT_MGR);
		//readyForMonitor(WComponentType.WAREHOUSE_OUTPUT_MGR);
	}
	private void readyForMonitor(WComponentType component){
		addBus(component);
		mSystemReadyMap.put(component, false);
	}

	@Override
	protected void eventHandle(EventMessage event) {
		// TODO Auto-generated method stub
		switch(event.getType()){
		case READY_TO_OPERATE:
			if(mIsSystemReady){
				logger.info("SYSTEM ALREADY READY, but READY_TO_OPERATE received " + event);
			}
			mSystemReadyMap.put(WComponentType.valueOf(event.getSrc()), true);
			for(boolean componentReady : mSystemReadyMap.values()){
				if(!componentReady)
					return;
			}
			mIsSystemReady = true;
			logger.info("All System Ready");
			sendSystemReadyMsg();
			break;
		default:
			logger.info("unhandled event :"+event);
			break;
		}
	}
	private void sendSystemReadyMsg(){
		for(WComponentType component : mSystemReadyMap.keySet()){
			sendMsg(component, EventMessageType.SYSTEM_READY, null);
		}
	}

	@Override
	protected void threadStart(){
		logger.info(Thread.currentThread().getName()+" start");
	}
	@Override
	public void ping() {
		// TODO Auto-generated method stub
		if(WarehouseContext.TEST_MODE)
			WarehouseTestSystem.ping();
		OrderingSystem.ping();
		Supervisor.ping();
		Manager.ping();
	}
	
	public static WarehouseMain initWarehouseSystem(){
		WarehouseMain system = new WarehouseMain();

		if(WarehouseContext.TEST_MODE)
			WarehouseTestSystem.initialize();
		OrderingSystem.initialize();
		Supervisor.initialize();
		//Manager.initialize();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			Logger.getLogger(WarehouseMain.class.getName()).log(Level.SEVERE, null, ex);
		}
		return system;
	}
	public static final void main(String[] args){
		
		WarehouseMain system = initWarehouseSystem();
		system.run();
	}
}
