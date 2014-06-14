/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.supervisor;

import java.lang.Runnable;

/**
 *
 * @author kihyung2.lee
 */
public class OrderProvider implements Runnable {
    private static OrderProvider sInstance = null;
    
    private OrderProvider() {}
    
    public void initialize() {
        System.out.println("OrderProvider has been initialized");
    }
    
    @Override
    public void run() {
        // TODO: implement main loop here
    }

    public static OrderProvider getInstance() {
        if (sInstance == null) {
            sInstance = new OrderProvider();
        }
        return sInstance;
    }

}
