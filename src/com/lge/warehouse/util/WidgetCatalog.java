/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.util;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import com.lge.warehouse.util.WidgetCatalogRepository.WidgetInfo;

/**
 *
 * @author seuki77
 */
public class WidgetCatalog implements Serializable{
	private static Logger logger = Logger.getLogger(WidgetCatalog.class);
	private List<WidgetInfo> mWidgetList;
	public WidgetCatalog(List<WidgetInfo> widgetInfoList){
		mWidgetList = widgetInfoList;
	}
	public int getWidgetInfoCnt(){
		return mWidgetList.size();
	}
	public WidgetInfo getWidgetInfoAt(int index){
		return mWidgetList.get(index);
	}
	public List<WidgetInfo> getWidgetList(){
		return mWidgetList;
	}
	public void dump(){
		logger.info("WidgetCatalog dump");
		for(WidgetInfo widgetInfo : mWidgetList){
			logger.info(widgetInfo);
		}
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mWidgetList == null) ? 0 : mWidgetList.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WidgetCatalog other = (WidgetCatalog) obj;
		if (mWidgetList == null) {
			if (other.mWidgetList != null)
				return false;
		} else if (!mWidgetList.equals(other.mWidgetList))
			return false;
		return true;
	}
}
