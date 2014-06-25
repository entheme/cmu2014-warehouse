/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.common.test;

import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.manager.ArduinoConnector;
import com.lge.warehouse.manager.RobotOutputMgr;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author jaehak.lee
 */
public class DeviceOutput implements Runnable {
    
    private static DeviceOutput sInstance = null;
    ArduinoConnector mArduinoCon = null;
    String mStr = null;
    
    private DeviceOutput() {
        
    }
    
    protected void setArduinoConnector(ArduinoConnector arduinoCon) {
      mArduinoCon = arduinoCon;
    }
    
    public static DeviceOutput getInstance() {
        if (sInstance == null) {
            sInstance = new DeviceOutput();
        }
        return sInstance;
    }
    
    public void getCommand() {
        InputStreamReader isr = new InputStreamReader(System.in);
        try {
            BufferedReader br = new BufferedReader(isr);
            System.out.print("\nInsert command: "); 
            mStr = br.readLine();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(DeviceOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while(true) {
            getCommand();
            System.out.println("Send Data :" + mStr);
            mArduinoCon.writeData("K");
        }
    }
    
    public static void start(ArduinoConnector arduinoCon) {
        getInstance().setArduinoConnector(arduinoCon);
        new Thread(getInstance()).start();
    }  
}
