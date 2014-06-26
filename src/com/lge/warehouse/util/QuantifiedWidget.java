/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.util;

import java.io.Serializable;

import com.lge.warehouse.supervisor.WidgetInfo;
import java.util.List;

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
    public static String getListString(List<QuantifiedWidget> list) {
        StringBuffer sb = new StringBuffer();
        for(QuantifiedWidget quantifiedWidget : list) {
            sb.append(quantifiedWidget.toString()).append("\n");
        }
        return sb.toString();
    }
    
    public String toString(){
    	return mWidget+" quantity : "+mQuantity+"EA";
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mWidget == null) ? 0 : mWidget.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuantifiedWidget other = (QuantifiedWidget) obj;
		if (mWidget == null) {
			if (other.mWidget != null)
				return false;
		} else if (!mWidget.equals(other.mWidget))
			return false;
		return true;
	}
    
}
