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
import static com.lge.warehouse.manager.WarehouseManagerController.logger;
import com.lge.warehouse.util.InventoryName;
import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.QuantifiedWidget;
import com.lge.warehouse.util.WarehouseInventoryInfo;
import com.lge.warehouse.util.WarehouseStatus;
import org.apache.log4j.Logger;

/**
 *
 * @author seuki77
 */
public class RobotInputMgr extends DeviceInputMgr {
    private static RobotInputMgr sInstance = null;
    static Logger logger = Logger.getLogger(RobotInputMgr.class);
    ArdunioReader mArdunioReader = new ArdunioReader();
    
    private RobotInputMgr() {
        super(WComponentType.ROBOT_INPUT_MGR);
    }
    
    public static RobotInputMgr getInstance() {
        if (sInstance == null) {
            sInstance = new RobotInputMgr();
        }
        return sInstance;
    }
    
    @Override
    protected void processingData(String inputData) {
        String value = null;
         
        if(inputData.startsWith("E") == true) { 
             value  = inputData.substring(1);
            //Send processed robot's error information to WAREHOUSE_MANAGER_CONTROLLER
            sendMsg(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, EventMessageType.SEND_ROBOT_ERROR, value);
        }
    }
    
    @Override
    protected void threadStart(){
            super.threadStart();
            setPortNum(505);
     }
    
    @Override
    public void ping() {   
        String test = new String("RobotInputMgr");
        sendMsg(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, EventMessageType.COMPONENT_HELLO, test);
    }
    
    public static void start() {
        logger.info("RobotInputMgr start");
        new Thread(getInstance()).start();
    }
}
