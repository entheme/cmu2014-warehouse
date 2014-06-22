/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.supervisor;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.lge.warehouse.util.WidgetInfo;

/**
 *
 * @author seuki77
 */
public class WarehouseInventoryInfoRepository {
	private static Logger logger = Logger.getLogger(WarehouseInventoryInfoRepository.class);
    private HashMap<WidgetInfo, Integer> mWidgetInventoryMap; // WidgetInfo, Count of Widget
    
    public WarehouseInventoryInfoRepository(){
    	mWidgetInventoryMap = new HashMap<WidgetInfo, Integer>();
    }
    public void updateInventoryInfo(WidgetInfo wi, int count){
    	mWidgetInventoryMap.put(wi, count);
    }
    
    public int getInventoryCount(WidgetInfo wi){
    	if(!mWidgetInventoryMap.containsKey(wi))
    		return 0;
    	return mWidgetInventoryMap.get(wi);
    }
	public void dump() {
		// TODO Auto-generated method stub
		logger.info("WarehouseInventoryRepository dump");
		for(WidgetInfo wi : mWidgetInventoryMap.keySet()){
			logger.info(wi.getName()+" = "+mWidgetInventoryMap.get(wi));
		}
	}
}
