/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.util;

/**
 *
 * @author kihyung2.lee
 */
public class QuantifiedWidget {
    private Widget mWidget;
    private int mQuantity;
    
    public QuantifiedWidget(Widget w, int quantity) {
        mWidget = w;
        mQuantity = quantity; 
    }
    
    public Widget getWidget() { return mWidget; }
    public int getQuantity() { return mQuantity; }
}
