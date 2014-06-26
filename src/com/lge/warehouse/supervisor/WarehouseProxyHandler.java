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
import com.lge.warehouse.util.WarehouseStatus;

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
        wh.sendWidgetCatalog();
        wh.sendWarehouseInventoryInfo();
    }
	public void sendWidgetCatalog() {
		for(WarehouseProxy wp : mWarehouses.values()){
			wp.sendWidgetCatalog();
		}
	}
    
	public void fillInventoryWidget(WarehouseInventoryInfo warehouseInventoryInfo) {
		// TODO Auto-generated method stub
		WarehouseProxy wp = mWarehouses.get(WComponentType.WM_MSG_HANDLER.name()+warehouseInventoryInfo.getWarehouseId());
		wp.fillInventoryInfo(warehouseInventoryInfo);
	}
	public boolean requestFillOrder(Order order) {
		// TODO Auto-generated method stub
		for(WarehouseProxy wp : mWarehouses.values()){
    		if(wp.hasEnoughInventory(order)){
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
	
	public void updateWarehouseStatus(String warehouseName,
			WarehouseStatus warehouseStatus) {
		// TODO Auto-generated method stub 
		WarehouseProxy wp = mWarehouses.get(warehouseName);
		wp.updateWarehouseStatus(warehouseStatus);
	}
	public WarehouseInventoryInfo getInventoryInfo() {
		// TODO Auto-generated method stub
		for(WarehouseProxy wp : mWarehouses.values()){
			return wp.getInventoryInfo();
		}
		return null;
	}
	public WarehouseStatus getWarehouseStatus() {
		// TODO Auto-generated method stub
		for(WarehouseProxy wp : mWarehouses.values()){
			return wp.getWarehouseStatus();
		}
		return null;
	}
	public boolean isBusy() {
		// TODO Auto-generated method stub
		for(WarehouseProxy wp : mWarehouses.values()){
			if(!wp.isBusy()){
				return false;
			}
		}
		return true;
	}
	
}
