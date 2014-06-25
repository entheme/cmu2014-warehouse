package com.lge.warehouse.common.app;

import java.util.Timer;
import java.util.TimerTask;

public final class HeartBeatHandler{
	/**
	 * 
	 */
	private final WarehouseMain mSystem;
	boolean mHeartBeatReceived = false;
	private HeartBeatChecker mHeartBeatChecker ;
	private WComponentType mTarget;
	private Timer timer;
	public HeartBeatHandler(WarehouseMain warehouseMain, WComponentType target){
		mSystem = warehouseMain;
		mHeartBeatChecker = new HeartBeatChecker(this);
		mTarget = target;
		timer = new Timer();
		timer.schedule(mHeartBeatChecker,100, 4000);
	}
	private final class HeartBeatChecker extends TimerTask{
		HeartBeatHandler mHandler;
		public HeartBeatChecker(HeartBeatHandler handler){
			mHandler = handler;
		}
		@Override
		public void run() {
			//WarehouseMain.logger.info("Heartbeat check");
			// TODO Auto-generated method stub
			if(!mHandler.mHeartBeatReceived){
				WarehouseMain.logger.info(mHandler.mTarget.name()+" is disconnected");
				mSystem.removeHeartHandler(mTarget);
				mHandler.timer.cancel();
			}else{
				mHandler.mHeartBeatReceived = false;
				
				//mHandler.timer.schedule(mHandler.mHeartBeatChecker, 4000);
				
			}
		}
		
	}
	public void setHeartBeatReceived(boolean b) {
		// TODO Auto-generated method stub
		mHeartBeatReceived = true;
	}
}