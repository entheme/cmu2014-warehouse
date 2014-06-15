/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.supervisor;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.common.bus.p2p.P2PSender;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 *
 * @author kihyung2.lee
 */
public final class WarehouseServiceManager extends WarehouseRunnable {
    private static WarehouseServiceManager sInstance = null;
    static Logger logger = Logger.getLogger(WarehouseServiceManager.class);
    private int mWarehouseCounter;
    Vector<Warehouse> mWarehouses = new Vector<Warehouse>();
    private WarehouseServiceManager() {
        super(WComponentType.WAREHOUSE_SERVICE_MANAGER);
    }
    
    public static WarehouseServiceManager getInstance() {
        if (sInstance == null) {
            sInstance = new WarehouseServiceManager();
        }
        return sInstance;
    }
    
    @Override
    protected void eventHandle(EventMessage event) {
        switch(event.getType()){
            case WAREHOUSE_ADD_REQUEST:
                mWarehouseCounter++;
                Warehouse wh = new Warehouse(mWarehouseCounter, this);
                mWarehouses.add(wh);
                sendMsg(WComponentType.WM_MSG_HANDLER, EventMessageType.WAREHOUSE_ADD_ACCEPT, new Integer(mWarehouseCounter));
                break;
                
             
        }
    }

    public static void start() {
        logger.info("WarehouseManagerContainer start");
        new Thread(getInstance()).start();
    }

    @Override
    protected void initBus() {
        addBus(WComponentType.ORDER_PROVIDER);
        addBus(WComponentType.SUPERVISOR_UI);
        addBus(WComponentType.WM_MSG_HANDLER);
    }

    @Override
    public void ping() {
        sendMsg(WComponentType.ORDER_PROVIDER, EventMessageType.COMPONENT_HELLO, null);
        sendMsg(WComponentType.SUPERVISOR_UI, EventMessageType.COMPONENT_HELLO, null);
        for(Warehouse warehouse: mWarehouses){
            warehouse.sendObj(EventMessageType.COMPONENT_HELLO, null);
        }
    }
}
