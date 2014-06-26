/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.common.app;

/**
 *
 * @author seuki77
 */
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.supervisor.CustomerServiceManager;

public abstract class WarehouseRunnable extends WarehouseComponent implements Runnable {
	private BlockingQueue<EventMessage> mQueue;
	private boolean mExit = false;
	private boolean mStopped = false;
	static Logger logger = Logger.getLogger(WarehouseRunnable.class);
	private boolean mEnableHartBeat = false;
	private Timer mHeartBeatTimer;
	protected WarehouseRunnable(WComponentType id, boolean enableHeartBeat) {
		super(id);
		mQueue = new ArrayBlockingQueue<EventMessage>(100);
		mEnableHartBeat = enableHeartBeat;
		mHeartBeatTimer = new Timer();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		threadStart();

		while(!mExit) {
			try {
				EventMessage event = mQueue.take();
				if(!handleSendMsgOnContext(event))
				{	
					if(event.getType() != EventMessageType.WAREHOUSE_RUNNABLE_HEARTBEAT_MSG)
						logger.info("Received : "+event);
					if(event.getType() == EventMessageType.COMPONENT_END)
						break;

					eventHandle(event);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		threadStop();
		mStopped = true;
	}
	protected final void sendMsgOnContext(EventMessage event){
		postEvent(new EventMessage(getId().name(),getId().name(), EventMessageType.WAREHOUSE_RUNNABLE_SEND_ON_THREAD_CONTEXT, event));
	}
	private boolean handleSendMsgOnContext(EventMessage event){
		if(event.getSrc().equals(getId().name())
				&&(event.getDest().equals(getId().name())
						&&event.getType()==EventMessageType.WAREHOUSE_RUNNABLE_SEND_ON_THREAD_CONTEXT)
				){
			//logger.info("self message send : "+event.getBody());
			sendObject((EventMessage)event.getBody());
			return true;
		}else
			return false;
	}
	public void postEvent(EventMessage event) {
		mQueue.add(event);
	}
	public void requestThreadStop(){
		postEvent(new EventMessage(getId().name(), getId().name(), EventMessageType.COMPONENT_END, null));
		mExit = true;
	}
	public boolean isStopped(){
		return mStopped;
	}
	protected void threadStart(){
		Thread.currentThread().setName(getId().name());

		if((getId() == WComponentType.CUSTOMER_SERVICE_MANAGER)||
				(getId() == WComponentType.BACKORDER_MANAGER)||
				(getId() == WComponentType.WAREHOUSE_SUPERVISOR)||
				((WarehouseContext.TEST_MODE==true)&&(getId() == WComponentType.WM_MSG_HANDLER))
				){
			sendMsg(WComponentType.SYSTEM, EventMessageType.READY_TO_OPERATE, null);
		}else if((getId() == WComponentType.WM_MSG_HANDLER)||
				(getId() == WComponentType.WAREHOUSE_MANAGER_CONTROLLER)||
				(getId() == WComponentType.ROBOT_OUTPUT_MGR)||
				(getId() == WComponentType.WAREHOUSE_OUTPUT_MGR)){
			sendMsg(WComponentType.MANAGER_SYSTEM, EventMessageType.READY_TO_OPERATE, null);
		}
		if(mEnableHartBeat){
			if(WarehouseContext.ENABLE_HEARTBEAT){
				addBus(WComponentType.SYSTEM);
				mHeartBeatTimer.schedule(new HeartBeatTask(), 1000, 3000);
			}
		}

		logger.info(getId()+" thread start");
	}
	protected void threadStop(){
		logger.info(getId()+" thread end");
	}

	protected abstract void initBus();

	protected abstract void eventHandle(EventMessage event);

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		if(message instanceof ObjectMessage){
			try {
				EventMessage em = (EventMessage)((ObjectMessage)message).getObject();
				postEvent(em);
			} catch (JMSException ex) {
				java.util.logging.Logger.getLogger(CustomerServiceManager.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	public abstract void ping();
	private class HeartBeatTask extends TimerTask{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			sendMsgOnContext(new EventMessage(getId().name(), WComponentType.SYSTEM.name(), EventMessageType.WAREHOUSE_RUNNABLE_HEARTBEAT_MSG, null));
		}

	}
}
