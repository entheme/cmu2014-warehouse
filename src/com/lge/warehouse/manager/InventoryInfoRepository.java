package com.lge.warehouse.manager;

// warehouse manager���� �� inventory�� ����Ǿ��ִ� ���� ����Ʈ�� ������ ����.
// ���� �κ��丮 ����Ʈ�� ���� Ŭ�������� ���� ��Ų��.
// ���� �κ��丮 ���� ID�� �ʿ��ϴ�. (���ϸ�� �����Ѵ�.)
public class InventoryInfoRepository {

	int InventoryID;
	
	int InventoryList; //TODO inventory list �� �ִ� Ŭ���� ��ü�� �����Ѵ�.
	

	public InventoryInfoRepository(int inventoryID) {
		super();
		InventoryID = inventoryID;
		
		// �ʱ�ȭ ������ ���Ͽ��� �κ��丮�� �����Ѵ�.
		LoadInventoryList();
	}
	
	public void InventoryUpdate(int list)
	{
		// supervisor UI ���� �Է��� InventoryUpdate ��ɿ� ���� �κ��丮�� ������Ʈ �Ѵ�.
		// ��¥�� list�� �Ѿ�´�.
		
		// ����.
		SaveInventoryList();
	}
	
	// ���� �ϳ��� ������. ������ �޾Ƽ� �ش� ������ŭ ���ְ� ����.
	public void OrderisClear()
	{
		
		SaveInventoryList();
	}
	
	public int GetInventoryList()
	{
		return InventoryList;
	}
	
	// �κ��丮 ����
	private void LoadInventoryList()
	{
		
	}
	
	// �κ��丮 ����.
	private void SaveInventoryList()
	{
		
	}
	 
}
