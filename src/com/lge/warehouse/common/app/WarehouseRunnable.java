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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;

import org.apache.log4j.Logger;

import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.ordersys.CustomerServiceManager;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public abstract class WarehouseRunnable extends WarehouseComponent implements Runnable {
	private BlockingQueue<EventMessage> mQueue;
	private boolean mExit = false;
	private boolean mStopped = false;
	static Logger logger = Logger.getLogger(WarehouseRunnable.class);

	protected WarehouseRunnable(WComponentType id) {
		super(id);
		mQueue = new ArrayBlockingQueue<EventMessage>(50);
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		threadStart();
		
		while(!mExit) {
			try {
				EventMessage event = mQueue.take();
				logger.info("Received : "+event);
				if(event.getType() == EventMessageType.COMPONENT_END)
					break;
				eventHandle(event);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		threadStop();
		mStopped = true;
	}

	public void postEvent(EventMessage event) {
		if(mQueue == null)
			logger.info("check postEvent"+event);
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
		if(WarehouseContext.TEST_MODE)
			sendMsg(WComponentType.SYSTEM, EventMessageType.READY_TO_OPERATE, null);
		else{
			if(!((getId()==WComponentType.CUSTOMER_INF)||(getId()==WComponentType.SUPERVISOR_UI))){
				sendMsg(WComponentType.SYSTEM, EventMessageType.READY_TO_OPERATE, null);
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
}
