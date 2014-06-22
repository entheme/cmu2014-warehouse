/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.util;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author seuki77
 */
public class WidgetCatalog implements Serializable{
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
}
