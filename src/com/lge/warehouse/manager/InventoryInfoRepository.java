package com.lge.warehouse.manager;

public class InventoryInfoRepository {

	int InventoryID;
	
	int InventoryList;
	

	public InventoryInfoRepository(int inventoryID) {
		super();
		InventoryID = inventoryID;
	
		LoadInventoryList();
	}
	
	public void InventoryUpdate(int list)
	{
		
		SaveInventoryList();
	}

	public void OrderisClear()
	{
		
		SaveInventoryList();
	}
	
	public int GetInventoryList()
	{
		return InventoryList;
	}
	
	private void LoadInventoryList()
	{
		
	}

	private void SaveInventoryList()
	{
		
	}
	 
}
