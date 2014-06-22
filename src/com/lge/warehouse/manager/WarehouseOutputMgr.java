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
import org.apache.log4j.Logger;

/**
 *
 * @author seuki77
 */
public class WarehouseOutputMgr extends WarehouseRunnable{
    private static WarehouseOutputMgr sInstance = null;
    static Logger logger = Logger.getLogger(WarehouseOutputMgr.class);
    
    private WarehouseOutputMgr() {
        super(WComponentType.WAREHOUSE_OUTPUT_MGR);
    }
    
    public static WarehouseOutputMgr getInstance() {
        if (sInstance == null) {
            sInstance = new WarehouseOutputMgr();
        }
        return sInstance;
    }

    @Override
    protected void eventHandle(EventMessage event) {

    }

    public static void start() {
        logger.info("WarehouseOutputMgr start");
        new Thread(getInstance()).start();
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
