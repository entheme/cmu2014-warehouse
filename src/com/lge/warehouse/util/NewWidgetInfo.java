package com.lge.warehouse.util;

import java.io.Serializable;

public class NewWidgetInfo implements Serializable{
	private String mName;
	private int mPrice;
	public NewWidgetInfo(String name, int price){
		mName = name;
		mPrice = price;
	}
	public String getWidgetName(){
		return mName;
	}
	public int getWidgetPrice(){
		return mPrice;
	}
}
