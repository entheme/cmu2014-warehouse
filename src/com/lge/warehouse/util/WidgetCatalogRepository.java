/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.util;

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
    private WidgetCatalogRepository(){
    	//for test [start]
    	mWidgetInfoList.add(new WidgetInfo(0,"Item1",100));
    	mWidgetInfoList.add(new WidgetInfo(1,"Item2",200));
    	mWidgetInfoList.add(new WidgetInfo(2,"Item3",300));
    	mWidgetInfoList.add(new WidgetInfo(3,"Item4",400));
    	mWidgetInfoList.add(new WidgetInfo(4,"Item5",500));
    	//for test [end];
    }
    
    public static WidgetCatalogRepository getInstance(){
        return sInstance;
    }
    public void setNewWidgetList(WidgetCatalog widgetCatalog){
    	mWidgetInfoList = widgetCatalog.getWidgetList();
    }
    public WidgetCatalog getWidgetCatalog(){
        return new WidgetCatalog(mWidgetInfoList);
    }
}
