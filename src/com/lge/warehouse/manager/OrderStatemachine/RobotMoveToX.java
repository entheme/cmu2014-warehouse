package com.lge.warehouse.manager.OrderStatemachine;

import java.io.Serializable;

public class RobotMoveToX extends WMorderStatemachineState implements Serializable{

	int ThisStateID; //what station heading for?
	
	public RobotMoveToX(int thisStateID) {
		super();
		ThisStateID = thisStateID;
	}
	public RobotMoveToX(WahouseStateMachine warehousestatemachine,
			int thisStateID) {
		super(warehousestatemachine);
		ThisStateID = thisStateID;
	}
	
	public int getID()
	{
		return ThisStateID;
	}

	@Override
	public void Evt_WareHouseSensorIsOn(int iSensorNum) {
		System.out.println(toString() + ": get Evt_WareHouseSensorIsOn" + iSensorNum);
		if(ThisStateID == iSensorNum)
		{
			if(navigationPath.get(0) instanceof RobotAtX)
			{
				RobotAtX tempRobotAtX = (RobotAtX)navigationPath.get(0);
				if(tempRobotAtX.getID() == ThisStateID)
				{
					System.out.println(toString() + "Robot is near to station");
					warehousestatemachine.getRobotAtXst(ThisStateID-1).PathClearAndSetnextPath(navigationPath);
					warehousestatemachine.setState(warehousestatemachine.getRobotAtXst(ThisStateID-1));
					// change state to wait worker button push.
				}
				else
				{
					System.out.println(toString() + "Path is wrong!!");
				}
			}
			else // this inventory station is not loading station 
			{
				System.out.println(toString() + "Bypass this inventory station");
				warehousestatemachine.getRobotMoveToXst(ThisStateID).PathClearAndSetnextPath(navigationPath);
				warehousestatemachine.setState(warehousestatemachine.getRobotMoveToXst(ThisStateID));
			}
		}
		else
		{
			// ignore sensor enent?
			System.out.println(toString() + "Activate Sensor is not for this station!!");
		}
		
	}

	@Override
	public void Evt_RobotErrorStateChange(int iRobotErrorState) {
		System.out.println(toString() + " Evt_RobotErrorStateChange : #" + iRobotErrorState + "is change");
		AdoinoErrorState tempState = (AdoinoErrorState)warehousestatemachine.getAduinoError();
		tempState.SetBeforeErrorState(this);
		tempState.Evt_RobotErrorStateChange(iRobotErrorState);
		warehousestatemachine.setState(tempState);
	}

	@Override
	public void Evt_WareHouseErrorStateChange(int iWareHouseState) {
		System.out.println(toString() + " Evt_WareHouseStateChange : #" + iWareHouseState + "is change");
		AdoinoErrorState tempState = (AdoinoErrorState)warehousestatemachine.getAduinoError();
		tempState.SetBeforeErrorState(this);
		tempState.Evt_WareHouseErrorStateChange(iWareHouseState);
		warehousestatemachine.setState(tempState);
	}
	
	@Override
	public String toString() {
		return "[RobotMoveToX"+ThisStateID+"]";
	}

}
