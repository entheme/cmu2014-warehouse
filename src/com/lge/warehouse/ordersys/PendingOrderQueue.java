/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.ordersys;

import com.lge.warehouse.util.Order;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author seuki77
 */
public class PendingOrderQueue {
    static Logger logger = Logger.getLogger(PendingOrderQueue.class);
    private static PendingOrderQueue sInstance;
    private List<Order> mOrderList = new ArrayList<Order>();
    
    private PendingOrderQueue(){}
    public static PendingOrderQueue getInstance() {
        if(sInstance == null)
            sInstance = new PendingOrderQueue();
        return sInstance;
    }
    
    public void putOrder(Order order){
        synchronized(this){
            mOrderList.add(order);
        }
    }
    
    public Order getOrder(){
        if (!mOrderList.isEmpty()){
            synchronized(this){
                if (!mOrderList.isEmpty()){
                    return mOrderList.remove(0);
                }
                return null;
            }
        }
        return null;
    }
}
