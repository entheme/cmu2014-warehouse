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
import com.lge.warehouse.supervisor.OrderingSystem;
import com.lge.warehouse.supervisor.Supervisor;
import com.lge.warehouse.util.CustomerStatus;
import com.lge.warehouse.util.SystemEvent;
import com.lge.warehouse.util.WarehouseInfo;

/**
 *
 * @author seuki77
 */
public class WarehouseMain extends WarehouseRunnable implements ISystemStatusReport{
	WComponentType mId = WComponentType.SYSTEM;
	static HashMap<WComponentType, Boolean> mSystemReadyMap = new HashMap<WComponentType, Boolean>();
	private BlockingQueue<EventMessage> mQueue;
	boolean mIsSystemReady;
	private HashMap<WComponentType, HeartBeatHandler> mHeartBeatHandlerMap = new HashMap<WComponentType, HeartBeatHandler>();
	private CustomerStatus mCustomerStatus = CustomerStatus.OFF;
	private WarehouseInfo mWarehouseStatus = WarehouseInfo.OFF;
	public WarehouseMain(){
		super(WComponentType.SYSTEM, false);
	}

	@Override
	protected void initBus() {
		// TODO Auto-generated method stub
		if(WarehouseContext.TEST_MODE){
			readyForMonitor(WComponentType.WM_MSG_HANDLER);
		}
		readyForMonitor(WComponentType.CUSTOMER_SERVICE_MANAGER);
		readyForMonitor(WComponentType.BACKORDER_MANAGER);
		readyForMonitor(WComponentType.WAREHOUSE_SUPERVISOR);
		addBus(WComponentType.CUSTOMER_INF);
		addBus(WComponentType.SUPERVISOR_UI);
		addBus(WComponentType.MANAGER_SYSTEM);
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
		case WAREHOUSE_RUNNABLE_HEARTBEAT_MSG:
			if(!mHeartBeatHandlerMap.containsKey(WComponentType.valueOf(event.getSrc()))){
				mHeartBeatHandlerMap.put(WComponentType.valueOf(event.getSrc()), new HeartBeatHandler(this, WComponentType.valueOf(event.getSrc())));
				mHeartBeatHandlerMap.get(WComponentType.valueOf(event.getSrc())).setHeartBeatReceived(true);
				if(WComponentType.CUSTOMER_INF.name().equals(event.getSrc())){
					mCustomerStatus = CustomerStatus.ON;
				}else if(WComponentType.MANAGER_SYSTEM.name().equals(event.getSrc())){
					mWarehouseStatus = WarehouseInfo.ON;
				}
				updateInfo();
				reportStatus(event.getSrc()+" connected");
				
			}else {
//				logger.info("Heartbeat reset "+true);
				mHeartBeatHandlerMap.get(WComponentType.valueOf(event.getSrc())).setHeartBeatReceived(true);
			}
			break;
		default:
			logger.info("unhandled event :"+event);
			break;
		}
	}
	public void setWarehouseStatus(WarehouseInfo status){
		mWarehouseStatus = status;
	}
	public void setCustomerStatus(CustomerStatus status){
		mCustomerStatus = status;
	}
	private void sendSystemReadyMsg(){
		for(WComponentType component : mSystemReadyMap.keySet()){
			sendMsg(component, EventMessageType.SYSTEM_READY, null);
		}
	}
	/* (non-Javadoc)
	 * @see com.lge.warehouse.common.app.ISystemStatusReport#reportStatus(java.lang.String)
	 */
	@Override
	public void reportStatus(String status){
		logger.info(status);
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

	public void removeHeartHandler(WComponentType target) {
		// TODO Auto-generated method stub
		mHeartBeatHandlerMap.remove(target);
	}

	public void updateInfo() {
		// TODO Auto-generated method stub
		if(mHeartBeatHandlerMap.containsKey(WComponentType.SUPERVISOR_UI)){
			sendMsg(WComponentType.SUPERVISOR_UI, EventMessageType.SYSTEM_STATUS_REPORT, new SystemEvent(mCustomerStatus, mWarehouseStatus));
		}
	}
}
