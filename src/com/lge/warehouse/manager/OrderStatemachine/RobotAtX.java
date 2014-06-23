package com.lge.warehouse.manager.OrderStatemachine;

import java.io.Serializable;

public class RobotAtX extends WMorderStatemachineState implements Serializable{

	int ThisStateID; // ��� �����̼��ΰ�
	
	
	public RobotAtX(WahouseStateMachine warehousestatemachine, int thisStateID) {
		super(warehousestatemachine);
		ThisStateID = thisStateID;//�� ��ȿ�� üũ�� �� �ʿ䰡 ������?
	}

	@Override
	public void Evt_WareHouseButtonIsOn(int iButtonNum) {
		System.out.println("RobotAtX" + ThisStateID +": get Evt_WareHouseButtonIsOn" + iButtonNum);
		// �ڽ��� State�� �´� ��ư������ Ȯ�� �ؾ��Ѵ�.

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
			//���ɾ��� �������� ������... �̶� ������ �������� �ϴ����� �̾߱��غ��� ����.
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
