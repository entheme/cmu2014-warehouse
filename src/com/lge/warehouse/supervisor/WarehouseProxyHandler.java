/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.lge.warehouse.supervisor;

import java.util.HashMap;

import javax.jms.MessageListener;

import org.apache.log4j.Logger;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.MsgInterface;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.util.Order;
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
	public void updateInventory(String warehouseName,
			HashMap<WidgetInfo, Integer> inventoryMap) {
		// TODO Auto-generated method stub
		WarehouseProxy wp = mWarehouses.get(warehouseName);
		wp.updateInventoryInfo(inventoryMap);
	}
	public void requestFillOrder(Order order) {
		// TODO Auto-generated method stub
		for(WarehouseProxy wp : mWarehouses.values()){
    		if(wp.hasInventory(order))
    			wp.handleOrder(order);
    	}
	}
	public void finishFillOrder(String warehouseName, Order order) {
		// TODO Auto-generated method stub
		WarehouseProxy wp = mWarehouses.get(warehouseName);
		wp.finishFillOrder(order);
	}
}
