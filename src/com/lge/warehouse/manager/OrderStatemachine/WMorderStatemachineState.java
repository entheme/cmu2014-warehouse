package com.lge.warehouse.manager.OrderStatemachine;

import java.io.Serializable;


/*
 * 오더를 처리하는 동안 나타나는 스테이트들을 정의 하기 위한 클래스.
 * 모든 스테이트가 모든 Evt에 반응할 필요는 없음으로 필요한 부분만 구현하려고 인터페이스 클레스로 구현 하지 않음.
 * 분석한 결과 모든 스테이트에서 Robot, Warehouse 이벤트에 반응해야 함으로 abstract 처리 하였음. 
 * -> 전체가 동일하게 반응한다면 그냥 abstract 하지 않고 그냥 써도 될듯 final로 묶어서..
 * 
 */
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
