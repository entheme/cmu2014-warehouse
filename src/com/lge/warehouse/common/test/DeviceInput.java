/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.common.test;

import com.lge.warehouse.manager.ArduinoConnector;
import com.lge.warehouse.manager.RobotInputMgr;
import static com.lge.warehouse.manager.WarehouseInputMgr.getInstance;

/**
 *
 * @author jaehak.lee
 */
public class DeviceInput implements Runnable  {
        ArduinoConnector mArduinoCon = null;
        protected int mPortNum = 505;	
        private static DeviceInput sInstance = null;
         
        private DeviceInput() {
      
        }
        protected void setArduinoConnector(ArduinoConnector arduinoCon) {
            mArduinoCon = arduinoCon;
        }     
        
        public void setPortNum(int portNum)
        {
            mPortNum = portNum;
        }
             
        public static DeviceInput getInstance() {
            if (sInstance == null) {
                sInstance = new DeviceInput();
            }
            return sInstance;
        }
            
        @Override
        public void run() {
            // TODO Auto-generated method stub   
           String inputData = null;
           mArduinoCon.setPortNum(mPortNum);

           System.out.println("Call startServer");
           if(mArduinoCon.startServer() == true) {
               while(true) {
                   if(mArduinoCon.IsConnected() == false)
                       mArduinoCon.startServer();
                   inputData = mArduinoCon.readData();
                   if(inputData != null) {
                       System.out.println(inputData);
                   }
               }
           }
        }
        
        public static void start(ArduinoConnector arduinoCon, int portNum) {
            getInstance().setArduinoConnector(arduinoCon);
            getInstance().setPortNum(portNum);
            new Thread(getInstance()).start();
        }  
}
