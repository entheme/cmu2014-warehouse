/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.customer.ui;

import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.QuantifiedWidget;
import com.lge.warehouse.util.WidgetCatalog;
import com.lge.warehouse.util.WidgetInfo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jihun
 */
public class OrderSysWidgetCart {
    private static final ArrayList<QuantifiedWidget> widgetCart = new ArrayList<QuantifiedWidget>();
    //private static final Order order = new Order();
    private static WidgetCatalog widgetCatalog = null;
    
  
    static {
        initWidgetCart();
    }
    
    public static String getWidgetNameByIndex(int index){
        WidgetInfo widget = widgetCatalog.getWidgetInfoAt(index);
        return widget.getName();
    }
    
    public static int getWidgetCatlogSize(){
        return widgetCatalog.getWidgetInfoCnt();
    }
    
    public static void setWidgetQuantity(int index, int quantity){
        QuantifiedWidget quantifiedWidget = widgetCart.get(index);
        quantifiedWidget.setQuantity(quantity);
        widgetCart.set(index, quantifiedWidget);
    }
    
    public static int getWidgetQuantity(int index){
        return widgetCart.get(index).getQuantity();
    }
    
    public static void initWidgetCart(){
        List <WidgetInfo> widgetInfoList = new ArrayList<WidgetInfo>();
        for(int i= 0 ;i<5; i++){
            widgetInfoList.add(new WidgetInfo(i, "item"+i, 500));
            
        }
        widgetCatalog = new WidgetCatalog(widgetInfoList);
        
        int widgetCount = getWidgetCatlogSize();
        boolean bWidgetCartCreated = false;
        
        if(widgetCart.size() > 0)
            bWidgetCartCreated = true;
         
        for(int i=0; i<widgetCount; i++){
            if(bWidgetCartCreated) {
                setWidgetQuantity(i, 0);
            }
            else {
                QuantifiedWidget quantifiedWidget = new QuantifiedWidget(widgetCatalog.getWidgetInfoAt(i), 0);
                widgetCart.add(quantifiedWidget);
            }
        }
        
        //
        Order order1 = new Order();
      
           
    }
    
}
