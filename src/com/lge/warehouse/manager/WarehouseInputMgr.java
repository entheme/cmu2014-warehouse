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
public class WarehouseInputMgr implements Runnable {
    private static WarehouseInputMgr sInstance = null;
    static Logger logger = Logger.getLogger(WarehouseInputMgr.class);
    ArdunioReader mArdunioReader = new ArdunioReader();
        
    private WarehouseInputMgr() {
     
    }
    
    public static WarehouseInputMgr getInstance() {
        if (sInstance == null) {
            sInstance = new WarehouseInputMgr();
        }
        return sInstance;
    }

    @Override
    public void run() {
            // TODO Auto-generated method stub
            String inputData = null;
            String value = null;
            if(mArdunioReader.startServer() == true) {
                while(true) {
                    inputData = mArdunioReader.readData();
                    if(inputData != null) {
                       
                        if(inputData.startsWith("L") == true || inputData.startsWith("R") == true) {
                          value  = inputData.substring(1);
                          //ToDo : Send value to WarehouseManageController
                        }
                    }
                }
            }
    }

    public static void start() {
        logger.info("WarehouseInputMgr start");
        new Thread(getInstance()).start();
    }  
}
