package com.lge.warehouse.supervisor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

import com.lge.warehouse.util.InventoryName;
import com.lge.warehouse.util.Order;
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
	public void reduceInventoryWidget(InventoryName inventoryName, WidgetInfo widgetInfo, int count){
		if(!mInventoryInfoList.containsKey(inventoryName)){
			mInventoryInfoList.put(inventoryName, new HashMap<WidgetInfo,Integer>());
		}
		int prevCnt = mInventoryInfoList.get(inventoryName).get(widgetInfo);
		mInventoryInfoList.get(inventoryName).put(widgetInfo,prevCnt-count);
	}
	public void reduceInventoryWidget(WidgetInfo widgetInfo, int count){
		for(InventoryName inventoryName : InventoryName.values()){
			if(mInventoryInfoList.containsKey(inventoryName)){
				if(mInventoryInfoList.get(inventoryName).containsKey(widgetInfo)){
					int prevCnt = mInventoryInfoList.get(inventoryName).get(widgetInfo);
					mInventoryInfoList.get(inventoryName).put(widgetInfo, prevCnt-count);
					logger.info("reduceInventoryWidget "+widgetInfo+", "+prevCnt+"->"+(prevCnt-count));
				}
			}
		}
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
	public boolean hasInventory(Order order){
		for(QuantifiedWidget orderItem : order.getItemList()){
			for(InventoryName inventoryName : InventoryName.values()){
				if(hasInventoryStation(inventoryName)){
					for(QuantifiedWidget inventoryItem : getInventoryInfo(inventoryName)){
						if(orderItem.getWidget().equals(inventoryItem.getWidget())){
							if(orderItem.getQuantity()>inventoryItem.getQuantity()){
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}
	public int getWarehouseId(){
		return mWarehouseId;
	}


	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		List<QuantifiedWidget> list = new ArrayList<QuantifiedWidget>();

		//sb.append("Warehouse\n");
		sb.append("Inventory Info\n\n");

		for(int i=0;i<mInventoryInfoList.size();i++) {
			InventoryName inventoryName = InventoryName.fromInteger(i);
			list = getInventoryInfo(inventoryName);
			sb.append("-").append(inventoryName).append("\n");
			for(QuantifiedWidget quantifiedWidget:list) {
				sb.append(quantifiedWidget.toString()+"\n");
			}
			sb.append("\n");
		}

		return sb.toString();
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
	public void fillInventoryInfo(WarehouseInventoryInfo warehouseInventoryInfo){
		for(InventoryName inventoryName : InventoryName.values()){
			if(warehouseInventoryInfo.hasInventoryStation(inventoryName)){
				List<QuantifiedWidget> qwList = warehouseInventoryInfo.getInventoryInfo(inventoryName);
				for(QuantifiedWidget qw : qwList){
					if(this.hasInventory(inventoryName, qw.getWidget())){
						this.fillInventoryWidget(inventoryName, qw.getWidget(), qw.getQuantity());
						logger.info("already existed widget, fillInventoryWidget ");
					}else{
						this.addNewWidgetToInventory(inventoryName, qw.getWidget(), qw.getQuantity());
						logger.info("new Widget, addNewWidgetToInventory");
					}
				}
			}
		}
	}
}
