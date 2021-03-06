package com.lge.warehouse.manager.OrderStatemachine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lge.warehouse.manager.OrderStatemachine.CmdToOther;
import com.lge.warehouse.util.QuantifiedWidget;

public abstract class WMorderStatemachineState implements Serializable{
	
	WahouseStateMachine warehousestatemachine;
	List<WMorderStatemachineState> navigationPath;
	List<WMorderStatemachineState> passedNavigationPath;
	
	public WMorderStatemachineState(){
		this.warehousestatemachine = null;
		passedNavigationPath = new ArrayList<WMorderStatemachineState>();
		navigationPath = new ArrayList<WMorderStatemachineState>();
	}
	
	public WMorderStatemachineState(WahouseStateMachine warehousestatemachine) {
		passedNavigationPath = new ArrayList<WMorderStatemachineState>();
		navigationPath = new ArrayList<WMorderStatemachineState>();
		this.warehousestatemachine = warehousestatemachine;
	}
	
	public void SetWarehouseStatemachine(WahouseStateMachine warehousestatemachine) {
		this.warehousestatemachine = warehousestatemachine;
	}
	
	public void setPassedNavigationPath(List<WMorderStatemachineState> NewpassedNavigationPath) 
	{
		passedNavigationPath.clear();
		for(WMorderStatemachineState qw : NewpassedNavigationPath)
		{
			passedNavigationPath.add(qw);
	 	}
		
		String tempPath = "[WMorderStatemachineState] Get PassedNavation path is";
		for(WMorderStatemachineState state : passedNavigationPath)
		{
			tempPath = tempPath + "+" + state.toString();
		}
		System.out.println(tempPath);
	}

	public List<WMorderStatemachineState> getPassedNavigationPath() {
		return passedNavigationPath;
	}
	
	public void flushPassNavigationPath(){
		passedNavigationPath.clear();
	}

	public void setNavigationPath(List<WMorderStatemachineState> newPath)
	{
		if(newPath.size() == 0)
		{
			System.out.println("[WMorderStatemachineState] final path");
			return;
		}
		else
		{
			navigationPath.clear();
			for(WMorderStatemachineState qw : newPath)
			{
				navigationPath.add(qw);
			}
				
			String tempPath = "[WMorderStatemachineState]setNavigationPath :: Get Navigationpath is";
			for(WMorderStatemachineState state : navigationPath)
			{
				tempPath = tempPath + "+" + state.toString();
			}
			System.out.println(tempPath);
		}
    	
	}
	
	public void PathClearAndSetnextPath(List<WMorderStatemachineState> newPath)
	{
		if(newPath.size() == 0)
		{
			System.out.println("[WMorderStatemachineState] final path");
			return;
		}
		else
		{
			passedNavigationPath.add(newPath.get(0)); // set visited path
			newPath.remove(0);
			navigationPath.clear();
			for(WMorderStatemachineState qw : newPath)
			{
				navigationPath.add(qw);
		 	}
		}
		
		String PassedtempPath = "[WMorderStatemachineState] Visit path is";
    	for(WMorderStatemachineState state : passedNavigationPath)
		{
    		PassedtempPath = PassedtempPath + "+" + state.toString();
		}
    	System.out.println(PassedtempPath);
		
		String tempPath = "[WMorderStatemachineState] Remain path is";
    	for(WMorderStatemachineState state : navigationPath)
		{
    		tempPath = tempPath + "+" + state.toString();
		}
    	System.out.println(tempPath);
    	
	}

	
	public List<WMorderStatemachineState> getNavigationPath()
	{
		return navigationPath;
	}
	
	public CmdToOther Evt_initComplete()
	{
		System.out.println("[WMorderStatemachineState]Evt_initComplete : not implement event");
		return CmdToOther.CMD_NONE;
	};
	
	public CmdToOther Evt_NewOrder(List<WMorderStatemachineState> path)
	{
		System.out.println("[WMorderStatemachineState]Evt_NewOrder : not implement event");
		return CmdToOther.CMD_NONE;
	};
	
	public CmdToOther Evt_WareHouseSensorIsOn(int iSensorNum)
	{
		System.out.println("[WMorderStatemachineState]Evt_WareHouseSensorOn : #" + iSensorNum + "is change, not implement event");
		return CmdToOther.CMD_NONE;
	};
	
	public CmdToOther Evt_WareHouseButtonIsOn(int iButtonNum)
	{
		System.out.println("[WMorderStatemachineState]Evt_WareHouseButtonIsOn : #" + iButtonNum + "is change, not implement event");
		return CmdToOther.CMD_NONE;
	};
	
	public CmdToOther Evt_ReadyToGo()
	{
		System.out.println("[WMorderStatemachineState]Evt_ReadyToGo : not implement event");
		return CmdToOther.CMD_NONE;
	}
	
	abstract CmdToOther Evt_RobotErrorStateChange(int iRobotErrorState);
	//{System.out.println("Evt_RobotErrorStateChange : #" + iRobotErrorState + "is change, not implement event");};
	
	abstract CmdToOther Evt_WareHouseErrorStateChange(int iWareHouseState);
	//{System.out.println("Evt_WareHouseStateChange : #" + iRobotErrorState + "is change, not implement event");};
}