package com.lge.warehouse.manager.OrderStatemachine;

import java.io.Serializable;

public class RobotMoveToX extends WMorderStatemachineState implements Serializable{

	int ThisStateID; //what station heading for?
	boolean ReadyToGo;
	boolean ErrorRecovery;
	
	public RobotMoveToX(int thisStateID) {
		super();
		ThisStateID = thisStateID;
		ReadyToGo = false;
		ErrorRecovery = false;
	}
	
	public RobotMoveToX(WahouseStateMachine warehousestatemachine, int thisStateID)
	{
		super(warehousestatemachine);
		ThisStateID = thisStateID;
		ReadyToGo = false;
		ErrorRecovery = false;
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
					ReadyToGo = false;
					System.out.println(toString() + "Robot is near to station");
					if(ThisStateID == 4)
					{
						// no more navi path...
						warehousestatemachine.getRobotAtXst(ThisStateID-1).setPassedNavigationPath(passedNavigationPath);
						warehousestatemachine.getRobotAtXst(ThisStateID-1).PathClearAndSetnextPath(navigationPath);
					}
					else
					{
						warehousestatemachine.getRobotAtXst(ThisStateID-1).setPassedNavigationPath(passedNavigationPath);
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
				if(ErrorRecovery == true)
				{
					warehousestatemachine.getRobotMoveToXst(ThisStateID).setPassedNavigationPath(passedNavigationPath);
					warehousestatemachine.getRobotMoveToXst(ThisStateID).PathClearAndSetnextPath(navigationPath);
					warehousestatemachine.setState(warehousestatemachine.getRobotMoveToXst(ThisStateID));
					returnval = CmdToOther.ROBOT_MOVE_TONEXT;
				}
				else
				{
					ReadyToGo = true;
					returnval = CmdToOther.CMD_NONE;
				}
				
				
				System.out.println(toString() + "Ready To go!!");
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
	
	
	
	public boolean isReadyToGo() {
		return ReadyToGo;
	}

	public void setReadyToGo(boolean readyToGo) {
		ReadyToGo = readyToGo;
	}

	@Override
	public CmdToOther Evt_ReadyToGo() 
	{
		CmdToOther returnval = CmdToOther.CMD_NONE;
		if(ReadyToGo == true)
		{
			System.out.println(toString() + "Bypass this inventory station");
			warehousestatemachine.getRobotMoveToXst(ThisStateID).setPassedNavigationPath(passedNavigationPath);
			warehousestatemachine.getRobotMoveToXst(ThisStateID).PathClearAndSetnextPath(navigationPath);
			warehousestatemachine.setState(warehousestatemachine.getRobotMoveToXst(ThisStateID));
			returnval = CmdToOther.ROBOT_MOVE_TONEXT;
		}
		else
		{
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
			
			WMorderStatemachineState tempNavi = passedNavigationPath.get(passedNavigationPath.size()-1);
			tempState.setPassedNavigationPath(passedNavigationPath);
			
			//if(navigationPath.get(0) != tempNavi)
			//{
			//	navigationPath.add(0, tempNavi);
			//}
			tempState.setNavigationPath(navigationPath);
			
			
			tempState.SetBeforeErrorState(this);
			tempState.Evt_RobotErrorStateChange(iRobotErrorState);//pass error info
			warehousestatemachine.setState(tempState);
			returnval = CmdToOther.CMD_NONE;
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
			ErrorRecovery = true;
			
			AdoinoErrorState tempState = (AdoinoErrorState)warehousestatemachine.getAduinoError();
			
			WMorderStatemachineState tempNavi = passedNavigationPath.get(passedNavigationPath.size()-1);
			tempState.setPassedNavigationPath(passedNavigationPath);
			
			//if(navigationPath.get(0) != tempNavi)
			//{
			//	navigationPath.add(0, tempNavi);
			//}
			tempState.setNavigationPath(navigationPath);
			
			tempState.SetBeforeErrorState(this);
			tempState.Evt_WareHouseErrorStateChange(iWareHouseState);//pass error info
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
