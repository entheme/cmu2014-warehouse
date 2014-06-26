package com.lge.warehouse.util;

import java.io.Serializable;

public class SystemEvent implements Serializable{
	CustomerStatus mCustomerStatus;
	WarehouseInfo mWarehouseInfo;
	
	public SystemEvent(CustomerStatus customerStatus, WarehouseInfo warehouseInfo){
		mCustomerStatus = customerStatus;
		mWarehouseInfo = warehouseInfo;
	}
	public CustomerStatus getCustomerStatus() {
		return mCustomerStatus;
	}
	public void setCustomerStatus(CustomerStatus mCustomerStatus) {
		this.mCustomerStatus = mCustomerStatus;
	}
	public WarehouseInfo getWarehouseInfo() {
		return mWarehouseInfo;
	}
	public void setWarehouseInfo(WarehouseInfo mWarehouseInfo) {
		this.mWarehouseInfo = mWarehouseInfo;
	}
	
}
