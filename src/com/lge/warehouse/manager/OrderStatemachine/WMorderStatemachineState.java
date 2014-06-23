package com.lge.warehouse.manager.OrderStatemachine;

import java.io.Serializable;


public abstract class WMorderStatemachineState implements Serializable{
	
	WahouseStateMachine warehousestatemachine;
	
	
	public WMorderStatemachineState(WahouseStateMachine warehousestatemachine) {
		this.warehousestatemachine = warehousestatemachine;
	}
	
	public void SetWarehouseStatemachine(WahouseStateMachine warehousestatemachine) {
		this.warehousestatemachine = warehousestatemachine;
	}
	public void Evt_initComplete()
	{System.out.println("Evt_initComplete : not implement event");};
	
	public void Evt_NewOrder()
	{System.out.println("Evt_NewOrder : not implement event");};
	
	public void Evt_WareHouseSensorIsOn(int iSensorNum)
	{System.out.println("Evt_WareHouseSensorOn : #" + iSensorNum + "is change, not implement event");};
	
	public void Evt_WareHouseButtonIsOn(int iButtonNum)
	{System.out.println("Evt_WareHouseButtonIsOn : #" + iButtonNum + "is change, not implement event");};
	
	abstract public void Evt_RobotErrorStateChange(int iRobotErrorState);
	//{System.out.println("Evt_RobotErrorStateChange : #" + iRobotErrorState + "is change, not implement event");};
	
	abstract public void Evt_WareHouseErrorStateChange(int iWareHouseState);
	//{System.out.println("Evt_WareHouseStateChange : #" + iRobotErrorState + "is change, not implement event");};
}
