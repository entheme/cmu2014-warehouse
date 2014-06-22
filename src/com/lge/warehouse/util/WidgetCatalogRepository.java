/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.util;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author kihyung2.lee
 */
public class WidgetCatalogRepository {
    private static WidgetCatalogRepository sInstance = new WidgetCatalogRepository();
    private static int mWidgetCnt = 0;
    private HashMap<String, WidgetInfo> mWidgetInfoList = new HashMap<String, WidgetInfo>();
    static Logger logger = Logger.getLogger(WidgetCatalogRepository.class);
    private WidgetCatalogRepository(){
        
    }
    public WidgetCatalogRepository getInstance(){
        return sInstance;
    }
    public boolean addNewWidget(String name, int price){
        if(mWidgetInfoList.containsKey(name)){
            logger.info(name+" is already in WidgetCatalogRepository");
            return false;
        }
        mWidgetInfoList.put(name,new WidgetInfo(++mWidgetCnt, name, price));
        return true;
    }
    public WidgetCatalog getWidgetCatalog(){
        return new WidgetCatalog(new ArrayList(mWidgetInfoList.values()));
    }
}
