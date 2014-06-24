package com.lge.warehouse.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.lge.warehouse.supervisor.WidgetInfo;

public class WarehouseInventoryInfo implements Serializable{
	private static Logger logger = Logger.getLogger(WarehouseInventoryInfo.class);
	HashMap<InventoryName, HashMap<WidgetInfo, Integer>> mInventoryInfoList = new HashMap<InventoryName, HashMap<WidgetInfo, Integer>>();
	private int mWarehouseId = -1;
	public WarehouseInventoryInfo(){}
	public WarehouseInventoryInfo(int warehouseId){
		mWarehouseId = warehouseId;
	}
	public void addNewWidgetToInventory(InventoryName inventoryName, WidgetInfo widgetInfo, int count){
		if(!mInventoryInfoList.containsKey(inventoryName)){
			mInventoryInfoList.put(inventoryName, new HashMap<WidgetInfo,Integer>());
		}
		mInventoryInfoList.get(inventoryName).put(widgetInfo,count);
	}
	public void fillInventoryWidget(InventoryName inventoryName, WidgetInfo widgetInfo, int count){
		if(!mInventoryInfoList.containsKey(inventoryName)){
			mInventoryInfoList.put(inventoryName, new HashMap<WidgetInfo,Integer>());
		}
		int prevCnt = mInventoryInfoList.get(inventoryName).get(widgetInfo);
		mInventoryInfoList.get(inventoryName).put(widgetInfo,count+prevCnt);
	}
	public boolean hasInventoryStation(InventoryName inventoryName){
		return mInventoryInfoList.containsKey(inventoryName);
	}
	public List<QuantifiedWidget> getInventoryInfo(InventoryName inventoryName){
		List<QuantifiedWidget> list = new ArrayList<QuantifiedWidget>();
		if(mInventoryInfoList.containsKey(inventoryName)){
			HashMap<WidgetInfo, Integer> widgetInfoMap = mInventoryInfoList.get(inventoryName);
			for(WidgetInfo wi : widgetInfoMap.keySet()){
				list.add(new QuantifiedWidget(wi, widgetInfoMap.get(wi)));
			}
		}
		return list;
	}
	public int getWarehouseId(){
		return mWarehouseId;
	}
}
