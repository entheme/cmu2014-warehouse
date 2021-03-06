package com.lge.warehouse.util;

import com.lge.warehouse.common.app.WarehouseContext;
import com.lge.warehouse.util.Order.OrderCompare;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

public class OrderStatusInfo implements Serializable {
	private static Logger logger = Logger.getLogger(OrderStatusInfo.class);
	Set<Order> mPendingOrderSet = new HashSet<Order>();
	Set<Order> mBackOrderSet = new HashSet<Order>();
	Set<Order> mInProgressOrderSet = new HashSet<Order>();
	Set<Order> mCompleteOrderSet = new HashSet<Order>();
	
	public OrderStatusInfo(){}
	
	public void addPendingOrder(Order order){
		if(checkOrderStatus(Order.Status.ORDER_PENDING, order))
			mPendingOrderSet.add(order);
	}

	public void addBackOrder(Order order){
		if(checkOrderStatus(Order.Status.ORDER_BACK_ORDERED, order))
			mBackOrderSet.add(order);
	}
	
	public void addInProgressOrder(Order order){
		if(checkOrderStatus(Order.Status.ORDER_IN_PROGRESS, order))
			mInProgressOrderSet.add(order);
	}
	
	public void addCompleteOrder(Order order){
		if(checkOrderStatus(Order.Status.ORDER_COMPLETE, order))
			mCompleteOrderSet.add(order);
	}
	
	private boolean checkOrderStatus(Order.Status status, Order order){
		if(order.getOrderStatus() != status){
			String errlog = "expected : "+status+", but "+order;
			if(WarehouseContext.DEBUG_WITH_RUNTIME_EXCEPTION){
				throw new RuntimeException(errlog);
			}else{
				logger.info(errlog);
			}
			return false;
		}
		return true;
	}
	
	public List<Order> getPendingOrderList(){
		return new ArrayList(mPendingOrderSet);
	}
	
	public List<Order> getBackOrderList(){
		return new ArrayList(mBackOrderSet);
	}
	
	public List<Order> getInProgressOrderList(){
		return new ArrayList(mInProgressOrderSet);
	}
	
	public List<Order> getCompleteOrderList(){
		return new ArrayList(mCompleteOrderSet);
	}

    @Override
    public String toString() {
        String result = new String();
        List<Order> orderList = new ArrayList<Order>();
        
        orderList.addAll(getPendingOrderList());
        orderList.addAll(getBackOrderList());
        orderList.addAll(getInProgressOrderList());
        orderList.addAll(getCompleteOrderList());
        
        Collections.sort(orderList, new OrderCompare());

        for(Order order : orderList) {
            result += order.toString() + "\n";
        }
        
        return result;
    }
 
}
