package com.lge.warehouse.manager.OrderStatemachine;

import java.io.Serializable;

/*
 * waiting state for until initial environment is ready to go..
 * If needs not than remove this
 */
public class InitState extends WMorderStatemachineState implements Serializable {
	
	public InitState(WahouseStateMachine warehousestatemachine) {
		super(warehousestatemachine);
	}

	@Override
	public CmdToOther Evt_initComplete() {
		System.out.println("InitState : Evt_initComplete");
		warehousestatemachine.setState(warehousestatemachine.getWaitNewOrderState());
		return CmdToOther.CMD_NONE;
	}

	// initial state don't handle Error state
	@Override
	public CmdToOther Evt_RobotErrorStateChange(int iRobotErrorState) {
		System.out.println("InitState  Evt_RobotErrorStateChange : #" + iRobotErrorState + "is change, not implement event");
		return CmdToOther.CMD_NONE;
	}

	@Override
	public CmdToOther Evt_WareHouseErrorStateChange(int iWareHouseState) {
		System.out.println("InitState Evt_WareHouseStateChange : #" + iWareHouseState + "is change, not implement event");
		return CmdToOther.CMD_NONE;
	}

	@Override
	public String toString() {
		return "InitState";
	}
	
	

}