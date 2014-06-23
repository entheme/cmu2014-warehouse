package com.lge.warehouse.manager.OrderStatemachine;

import java.io.Serializable;

public class AdoinoErrorState extends WMorderStatemachineState implements Serializable{

	//Todo ������ ������ȣ�� int�� ó���Ͽ����� object�� �ٸ� ���·� ó���� �ؾ��� �� ����. ���� �߻� ����.. 
	int RobotError; // ���� ��ȣ
	int WarehouseError;
	
	WMorderStatemachineState BeforeErrorState; // �������°� ������ �ƴ� ���¶�
	WMorderStatemachineState TempBeforeErrorState; // �������°� ������ �ƴ� ���¶�
	
	public AdoinoErrorState(WahouseStateMachine warehousestatemachine) 
	{
		super(warehousestatemachine);
		RobotError = 0;
		WarehouseError = 0;
	}
	
	public void SetBeforeErrorState(WMorderStatemachineState BeforeError)
	{
		BeforeErrorState = BeforeError; //���� �߻� ���� ������Ʈ.  �ٸ� ������Ʈ���� �������� �ڽ��� ������Ʈ�� �Ѱ���..
	}

	@Override
	public void Evt_RobotErrorStateChange(int iRobotErrorState) {
		//TODO �Է°��� ��ȿ�� �Ǵ� �κ� �ʿ�
		// ���������� ������ �߻��� �� �ִµ� �ԷµǴ� ���� �ջ�� ���������� ������
		// ������ ������ ������������ ������ �� �޸� ������Ѵ�.
		System.out.println("AdoinoErrorState Get RobotError State:" + iRobotErrorState);
		RobotError = iRobotErrorState;
		DeterminRecoverLastStateOrNot();
	}

	@Override
	public void Evt_WareHouseErrorStateChange(int iWareHouseState) {
		// TODO �Է°��� ��ȿ�� �Ǵ� �κ� �ʿ�
		System.out.println("WarehouseErrorState Get WarehouseErrorState:" + iWareHouseState);
		WarehouseError = iWareHouseState;
		DeterminRecoverLastStateOrNot();
	}
	
	private void DeterminRecoverLastStateOrNot()
	{
		// �Ƶ��̳볪 �����Ͽ콺�� ������ �� ������ ���¶�� ���� ������Ʈ�� ���ư��� �Ѵ�.
		// ������ ������ ������������ ������ �� �޸� ������Ѵ�.
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