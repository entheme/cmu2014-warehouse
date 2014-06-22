/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.util;

import java.io.Serializable;

/**
 *
 * @author kihyung2.lee
 */
public class WidgetInfo implements Serializable{
    private final int mProductId;
    private final String mName;
    private final int mPrice;
    public WidgetInfo(int id, String name, int price) {
        mProductId = id;
        mName = name;
        mPrice = price;
    }
    
    public int getProuctId() {
        return mProductId;
    }
    
    public String getName(){
        return mName;
    }
    
    public int getPrice(){
        return mPrice;
    }
    
    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("WidgetInfo : ").append(mProductId).append(":").append(mName).append(":").append(mPrice);
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object ob){
        if(!(ob instanceof WidgetInfo))
            return false;
        WidgetInfo wi = (WidgetInfo)ob;
        if((wi.mName.equals(this.mName))&&
                (wi.mProductId==this.mProductId)&&
                (wi.mPrice== this.mPrice))
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        return mPrice+mName.hashCode()+mProductId; //To change body of generated methods, choose Tools | Templates.
    }
    
}
