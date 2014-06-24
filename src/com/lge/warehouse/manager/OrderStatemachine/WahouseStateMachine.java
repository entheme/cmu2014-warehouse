package com.lge.warehouse.manager.OrderStatemachine;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class WahouseStateMachine implements Serializable{
	
	InitState initState;
	WaitNewOrderState waitNewOrderState;
	RobotMoveToX robotMoveToX[] = new RobotMoveToX[4];
	RobotAtX robotAtX[] = new RobotAtX[4];
	AdoinoErrorState aduinoHasError;
	
	
	private WMorderStatemachineState CurrentState;
	WMorderStatemachineState SaveState;
	
	public WahouseStateMachine()
	{
		initState = new InitState(this);
		waitNewOrderState = new WaitNewOrderState(this);
		robotMoveToX[0] = new RobotMoveToX(this,1);
		robotMoveToX[1] = new RobotMoveToX(this,2);
		robotMoveToX[2] = new RobotMoveToX(this,3);
		robotMoveToX[3] = new RobotMoveToX(this,4);
		robotAtX[0] = new RobotAtX(this,1);
		robotAtX[1] = new RobotAtX(this,2);
		robotAtX[2] = new RobotAtX(this,3);
		robotAtX[3] = new RobotAtX(this,4);
		aduinoHasError = new AdoinoErrorState(this);
		
		//SaveState = initState;
		// If This Save state is not initstate mean Error recovery logic is run
		CurrentState = waitNewOrderState;
		load();
		if(SaveState != null)
		{
			setState(SaveState);
		}
		else
		{
			CurrentState = initState;
		}
		
	}
	
	public void Evt_initComplete() {CurrentState.Evt_initComplete();}
	public void Evt_NewOrder(List<WMorderStatemachineState> path) {CurrentState.Evt_NewOrder(path);}
	public void Evt_WareHouseSensorIsOn(int sensorNum) {CurrentState.Evt_WareHouseSensorIsOn(sensorNum);}
	public void Evt_WareHouseButtonIsOn(int buttonNum) {CurrentState.Evt_WareHouseButtonIsOn(buttonNum);}
	public void Evt_RobotErrorStateChange(int ErrorNum) {CurrentState.Evt_RobotErrorStateChange(ErrorNum);}
	public void Evt_WareHouseErrorStateChange(int ErrorNum) {CurrentState.Evt_WareHouseErrorStateChange(ErrorNum);}
	
		
    public WMorderStatemachineState getInitState() {return initState;}
    public WMorderStatemachineState getWaitNewOrderState() {return waitNewOrderState;}
	public WMorderStatemachineState getRobotMoveToXst(int targetX) {return robotMoveToX[targetX];}
	public WMorderStatemachineState getRobotAtXst(int targetX) {return robotAtX[targetX];	}
	public WMorderStatemachineState getAduinoError() {return aduinoHasError;	}
	
	public WMorderStatemachineState getCurrentState() { return CurrentState;	}
	public WMorderStatemachineState getLastState() {return SaveState;	}


	void setState(WMorderStatemachineState state) {
		System.out.println("[StateMachine] StateChange " + CurrentState + "->" + state);
    	this.CurrentState = state;
    	this.SaveState = this.CurrentState; 
    	save();
    	//TODO This function need to say other process
    }
	
	public void save(){
		BufferedOutputStream bufferOut = null;
    	
			try {
				bufferOut = new BufferedOutputStream(new FileOutputStream("WareHouseManagerState.txt"));
				ObjectOutputStream outDev = null;
				try {
					outDev = new ObjectOutputStream(bufferOut);
					outDev.writeObject(this.SaveState);
				} catch (IOException e) {
					e.printStackTrace();
				}finally {
					try {
						outDev.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}finally {
				try {
					bufferOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
    }
	
	public void load(){
    	
    	File mobFile = new File("WareHouseManagerState.txt");
    	BufferedInputStream bufferIn = null;
		
		if(!mobFile.exists())
		{
			System.out.println("No file - Saved order is none");
			return;
		}

		ObjectInputStream inObj = null;
		try {
			bufferIn = new BufferedInputStream(new FileInputStream(mobFile));
			inObj = new ObjectInputStream(bufferIn);
			Object inDev = null;
			inDev = inObj.readObject();
			
			SaveState = (WMorderStatemachineState)inDev;
			SaveState.SetWarehouseStatemachine(this);
			System.out.println("[StateMachine] Read State is " + SaveState.getClass());
			
		} catch (EOFException e){
			System.out.println("End of file");
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}finally {
			try {
				bufferIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	
    }


}
