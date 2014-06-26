/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.common.test;



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
public class WarehouseConClient {
    
        private Socket socket;
        private BufferedWriter bos;
	private BufferedReader bis;
	public String mStr = null;
        
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
                System.out.println("sendMessage: " + message);
            } catch (IOException ex) {
                Logger.getLogger(WarehouseConClient.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
        
        public void receiveMessage() {  
            try {  
                 char[] chars = new char[4096];
                 bis.read(chars);
                 mStr = String.valueOf(chars);
                 System.out.println("ReceiveMssage: " + mStr);  
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
                 
      public static final void main(String[] args){
            
            WarehouseConClient w = new WarehouseConClient();
         
            //Note: Whne you use this client sample program, you must comment out following code!!!!
            //"clientSocket.setSoTimeout(7000);" in ArduinoConnector.java in com.lge.warehouse.manager. 
            
            //int portNum = 507; //warehouse
            int portNum = 550; //robot
          
            if(args.length > 0) {
                w.connect(args[0], Integer.parseInt(args[1]));
            } else {
                 //w.connect("128.237.235.111", portNum);
                 w.connect("127.0.0.1", portNum);
            }
            
            while(true) {
                if(portNum == 550) { //robot
                    w.receiveMessage();
                    if(w.mStr.startsWith("M") == true)
                        w.sendMessage("A\n");
                } else if(portNum == 507) { //warehouse
                    w.getCommand();
                    w.sendMessage(w.mStr+"\n");
                }
            }
        }
}
