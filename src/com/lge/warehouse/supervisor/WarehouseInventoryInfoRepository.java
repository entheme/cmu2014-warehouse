/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.supervisor;

import java.util.List;

import org.apache.log4j.Logger;

import com.lge.warehouse.util.InventoryName;
import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.QuantifiedWidget;

/**
 *
 * @author seuki77
 */
class WarehouseInventoryInfoRepository {
	private static Logger logger = Logger.getLogger(WarehouseInventoryInfoRepository.class);
	WarehouseInventoryInfo mInventoryInfo = new WarehouseInventoryInfo();
	private static WarehouseInventoryInfoRepository sInstance;
	private WarehouseInventoryInfoRepository(){
		mInventoryInfo.addNewWidgetToInventory(InventoryName.INVENTORY_1, WidgetCatalogRepository.getInstance().getWidgetInfo(0), 0);
		mInventoryInfo.addNewWidgetToInventory(InventoryName.INVENTORY_2, WidgetCatalogRepository.getInstance().getWidgetInfo(1), 0);
		mInventoryInfo.addNewWidgetToInventory(InventoryName.INVENTORY_2, WidgetCatalogRepository.getInstance().getWidgetInfo(2), 0);
		mInventoryInfo.addNewWidgetToInventory(InventoryName.INVENTORY_3, WidgetCatalogRepository.getInstance().getWidgetInfo(3), 0);
		mInventoryInfo.addNewWidgetToInventory(InventoryName.INVENTORY_3, WidgetCatalogRepository.getInstance().getWidgetInfo(4), 0);
//		mInventoryInfo.addNewWidgetToInventory(InventoryName.INVENTORY_4, WidgetCatalogRepository.getInstance().getWidgetInfo(3), 100);
//		mInventoryInfo.addNewWidgetToInventory(InventoryName.INVENTORY_4, WidgetCatalogRepository.getInstance().getWidgetInfo(4), 100);
	}
	public static WarehouseInventoryInfoRepository getInstance() {
		if(sInstance == null)
			sInstance = new WarehouseInventoryInfoRepository();
		return sInstance;
	}
	public void setInventoryInfo(WarehouseInventoryInfo inventoryInfo){
		mInventoryInfo = inventoryInfo;
	}
	public int getInventoryCount(WidgetInfo wi){
		int inventoryCnt = 0;
		for(InventoryName inventoryName : InventoryName.values()){
			if(mInventoryInfo.hasInventoryStation(inventoryName)){
				for(QuantifiedWidget qw : mInventoryInfo.getInventoryInfo(inventoryName)){
					if (qw.getWidget().equals(wi)){
						inventoryCnt += qw.getQuantity();
					}
				}
			}
		}
		return inventoryCnt;
	}
	public void dump(String title) {
		// TODO Auto-generated method stub
		logger.info(title);
		logger.info("Repository status");
		int inventoryCnt = 0;
		for(InventoryName inventoryName : InventoryName.values()){
			StringBuffer sb = new StringBuffer(inventoryName.name());
			if(mInventoryInfo.hasInventoryStation(inventoryName)){
				for(QuantifiedWidget qw : mInventoryInfo.getInventoryInfo(inventoryName)){
					sb.append(" : ").append(qw);
				}
			}
			logger.info(sb.toString());
		}
	}
	public void fillInventoryWidget(
			WarehouseInventoryInfo warehouseInventoryInfo) {
		// TODO Auto-generated method stub
//		for(InventoryName inventoryName : InventoryName.values()){
//			if(warehouseInventoryInfo.hasInventoryStation(inventoryName)){
//				List<QuantifiedWidget> qwList = warehouseInventoryInfo.getInventoryInfo(inventoryName);
//				for(QuantifiedWidget qw : qwList){
//					if(mInventoryInfo.hasInventory(inventoryName, qw.getWidget())){
//						mInventoryInfo.fillInventoryWidget(inventoryName, qw.getWidget(), qw.getQuantity());
//						logger.info("already existed widget, fillInventoryWidget ");
//					}else{
//						mInventoryInfo.addNewWidgetToInventory(inventoryName, qw.getWidget(), qw.getQuantity());
//						logger.info("new Widget, addNewWidgetToInventory");
//					}
//				}
//			}
//		}
		mInventoryInfo.fillInventoryInfo(warehouseInventoryInfo);
	}
	public void reduceInventoryInfo(WidgetInfo widgetInfo, int count) {
		// TODO Auto-generated method stub
		mInventoryInfo.reduceInventoryWidget(widgetInfo, count);
	}
	public WarehouseInventoryInfo getWarehouseInventoryInfo() {
		// TODO Auto-generated method stub
		return mInventoryInfo;
	}
	public boolean hasEnoughInventory(Order order) {
		// TODO Auto-generated method stub
		return mInventoryInfo.hasInventory(order);
	}
}
