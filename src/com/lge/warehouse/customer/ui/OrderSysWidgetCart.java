/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.customer.ui;

import com.lge.warehouse.util.Widget;
import com.lge.warehouse.util.WidgetCatalog;
import java.util.ArrayList;

/**
 *
 * @author Jihun
 */
public class OrderSysWidgetCart {
    private static final ArrayList<Integer> widgetQuantityList = new ArrayList<Integer>();
  
    static {
        initWidgetCart();
        //System.out.println("array size = " + widgetQuantityList.size());
    }
    
    public static String getWidgetNameByIndex(int index){
        Widget widget = WidgetCatalog.getWidgetById(index);
        return widget.getName();
    }
    
    public static int getWidgetCatlogSize(){
        return WidgetCatalog.getSize();
    }
    
    public static void setWidgetQuantity(int index, int quantity){
        widgetQuantityList.set(index, new Integer(quantity));
    }
    
    public static int getWidgetQuantity(int index){
       return widgetQuantityList.get(index).intValue();
    }
    
    public static void initWidgetCart(){
        int widgetCount = getWidgetCatlogSize();
        boolean bWidgetCartCreated = false;
        
        if(widgetQuantityList.size() > 0)
            bWidgetCartCreated = true;
            
        for(int i=0; i<widgetCount; i++){
            if(bWidgetCartCreated)
                widgetQuantityList.set(i, new Integer(0));
            else
                widgetQuantityList.add(new Integer(0));
        }
           
    }
    
}
