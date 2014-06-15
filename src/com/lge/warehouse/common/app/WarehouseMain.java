/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.common.app;

import com.lge.warehouse.manager.Manager;
import com.lge.warehouse.ordersys.OrderingSystem;
import com.lge.warehouse.supervisor.Supervisor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author seuki77
 */
public class WarehouseMain {
    public static final void main(String[] args){
        OrderingSystem.initialize();
        Supervisor.initialize();
        Manager.initialize();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WarehouseMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        OrderingSystem.ping();
        Supervisor.ping();
        Manager.ping();
        
    }
}
