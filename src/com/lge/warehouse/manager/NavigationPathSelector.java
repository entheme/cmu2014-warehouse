package com.lge.warehouse.manager;
import java.util.ArrayList;
import java.util.List;






//import warehousemanager_orderStateMachine.AdoinoErrorState;
import com.lge.warehouse.manager.OrderStatemachine.RobotAtX;
import com.lge.warehouse.manager.OrderStatemachine.RobotMoveToX;
import com.lge.warehouse.manager.OrderStatemachine.WMorderStatemachineState;
import com.lge.warehouse.util.InventoryName;
import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.QuantifiedWidget;
import com.lge.warehouse.supervisor.WarehouseInventoryInfo;
//import warehousemanager_orderStateMachine.WaitNewOrderState;


// responsibility : Return proper path of robot navigation by parsing order with inventory list.
// mPathList must include path + inventoryX to loading
public class NavigationPathSelector {
	
	RobotMoveToX robotMoveToX[] = new RobotMoveToX[4];
	RobotAtX robotAtX[] = new RobotAtX[4];
	private List<WMorderStatemachineState> mPathList;
	WarehouseInventoryInfo minventoryInfo;
	public NavigationPathSelector(WarehouseInventoryInfo inventoryInfo)
	{
		minventoryInfo = inventoryInfo;
		mPathList = new ArrayList<WMorderStatemachineState>();
		robotMoveToX[0] = new RobotMoveToX(1);
		robotMoveToX[1] = new RobotMoveToX(2);
		robotMoveToX[2] = new RobotMoveToX(3);
		robotMoveToX[3] = new RobotMoveToX(4);
		robotAtX[0] = new RobotAtX(1);
		robotAtX[1] = new RobotAtX(2);
		robotAtX[2] = new RobotAtX(3);
		robotAtX[3] = new RobotAtX(4);
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
    public List<WMorderStatemachineState> MakeNewNavigationPath(Order neworder)
    {
    	//Visit 1, 3, 4 test
    	/*
    	addPath(robotMoveToX[0]);
    	addPath(robotAtX[0]);
    	addPath(robotMoveToX[1]);
    	
    	addPath(robotMoveToX[2]);
    	addPath(robotAtX[2]);
    	addPath(robotMoveToX[3]);
    	addPath(robotAtX[3]);
    	*/
    	
    	// visit all path test!
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
    	
    	//For information
    	System.out.println("[NavigationPathSelector]FILL_ORDER order id = "+neworder.getOrderId());
		for(QuantifiedWidget qw : neworder.getItemList())
		{
			System.out.println(qw.getWidget()+" : "+qw.getQuantity());
	 	}
		for(InventoryName inventoryName : InventoryName.values())//for all inventory type..
		{
			System.out.println("[NavigationPathSelector]WAREHOUSE_INVENTORY_INFO: inventoryName =" + inventoryName);
			if(minventoryInfo.hasInventoryStation(inventoryName)) // If my warehouse has that inventory type..
			{
				for(QuantifiedWidget qwInven : minventoryInfo.getInventoryInfo(inventoryName))
				{
					System.out.println(qwInven.getWidget()+" : "+qwInven.getQuantity());
				}
			}
			else
			{ ;
			}
		}
		//end information
		
		// make order QuantifiedWidget list for remove item that take route...
		List<QuantifiedWidget> OrderItemListfornavation = neworder.getItemList();
		for(QuantifiedWidget qw : neworder.getItemList())
		{
			OrderItemListfornavation.add(qw);
	 	}
		System.out.println("[NavigationPathSelector]Number of widget kind at Neworder : " + OrderItemListfornavation.size());
    	
    	// One inventory can have two kind of widget. (our system look like... can get more kind of widget..)
    	// Seek first inventory has that kind of widget? and more number of item?
		// than add path 
		// assume all warehouse have INVENTORY_1 ~ INVENTORY_4 !!
		int InvenIdx = 0;
		for(InventoryName inventoryName : InventoryName.values())//for all inventory type..
		{
			System.out.println("[NavigationPathSelector]WAREHOUSE_INVENTORY_INFO: inventoryName =" + inventoryName);
			if(minventoryInfo.hasInventoryStation(inventoryName)) // If my warehouse has that inventory type..
			{
				for(QuantifiedWidget qwInven : minventoryInfo.getInventoryInfo(inventoryName))
				{
					List<QuantifiedWidget> TempRemainOrderWidget = OrderItemListfornavation; // for multi widget remove.
					boolean bAddpath = false;
					
					for(int i = 0; i < OrderItemListfornavation.size(); i++)
					{
						QuantifiedWidget qwOrder = OrderItemListfornavation.get(i);
						if((qwOrder.getWidget() == qwInven.getWidget()) 
								&& (qwInven.getQuantity() >= qwOrder.getQuantity()))
						{
							System.out.println("ThisInven have more number of widget for order");
							System.out.println("Inven" + qwInven.getWidget()+" : "+qwInven.getQuantity());
							System.out.println("Order" + qwOrder.getWidget()+" : " + qwOrder.getQuantity());
							bAddpath = true;
							TempRemainOrderWidget.remove(i);
						}
						else
						{;							
						}
						
				 	}
					
					OrderItemListfornavation = TempRemainOrderWidget;
					if(bAddpath == true)
					{
						System.out.println("This inventory is added route");
						addPath(robotMoveToX[InvenIdx]);
						addPath(robotAtX[InvenIdx]);
					}
					else
					{
						System.out.println("This inventory is not included route");
						addPath(robotMoveToX[InvenIdx]);
					}
					bAddpath = false;
					
				}
			}
			else
			{ 
				// for 
				System.out.println("[NavigationPathSelector] this inventory has not [" + InvenIdx + "]st Inventory");
				addPath(robotMoveToX[InvenIdx]);
			}
			
			
			//TODO I just want only 4 inventory station.... because now there are only 4 station in real..
			InvenIdx++;
			if(InvenIdx == 4)
			{
				break;
			}
			else
			{;				
			}
		}
		
    	
		
    	
    	
    	
    	String tempPath = "[NavigationPathSelector] Make path to NewOrder! Selected path is";
    	for(WMorderStatemachineState state : mPathList)
		{
    		tempPath += "+" + state.toString();
		}
    	System.out.println(tempPath);
    	
    	return mPathList;
    }
	
}
