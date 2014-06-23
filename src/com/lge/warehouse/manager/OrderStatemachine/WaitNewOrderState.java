package com.lge.warehouse.manager.OrderStatemachine;

import java.io.Serializable;

public class WaitNewOrderState extends WMorderStatemachineState implements Serializable{


	public WaitNewOrderState(WahouseStateMachine warehousestatemachine) {
		super(warehousestatemachine);
	}

	@Override
	public void Evt_NewOrder() {
		System.out.println("WaitNewOrderState : Evt_NewOrder");
		System.out.println("Warehouse에 명령 전달 후, Robot에게 1번으로 가라고 명령한다.");
		warehousestatemachine.setState(warehousestatemachine.getRobotMoveToXst(0));
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
