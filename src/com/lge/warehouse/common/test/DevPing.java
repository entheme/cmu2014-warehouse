/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.common.test;

import static com.lge.warehouse.common.test.DeviceInput.getInstance;
import com.lge.warehouse.manager.ArduinoConnector;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jaehak.lee
 */
public class DevPing implements Runnable {
        
	public String mStr = null;
        public Socket socket;
        private BufferedWriter bos;
        private int portNum;
                
	/** Creates a new instance of Worker */
	public DevPing(Socket socket, int portNum) {
            this.socket = socket;
            this.portNum = portNum;
            try {
                bos = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (IOException ex) {
                Logger.getLogger(WarehouseConClient.class.getName()).log(Level.SEVERE, null, ex);
            }
	}

	public void sendMessage(String message) {
            try {
                bos.write(message);
                bos.flush();
                //System.out.println("sendMessage: " + message);
            } catch (IOException ex) {
                Logger.getLogger(WarehouseConClient.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
        
        @Override
        public void run() {
            while(true) {
                try {
                    Thread.sleep(1000);
                    if(portNum == 507)
                        sendMessage("w\n");
                    else if(portNum == 550)
                        sendMessage("r\n");
                } catch (InterruptedException ex) {
                    Logger.getLogger(DevPing.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
        
        public static void start(DevPing ping) {
            new Thread(ping).start();
        }  
}
