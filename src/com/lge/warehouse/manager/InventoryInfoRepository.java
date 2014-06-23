package com.lge.warehouse.manager;

// warehouse manager에서 각 inventory에 저장되어있는 위젯 리스트를 가지고 있음.
// 개별 인벤토리 리스트를 상위 클레스에서 동작 시킨다.
// 개별 인벤토리 마다 ID가 필요하다. (파일명과 연동한다.)
public class InventoryInfoRepository {

	int InventoryID;
	
	int InventoryList; //TODO inventory list 좀 있다 클레스 객체로 변경한다.
	

	public InventoryInfoRepository(int inventoryID) {
		super();
		InventoryID = inventoryID;
		
		// 초기화 시점에 파일에서 인벤토리를 복원한다.
		LoadInventoryList();
	}
	
	public void InventoryUpdate(int list)
	{
		// supervisor UI 에서 입력한 InventoryUpdate 명령에 따라 인벤토리를 업데이트 한다.
		// 통짜로 list가 넘어온다.
		
		// 저장.
		SaveInventoryList();
	}
	
	// 오더 하나가 끝났다. 오더를 받아서 해당 개수만큼 빼주고 저장.
	public void OrderisClear()
	{
		
		SaveInventoryList();
	}
	
	public int GetInventoryList()
	{
		return InventoryList;
	}
	
	// 인벤토리 복원
	private void LoadInventoryList()
	{
		
	}
	
	// 인벤토리 저장.
	private void SaveInventoryList()
	{
		
	}
	 
}
