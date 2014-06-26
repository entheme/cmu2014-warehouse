package com.lge.warehouse.supervisor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.lge.warehouse.common.app.MsgInterface;
import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.QuantifiedWidget;

public class OrderProvider {
	private static OrderProvider sInstance;
	static Logger logger = Logger.getLogger(OrderProvider.class);
	
	private OrderProvider(){
	}
	public static OrderProvider getInstance(){
		if(sInstance == null)
			sInstance = new OrderProvider();
		return sInstance;
	}
	
	public Order getOrder(){
		for(Order order : OrderStorage.getInstance().getPendingOrderList()){
			if(WarehouseInventoryInfoRepository.getInstance().hasEnoughInventory(order)){
				OrderStorage.getInstance().remove(order);
				return order;
			}
		}
		return null;
	}
	
	
	
}
