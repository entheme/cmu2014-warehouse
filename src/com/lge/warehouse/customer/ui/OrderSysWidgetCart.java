/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.customer.ui;

import com.lge.warehouse.supervisor.WidgetInfo;
import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.QuantifiedWidget;
import com.lge.warehouse.util.WidgetCatalog;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Jihun
 */
public class OrderSysWidgetCart {	
    private static Logger logger = Logger.getLogger(OrderSysWidgetCart.class);
    private static final ArrayList<QuantifiedWidget> widgetCart = new ArrayList<QuantifiedWidget>();
    private static WidgetCatalog widgetCatalog = new WidgetCatalog(new ArrayList<WidgetInfo>());
    private static OrderSysUiUpdate sUiUpdate = null;
    private static Order mLastOrder = null;
  
    static {
        //createWidgetCart();
    }
    
    private static void addWidget(WidgetInfo widgetInfo) {
        QuantifiedWidget quantifiedWidget = new QuantifiedWidget(widgetInfo, 0);
        widgetCart.add(quantifiedWidget);
    }
    
    public static String getWidgetInfoByIndex(int index){
        WidgetInfo widget = widgetCatalog.getWidgetInfoAt(index);
        return widget.getName() + " [" + widget.getPrice() + "$]";
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
    
    public static void createWidgetCart() {
        int widgetCount = getWidgetCatlogSize();
        
        if(widgetCart.size() > 0) {
            widgetCart.clear();
        }
         
        for(int i=0; i<widgetCount; i++){
            addWidget(widgetCatalog.getWidgetInfoAt(i));
        }
    }
    
    public static void initWidgetCart(){
        int widgetCount = getWidgetCatlogSize();
     
        for(int i=0; i<widgetCount; i++){
            setWidgetQuantity(i, 0);
        }
    }
    public static void setWidgetCatalog(WidgetCatalog widgetCatalog){
    	logger.info("setWidgetCatalog catalog size = "+widgetCatalog.getWidgetInfoCnt());
    	OrderSysWidgetCart.widgetCatalog = widgetCatalog;
    	sUiUpdate.updateUi();
        createWidgetCart();
    }
    public static void setUiUpdateListener(OrderSysUiUpdate uiUpdate){
    	sUiUpdate = uiUpdate;
    }
    public static Order getOrderFromCart() {
        Order order = new Order();
        int catalogSize = getWidgetCatlogSize();
        
        if(catalogSize == 0)
            return null;
        
        for(int i=0; i<getWidgetCatlogSize(); i++) {
            order.addItem(widgetCart.get(i).getWidget(), widgetCart.get(i).getQuantity());
        }
        
        return order;
    }
    public static void setLastOrder(Order order) {
        mLastOrder = order;
    }
    
    public static String getCartInfo() {
        int totalPrice = 0;
        StringBuffer sb = new StringBuffer();
        
        if(getTotalQuantity() == 0 || getWidgetCatlogSize() == 0) {
            sb.append("Cart is empty\n");
            if(mLastOrder != null) {
                sb.append("-------------------------\n");
                sb.append("        Last Order     \n");
                sb.append(QuantifiedWidget.getListString(mLastOrder.getItemList()));
 
            }
                
            return sb.toString();
        }
        
        for(int i=0; i<getWidgetCatlogSize(); i++) {
            sb.append(widgetCart.get(i).getWidget().getName()).append(" :").append(widgetCart.get(i).getQuantity()).append("EA\n");
            totalPrice += widgetCart.get(i).getQuantity() * widgetCart.get(i).getWidget().getPrice();
        }
        //info += newLine + "Total Price = " + totalPrice + "$" + newLine;
        sb.append("Total Price = ").append(totalPrice).append("$\n");
        
        return sb.toString();
    }
    
    public static int getTotalQuantity() {
        int quantity = 0;
        
        for(int i=0; i<getWidgetCatlogSize(); i++) {
            quantity += widgetCart.get(i).getQuantity();
        }
        return quantity;
    }
    
}
