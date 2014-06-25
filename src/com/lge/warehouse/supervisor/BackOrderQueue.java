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
public class BackOrderQueue {
    static Logger logger = Logger.getLogger(BackOrderQueue.class);
    private static BackOrderQueue sInstance;
    private List<Order> mOrderList = new ArrayList<Order>();
    
    private BackOrderQueue(){}
    public static BackOrderQueue getInstance() {
        if(sInstance == null)
            sInstance = new BackOrderQueue();
        return sInstance;
    }
    
    public void putOrder(Order order){
        synchronized(this){
        	order.setOrderStatus(Order.Status.ORDER_BACK_ORDERED);
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
    public synchronized List<Order> getBackOrderList(){
    	return mOrderList;
    }
	public synchronized void removeOrder(Order order) {
		// TODO Auto-generated method stub
		mOrderList.remove(order);
	}
}
