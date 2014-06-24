/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.supervisor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.lge.warehouse.util.NewWidgetInfo;
import com.lge.warehouse.util.WidgetCatalog;

/**
 *
 * @author kihyung2.lee
 */
class WidgetCatalogRepository {
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
    	addNewWidget("Basketball",400);
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
}
