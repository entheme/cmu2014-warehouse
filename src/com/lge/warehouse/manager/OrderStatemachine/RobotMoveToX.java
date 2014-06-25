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
	public CmdToOther Evt_WareHouseSensorIsOn(int iSensorNum) {
		System.out.println(toString() + ": get Evt_WareHouseSensorIsOn" + iSensorNum);
		CmdToOther returnval = CmdToOther.CMD_NONE;
		if(ThisStateID == iSensorNum)
		{
			if(navigationPath.get(0) instanceof RobotAtX)
			{
				RobotAtX tempRobotAtX = (RobotAtX)navigationPath.get(0);
				if(tempRobotAtX.getID() == ThisStateID)
				{
					System.out.println(toString() + "Robot is near to station");
					if(ThisStateID == 4)
					{
						// no more navi path...
					}
					else
					{
						warehousestatemachine.getRobotAtXst(ThisStateID).setPassedNavigationPath(passedNavigationPath);
						warehousestatemachine.getRobotAtXst(ThisStateID-1).PathClearAndSetnextPath(navigationPath);
					}
					warehousestatemachine.setState(warehousestatemachine.getRobotAtXst(ThisStateID-1));
					// change state to wait worker button push.
					returnval = CmdToOther.CMD_NONE;
				}
				else
				{
					System.out.println(toString() + "Path is wrong!!");
				}
			}
			else // this inventory station is not loading station 
			{
				System.out.println(toString() + "Bypass this inventory station");
				warehousestatemachine.getRobotMoveToXst(ThisStateID).setPassedNavigationPath(passedNavigationPath);
				warehousestatemachine.getRobotMoveToXst(ThisStateID).PathClearAndSetnextPath(navigationPath);
				warehousestatemachine.setState(warehousestatemachine.getRobotMoveToXst(ThisStateID));
				returnval = CmdToOther.ROBOT_MOVE_TONEXT;
			}
		}
		else
		{
			// ignore sensor enent?
			System.out.println(toString() + "Activate Sensor is not for this station!!");
			returnval = CmdToOther.CMD_NONE;
		}
		
		return returnval;
		
	}

	@Override
	public CmdToOther Evt_RobotErrorStateChange(int iRobotErrorState) {
		System.out.println(toString() + " Evt_RobotErrorStateChange : #" + iRobotErrorState + "is change");
		CmdToOther returnval = CmdToOther.CMD_NONE;
		if(iRobotErrorState == 0)
		{
			System.out.println("nothing is happen");
			returnval = CmdToOther.CMD_NONE;
		}
		else
		{
			AdoinoErrorState tempState = (AdoinoErrorState)warehousestatemachine.getAduinoError();
			tempState.SetBeforeErrorState(this);
			tempState.Evt_RobotErrorStateChange(iRobotErrorState);
			warehousestatemachine.setState(tempState);
			returnval = CmdToOther.ROBOT_STOP;
		}
		return returnval;
	}

	@Override
	public CmdToOther Evt_WareHouseErrorStateChange(int iWareHouseState) {
		System.out.println(toString() + " Evt_WareHouseStateChange : #" + iWareHouseState + "is change");
		CmdToOther returnval = CmdToOther.CMD_NONE;
		if(iWareHouseState == 0)
		{
			System.out.println("nothing is happen");
			returnval = CmdToOther.CMD_NONE;
		}
		else
		{
			AdoinoErrorState tempState = (AdoinoErrorState)warehousestatemachine.getAduinoError();
			tempState.SetBeforeErrorState(this);
			tempState.Evt_WareHouseErrorStateChange(iWareHouseState);
			warehousestatemachine.setState(tempState);
			returnval = CmdToOther.ROBOT_STOP;
		}
		return returnval;
	}
	
	@Override
	public String toString() {
		return "[RobotMoveToX"+ThisStateID+"]";
	}

}
