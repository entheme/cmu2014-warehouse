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
	public void Evt_NewOrder(List<WMorderStatemachineState> path) {
		System.out.println("[WaitNewOrderState] Evt_NewOrder, Send New order cmd to Warehouse, send cmd to Robot go new navigation.");
		
		if(path.get(0) instanceof RobotMoveToX)
		{
			RobotMoveToX tempRobotMoveToX = (RobotMoveToX)path.get(0);
			if(tempRobotMoveToX.getID() == 1)
			{
				warehousestatemachine.getRobotMoveToXst(0).setPassedNavigationPath(passedNavigationPath);
				warehousestatemachine.getRobotMoveToXst(0).PathClearAndSetnextPath(path);
				warehousestatemachine.setState(warehousestatemachine.getRobotMoveToXst(0));
			}
			else
			{
				System.out.println("[WaitNewOrderState] First Path is not to inventory 1!!");
			}
		}
		else
		{
			System.out.println("[WaitNewOrderState] First Path is not to inventory 1!!");
		}
		
		
	}

	@Override
	public void Evt_RobotErrorStateChange(int iRobotErrorState) {
		System.out.println("WaitNewOrderState  Evt_RobotErrorStateChange : #" + iRobotErrorState + "is change, not implement event");
		AdoinoErrorState tempState = (AdoinoErrorState)warehousestatemachine.getAduinoError();
		tempState.SetBeforeErrorState(this);
		tempState.Evt_RobotErrorStateChange(iRobotErrorState);
		warehousestatemachine.setState(tempState);
	}

	@Override
	public void Evt_WareHouseErrorStateChange(int iWareHouseState) {
		System.out.println("WaitNewOrderState Evt_WareHouseStateChange : #" + iWareHouseState + "is change, not implement event");
		AdoinoErrorState tempState = (AdoinoErrorState)warehousestatemachine.getAduinoError();
		tempState.SetBeforeErrorState(this);
		tempState.Evt_WareHouseErrorStateChange(iWareHouseState);
		warehousestatemachine.setState(tempState);
	}

	@Override
	public String toString() {
		return "WaitNewOrderState";
	}
}
