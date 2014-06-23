package com.lge.warehouse.manager.OrderStatemachine;

import java.io.Serializable;

public class RobotAtX extends WMorderStatemachineState implements Serializable{

	int ThisStateID;
	
	public RobotAtX(int thisStateID) {
		super();
		ThisStateID = thisStateID;
	}
	
	public RobotAtX(WahouseStateMachine warehousestatemachine, int thisStateID) {
		super(warehousestatemachine);
		ThisStateID = thisStateID;
	}
	
	public int getID()
	{
		return ThisStateID;
	}

	@Override
	public void Evt_WareHouseButtonIsOn(int iButtonNum) {
		System.out.println(toString() + ": get Evt_WareHouseButtonIsOn" + iButtonNum);
		// 

		if(ThisStateID == iButtonNum)
		{
			System.out.println(toString() + "Loding complete... ");
			if(iButtonNum == 4)
			{
				System.out.println(toString() + "Order complete...");
				warehousestatemachine.setState(warehousestatemachine.getWaitNewOrderState());
			}
			else
			{
				System.out.println(toString() + "Robot will go next station");
				warehousestatemachine.getRobotMoveToXst(ThisStateID).PathClearAndSetnextPath(navigationPath);
				warehousestatemachine.setState(warehousestatemachine.getRobotMoveToXst(ThisStateID));
			}
		}
		else
		{
			//The sensor value is not relate this inventory station
			// Need to make Error??
		}
	}

	@Override
	public void Evt_RobotErrorStateChange(int iRobotErrorState) {
		System.out.println(toString() + "Evt_RobotErrorStateChange : #" + iRobotErrorState + "is change");
		AdoinoErrorState tempState = (AdoinoErrorState)warehousestatemachine.getAduinoError();
		tempState.SetBeforeErrorState(this);
		tempState.Evt_RobotErrorStateChange(iRobotErrorState);
		warehousestatemachine.setState(tempState);
	}

	@Override
	public void Evt_WareHouseErrorStateChange(int iWareHouseState) {
		System.out.println(toString()+ "Evt_WareHouseStateChange : #" + iWareHouseState + "is change");
		AdoinoErrorState tempState = (AdoinoErrorState)warehousestatemachine.getAduinoError();
		tempState.SetBeforeErrorState(this);
		tempState.Evt_WareHouseErrorStateChange(iWareHouseState);
		warehousestatemachine.setState(tempState);
	}
	
	@Override
	public String toString() {
		return "[RobotAtX"+ThisStateID+"]";
	}

}

