package com.lge.warehouse.manager.OrderStatemachine;

import java.io.Serializable;

/*
 * 스테이트 머신을 둘러싼 환경이 모두 정비가 될 때 까지 대기하기 위한 스테이트
 * 스테이트 머신 시작과 동시에 첫 번째 오더를 처리가능 한 상태이거나, 
 * 기존 진행하던 오더를 resume 할 수 있다면 필요없는 스테이트임.
 */

public class InitState extends WMorderStatemachineState implements Serializable {
	
	public InitState(WahouseStateMachine warehousestatemachine) {
		super(warehousestatemachine);
	}

	@Override
	public void Evt_initComplete() {
		System.out.println("InitState : Evt_initComplete");
		warehousestatemachine.setState(warehousestatemachine.getWaitNewOrderState());
	}

	//init state 에서 로봇에 에러가 있다는 것 자체가 발생할 수 없다. 일단 waitneworder state에서 부터 에러 처리...
	@Override
	public void Evt_RobotErrorStateChange(int iRobotErrorState) {
		System.out.println("InitState  Evt_RobotErrorStateChange : #" + iRobotErrorState + "is change, not implement event");
		
	}

	@Override
	public void Evt_WareHouseErrorStateChange(int iWareHouseState) {
		System.out.println("InitState Evt_WareHouseStateChange : #" + iWareHouseState + "is change, not implement event");
	}

	@Override
	public String toString() {
		return "InitState";
	}
	
	

}
