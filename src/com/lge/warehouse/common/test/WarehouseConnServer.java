/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.common.test;

import com.lge.warehouse.manager.ArduinoConnector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jaehak.lee
 */
public class WarehouseConnServer {
        static ArduinoConnector mArduinoCon = new ArduinoConnector();	
    
        public static final void main(String[] args){
		 
            DeviceInput.start(mArduinoCon, 550); //for robot
            //DeviceInput.start(mArduinoCon, 507); //for warehouse
            DeviceOutput.start(mArduinoCon);
            
            while(true)
            {
                try {
                    Thread.sleep(60*1000*1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(WarehouseConnServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
	}
}
