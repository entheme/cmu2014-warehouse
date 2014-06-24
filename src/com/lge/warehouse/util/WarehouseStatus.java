/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jaehak.lee
 */
public class WarehouseStatus implements Serializable{
    private String mLocationOfBot;
    private List<QuantifiedWidget> mInventoryListOnBot = new ArrayList<QuantifiedWidget>();
    private List<String> mVisitedStationList = new ArrayList<String>();
    private String mNextStop;
    WarehouseInventoryInfo mInventoryInfo;

    public WarehouseStatus(){}
	
    public void setLocationOfBot(String locationOfBot){
        mLocationOfBot = locationOfBot;
    }
    
    public String getLocationOfBot(){
        return new String(mLocationOfBot);
    }
    
    public void setInventoryListOfBot(List<QuantifiedWidget> InventoryListOnBot){
        mInventoryListOnBot.clear();
        mInventoryListOnBot = InventoryListOnBot;
    }
    
    public void addInventoryOfBot(QuantifiedWidget inventoryOnBot) {
        mInventoryListOnBot.add(inventoryOnBot);
    }
    
    public void removeLocationOfBot(QuantifiedWidget inventoryOnBot) {
        mInventoryListOnBot.remove(inventoryOnBot);
    }
    
    public List<QuantifiedWidget> getInventoryListOfBot(){
	return new ArrayList(mInventoryListOnBot);
    }

    public void setVisitedStationListOfBot(List<String> visitedStationList){
        mVisitedStationList.clear();
        mVisitedStationList = visitedStationList;
    }
    
    public void addVisitedStationListOfBot(String visitedStation) {
        mVisitedStationList.add(visitedStation);
    }
    
    public void removeVisitedStationListOfBot(String visitedStation) {
        mVisitedStationList.remove(visitedStation);
    }
    
    public List<String> getVisitedStationListOfBot(){
	return new ArrayList(mVisitedStationList);
    }
     
    public void setNextStop(String nextStop){
        mNextStop = nextStop;
    }
    
    public String getNextStop(){
        return new String(mNextStop);
    }
    
    public void setWarehouseInventoryInfo(WarehouseInventoryInfo inventoryInfo){
        mInventoryInfo = inventoryInfo;
    }
    
    public WarehouseInventoryInfo getWarehouseInventoryInfo(){
        return mInventoryInfo;
    }
}
