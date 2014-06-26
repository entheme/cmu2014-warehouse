/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.manager;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import org.apache.log4j.Logger;

/**
 *
 * @author seuki77
 */
public class RobotInputMgr extends DeviceInputMgr {
    private static RobotInputMgr sInstance = null;
    static Logger logger = Logger.getLogger(RobotInputMgr.class);
    
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
        
        if(inputData == null) {
            logger.info("inpuptData is null");
            return;
        }
          
        if(inputData.startsWith("E") == true) { 
             value  = inputData.substring(1);
            //Send processed robot's error information to WAREHOUSE_MANAGER_CONTROLLER
            logger.info("inputData: " + inputData +" parsedValue: " + value + " is received from robot");
            sendMsg(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, EventMessageType.ROBOT_ERROR_STATUS, value);
        } else if(inputData.startsWith("A") == true) { 
            //Now, robot is arrived at some inventory station. Now, robot can receive the message such as moving.
            logger.info("inputData: " + inputData + " is received from robot(Now, robot is ready to recive message)");
            sendMsg(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, EventMessageType.ROBOT_IS_ARRIVE, null);
        } 
    }
    
    @Override
    protected void connectionDone() {
        logger.info("Robot is connected");
        sendMsg(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, EventMessageType.ROBOT_IS_CONNECTED, null);
    } 

    @Override
    protected void connectionLost() {
        logger.info("Robot is disconnected");
        sendMsg(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, EventMessageType.ROBOT_IS_DISCONNECTED, null);
    }
    
    @Override
    protected void threadStart(){
            super.threadStart();
            setPortNum(550);
     }
    
    @Override
    public void ping() {   
        String test = new String("RobotInputMgr");
        sendMsg(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, EventMessageType.COMPONENT_HELLO, test);
    }
    
    public static void start(ArduinoConnector arduinoCon) {
        logger.info("RobotInputMgr start");
        getInstance().setArduinoConnector(arduinoCon);
        new Thread(getInstance()).start();
    }  
}
