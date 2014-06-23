package com.lge.warehouse.manager.OrderStatemachine;

import java.io.Serializable;

public class RobotAtX extends WMorderStatemachineState implements Serializable{

	int ThisStateID; // 어느 스테이션인가
	
	
	public RobotAtX(WahouseStateMachine warehousestatemachine, int thisStateID) {
		super(warehousestatemachine);
		ThisStateID = thisStateID;//값 유효성 체크를 할 필요가 있을까?
	}

	@Override
	public void Evt_WareHouseButtonIsOn(int iButtonNum) {
		System.out.println("RobotAtX" + ThisStateID +": get Evt_WareHouseButtonIsOn" + iButtonNum);
		// 자신의 State와 맞는 버튼인지를 확인 해야한다.

		if(ThisStateID == iButtonNum)
		{
			System.out.println("Loding complete... ");
			if(iButtonNum == 4)
			{
				System.out.println("Order complete...");
				warehousestatemachine.setState(warehousestatemachine.getWaitNewOrderState());
			}
			else
			{
				warehousestatemachine.setState(warehousestatemachine.getRobotMoveToXst(ThisStateID));
			}
		}
		else
		{
			//관심없는 센서값이 들어왔음... 이때 에러를 만들어줘야 하는지는 이야기해보고 결정.
		}
	}

	@Override
	public void Evt_RobotErrorStateChange(int iRobotErrorState) {
		System.out.println("RobotAtX" + ThisStateID + "Evt_RobotErrorStateChange : #" + iRobotErrorState + "is change");
		AdoinoErrorState tempState = (AdoinoErrorState)warehousestatemachine.getAduinoError();
		tempState.SetBeforeErrorState(this);
		tempState.Evt_RobotErrorStateChange(iRobotErrorState);
		warehousestatemachine.setState(tempState);
	}

	@Override
	public void Evt_WareHouseErrorStateChange(int iWareHouseState) {
		System.out.println("RobotAtX" + ThisStateID + "Evt_WareHouseStateChange : #" + iWareHouseState + "is change");
		AdoinoErrorState tempState = (AdoinoErrorState)warehousestatemachine.getAduinoError();
		tempState.SetBeforeErrorState(this);
		tempState.Evt_WareHouseErrorStateChange(iWareHouseState);
		warehousestatemachine.setState(tempState);
	}
	
	@Override
	public String toString() {
		return "RobotAtX"+ThisStateID;
	}

}
