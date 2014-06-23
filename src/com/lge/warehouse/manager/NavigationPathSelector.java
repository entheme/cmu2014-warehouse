package com.lge.warehouse.manager;
import java.util.ArrayList;
import java.util.List;


//import warehousemanager_orderStateMachine.AdoinoErrorState;
import com.lge.warehouse.manager.OrderStatemachine.RobotAtX;
import com.lge.warehouse.manager.OrderStatemachine.RobotMoveToX;
import com.lge.warehouse.manager.OrderStatemachine.WMorderStatemachineState;
//import warehousemanager_orderStateMachine.WaitNewOrderState;


// responsibility : Return proper path of robot navigation by parsing order with inventory list.
// mPathList must include path + inventoryX to loading
public class NavigationPathSelector {
	
	//WaitNewOrderState waitNewOrderState;
	RobotMoveToX robotMoveToX[] = new RobotMoveToX[4];
	RobotAtX robotAtX[] = new RobotAtX[4];
	//AdoinoErrorState aduinoHasError;
	private List<WMorderStatemachineState> mPathList;
	public NavigationPathSelector()
	{
		mPathList = new ArrayList<WMorderStatemachineState>();
		
		//waitNewOrderState = new WaitNewOrderState();
		robotMoveToX[0] = new RobotMoveToX(1);
		robotMoveToX[1] = new RobotMoveToX(2);
		robotMoveToX[2] = new RobotMoveToX(3);
		robotMoveToX[3] = new RobotMoveToX(4);
		robotAtX[0] = new RobotAtX(1);
		robotAtX[1] = new RobotAtX(2);
		robotAtX[2] = new RobotAtX(3);
		robotAtX[3] = new RobotAtX(4);
		//aduinoHasError = new AdoinoErrorState();
	}

	
	
	public void addPath(WMorderStatemachineState w){
		mPathList.add(w);
    }
	
    public List<WMorderStatemachineState> getPath(){
        return mPathList;
    }
    
    public void flushPath()
    {
    	mPathList.clear();
    }
    
    // make new NavigationPathList to use robot navigation to fulfill order
    public List<WMorderStatemachineState> MakeNewNavigationPath(int order, int inventorylist)
    {
    	//Visit 1, 3, 4
    	addPath(robotMoveToX[0]);
    	addPath(robotAtX[0]);
    	addPath(robotMoveToX[1]);
    	
    	addPath(robotMoveToX[2]);
    	addPath(robotAtX[2]);
    	addPath(robotMoveToX[3]);
    	addPath(robotAtX[3]);
    	
    	// visit all path!
    	/*
    	addPath(robotMoveToX[0]);
    	addPath(robotAtX[0]);
    	addPath(robotMoveToX[1]);
    	addPath(robotAtX[1]);
    	addPath(robotMoveToX[2]);
    	addPath(robotAtX[2]);
    	addPath(robotMoveToX[3]);
    	addPath(robotAtX[3]);
    	*/
    	
    	
    	String tempPath = "[NavigationPathSelector] Make path to NewOrder! Selected path is";
    	for(WMorderStatemachineState state : mPathList)
		{
    		tempPath += "+" + state.toString();
		}
    	System.out.println(tempPath);
    	
    	return mPathList;
    }
	
}
