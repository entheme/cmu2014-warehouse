package com.lge.warehouse.manager.OrderStatemachine;

import java.io.Serializable;


public class AdoinoErrorState extends WMorderStatemachineState implements Serializable{

	// TODO need protocol or format of Error state
	// object or bitfield or something...
	
	int RobotError;
	int WarehouseError;
	
	WMorderStatemachineState BeforeErrorState;
	
	public AdoinoErrorState() {
		super();
		RobotError = 0;
		WarehouseError = 0;
	}
	
	public AdoinoErrorState(WahouseStateMachine warehousestatemachine) 
	{
		super(warehousestatemachine);
		RobotError = 0;
		WarehouseError = 0;
	}
	
	public void SetBeforeErrorState(WMorderStatemachineState BeforeError)
	{
		BeforeErrorState = BeforeError; 
	}

	@Override
	public CmdToOther Evt_RobotErrorStateChange(int iRobotErrorState) {
		// TODO need right error value error is come? (Ex -1)
		// TODO need protocol or format of Error state
		CmdToOther returnval = CmdToOther.CMD_NONE;
		RobotError = iRobotErrorState;
		System.out.println(toString()+"AdoinoErrorState Get RobotError State:" + iRobotErrorState);
		returnval = DeterminRecoverLastStateOrNot();
		return returnval;
	}

	@Override
	public CmdToOther Evt_WareHouseErrorStateChange(int iWareHouseState) {
		// TODO need right error value error is come? (Ex -1)
		CmdToOther returnval = CmdToOther.CMD_NONE;
		WarehouseError = iWareHouseState;
		System.out.println(toString()+ "WarehouseErrorState Get WarehouseErrorState:" + iWareHouseState);
		returnval = DeterminRecoverLastStateOrNot();
		return returnval;
	}
	
	private CmdToOther DeterminRecoverLastStateOrNot()
	{
		CmdToOther returnval = CmdToOther.CMD_NONE;
		// If no error is remain than return Last state
		//TODO need to check..  modify to kind of error
		if(RobotError == 0 && WarehouseError == 0)
		{
			if(BeforeErrorState instanceof RobotMoveToX)
			{
				returnval = CmdToOther.ROBOT_MOVE_TONEXT;
			}
			else
			{;
			}
			warehousestatemachine.setState(BeforeErrorState);
		}
		else
		{
			System.out.println(toString()+ "Error is remain! Robot Error:" + RobotError + " Warehouse :" + WarehouseError);
		}
		
		return returnval;
	}
	
	@Override
	public String toString()
	{
		return "[Robot"+RobotError+"OrWarehouseError"+WarehouseError+"State]";
	}

}