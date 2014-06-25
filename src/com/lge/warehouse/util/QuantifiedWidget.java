/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.util;

import java.io.Serializable;

import com.lge.warehouse.supervisor.WidgetInfo;

/**
 *
 * @author kihyung2.lee
 */
public class QuantifiedWidget implements Serializable{
    private WidgetInfo mWidget;
    private int mQuantity;
    
    public QuantifiedWidget(WidgetInfo w, int quantity) {
        mWidget = w;
        mQuantity = quantity; 
    }
    
    public WidgetInfo getWidget() { return mWidget; }
    public int getQuantity() { return mQuantity; }
    public void setQuantity(int quantity) { mQuantity = quantity; }
    
    public String toString(){
    	return mWidget+" quantity : "+mQuantity+"EA";
    }
}
