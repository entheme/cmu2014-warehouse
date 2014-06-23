package com.lge.warehouse.manager.OrderStatemachine;

import java.io.Serializable;

public class RobotMoveToX extends WMorderStatemachineState implements Serializable{

	int ThisStateID;
	
	public RobotMoveToX(WahouseStateMachine warehousestatemachine,
			int thisStateID) {
		super(warehousestatemachine);
		ThisStateID = thisStateID;
	}

	@Override
	public void Evt_WareHouseSensorIsOn(int iSensorNum) {
		System.out.println("RobotMoveTo" + ThisStateID +": get Evt_WareHouseSensorIsOn" + iSensorNum);
		
		if(ThisStateID == iSensorNum)
		{
			System.out.println("Robot is near to station ");
			warehousestatemachine.setState(warehousestatemachine.getRobotAtXst(ThisStateID-1));
		}
		else
		{
			
		}
		
	}

	@Override
	public void Evt_RobotErrorStateChange(int iRobotErrorState) {
		System.out.println("RobotMoveToX" + ThisStateID +" Evt_RobotErrorStateChange : #" + iRobotErrorState + "is change");
		AdoinoErrorState tempState = (AdoinoErrorState)warehousestatemachine.getAduinoError();
		tempState.SetBeforeErrorState(this);
		tempState.Evt_RobotErrorStateChange(iRobotErrorState);
		warehousestatemachine.setState(tempState);
	}

	@Override
	public void Evt_WareHouseErrorStateChange(int iWareHouseState) {
		System.out.println("RobotMoveToX" + ThisStateID +" Evt_WareHouseStateChange : #" + iWareHouseState + "is change");
		AdoinoErrorState tempState = (AdoinoErrorState)warehousestatemachine.getAduinoError();
		tempState.SetBeforeErrorState(this);
		tempState.Evt_WareHouseErrorStateChange(iWareHouseState);
		warehousestatemachine.setState(tempState);
	}
	
	@Override
	public String toString() {
		return "RobotMoveToX"+ThisStateID;
	}

}
