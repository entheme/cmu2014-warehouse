/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.manager;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WBus;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.common.bus.p2p.P2PConnection;
import com.lge.warehouse.common.bus.p2p.P2PReceiver;
import com.lge.warehouse.common.bus.p2p.P2PSender;
import org.apache.log4j.Logger;

/**
 *
 * @author seuki77
 */
public class WarehouseInputMgr extends DeviceInputMgr {
    private static WarehouseInputMgr sInstance = null;
    static Logger logger = Logger.getLogger(WarehouseInputMgr.class);
    ArdunioReader mArdunioReader = new ArdunioReader();
        
    private WarehouseInputMgr() {
      super(WComponentType.WAREHOUSE_INPUT_MGR);
    }
    
    public static WarehouseInputMgr getInstance() {
        if (sInstance == null) {
            sInstance = new WarehouseInputMgr();
        }
        return sInstance;
    }

    @Override
    protected void processingData(String inputData) {
        String value = null;
         
        if(inputData.startsWith("L") == true) { 
            value  = inputData.substring(1);
             /*Send processed warehouse's inventory complete information as inventory station number(0~3) to WAREHOUSE_MANAGER_CONTROLLER
               So, the value is inventory stattion number like following,
               inventory station number 0 : shipping center
               inventory station number 1 ~ 3 : inventory station  
             */
            sendMsg(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, EventMessageType.SEND_LOAD_STATUS, value);
        }
        else if(inputData.startsWith("R") == true) {
             value  = inputData.substring(1);
             /*Send processed robot's position to WAREHOUSE_MANAGER_CONTROLLER whenever the robot's position is changed
               To do : explain the value. 
             */
             sendMsg(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, EventMessageType.SEND_POS_STATUS, value);
        }
    }
 
    @Override
    protected void threadStart(){
            super.threadStart();
            setPortNum(507);
    }
    
    @Override
    public void ping() {   
        String test = new String("WarehouseInputMgr");
        sendMsg(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, EventMessageType.COMPONENT_HELLO, test);
    }
    
    public static void start() {
        logger.info("WarehouseInputMgr start");
        new Thread(getInstance()).start();
    }  
}
