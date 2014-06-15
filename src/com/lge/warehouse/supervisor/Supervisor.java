/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.supervisor;

import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import java.lang.InterruptedException;
import org.apache.log4j.Logger;

/**
 *
 * @author kihyung2.lee
 */
public final class Supervisor {
    private static Supervisor sInstance = null;
    static Logger logger = Logger.getLogger(Supervisor.class);
    
    public static void initialize() {
        logger.info("Warehouse Supervisor is initializing...");
        // TODO: Do initialization
        OrderProvider.start();
        WarehouseServiceManager.start();
        logger.info("Warehouse Supervisor has been initialized");
    }
    
    private Supervisor() {}
    
    private static Supervisor getInstance() {
        if (sInstance == null) {
            sInstance = new Supervisor();
        }
        return sInstance;
    }
    public static void ping(){
        OrderProvider.getInstance().ping();
        WarehouseServiceManager.getInstance().ping();
    }
}
