/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.ordersys.server;

import java.lang.Runnable;

/**
 *
 * @author kihyung2.lee
 */
public class Server implements Runnable  {
    private static Server sInstance = null;
    
    private Server() {}
    
    public void initialize() {
        System.out.println("OrderSys Server is initializing...");
        
        // TODO: Do initialization
        
        System.out.println("OrderSys Server has been initialized");
      }
    
    @Override
    public void run() {
        // TODO: implement main loop here
    }

    public static Server getInstance() {
        if (sInstance == null) {
            sInstance = new Server();
        }
        return sInstance;
    }
}
