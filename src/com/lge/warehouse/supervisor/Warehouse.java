/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.supervisor;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.common.bus.p2p.P2PConnection;
import com.lge.warehouse.common.bus.p2p.P2PReceiver;
import com.lge.warehouse.common.bus.p2p.P2PSender;
import java.io.Serializable;
import javax.jms.MessageListener;

/**
 *
 * @author seuki77
 */
public class Warehouse {
    String mDest;
    String mSrc;
    P2PSender mSender;

    public Warehouse(int id, MessageListener listener){
        mDest = WComponentType.WM_MSG_HANDLER.name();
        mSrc = WComponentType.WAREHOUSE_SERVICE_MANAGER.name();
        initBus(id, listener);
    }
    void initBus(int id, MessageListener listener){
        mSender = P2PConnection.createSender(mSrc+"_"+mDest+id);
        P2PReceiver receiver = P2PConnection.createReceiver(mDest+id+"_"+mSrc);
        receiver.setMessageListener(listener);
    }
    
    public void sendObj(EventMessageType type, Serializable body){
        mSender.sendObject(new EventMessage(mSrc,mDest,type, body));
    }
}
