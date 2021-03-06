package com.lge.warehouse.common.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.common.bus.p2p.P2PConnection;
import com.lge.warehouse.common.bus.p2p.P2PReceiver;
import com.lge.warehouse.common.bus.p2p.P2PSender;
import com.lge.warehouse.supervisor.WarehouseInventoryInfo;
import com.lge.warehouse.util.InventoryName;
import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.QuantifiedWidget;
import com.lge.warehouse.util.WarehouseStatus;
import com.lge.warehouse.util.WidgetCatalog;

public class WarehouseManagerStub extends WarehouseRunnable{
	private static WarehouseManagerStub sInstance = null;
	private static Logger logger = Logger.getLogger(WarehouseManagerStub.class);
	private WarehouseInventoryInfo inventoryInfo;
	WidgetCatalog widgetCatalog;
	private WarehouseManagerStub(){
		super(WComponentType.WM_MSG_HANDLER, false);
	}

	public static WarehouseManagerStub getInstance(){
		if(sInstance == null){
			sInstance = new WarehouseManagerStub();
		}	
		return sInstance;
	}

	@Override
	protected void threadStart() {
		// TODO Auto-generated method stub
		super.threadStart();

	}

	@Override
	protected void initBus() {
		// TODO Auto-generated method stub
		addBus(WComponentType.WAREHOUSE_SUPERVISOR);
	}

	@Override
	protected void eventHandle(EventMessage event) {
		// TODO Auto-generated method stub
		switch(event.getType()){
		case SYSTEM_READY:
			sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.WAREHOUSE_ADD_REQUEST, null);
			break;
		default: 
			break;
		case WAREHOUSE_ADD_ACCEPT:
			removeBus(WComponentType.WAREHOUSE_SUPERVISOR);
			int id= (Integer)event.getBody();
			setAlias(getId().name()+id);
			handleAddAccept(id);
			break;
		case FILL_INVENTORY_WIDGET:
			if(event.getBody() instanceof WarehouseInventoryInfo){
				WarehouseInventoryInfo warehouseInventoryInfo = (WarehouseInventoryInfo) event.getBody();
				inventoryInfo.fillInventoryInfo(warehouseInventoryInfo);
			}else{
				handleBodyError(event);
			}
			break;
		case WAREHOUSE_INVENTORY_INFO:
			if(event.getBody() instanceof WarehouseInventoryInfo){
				inventoryInfo = (WarehouseInventoryInfo)event.getBody();
				for(InventoryName inventoryName : InventoryName.values()){
					//Note: Now, The InventoryName means the name of inventory stataion.
					if(inventoryInfo.hasInventoryStation(inventoryName)){
						for(QuantifiedWidget qw : inventoryInfo.getInventoryInfo(inventoryName)){
							logger.info(qw.getWidget()+" : "+qw.getQuantity());
						}
					}
				}
				//				sendMsg(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, EventMessageType.WAREHOUSE_INVENTORY_INFO, inventoryInfo);
				
			}else{
				handleBodyError(event);
			}
			break;
		case SEND_WDIGET_CATALOG_TO_WM_MSG_HANDLER:
			if(event.getBody() instanceof WidgetCatalog){
				widgetCatalog = (WidgetCatalog)event.getBody();
				//To be implemented
			}
			break;
		

		case FILL_ORDER:
			if(event.getBody() instanceof Order){
				//For Test [START]
				Order order = (Order)event.getBody();
				//logger.info("FILL_ORDER order id = "+order.getOrderId());
				for(QuantifiedWidget qw : order.getItemList()){
					logger.info(qw.getWidget()+" : "+qw.getQuantity());
				}
				WarehouseStatus warehouseStatus = new WarehouseStatus();
				List<QuantifiedWidget> inventoryListOnBot = new ArrayList<QuantifiedWidget>();
				//logger.info("WAREHOUSE_STATUS_INFO");
				for(InventoryName inventoryName : InventoryName.values()){
					if(inventoryInfo.hasInventoryStation(inventoryName))
						logger.info(inventoryInfo.getInventoryInfo(inventoryName));
				}
				warehouseStatus.addVisitedStationListOfBot("");
				warehouseStatus.setInventoryListOfBot(inventoryListOnBot);
				warehouseStatus.setLocationOfBot("0->1");
				warehouseStatus.setNextStop("Inventory status 1");
				warehouseStatus.setWarehouseInventoryInfo(inventoryInfo);
				sendWarehouseStatus(warehouseStatus);
				
				sleep(5);
				testFillOrder(InventoryName.INVENTORY_1, order, inventoryListOnBot, warehouseStatus);

				sleep(5);
				testFillOrder(InventoryName.INVENTORY_2, order, inventoryListOnBot, warehouseStatus);
				
				sleep(5);
				testFillOrder(InventoryName.INVENTORY_3, order, inventoryListOnBot, warehouseStatus);
				
				sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.FINISH_FILL_ORDER, order);
			}else {
				handleBodyError(event);
			}
			break;
		}
	}
	private void sleep(int i) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(i*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void testFillOrder(InventoryName inventoryName, Order order, List<QuantifiedWidget> inventoryListOnBot, WarehouseStatus warehouseStatus){
		for(QuantifiedWidget quantifiedWidget : order.getItemList()){
			if(inventoryInfo.hasInventory(inventoryName, quantifiedWidget.getWidget())){
				logger.info("testFillOrder "+inventoryName.name()+"widget="+quantifiedWidget.getWidget()+" -"+quantifiedWidget.getQuantity());
				inventoryInfo.reduceInventoryWidget(inventoryName, quantifiedWidget.getWidget(), quantifiedWidget.getQuantity());
				inventoryListOnBot.add(new QuantifiedWidget(quantifiedWidget.getWidget(), quantifiedWidget.getQuantity()));
			}
		}
		warehouseStatus.addVisitedStationListOfBot(inventoryName.name());
		warehouseStatus.setInventoryListOfBot(inventoryListOnBot);
		warehouseStatus.setLocationOfBot(inventoryName.name());
		warehouseStatus.setNextStop(InventoryName.fromInteger(InventoryName.fromInventoryName(inventoryName)+1).name());
		warehouseStatus.setWarehouseInventoryInfo(inventoryInfo);
		logger.info("after testFillOrder UPDATE_WAREHOUSE_STATUS");
		for(InventoryName name : InventoryName.values()){
			if(inventoryInfo.hasInventoryStation(name))
				logger.info(inventoryInfo.getInventoryInfo(name));
		}
		sendWarehouseStatus(warehouseStatus);
	}
	private void sendWarehouseStatus(WarehouseStatus warehouseStatus){
		sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.UPDATE_WAREHOUSE_STATUS, warehouseStatus);
	}
	public void handleAddAccept(int id){
		String src = WComponentType.WM_MSG_HANDLER.name()+id;
		String dest = WComponentType.WAREHOUSE_SUPERVISOR.name();
		P2PSender sender = P2PConnection.createSender(src+"_"+dest);
		mP2PSenderMap.put(dest, sender);
		P2PReceiver receiver = P2PConnection.createReceiver(dest+"_"+src);
		receiver.setMessageListener(this);
		mP2PReceiverMap.put(dest, receiver);
	}
	public static void start() {
		new Thread(WarehouseManagerStub.getInstance()).start();
	}
	@Override
	public void ping() {
		// TODO Auto-generated method stub
		sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.COMPONENT_HELLO, null);
	}


}
