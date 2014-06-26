/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.supervisor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jms.MessageListener;

import org.apache.log4j.Logger;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseContext;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.common.bus.p2p.P2PConnection;
import com.lge.warehouse.common.bus.p2p.P2PReceiver;
import com.lge.warehouse.common.bus.p2p.P2PSender;
import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.QuantifiedWidget;
import com.lge.warehouse.util.WarehouseStatus;

/**
 *
 * @author seuki77
 */
public class WarehouseProxy {
	private static Logger logger = Logger.getLogger(WarehouseProxy.class);
    String mDest;
    String mSrc;
    P2PSender mSender;
    
    WarehouseStatus mWarehouseStatus = null;

    public WarehouseProxy(int id, MessageListener listener){
        mDest = WComponentType.WM_MSG_HANDLER.name()+id;
        mSrc = WComponentType.WAREHOUSE_SUPERVISOR.name();
        initBus(id, listener);
    }
    void initBus(int id, MessageListener listener){
        mSender = P2PConnection.createSender(mSrc+"_"+mDest);
        P2PReceiver receiver = P2PConnection.createReceiver(mDest+"_"+mSrc);
        receiver.setMessageListener(listener);
    }
    
    public void sendObj(EventMessageType type, Serializable body){
        mSender.sendObject(new EventMessage(mSrc,mDest,type, body));
    }
    
    public String getWarehouseIdentity(){
    	return mDest;
    }
    public void fillInventoryInfo(WarehouseInventoryInfo warehouseInventoryInfo){
    	WarehouseInventoryInfoRepository.getInstance().fillInventoryWidget(warehouseInventoryInfo);
    	sendWarehouseFillInventoryInfo(warehouseInventoryInfo);
    }
    public void sendWarehouseFillInventoryInfo(WarehouseInventoryInfo warehouseInventoryInfo){
    	//WarehouseInventoryInfo warehouseInventoryInfo = WarehouseInventoryInfoRepository.getInstance().getWarehouseInventoryInfo();
    	mSender.sendObject(new EventMessage(mSrc, mDest, EventMessageType.FILL_INVENTORY_WIDGET, warehouseInventoryInfo));
    }
    public void sendWarehouseInventoryInfo() {
		// TODO Auto-generated method stub
    	WarehouseInventoryInfo warehouseInventoryInfo = WarehouseInventoryInfoRepository.getInstance().getWarehouseInventoryInfo();
    	mSender.sendObject(new EventMessage(mSrc, mDest, EventMessageType.WAREHOUSE_INVENTORY_INFO, warehouseInventoryInfo));
	}    
	public void handleOrder(Order order) {
		// TODO Auto-generated method stub
		OrderStorage.getInstance().setInProgressOrder(this, order);
		sendObj(EventMessageType.FILL_ORDER, order);
	}
	public void finishFillOrder(Order order) {
		// TODO Auto-generated method stub
		if(order.equals(OrderStorage.getInstance().getInProgressOrder(this))){
			logger.info("finish fill order, orderId = "+order.getOrderId());
			OrderStorage.getInstance().setCompleteOrder(this, order);

			order.setOrderStatus(Order.Status.ORDER_COMPLETE);
		}else{
			logger.info("received order = "+order+", stored order"+OrderStorage.getInstance().getInProgressOrder(this));
			String errorLog = "Completed order is not matching progress order";
			if(WarehouseContext.DEBUG_WITH_RUNTIME_EXCEPTION)
				throw new RuntimeException(errorLog);
			else
				logger.info(errorLog);
		}
	}
	
	public void updateWarehouseStatus(WarehouseStatus warehouseStatus) {
		// TODO Auto-generated method stub
		mWarehouseStatus = warehouseStatus;
		OrderStorage.getInstance().updateInProgressOrder(this, warehouseStatus.getInventoryListOfBot());
		WarehouseInventoryInfoRepository.getInstance().setInventoryInfo(warehouseStatus.getWarehouseInventoryInfo());
	}
	public void sendWidgetCatalog() {
		// TODO Auto-generated method stub
		sendObj(EventMessageType.SEND_WDIGET_CATALOG_TO_WM_MSG_HANDLER, WidgetCatalogRepository.getInstance().getWidgetCatalog());
	}
	public WarehouseInventoryInfo getInventoryInfo() {
		// TODO Auto-generated method stub
		return WarehouseInventoryInfoRepository.getInstance().getWarehouseInventoryInfo();
	}
	public WarehouseStatus getWarehouseStatus() {
		// TODO Auto-generated method stub
		return mWarehouseStatus;
	}
	public boolean isBusy() {
		// TODO Auto-generated method stub
		return (OrderStorage.getInstance().getInProgressOrder(this) != null);
	}
	public boolean hasEnoughInventory(Order order) {
		// TODO Auto-generated method stub
		return WarehouseInventoryInfoRepository.getInstance().hasEnoughInventory(order);
	}
}
