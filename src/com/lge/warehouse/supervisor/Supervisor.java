/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.supervisor;

import java.lang.Runnable;
import java.lang.InterruptedException;

/**
 *
 * @author kihyung2.lee
 */
public class Supervisor implements Runnable {
    private static Supervisor sInstance = null;
    private OrderProvider mOrderProvider;
    private WarehouseManagerContainer mContainer;
    
    private Supervisor() {}
    
    public void initialize() {
        System.out.println("Warehouse Supervisor is initializing...");
        
        // TODO: Do initialization
        mOrderProvider = OrderProvider.getInstance();
        new Thread(mOrderProvider).start();
        
        mContainer = WarehouseManagerContainer.getInstance();
        new Thread(mContainer).start();
        
        System.out.println("Warehouse Supervisor has been initialized");
    }
    
    @Override
    public void run() {
        // TODO: implement main loop here
        while(true) {
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    
                }
            }
        }
    }
    
    public static Supervisor getInstance() {
        if (sInstance == null) {
            sInstance = new Supervisor();
        }
        return sInstance;
    }

    public static void main(String args[]) {
        // Initialize Supervisor
        Supervisor.getInstance().initialize();
        
        // We do not use Thread(Runnable).start() here.
        // Instead, we user main thread in Supervisor directly.
        Supervisor.getInstance().run();
    }

}
