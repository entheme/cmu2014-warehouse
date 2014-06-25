package com.lge.warehouse.manager;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
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
	
	// for make path logic
	private List<QuantifiedWidget> orderItemListfornavation;
	private List<QuantifiedWidget> tempRemainOrderWidget;
	// for robot hold list at station.
	private List<QuantifiedWidget> RobotHoldWidgetList;
	
	static Logger logger = Logger.getLogger(NavigationPathSelector.class); 
	
	public NavigationPathSelector(WarehouseInventoryInfo inventoryInfo)
	{
		orderItemListfornavation = new ArrayList<QuantifiedWidget>();
		tempRemainOrderWidget = new ArrayList<QuantifiedWidget>();
		RobotHoldWidgetList = new ArrayList<QuantifiedWidget>();
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
    
    public void SetNewInventory(WarehouseInventoryInfo inventoryInfo)
    {
    	minventoryInfo = inventoryInfo;
    }
    
    // make new NavigationPathList to use robot navigation to fulfill order
    public List<WMorderStatemachineState> MakeNewNavigationPath(Order neworder)
    {
    	flushPath();
    	
    	//For information
    	/*
    	logger.info("FILL_ORDER order id = "+neworder.getOrderId());
		for(QuantifiedWidget qw : neworder.getItemList())
		{
			System.out.println(qw.getWidget()+" : "+qw.getQuantity());
	 	}
		for(InventoryName inventoryName : InventoryName.values())//for all inventory type..
		{
			logger.info("WAREHOUSE_INVENTORY_INFO: inventoryName =" + inventoryName);
			if(minventoryInfo.hasInventoryStation(inventoryName)) // If my warehouse has that inventory type..
			{
				for(QuantifiedWidget qwInven : minventoryInfo.getInventoryInfo(inventoryName))
				{
					logger.info(qwInven.getWidget()+" : "+qwInven.getQuantity());
				}
			}
			else
			{ ;
			}
		}
		*/
		//end information
		
		
		// copy list for inventory selection.
		// only for real order.. not for 0 widget.
		orderItemListfornavation.clear();
		for(QuantifiedWidget qw : neworder.getItemList())
		{
			if(qw.getQuantity() != 0) 
			{
				orderItemListfornavation.add(qw);
			}
	 	}
		
		System.out.println("[NavigationPathSelector]Number of widget kind at Neworder : " + orderItemListfornavation.size());
    	
    	// One inventory can have two kind of widget. (our system look like... can get more kind of widget..)
    	// Seek first inventory has that kind of widget? and more number of item?
		// than add path 
		// assume all warehouse have INVENTORY_1 ~ INVENTORY_4 !!
		int InvenIdx = 0;
		RobotHoldWidgetList.clear();
		
		for(InventoryName inventoryName : InventoryName.values())//for all inventory type..
		{
			System.out.println("[NavigationPathSelector]WAREHOUSE_INVENTORY_INFO: inventoryName =" + inventoryName);
			if(minventoryInfo.hasInventoryStation(inventoryName)) // If my warehouse has that inventory type..
			{
				boolean bAddpath = false;
				for(QuantifiedWidget qwInven : minventoryInfo.getInventoryInfo(inventoryName)) //for each widget in Inventory.
				{
					tempRemainOrderWidget.clear();
					for(QuantifiedWidget qw : orderItemListfornavation)
					{
						tempRemainOrderWidget.add(qw);
				 	}
					
					
					for(int i = 0; i < orderItemListfornavation.size(); i++) // for each widget..
					{
						QuantifiedWidget qwOrder = orderItemListfornavation.get(i);
						if(qwOrder.getWidget().equals(qwInven.getWidget())) 
						{
							if(qwInven.getQuantity() >= qwOrder.getQuantity())
							{
								System.out.println("ThisInven have more number of widget for order");
								System.out.println("Inven" + qwInven.getWidget()+" : "+qwInven.getQuantity());
								System.out.println("Order" + qwOrder.getWidget()+" : " + qwOrder.getQuantity());
								bAddpath = true;
								RobotHoldWidgetList.add(qwOrder);
								tempRemainOrderWidget.remove(i);
							}
							else
							{;							
							}
						}
						
				 	}
					
					orderItemListfornavation.clear();
					for(QuantifiedWidget qw : tempRemainOrderWidget)
					{
						orderItemListfornavation.add(qw);
				 	}
					
					
				}
				
				if(bAddpath == true)
				{
					System.out.println("This inventory is added route");
					robotAtX[InvenIdx].setQwOrderList(RobotHoldWidgetList);
					addPath(robotMoveToX[InvenIdx]);
					addPath(robotAtX[InvenIdx]);
				}
				else
				{
					System.out.println("This inventory is not included route");
					addPath(robotMoveToX[InvenIdx]);
				}
				bAddpath = false;
				RobotHoldWidgetList.clear();
			}
			else
			{ 
				// for not 
				System.out.println("[NavigationPathSelector] this inventory has not [" + InvenIdx + "]st Inventory");
				addPath(robotMoveToX[InvenIdx]);
			}
			
			
			//TODO I just want only 3 inventory station.... because now there are only 4 station in real..
			InvenIdx++;
			if(InvenIdx == 3)
			{
				addPath(robotMoveToX[InvenIdx]); // for shipping
				addPath(robotAtX[InvenIdx]);
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
