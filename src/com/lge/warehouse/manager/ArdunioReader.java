package com.lge.warehouse.manager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class ArdunioReader {
	
	private ServerSocket serverSocket	= null;
	private Socket clientSocket			= null;		// The socket.
	private int portNum					= 505;		// Port number for socket read
	
	private String ardData				= null;

	public ArdunioReader() {
		
	}
	
        public void setPortNum(int portNum)
        {
            this.portNum = portNum;
        }
        
	public boolean startServer() {
		try
		{
    		serverSocket = new ServerSocket(portNum);
    		System.out.println ( "\n\nWaiting for Arduino on port " + portNum + "." );
    	}
		catch (IOException e)
    	{
    		System.err.println( "\n\nCould not instantiate socket on port: " + portNum );
    		return false;
    	}
		
		return true;
	}

	public String readData()
 	{
		ardData = null;
		
		if (serverSocket == null)
		{
			System.out.println("Error:: Server is not running...");
			return null;
		}
		
		try
		{
    		clientSocket = serverSocket.accept();
    	}
		catch (Exception e)
    	{
    		System.err.println("Accept failed.");
    		return null;
    	}
		
		BufferedReader in = null;
		try {
			in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try
	    {
			//while (in.ready() == false)
			//	Thread.sleep(50);
			ardData = in.readLine();
	    	//System.out.println ("Data from Arduino: " + ardData);
		} catch (Exception e) {
			System.err.println("readLine failed::");
    		return null;
		}
		
		// To DO
		getCmdFromData(ardData);
		
		return ardData;
 	}
	
	private int getCmdFromData(String str) {
		return 0;
	}
}
