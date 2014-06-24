/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.supervisor;

import java.util.List;

import org.apache.log4j.Logger;

import com.lge.warehouse.common.app.WarehouseContext;
import com.lge.warehouse.util.InventoryName;
import com.lge.warehouse.util.QuantifiedWidget;
import com.lge.warehouse.util.WarehouseInventoryInfo;

/**
 *
 * @author seuki77
 */
class WarehouseInventoryInfoRepository {
	private static Logger logger = Logger.getLogger(WarehouseInventoryInfoRepository.class);
	WarehouseInventoryInfo mInventoryInfo = new WarehouseInventoryInfo();
	public WarehouseInventoryInfoRepository(){
		mInventoryInfo.addNewWidgetToInventory(InventoryName.INVENTORY_1, WidgetCatalogRepository.getInstance().getWidgetInfo(0), 100);
		mInventoryInfo.addNewWidgetToInventory(InventoryName.INVENTORY_2, WidgetCatalogRepository.getInstance().getWidgetInfo(1), 100);
		mInventoryInfo.addNewWidgetToInventory(InventoryName.INVENTORY_3, WidgetCatalogRepository.getInstance().getWidgetInfo(2), 100);
		mInventoryInfo.addNewWidgetToInventory(InventoryName.INVENTORY_4, WidgetCatalogRepository.getInstance().getWidgetInfo(3), 100);
		mInventoryInfo.addNewWidgetToInventory(InventoryName.INVENTORY_4, WidgetCatalogRepository.getInstance().getWidgetInfo(4), 100);
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
	public void dump() {
		// TODO Auto-generated method stub
		logger.info("WarehouseInventoryRepository dump");
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
		for(InventoryName inventoryName : InventoryName.values()){
			if(warehouseInventoryInfo.hasInventoryStation(inventoryName)){
				List<QuantifiedWidget> qwList = warehouseInventoryInfo.getInventoryInfo(inventoryName);
				for(QuantifiedWidget qw : qwList){
					mInventoryInfo.fillInventoryWidget(inventoryName, qw.getWidget(), qw.getQuantity());
				}
			}
		}
	}
	public void reduceInventoryInfo(WidgetInfo widget, int count) {
		// TODO Auto-generated method stub
		for(InventoryName inventoryName : InventoryName.values()){
			if(mInventoryInfo.hasInventoryStation(inventoryName)){
				for(QuantifiedWidget qw : mInventoryInfo.getInventoryInfo(inventoryName)){
					if(qw.getWidget().equals(widget)){
						if(qw.getQuantity()>count){
							qw.setQuantity(count);
						}else{
							String errLog = "update count is bigger than stored count";
							if(WarehouseContext.DEBUG_WITH_RUNTIME_EXCEPTION)
								throw new RuntimeException(errLog);
							else
								logger.info(errLog);
						}
					}
				}
			}
		}
	}
	public WarehouseInventoryInfo getWarehouseInventoryInfo() {
		// TODO Auto-generated method stub
		return mInventoryInfo;
	}
}
