package com.lge.warehouse.supervisor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.lge.warehouse.util.InventoryName;
import com.lge.warehouse.util.QuantifiedWidget;

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
	public void reductInventoryWidget(InventoryName inventoryName, WidgetInfo widgetInfo, int count){
		if(!mInventoryInfoList.containsKey(inventoryName)){
			mInventoryInfoList.put(inventoryName, new HashMap<WidgetInfo,Integer>());
		}
		int prevCnt = mInventoryInfoList.get(inventoryName).get(widgetInfo);
		mInventoryInfoList.get(inventoryName).put(widgetInfo,prevCnt-count);
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

	
    @Override
    public String toString() {
        return "WarehouseInventoryInfo{" + "mInventoryInfoList=" + mInventoryInfoList + ", mWarehouseId=" + mWarehouseId + '}';
    }
	public boolean hasInventory(InventoryName inventoryName, WidgetInfo widget) {
		// TODO Auto-generated method stub
		if(mInventoryInfoList.containsKey(inventoryName)){
			HashMap<WidgetInfo, Integer> widgetInfoMap = mInventoryInfoList.get(inventoryName);
			for(WidgetInfo wi : widgetInfoMap.keySet()){
				if(wi.equals(widget))
					return true;
			}
		}
		return false;
	}
        
}
