/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 *
 * @author kihyung2.lee
 */
public class WidgetCatalogRepository {
    private static WidgetCatalogRepository sInstance = new WidgetCatalogRepository();
    private List<WidgetInfo> mWidgetInfoList = new ArrayList<WidgetInfo>();
    static Logger logger = Logger.getLogger(WidgetCatalogRepository.class);
    private int mWidgetIdCnt = 0;
    private WidgetCatalogRepository(){
    	//for test [start]
    	addNewWidget("Soccer",300);
    	addNewWidget("Tennis",10);
    	addNewWidget("Baseball",150);
    	addNewWidget("Football",500);
    	//for test [end];
    }
    public WidgetInfo getWidgetInfo(int index){
    	if(index < mWidgetInfoList.size()){
    		return mWidgetInfoList.get(index);
    	}else{
    		return null;
    	}
    }
    public static WidgetCatalogRepository getInstance(){
        return sInstance;
    }
    public void addNewWidget(NewWidgetInfo newWidgetInfo){
    	mWidgetInfoList.add(new WidgetInfo(++mWidgetIdCnt, newWidgetInfo.getWidgetName(), newWidgetInfo.getWidgetPrice()));
    }
    public void addNewWidget(String widgetName, int price){
    	mWidgetInfoList.add(new WidgetInfo(++mWidgetIdCnt, widgetName, price));
    }
    public WidgetCatalog getWidgetCatalog(){
        return new WidgetCatalog(mWidgetInfoList);
    }
    public static class WidgetInfo implements Serializable{
        private final int mProductId;
        private final String mName;
        private final int mPrice;
        private WidgetInfo(int id, String name, int price) {
            mProductId = id;
            mName = name;
            mPrice = price;
        }
        
        public int getProuctId() {
            return mProductId;
        }
        
        public String getName(){
            return mName;
        }
        
        public int getPrice(){
            return mPrice;
        }
        
        @Override
        public String toString(){
            StringBuffer sb = new StringBuffer();
            sb.append("WidgetInfo : ").append(mProductId).append(":").append(mName).append(":").append(mPrice);
            return sb.toString();
        }
        
        @Override
    	public boolean equals(Object obj) {
    		if (this == obj)
    			return true;
    		if (obj == null)
    			return false;
    		if (getClass() != obj.getClass())
    			return false;
    		WidgetInfo other = (WidgetInfo) obj;
    		if (mName == null) {
    			if (other.mName != null)
    				return false;
    		} else if (!mName.equals(other.mName))
    			return false;
    		if (mPrice != other.mPrice)
    			return false;
    		if (mProductId != other.mProductId)
    			return false;
    		return true;
    	}

        @Override
    	public int hashCode() {
    		final int prime = 31;
    		int result = 1;
    		result = prime * result + ((mName == null) ? 0 : mName.hashCode());
    		result = prime * result + mPrice;
    		result = prime * result + mProductId;
    		return result;
    	}
    }
}
