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

import org.apache.log4j.Logger;

import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.common.bus.p2p.P2PConnection;
import com.lge.warehouse.common.bus.p2p.P2PReceiver;
import com.lge.warehouse.common.bus.p2p.P2PSender;
import com.lge.warehouse.ordersys.CustomerServiceManager;
import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Level;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public abstract class WarehouseRunnable implements Runnable , MessageListener{
	private BlockingQueue<EventMessage> mQueue;
	private boolean mExit = false;
	private boolean mStopped = false;
        private WComponentType mId;
        protected HashMap<String, P2PSender> mP2PSenderMap = new HashMap<String, P2PSender>();
        protected HashMap<String, P2PReceiver> mP2PReceiverMap = new HashMap<String, P2PReceiver>();
	static Logger logger = Logger.getLogger(WarehouseRunnable.class);
        
	protected WarehouseRunnable(WComponentType id) {
		mQueue = new ArrayBlockingQueue<EventMessage>(50);
                mId = id;
                initBus();
	}
	public WComponentType getId() {
		return mId;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		threadStart();
		while(!mExit) {
			try {
				EventMessage event = mQueue.take();
                                logger.debug("Received : "+event);
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
        
        protected abstract void initBus();
        
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

        public boolean addBus(WComponentType dest) {
            WBus bus= WBus.getBus(getId(), dest);
            if (bus== WBus.P2P_NONE){
                return false;
            }
            P2PSender sender = P2PConnection.createSender(bus);
            if ( sender == null)
                return false;
            mP2PSenderMap.put(dest.name(), sender);
            bus = WBus.getBus(dest, getId());
            P2PReceiver receiver = P2PConnection.createReceiver(bus);
            if (receiver == null)
                return false;
            receiver.setMessageListener(this);
            mP2PReceiverMap.put(dest.name(), receiver);
            return true;
        }
        
        
        
        public void removeBus(WComponentType dest){
            P2PSender sender = mP2PSenderMap.remove(dest.name());
            sender.stop();
            
            P2PReceiver receiver = mP2PReceiverMap.remove(dest.name());
            receiver.stop();
        }
        public void sendMsg(WComponentType dest,EventMessageType type, Serializable body){
            logger.debug("sendMsg "+dest.name()+", "+type.name());
            P2PSender sender = mP2PSenderMap.get(dest.name());
            EventMessage em = new EventMessage(getId().name(), dest.name(), type, body);
            sender.sendObject(em);
        }

	protected abstract void eventHandle(EventMessage event);
	protected void threadStart(){
		Thread.currentThread().setName(getId().name());
		logger.info(getId()+" thread start");
	}
	protected void threadStop(){
		logger.info(getId()+" thread end");
	}
        @Override
        public void onMessage(Message message) {
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
