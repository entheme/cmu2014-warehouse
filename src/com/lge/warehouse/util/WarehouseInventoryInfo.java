package com.lge.warehouse.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WarehouseInventoryInfo implements Serializable{
	HashMap<InventoryName, HashMap<WidgetInfo, Integer>> mInventoryInfoList = new HashMap<InventoryName, HashMap<WidgetInfo, Integer>>();
	private int mWarehouseId = -1;
	public WarehouseInventoryInfo(){}
	public WarehouseInventoryInfo(int warehouseId){
		mWarehouseId = warehouseId; 
	}
	public void addInventory(InventoryName inventoryName, WidgetInfo widgetInfo, int count){
		if(!mInventoryInfoList.containsKey(inventoryName))
			mInventoryInfoList.put(inventoryName, new HashMap<WidgetInfo,Integer>());
		mInventoryInfoList.get(inventoryName).put(widgetInfo,count);
	}
	public boolean hasInventoryStation(InventoryName inventoryName){
		return mInventoryInfoList.containsKey(inventoryName);
	}
	public List<QuantifiedWidget> getInventoryInfo(InventoryName inventoryName){
		if(mInventoryInfoList.containsKey(inventoryName)){
			List<QuantifiedWidget> list = new ArrayList<QuantifiedWidget>();
			HashMap<WidgetInfo, Integer> widgetInfoMap = mInventoryInfoList.get(inventoryName);
			for(WidgetInfo wi : widgetInfoMap.keySet()){
				list.add(new QuantifiedWidget(wi, widgetInfoMap.get(wi)));
			}
			return list;
		}else
			return null;
	}
	public int getWarehouseId(){
		return mWarehouseId;
	}
}
