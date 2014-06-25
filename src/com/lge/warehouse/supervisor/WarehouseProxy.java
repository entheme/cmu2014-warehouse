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
    List<Order> mCompletedOrderList = new ArrayList<Order>();
    Order mProgressOrder = null;
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
    	sendWarehouseInventoryInfo();
    }
    public void sendWarehouseInventoryInfo(){
    	WarehouseInventoryInfo warehouseInventoryInfo = WarehouseInventoryInfoRepository.getInstance().getWarehouseInventoryInfo();
    	mSender.sendObject(new EventMessage(mSrc, mDest, EventMessageType.WAREHOUSE_INVENTORY_INFO, warehouseInventoryInfo));
    	
    }
    public boolean hasInventory(Order order){
    	logger.info("hasInventory");
    	WarehouseInventoryInfoRepository.getInstance().dump();
    	if(mProgressOrder != null) 
    		return false;
    	for(QuantifiedWidget qw : order.getItemList()){
    		if(WarehouseInventoryInfoRepository.getInstance().getInventoryCount(qw.getWidget())<qw.getQuantity()){
    			return false;
    		}
    	}
    	return true;
    }
	public void handleOrder(Order order) {
		// TODO Auto-generated method stub
		mProgressOrder = order;
		sendObj(EventMessageType.FILL_ORDER, order);
	}
	public void finishFillOrder(Order order) {
		// TODO Auto-generated method stub
		if(order.equals(mProgressOrder)){
			logger.info("finish fill order, orderId = "+order.getOrderId());
			mCompletedOrderList.add(order);
			mProgressOrder = null;
//			for(QuantifiedWidget qw : order.getItemList()){
//				int prevCount = mInventoryRepository.getInventoryCount(qw.getWidget());
//				prevCount -= qw.getQuantity();
//				mInventoryRepository.reduceInventoryInfo(qw.getWidget(),prevCount);
//			}
			order.setOrderStatus(Order.Status.ORDER_COMPLETE);
			WarehouseInventoryInfoRepository.getInstance().dump();
		}else{
			logger.info("received order = "+order+", stored order"+mProgressOrder);
			String errorLog = "Completed order is not matching progress order";
			if(WarehouseContext.DEBUG_WITH_RUNTIME_EXCEPTION)
				throw new RuntimeException(errorLog);
			else
				logger.info(errorLog);
		}
	}
	public List<Order> getCompletedOrderList() {
		// TODO Auto-generated method stub
		return mCompletedOrderList;
	}
	public Order getInProgressOrderList() {
		// TODO Auto-generated method stub
		return mProgressOrder;
	}
	public void updateWarehouseStatus(WarehouseStatus warehouseStatus) {
		// TODO Auto-generated method stub
//		List<QuantifiedWidget> list = warehouseStatus.getInventoryListOfBot();
//		for(QuantifiedWidget qw : list){
//			int prevCount = mInventoryRepository.getInventoryCount(qw.getWidget());
//			prevCount -= qw.getQuantity();
//			mInventoryRepository.reduceInventoryInfo(qw.getWidget(),prevCount);
//		}
		mWarehouseStatus = warehouseStatus;
		WarehouseInventoryInfoRepository.getInstance().setInventoryInfo(warehouseStatus.getWarehouseInventoryInfo());
		WarehouseInventoryInfoRepository.getInstance().dump();
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
		return (mProgressOrder != null);
	}
}
