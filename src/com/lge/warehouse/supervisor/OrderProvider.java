package com.lge.warehouse.supervisor;

import java.util.List;

import org.apache.log4j.Logger;

import com.lge.warehouse.common.app.MsgInterface;
import com.lge.warehouse.util.Order;

public class OrderProvider {
	static Logger logger = Logger.getLogger(OrderProvider.class);
	private MsgInterface mMsgInf;
	WarehouseProxyHandler mWarehouseProxyHandler;
	public OrderProvider(MsgInterface msgInf,WarehouseProxyHandler warehouseProxyHandler ){
		mMsgInf = msgInf;
		mWarehouseProxyHandler = warehouseProxyHandler;
	}
	public void pushPendingOrder(Order order) {
		// TODO Auto-generated method stub
		OrderQueue.getInstance().put(order);
	}
	public Order getOrder(){
		for(Order order : OrderQueue.getInstance().getPendingOrderList()){
			if(mWarehouseProxyHandler.hasInventory(order)){
				OrderQueue.getInstance().remove(order);
				return order;
			}
		}
		return null;
	}
	public List<Order> getPendingOrderList() {
		// TODO Auto-generated method stub
		return OrderQueue.getInstance().getPendingOrderList();
	}
}
