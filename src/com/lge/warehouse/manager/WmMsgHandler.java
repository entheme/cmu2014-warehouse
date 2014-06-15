/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.lge.warehouse.manager;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.common.bus.p2p.P2PConnection;
import com.lge.warehouse.common.bus.p2p.P2PReceiver;
import com.lge.warehouse.common.bus.p2p.P2PSender;
import org.apache.log4j.Logger;

/**
 *
 * @author kihyung2.lee
 */
public final class WmMsgHandler extends WarehouseRunnable  {
    private static WmMsgHandler sInstance = null;
    static Logger logger = Logger.getLogger(WmMsgHandler.class);
    
    private WmMsgHandler() {
        super(WComponentType.WM_MSG_HANDLER);
    }
    
    public static WmMsgHandler getInstance() {
        if (sInstance == null) {
            sInstance = new WmMsgHandler();
        }
        return sInstance;
    }
    
    @Override
    protected void eventHandle(EventMessage event) {
        switch(event.getType()){
            case WAREHOUSE_ADD_ACCEPT:
                removeBus(WComponentType.WAREHOUSE_SERVICE_MANAGER);
                int id= (Integer)event.getBody();
                handleAddAccept(id);
        }
    }
    public void handleAddAccept(int id){
        String src = WComponentType.WM_MSG_HANDLER.name()+id;
        String dest = WComponentType.WAREHOUSE_SERVICE_MANAGER.name();
        P2PSender sender = P2PConnection.createSender(src+"_"+dest);
        mP2PSenderMap.put(dest, sender);
        P2PReceiver receiver = P2PConnection.createReceiver(dest+"_"+src);
        receiver.setMessageListener(this);
        mP2PReceiverMap.put(dest, receiver);
    }
    public static void start() {
        logger.info("WmMsgHandler start");
        new Thread(getInstance()).start();
    }
    
    @Override
    protected void threadStart(){
        super.threadStart();
        addBus(WComponentType.WAREHOUSE_SERVICE_MANAGER);
        sendMsg(WComponentType.WAREHOUSE_SERVICE_MANAGER, EventMessageType.WAREHOUSE_ADD_REQUEST, null);
    }
    @Override
    protected void initBus() {
        addBus(WComponentType.WAREHOUSE_MANAGER_CONTROLLER);
    }

    @Override
    public void ping() {
        sendMsg(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, EventMessageType.COMPONENT_HELLO,null);
    }
}
