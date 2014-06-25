package com.lge.warehouse.manager;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;



import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseContext;
import com.lge.warehouse.common.app.WarehouseMain;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.common.test.WarehouseTestSystem;
import com.lge.warehouse.ordersys.OrderingSystem;
import com.lge.warehouse.supervisor.Supervisor;

public class WarehouseManagerMain extends WarehouseRunnable{
	private static Logger logger = Logger.getLogger(WarehouseManagerMain.class);
	WComponentType mId = WComponentType.SYSTEM;
	static HashMap<WComponentType, Boolean> mSystemReadyMap = new HashMap<WComponentType, Boolean>();
	private BlockingQueue<EventMessage> mQueue;
	boolean mIsSystemReady;
	public WarehouseManagerMain(){
		super(WComponentType.MANAGER_SYSTEM);
	}

	@Override
	protected void initBus() {
		// TODO Auto-generated method stub
		
		readyForMonitor(WComponentType.WM_MSG_HANDLER);
		readyForMonitor(WComponentType.WAREHOUSE_MANAGER_CONTROLLER);
		//readyForMonitor(WComponentType.ROBOT_INPUT_MGR);
		readyForMonitor(WComponentType.ROBOT_OUTPUT_MGR);
		//readyForMonitor(WComponentType.WAREHOUSE_INPUT_MGR);
		readyForMonitor(WComponentType.WAREHOUSE_OUTPUT_MGR);
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
				logger.info("Manager SYSTEM ALREADY READY, but READY_TO_OPERATE received " + event);
			}
			mSystemReadyMap.put(WComponentType.valueOf(event.getSrc()), true);
			for(boolean componentReady : mSystemReadyMap.values()){
				if(!componentReady)
					return;
			}
			mIsSystemReady = true;
			logger.info("All Manager System Ready");
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
		Manager.ping();
	}
	
	public static WarehouseManagerMain initWarehouseManagerSystem(){
		WarehouseManagerMain system = new WarehouseManagerMain();
		Manager.initialize();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		return system;
	}
	public static final void main(String[] args){
		
		WarehouseManagerMain system = initWarehouseManagerSystem();
		system.run();
	}
}
