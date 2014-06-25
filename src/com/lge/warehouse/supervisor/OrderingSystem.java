/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.lge.warehouse.supervisor;

import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import org.apache.log4j.Logger;

/**
 *
 * @author kihyung2.lee
 */
public final class OrderingSystem{
    private static OrderingSystem sInstance = null;
    static Logger logger = Logger.getLogger(OrderingSystem.class);
    
    public static void initialize() {
        logger.info("Ordering System is initializing...");
        // TODO: Do initialization
        
        CustomerServiceManager.start();
        BackOrderManager.start();
        logger.info("Ordering System has been initialized");
    }
    
    private OrderingSystem(){}
    
    public static OrderingSystem getInstance() {
        if (sInstance == null) {
            sInstance = new OrderingSystem();
        }
        return sInstance;
    }
    public static void ping(){
        CustomerServiceManager.getInstance().ping();
        BackOrderManager.getInstance().ping();
    }
}
