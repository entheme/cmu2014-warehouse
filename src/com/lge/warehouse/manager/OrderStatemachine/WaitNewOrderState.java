package com.lge.warehouse.manager.OrderStatemachine;

import java.io.Serializable;
import java.util.List;

public class WaitNewOrderState extends WMorderStatemachineState implements Serializable{


	public WaitNewOrderState() {
		super();
	}
	
	public WaitNewOrderState(WahouseStateMachine warehousestatemachine) {
		super(warehousestatemachine);
	}

	@Override
	public CmdToOther Evt_NewOrder(List<WMorderStatemachineState> path) {
		System.out.println("[WaitNewOrderState] Evt_NewOrder, Send New order cmd to Warehouse, send cmd to Robot go new navigation.");
		CmdToOther returnval = CmdToOther.CMD_NONE;
		if(path.get(0) instanceof RobotMoveToX)
		{
			RobotMoveToX tempRobotMoveToX = (RobotMoveToX)path.get(0);
			if(tempRobotMoveToX.getID() == 1)
			{
				warehousestatemachine.getRobotMoveToXst(0).setPassedNavigationPath(passedNavigationPath);
				warehousestatemachine.getRobotMoveToXst(0).PathClearAndSetnextPath(path);
				warehousestatemachine.setState(warehousestatemachine.getRobotMoveToXst(0));
				returnval = CmdToOther.ROBOT_MOVE_TONEXT;
			}
			else
			{
				System.out.println("[WaitNewOrderState] First Path is not to inventory 1!!");
				returnval = CmdToOther.CMD_NONE;
			}
		}
		else
		{
			System.out.println("[WaitNewOrderState] First Path is not to inventory 1!!");
			returnval = CmdToOther.CMD_NONE;
		}
		
		return returnval;
	}

	@Override
	public CmdToOther Evt_RobotErrorStateChange(int iRobotErrorState) {
		System.out.println("WaitNewOrderState  Evt_RobotErrorStateChange : #" + iRobotErrorState + "is change, not implement event");
		CmdToOther returnval = CmdToOther.CMD_NONE;
		if(iRobotErrorState == 0)
		{
			System.out.println("nothing is happen");
			returnval = CmdToOther.CMD_NONE;
		}
		else
		{
			AdoinoErrorState tempState = (AdoinoErrorState)warehousestatemachine.getAduinoError();
			tempState.setPassedNavigationPath(passedNavigationPath);
			tempState.setNavigationPath(navigationPath);
			tempState.SetBeforeErrorState(this);
			tempState.Evt_RobotErrorStateChange(iRobotErrorState);//pass error info
			warehousestatemachine.setState(tempState);
			returnval = CmdToOther.ROBOT_STOP;
		}
		
		return returnval;
	}

	@Override
	public CmdToOther Evt_WareHouseErrorStateChange(int iWareHouseState) {
		System.out.println("WaitNewOrderState Evt_WareHouseStateChange : #" + iWareHouseState + "is change, not implement event");
		CmdToOther returnval = CmdToOther.CMD_NONE;
		if(iWareHouseState == 0)
		{
			System.out.println("nothing is happen");
			returnval = CmdToOther.CMD_NONE;
		}
		else
		{
			AdoinoErrorState tempState = (AdoinoErrorState)warehousestatemachine.getAduinoError();
			
			tempState.setPassedNavigationPath(passedNavigationPath);
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
		return "[WaitNewOrderState]";
	}
}
