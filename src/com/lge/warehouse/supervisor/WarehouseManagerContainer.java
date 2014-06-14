/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.supervisor;

import java.util.List;

/**
 *
 * @author kihyung2.lee
 */
public class WarehouseManagerContainer implements Runnable {
    private static WarehouseManagerContainer sInstance = null;

    private List<WarehouseManagerProxy> mProxyList;
    
    public void initialize() {
        System.out.println("WarehouseManager Container has been initialized");
    }

    @Override
    public void run() {
        // TODO: implement main loop here
    }

    public static WarehouseManagerContainer getInstance() {
        if (sInstance == null) {
            sInstance = new WarehouseManagerContainer();
        }
        return sInstance;
    }

}
