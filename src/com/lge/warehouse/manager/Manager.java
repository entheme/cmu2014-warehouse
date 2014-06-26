/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.manager;

import org.apache.log4j.Logger;

/**
 *
 * @author seuki77
 */
public class Manager {
    private static Manager sInstance = null;
    static Logger logger = Logger.getLogger(Manager.class);

     
    public static void initialize() {
        logger.info("Manager is initializing...");
        // TODO: Do initialization
        WmMsgHandler.start();
        WarehouseManagerController.start();


        logger.info("Manager has been initialized");
    }
    
    private Manager(){}
    
    public static Manager getInstance() {
        if (sInstance == null) {
            sInstance = new Manager();
        }
        return sInstance;
    }
    public static void ping(){
        WmMsgHandler.getInstance().ping();
        WarehouseManagerController.getInstance().ping();
        WarehouseInputMgr.getInstance().ping();
        WarehouseOutputMgr.getInstance().ping();
        RobotInputMgr.getInstance().ping();
        RobotOutputMgr.getInstance().ping();
    }
}
