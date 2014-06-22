/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.common.test;

import org.apache.log4j.Logger;

/**
 *
 * @author seuki77
 */
public class WarehouseTestSystem {
    private static WarehouseTestSystem sInstance = null;
    static Logger logger = Logger.getLogger(WarehouseTestSystem.class);
    
    public static void initialize() {
        logger.info("Ordering System is initializing...");
        // TODO: Do initialization
        ClientInterfaceStub.start();        
        
        logger.info("Ordering System has been initialized");
    }
    
    private WarehouseTestSystem(){}
    
    public static WarehouseTestSystem getInstance() {
        if (sInstance == null) {
            sInstance = new WarehouseTestSystem();
        }
        return sInstance;
    }
    public static void ping(){
        ClientInterfaceStub.getInstance().ping();
    }
}
