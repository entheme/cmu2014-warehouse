/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.supervisor;

import com.lge.warehouse.util.Order;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author seuki77
 */
public class OrderQueue {
    static Logger logger = Logger.getLogger(OrderQueue.class);
    private static OrderQueue sInstance;
    private OrderQueue(){}
    private List<Order> mPendingOrderList = new ArrayList<Order>();
    
    public static OrderQueue getInstance(){
        if(sInstance == null)
            sInstance = new OrderQueue();
        return sInstance;
    }
    public synchronized void put(Order order){
    	order.setOrderStatus(Order.Status.ORDER_PENDING);
        mPendingOrderList.add(order);
    }
    public synchronized void remove(Order order){
    	mPendingOrderList.remove(order);
    }
    public synchronized List<Order> getPendingOrderList(){
        return mPendingOrderList;
    }
    public int getSize(){
        return mPendingOrderList.size();
    }
}
