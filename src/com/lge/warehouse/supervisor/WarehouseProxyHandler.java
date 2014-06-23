/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.lge.warehouse.supervisor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.jms.MessageListener;

import org.apache.log4j.Logger;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.MsgInterface;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.WarehouseInventoryInfo;
import com.lge.warehouse.util.WidgetInfo;

/**
 *
 * @author seuki77
 */
public class WarehouseProxyHandler {
    static Logger logger = Logger.getLogger(WarehouseProxyHandler.class);
    private int mWarehouseCounter;
    private MsgInterface mMsgInf;
    private MessageListener mMsgListener;
    HashMap<String, WarehouseProxy> mWarehouses = new HashMap<String,WarehouseProxy>();
    public WarehouseProxyHandler(MsgInterface msgInf, MessageListener listener){
        mMsgInf = msgInf;
        mMsgListener = listener;
    }
    public void handleWarehouseAddRequest(){
        mWarehouseCounter++;
        WarehouseProxy wh = new WarehouseProxy(mWarehouseCounter, mMsgListener);
        mWarehouses.put(wh.getWarehouseIdentity(), wh);
        mMsgInf.sendMsg(WComponentType.WM_MSG_HANDLER, EventMessageType.WAREHOUSE_ADD_ACCEPT, new Integer(mWarehouseCounter));
    }
    public boolean hasInventory(Order order){
    	for(WarehouseProxy wp : mWarehouses.values()){
    		if(wp.hasInventory(order))
    			return true;
    	}
        return false;
    }
	public void updateInventory(WarehouseInventoryInfo warehouseInventoryInfo) {
		// TODO Auto-generated method stub
		WarehouseProxy wp = mWarehouses.get(WComponentType.WM_MSG_HANDLER.name()+warehouseInventoryInfo.getWarehouseId());
		wp.updateInventoryInfo(warehouseInventoryInfo);
	}
	public boolean requestFillOrder(Order order) {
		// TODO Auto-generated method stub
		for(WarehouseProxy wp : mWarehouses.values()){
    		if(wp.hasInventory(order)){
    			order.setOrderStatus(Order.Status.ORDER_IN_PROGRESS);
    			wp.handleOrder(order);
    			return true;
    		}
    	}
		return false;
	}
	public void finishFillOrder(String warehouseName, Order order) {
		// TODO Auto-generated method stub
		WarehouseProxy wp = mWarehouses.get(warehouseName);
		wp.finishFillOrder(order);
	}
	public List<Order> getCompletedOrderList() {
		// TODO Auto-generated method stub
		List<Order> completedOrderList = new ArrayList<Order>();
		for(WarehouseProxy wp : mWarehouses.values()){
			completedOrderList.addAll(wp.getCompletedOrderList());
		}
		return completedOrderList;
	}
	public List<Order> getInProgressOrderList() {
		// TODO Auto-generated method stub
		List<Order> inProgressOrderList = new ArrayList<Order>();
		for(WarehouseProxy wp : mWarehouses.values()){
			Order order = wp.getInProgressOrderList();
			inProgressOrderList.add(order);
		}
		return inProgressOrderList;
	}
	
}
