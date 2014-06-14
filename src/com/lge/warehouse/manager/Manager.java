/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.manager;

import java.lang.Runnable;

/**
 *
 * @author kihyung2.lee
 */
public class Manager implements Runnable  {
    private static Manager sInstance = null;
    
    private Manager() {}
    
    public void initialize() {
        System.out.println("Warehouse Manager is initializing...");
        
        // TODO: Do initialization
        
        System.out.println("Warehouse Manager has been initialized");
      }

    @Override
    public void run() {
        // TODO: implement main loop here
    }

    public static Manager getInstance() {
        if (sInstance == null) {
            sInstance = new Manager();
        }
        return sInstance;
    }
}
