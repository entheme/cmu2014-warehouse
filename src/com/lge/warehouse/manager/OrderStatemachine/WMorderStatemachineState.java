package com.lge.warehouse.manager.OrderStatemachine;

import java.io.Serializable;
import java.util.List;

public abstract class WMorderStatemachineState implements Serializable{
	
	WahouseStateMachine warehousestatemachine;
	List<WMorderStatemachineState> navigationPath;
	
	public WMorderStatemachineState(){
		this.warehousestatemachine = null;
	}
	
	public WMorderStatemachineState(WahouseStateMachine warehousestatemachine) {
		this.warehousestatemachine = warehousestatemachine;
	}
	
	public void SetWarehouseStatemachine(WahouseStateMachine warehousestatemachine) {
		this.warehousestatemachine = warehousestatemachine;
	}
	
	public void setNavigationPath(List<WMorderStatemachineState> newPath)
	{
		navigationPath = newPath;
		
		String tempPath = "[WMorderStatemachineState] Get path is";
    	for(WMorderStatemachineState state : navigationPath)
		{
    		tempPath = tempPath + "+" + state.toString();
		}
    	System.out.println(tempPath);
    	
	}
	
	public void PathClearAndSetnextPath(List<WMorderStatemachineState> newPath)
	{
		if(newPath.size() <= 1)
		{
			System.out.println("[WMorderStatemachineState] final path");
			return;
		}
		else
		{
			newPath.remove(0);
			navigationPath = newPath;
		}
		
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
	
	public void Evt_initComplete()
	{System.out.println("[WMorderStatemachineState]Evt_initComplete : not implement event");};
	
	public void Evt_NewOrder(List<WMorderStatemachineState> path)
	{System.out.println("[WMorderStatemachineState]Evt_NewOrder : not implement event");};
	
	public void Evt_WareHouseSensorIsOn(int iSensorNum)
	{System.out.println("[WMorderStatemachineState]Evt_WareHouseSensorOn : #" + iSensorNum + "is change, not implement event");};
	
	public void Evt_WareHouseButtonIsOn(int iButtonNum)
	{System.out.println("[WMorderStatemachineState]Evt_WareHouseButtonIsOn : #" + iButtonNum + "is change, not implement event");};
	
	abstract public void Evt_RobotErrorStateChange(int iRobotErrorState);
	//{System.out.println("Evt_RobotErrorStateChange : #" + iRobotErrorState + "is change, not implement event");};
	
	abstract public void Evt_WareHouseErrorStateChange(int iWareHouseState);
	//{System.out.println("Evt_WareHouseStateChange : #" + iRobotErrorState + "is change, not implement event");};
}