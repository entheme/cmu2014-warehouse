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
    private BackOrderQueue(){}
    private List<Order> mBackOrderList = new ArrayList<Order>();
    
    public static BackOrderQueue getInstance(){
        if(sInstance == null)
            sInstance = new BackOrderQueue();
        return sInstance;
    }
    public void put(Order order){
        mBackOrderList.add(order);
    }
    public void remove(Order order){
    	mBackOrderList.remove(order);
    }
    public List<Order> getBackOrderList(){
        return mBackOrderList;
    }
    public int getSize(){
        return mBackOrderList.size();
    }
}
