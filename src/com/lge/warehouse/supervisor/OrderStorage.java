/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.supervisor;

import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.QuantifiedWidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author seuki77
 */
public class OrderStorage {
    static Logger logger = Logger.getLogger(OrderStorage.class);
    private static OrderStorage sInstance;
    private OrderStorage(){}
    private List<Order> mPendingOrderList = new ArrayList<Order>();
    private HashMap<WarehouseProxy, Order> mInProgressOrderMap = new HashMap<WarehouseProxy, Order>();
	private List<Order> mCompletedOrderList = new ArrayList<Order>();
	
	public void pushPendingOrder(Order order) {
		// TODO Auto-generated method stub
		OrderStorage.getInstance().put(order);
	}
	
    public static OrderStorage getInstance(){
        if(sInstance == null)
            sInstance = new OrderStorage();
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
    public void setInProgressOrder(WarehouseProxy warehouseProxy, Order order) {
		// TODO Auto-generated method stub
		if(mInProgressOrderMap.containsKey(warehouseProxy)){
			logger.info("Already have an InProgressOrder "+mInProgressOrderMap.get(warehouseProxy));
			mInProgressOrderMap.remove(warehouseProxy);
		}
		order.setOrderStatus(Order.Status.ORDER_IN_PROGRESS);
		mInProgressOrderMap.put(warehouseProxy, order);
	}
	public Order getInProgressOrder(WarehouseProxy warehouseProxy) {
		// TODO Auto-generated method stub
		return mInProgressOrderMap.get(warehouseProxy);
	}
	public void setCompleteOrder(WarehouseProxy warehouseProxy, Order order) {
		// TODO Auto-generated method stub
		if(mInProgressOrderMap.containsKey(warehouseProxy)){
			if(mInProgressOrderMap.get(warehouseProxy).equals(order)){
				mInProgressOrderMap.remove(warehouseProxy);
				order.setOrderStatus(Order.Status.ORDER_COMPLETE);
				mCompletedOrderList.add(order);
			}
		}
	}
	public List<Order> getCompletedOrderList() {
		// TODO Auto-generated method stub
		return mCompletedOrderList;
	}
	public List<Order> getInProgressOrderList() {
		// TODO Auto-generated method stub
		List<Order> inProgressOrderList = new ArrayList<Order>();
		for(Order order : mInProgressOrderMap.values()){
			inProgressOrderList.add(order);
		}
		return inProgressOrderList;
	}
	public void updateInProgressOrder(WarehouseProxy warehouseProxy, List<QuantifiedWidget> inventoryListOfBot) {
		// TODO Auto-generated method stub
		Order order = mInProgressOrderMap.get(warehouseProxy);
		order.setLoadedItem(inventoryListOfBot);
	}

	public List<QuantifiedWidget> getLoadedItemList() {
		// TODO Auto-generated method stub
		List<QuantifiedWidget> loadedItemList = new ArrayList<QuantifiedWidget>();
		for(Order order : mInProgressOrderMap.values()){
			loadedItemList.addAll(order.getLoadedItem());
		}
		return loadedItemList;
	}
}
