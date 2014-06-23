package com.lge.warehouse.manager.OrderStatemachine;

import java.io.Serializable;

public class AdoinoErrorState extends WMorderStatemachineState implements Serializable{

	//Todo 지금은 에러번호를 int로 처리하였지만 object나 다른 형태로 처리를 해야할 거 같다. 동시 발생 에러.. 
	int RobotError; // 에러 번호
	int WarehouseError;
	
	WMorderStatemachineState BeforeErrorState; // 직전상태가 에러가 아닌 상태라
	WMorderStatemachineState TempBeforeErrorState; // 직전상태가 에러가 아닌 상태라
	
	public AdoinoErrorState(WahouseStateMachine warehousestatemachine) 
	{
		super(warehousestatemachine);
		RobotError = 0;
		WarehouseError = 0;
	}
	
	public void SetBeforeErrorState(WMorderStatemachineState BeforeError)
	{
		BeforeErrorState = BeforeError; //에러 발생 전의 스테이트.  다른 스테이트에서 이쪽으로 자신의 스테이트를 넘겨줌..
	}

	@Override
	public void Evt_RobotErrorStateChange(int iRobotErrorState) {
		//TODO 입력값의 유효성 판단 부분 필요
		// 여러가지의 에러가 발생할 수 있는데 입력되는 값이 합산된 에러값으로 들어올지
		// 별도의 에러로 들어올지에따라 구현을 좀 달리 해줘야한다.
		System.out.println("AdoinoErrorState Get RobotError State:" + iRobotErrorState);
		RobotError = iRobotErrorState;
		DeterminRecoverLastStateOrNot();
	}

	@Override
	public void Evt_WareHouseErrorStateChange(int iWareHouseState) {
		// TODO 입력값의 유효성 판단 부분 필요
		System.out.println("WarehouseErrorState Get WarehouseErrorState:" + iWareHouseState);
		WarehouseError = iWareHouseState;
		DeterminRecoverLastStateOrNot();
	}
	
	private void DeterminRecoverLastStateOrNot()
	{
		// 아두이노나 웨어하우스의 에러가 다 없어진 상태라면 직전 스테이트로 돌아가게 한다.
		// 별도의 에러로 들어올지에따라 구현을 좀 달리 해줘야한다.
		if(RobotError == 0 && WarehouseError == 0)
		{
			warehousestatemachine.setState(BeforeErrorState);
		}
		else
		{
			System.out.println("Error is remain! Robot Error:" + RobotError + " Warehouse :" + WarehouseError);
		}
	}
	
	@Override
	public String toString() {
		return "RobotErrorState";
	}

}