/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author kihyung2.lee
 */
public class Order implements Serializable{
    enum Status {
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
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (!(obj instanceof Order))
			return false;
		Order others = (Order)obj;
		if(others.mOrderId==this.mOrderId){
			return true;
		}
		return false;
	}
    
}
