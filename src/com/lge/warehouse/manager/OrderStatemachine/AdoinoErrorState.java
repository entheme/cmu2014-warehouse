package com.lge.warehouse.manager.OrderStatemachine;

import java.io.Serializable;

public class AdoinoErrorState extends WMorderStatemachineState implements Serializable{

	int RobotError;
	int WarehouseError;
	
	WMorderStatemachineState BeforeErrorState;
	WMorderStatemachineState TempBeforeErrorState;
	
	public AdoinoErrorState(WahouseStateMachine warehousestatemachine) 
	{
		super(warehousestatemachine);
		RobotError = 0;
		WarehouseError = 0;
	}
	
	public void SetBeforeErrorState(WMorderStatemachineState BeforeError)
	{
		BeforeErrorState = BeforeError;
	}

	@Override
	public void Evt_RobotErrorStateChange(int iRobotErrorState) {
		System.out.println("AdoinoErrorState Get RobotError State:" + iRobotErrorState);
		RobotError = iRobotErrorState;
		DeterminRecoverLastStateOrNot();
	}

	@Override
	public void Evt_WareHouseErrorStateChange(int iWareHouseState) {
		System.out.println("WarehouseErrorState Get WarehouseErrorState:" + iWareHouseState);
		WarehouseError = iWareHouseState;
		DeterminRecoverLastStateOrNot();
	}
	
	private void DeterminRecoverLastStateOrNot()
	{
		if(RobotError == 0 && WarehouseError == 0)
		{
			warehousestatemachine.setState(BeforeErrorState);
		}
		else
		{
			System.out.println("Error is remain! Robot Error:" + RobotError + " Warehouse :" + WarehouseError);
		}
	}
	
	@Override
	public String toString() {
		return "RobotErrorState";
	}

}