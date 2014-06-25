/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.common.test;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jaehak.lee
 */
public class WarehouseConClient {
    
        private Socket socket;
        private BufferedWriter bos;
	private BufferedReader bis;
	

	/** Creates a new instance of Worker */
	public WarehouseConClient() {
		socket = null;
		bis = null;
		bos = null;
	}

	public void connect(String host, int port) {
            try {
                socket = new Socket(host, port);
            } catch (IOException ex) {
                Logger.getLogger(WarehouseConClient.class.getName()).log(Level.SEVERE, null, ex);
            }
      
            try {
                bos = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (IOException ex) {
                Logger.getLogger(WarehouseConClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                bis = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException ex) {
                Logger.getLogger(WarehouseConClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            
	}

	public void sendMessage(String message) {
            try {
                bos.write(message);
                bos.flush();
            } catch (IOException ex) {
                Logger.getLogger(WarehouseConClient.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
        
        public void receiveMessage() {  
            try {  
                 char[] chars = new char[4096];
                 bis.read(chars);
                 System.out.println(String.valueOf(chars));  
            } catch (IOException ex) {
                Logger.getLogger(WarehouseConClient.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("exeception...");
            }  
        }
	public void disconnect()  {
            try {
                bis.close();
                bos.close();
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(WarehouseConClient.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
        
      public static final void main(String[] args){
            
            WarehouseConClient w = new WarehouseConClient();
            
            if(args.length > 0) {
                w.connect(args[0], Integer.parseInt(args[1]));
            } else {
                 //w.connect("127.0.0.1", 507);
                 w.connect("127.0.0.1", 550);
            }

            StringBuffer message = new StringBuffer();
            message.append("E9\n");
            //message.append("Host: 0pen.us\r\n");
            //message.append("\r\n");

            w.sendMessage(new String(message));
              
            while(true)
            {   
                w.receiveMessage();
            }
      }
}
