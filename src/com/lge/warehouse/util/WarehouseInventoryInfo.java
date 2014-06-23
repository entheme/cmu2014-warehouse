package com.lge.warehouse.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WarehouseInventoryInfo implements Serializable{
	HashMap<InventoryName, List<QuantifiedWidget>> mInventoryInfoList = new HashMap<InventoryName, List<QuantifiedWidget>>();
	private int mWarehouseId = -1;
	public WarehouseInventoryInfo(){}
	public WarehouseInventoryInfo(int warehouseId){
		mWarehouseId = warehouseId; 
	}
	public void addInventory(InventoryName inventoryName, WidgetInfo widgetInfo, int count){
		if(!mInventoryInfoList.containsKey(inventoryName))
			mInventoryInfoList.put(inventoryName, new ArrayList<QuantifiedWidget>());
		mInventoryInfoList.get(inventoryName).add(new QuantifiedWidget(widgetInfo, count));
	}
	public List<QuantifiedWidget> getInventoryInfo(InventoryName inventoryName){
		if(mInventoryInfoList.containsKey(inventoryName))
			return mInventoryInfoList.get(inventoryName);
		else
			return null;
	}
	public int getWarehouseId(){
		return mWarehouseId;
	}
}
