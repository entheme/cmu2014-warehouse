/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.controller;

import java.lang.Runnable;

/**
 *
 * @author kihyung2.lee
 */
public class Controller implements Runnable {
    private static Controller sInstance = null;
    
    private Controller() {}
    
    public void initialize() {
        System.out.println("Warehouse Controller is initializing...");
        
        // TODO: Do initialization
        
        System.out.println("Warehouse Controller has been initialized");
      }
    
    @Override
    public void run() {
        // TODO: implement main loop here
    }
    
    public static Controller getInstance() {
        if (sInstance == null) {
            sInstance = new Controller();
        }
        return sInstance;
    }
}
