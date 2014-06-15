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
public class RobotOutputMgr extends WarehouseRunnable{
    private static RobotOutputMgr sInstance = null;
    static Logger logger = Logger.getLogger(RobotOutputMgr.class);
    
    private RobotOutputMgr() {
        super(WComponentType.ROBOT_OUTPUT_MGR);
    }
    
    public static RobotOutputMgr getInstance() {
        if (sInstance == null) {
            sInstance = new RobotOutputMgr();
        }
        return sInstance;
    }

    @Override
    protected void eventHandle(EventMessage event) {

    }

    public static void start() {
        logger.info("RobotOutputMgr start");
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
