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
abstract public class Widget {
    private final int mProductId;
    
    protected Widget(int id) {
        mProductId = id;
    }
    
    public int getProuctId() {
        return mProductId;
    }
    
    abstract public String getName();
}
