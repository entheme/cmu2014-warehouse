/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.manager;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WBus;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.common.bus.p2p.P2PConnection;
import com.lge.warehouse.common.bus.p2p.P2PSender;
import java.io.Serializable;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author jaehak.lee
 */
public abstract class DeviceInputMgr implements Runnable {
        static Logger logger = Logger.getLogger(DeviceInputMgr.class);
        protected WComponentType mId;
        private boolean mExit = false;
        private boolean mStopped = false;
        protected HashMap<String, P2PSender> mP2PSenderMap = new HashMap<String, P2PSender>();
        ArduinoConnector mArduinoCon = null;
        protected String mAlias = null;
        protected int mPortNum = 505;	
      
        protected DeviceInputMgr(WComponentType id) {
            mId = id;
        }
        
        protected void setArduinoConnector(ArduinoConnector arduinoCon) {
            mArduinoCon = arduinoCon;
        }

        @Override
        public void run() {
                 // TODO Auto-generated method stub
                addBus(WComponentType.WAREHOUSE_MANAGER_CONTROLLER);
               
                threadStart();

                String inputData = null;
                mArduinoCon.setPortNum(mPortNum);
                
                logger.info("Call startServer");
                if(mArduinoCon.startServer() == true) {
                    
                    connectionDone();
                    
                    while(!mExit) {
                        
                        if(mArduinoCon.IsConnected() == false) {
                            connectionLost();
                            if(mArduinoCon.startServer() == true)
                                connectionDone();
                        }
                        
                        inputData = mArduinoCon.readData();
                        if(inputData != null) {
                            processingData(inputData);
                        }
                    }
                }

                threadStop();
                mStopped = true;
        }
    
        protected abstract void processingData(String inputData);
        protected abstract void connectionDone();
        protected abstract void connectionLost();
        
        protected void setPortNum(int portNum) {
		mPortNum = portNum;
	}  
        public void requestThreadStop(){
                mExit = true;
        }
        public boolean isStopped(){
                return mStopped;
        }
        
        public WComponentType getId() {
                return mId;
	}
                
        protected void threadStart(){
                Thread.currentThread().setName(getId().name());
                logger.info(getId()+" thread start");
        }
        
	protected void threadStop(){
		logger.info(getId()+" thread end");
	}
        
        protected void setAlias(String alias) {
		mAlias = alias;
	}
        
        protected boolean addBus(WComponentType dest) {
                WBus bus= WBus.getBus(getId(), dest);
                if (bus== WBus.P2P_NONE){
                        return false;
                }
                P2PSender sender = P2PConnection.createSender(bus);
                if ( sender == null)
                        return false;
                mP2PSenderMap.put(dest.name(), sender);
                return true;
        }
        
        protected void sendMsg(WComponentType dest, EventMessageType type, Serializable body) {
		logger.debug("sendMsg "+dest.name()+", "+type.name());
		P2PSender sender = mP2PSenderMap.get(dest.name());
		EventMessage em = new EventMessage(mAlias==null?getId().name():mAlias, dest.name(), type, body);
		sender.sendObject(em);
	}
        
        public void ping() {
            sendMsg(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, EventMessageType.COMPONENT_HELLO,null);
        }
}
