package com.lge.warehouse.manager.OrderStatemachine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lge.warehouse.util.QuantifiedWidget;

public class RobotAtX extends WMorderStatemachineState implements Serializable{

	int ThisStateID;
	private List<QuantifiedWidget> RobotHand_qwOrderList; // for robot holding widget list.
	
	public RobotAtX(int thisStateID) {
		super();
		ThisStateID = thisStateID;
		RobotHand_qwOrderList = new ArrayList<QuantifiedWidget>();
	}
	
	public RobotAtX(WahouseStateMachine warehousestatemachine, int thisStateID) {
		super(warehousestatemachine);
		ThisStateID = thisStateID;
		RobotHand_qwOrderList = new ArrayList<QuantifiedWidget>();
	}
	
	public int getID()
	{
		return ThisStateID;
	}


	public List<QuantifiedWidget> getQwOrderList() {
		return RobotHand_qwOrderList;
	}

	public void setQwOrderList(List<QuantifiedWidget> qwOrderList) {
		RobotHand_qwOrderList.clear();
		for(QuantifiedWidget qw : qwOrderList)
		{
			RobotHand_qwOrderList.add(qw);
	 	}
	}
	
	public void addQwOrderList(QuantifiedWidget qwOrderList)
	{
		this.RobotHand_qwOrderList.add(qwOrderList);
	}

	@Override
	public CmdToOther Evt_WareHouseButtonIsOn(int iButtonNum) {
		System.out.println(toString() + ": get Evt_WareHouseButtonIsOn" + iButtonNum);
		CmdToOther returnval = CmdToOther.CMD_NONE;
		
		if(ThisStateID == iButtonNum)
		{
			System.out.println(toString() + "Loding complete... ");
			if(iButtonNum == 4)
			{
				System.out.println(toString() + "Order complete...");
				warehousestatemachine.setState(warehousestatemachine.getWaitNewOrderState());
				returnval = CmdToOther.ORDER_COMPLETE;
			}
			else
			{
				System.out.println(toString() + "Robot will go next station");
				warehousestatemachine.getRobotMoveToXst(ThisStateID).setPassedNavigationPath(passedNavigationPath);
				warehousestatemachine.getRobotMoveToXst(ThisStateID).PathClearAndSetnextPath(navigationPath);
				warehousestatemachine.setState(warehousestatemachine.getRobotMoveToXst(ThisStateID));
				returnval = CmdToOther.ROBOT_MOVE_TONEXT;
			}
		}
		else
		{
			//The sensor value is not relate this inventory station
			// Need to make Error??
			returnval = CmdToOther.ROBOT_MOVE_TONEXT;
		}
		
		return returnval;
	}

	@Override
	public CmdToOther Evt_RobotErrorStateChange(int iRobotErrorState) {
		
		System.out.println(toString() + "Evt_RobotErrorStateChange : #" + iRobotErrorState + "is change");
		CmdToOther returnval = CmdToOther.CMD_NONE;
		if(iRobotErrorState == 0)
		{
			System.out.println("nothing is happen");
			returnval = CmdToOther.CMD_NONE;
		}
		else
		{
			AdoinoErrorState tempState = (AdoinoErrorState)warehousestatemachine.getAduinoError();
			tempState.SetBeforeErrorState(this);
			tempState.Evt_RobotErrorStateChange(iRobotErrorState);
			warehousestatemachine.setState(tempState);
			returnval = CmdToOther.ROBOT_STOP;
		}
		return returnval;
	}

	@Override
	public CmdToOther Evt_WareHouseErrorStateChange(int iWareHouseState) {
		
		System.out.println(toString()+ "Evt_WareHouseStateChange : #" + iWareHouseState + "is change");
		CmdToOther returnval = CmdToOther.CMD_NONE;
		if(iWareHouseState == 0)
		{
			System.out.println("nothing is happen");
			returnval = CmdToOther.CMD_NONE;
		}
		else
		{
			AdoinoErrorState tempState = (AdoinoErrorState)warehousestatemachine.getAduinoError();
			tempState.SetBeforeErrorState(this);
			tempState.Evt_WareHouseErrorStateChange(iWareHouseState);
			warehousestatemachine.setState(tempState);
			returnval = CmdToOther.ROBOT_STOP;
		}
		return returnval;
	}
	
	@Override
	public String toString() {
		return "[RobotAtX"+ThisStateID+"]";
	}

}

