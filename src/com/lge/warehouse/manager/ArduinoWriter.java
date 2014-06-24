package com.lge.warehouse.manager;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ArduinoWriter {
	
	private String targetIP		= null;		// IP address of Ardunio
	private Socket clientSocket = null;		// The socket.
	private int portNum			= 504;		// Port number of socket write
	BufferedWriter socketWriter	= null;		// Socket output to server

	public ArduinoWriter(String ipaddr) {
		
		this.targetIP = ipaddr;
	}

        public void setPortNum(int portNum)
        {
            this.portNum = portNum;
        }
        
	public boolean writeData(String cmd)
 	{
		if (cmd == null)
		{
			System.out.println("Invalid command for arduino..."+cmd);
			return false;
		}
		
		if (clientSocket == null)
		{
			try	
	   		{
	   			clientSocket = new Socket(targetIP, portNum);
	   			socketWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			} catch (Exception e) {
				System.err.println( "Socket Error::" + e);
				return false;
			}
			
			try {
				socketWriter.write(cmd, 0, cmd.length());
				socketWriter.flush();
				
				System.out.println("Disconnect...");
				socketWriter.close();
				clientSocket.close();
				clientSocket = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return true;
 	}
}
