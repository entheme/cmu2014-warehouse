/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author jaehak.lee
 */
package com.lge.warehouse.manager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;


public class ArduinoConnector {
	static Logger logger = Logger.getLogger(DeviceInputMgr.class);
	private ServerSocket serverSocket	= null;
	private Socket clientSocket		= null;		// The socket.
	private int portNum			= 505;		// Port number for socket read
	private String ardData			= null;
        private BufferedReader in = null;
        private BufferedWriter out = null;
        private boolean isRun = false;
        
	public ArduinoConnector() {
		
	}
	
        public void setPortNum(int portNum)
        {
            this.portNum = portNum;
        }
        
	public boolean startServer() {
		try
		{
                    serverSocket = new ServerSocket(portNum);
                    logger.info("Waiting for Arduino on port " + portNum + "." );
                    
                    clientSocket = serverSocket.accept();
                    clientSocket.setSoTimeout(7000);
                    
                    logger.info("Client is connected: " + clientSocket.toString());
                    
                    in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    
                    isRun = true;
                }
		catch (IOException e)
                {
                    logger.info( "Could not instantiate socket on port: " + portNum );
                    return false;
                }
		
		return true;
	}
        
        public synchronized boolean IsConnected() {
            return isRun;
        }
        
        private synchronized void connectionLost() {
                
                if(IsConnected() == false)
                    return;
                
                logger.debug("Client is disconnected");
                
                try {
                    isRun = false;
                                       
                    serverSocket.close();
                    clientSocket.close();
                    in.close();
                    out.close();

                    serverSocket = null;
                    clientSocket = null;
                    in = null;
                    out = null;
 
                } catch (IOException ex) {
                    logger.info("connectionLost processing exception is happend");
                }
        }
        
	public String readData()
 	{
		ardData = null;
                         
                if(IsConnected() == false)
                {
                    logger.debug("client was not connected!!");
                    return null;
                }
                       
		try
                {
                    if(in != null)
                    {
                        ardData = in.readLine();
                        if(ardData != null)
                            logger.debug("data :" + ardData);
                    }
                        
	    	//System.out.println ("Data from Arduino: " + ardData);
		} catch (IOException e) {
			logger.debug("readLine failed::");
                        connectionLost();
                        return null;
		}
		
		return ardData;
 	}
	
        public boolean writeData(String cmd)
 	{             
		if (cmd == null)
		{
                    logger.debug("Invalid command for arduino..."+cmd);
                    return false;
		}
				
                if(IsConnected() == false)
                {
                    logger.debug("client was not connected!!");
                    return false;
                }
		
                try {
                        logger.info("Write data: " + cmd + " to" + clientSocket.toString());
                        if(out != null)
                        {
                            out.write(cmd, 0, cmd.length());
                            out.flush();
                        }
                        
                } catch (IOException e) {
                        e.printStackTrace();
                }
		return true;
 	}
}

