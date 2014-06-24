/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lge.warehouse.util.WidgetCatalogRepository.WidgetInfo;

/**
 *
 * @author kihyung2.lee
 */
public class Order implements Serializable{
    public enum Status {
        ORDER_PENDING,
        ORDER_IN_PROGRESS,
        ORDER_BACK_ORDERED,
        ORDER_COMPLETE
    }
    private long mOrderId = -1;
    private Status mStatus;
    private List<QuantifiedWidget> mItemList = new ArrayList<QuantifiedWidget>();
    
    public void addItem(WidgetInfo w, int count){
        mItemList.add(new QuantifiedWidget(w,count));
    }
    public List<QuantifiedWidget> getItemList(){
        return mItemList;
    }
    public void setOrderId(long id){
    	mOrderId = id;
    }
    public long getOrderId(){
    	return mOrderId;
    }
    public int getItemCnt(){
        return mItemList.size();
    }
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mItemList == null) ? 0 : mItemList.hashCode());
		result = prime * result + (int) (mOrderId ^ (mOrderId >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (mItemList == null) {
			if (other.mItemList != null)
				return false;
		}
		if (mOrderId != other.mOrderId)
			return false;
		return true;
	}
	
	public void setOrderStatus(Status status) {
		// TODO Auto-generated method stub
		mStatus = status; 
	}
	public Status getOrderStatus(){
		return mStatus;
	}
    public String toString(){
    	return "Order id = "+mOrderId+", status="+mStatus;
    }
}
