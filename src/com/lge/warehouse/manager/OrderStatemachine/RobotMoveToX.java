package com.lge.warehouse.manager.OrderStatemachine;

import java.io.Serializable;

public class RobotMoveToX extends WMorderStatemachineState implements Serializable{

	int ThisStateID; //어느 스테이션으로 가고있는가?
	
	public RobotMoveToX(WahouseStateMachine warehousestatemachine,
			int thisStateID) {
		super(warehousestatemachine);
		ThisStateID = thisStateID; //값 유효성 체크를 할 필요가 있을까?
	}

	@Override
	public void Evt_WareHouseSensorIsOn(int iSensorNum) {
		System.out.println("RobotMoveTo" + ThisStateID +": get Evt_WareHouseSensorIsOn" + iSensorNum);
		// 자신의 State와 맞는 센서인지를 확인 해야한다.
		if(ThisStateID == iSensorNum)
		{
			System.out.println("Robot is near to station ");
			warehousestatemachine.setState(warehousestatemachine.getRobotAtXst(ThisStateID-1));
		}
		else
		{
			//관심없는 센서값이 들어왔음... 이때 에러를 만들어줘야 하는지는 이야기해보고 결정.
		}
		
	}

	@Override
	public void Evt_RobotErrorStateChange(int iRobotErrorState) {
		System.out.println("RobotMoveToX" + ThisStateID +" Evt_RobotErrorStateChange : #" + iRobotErrorState + "is change");
		AdoinoErrorState tempState = (AdoinoErrorState)warehousestatemachine.getAduinoError();
		tempState.SetBeforeErrorState(this);
		tempState.Evt_RobotErrorStateChange(iRobotErrorState);
		warehousestatemachine.setState(tempState);
	}

	@Override
	public void Evt_WareHouseErrorStateChange(int iWareHouseState) {
		System.out.println("RobotMoveToX" + ThisStateID +" Evt_WareHouseStateChange : #" + iWareHouseState + "is change");
		AdoinoErrorState tempState = (AdoinoErrorState)warehousestatemachine.getAduinoError();
		tempState.SetBeforeErrorState(this);
		tempState.Evt_WareHouseErrorStateChange(iWareHouseState);
		warehousestatemachine.setState(tempState);
	}
	
	@Override
	public String toString() {
		return "RobotMoveToX"+ThisStateID;
	}

}
