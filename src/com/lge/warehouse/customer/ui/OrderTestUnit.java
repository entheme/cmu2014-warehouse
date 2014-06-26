/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.customer.ui;

/**
 *
 * @author Jihun
 */
public class OrderTestUnit {
    private int widgetId;
    private int quantity;
    
    public OrderTestUnit(int widgetId, int quantity) {
        this.widgetId = widgetId;
        this.quantity = quantity;
    }
    
    public int getWidgetId() {
        return widgetId;
    }
    
    public int getQuantity() {
        return quantity;
    }
}
