package com.lge.warehouse.common.test;

import org.apache.log4j.Logger;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.util.OrderStatusInfo;
import com.lge.warehouse.supervisor.WarehouseInventoryInfo;
import com.lge.warehouse.util.WidgetCatalog;

public class SupervisorUIStub extends WarehouseRunnable {
	private static Logger logger = Logger.getLogger(SupervisorUIStub.class);
	private static SupervisorUIStub sInstance = null;
	private OrderStatusInfo mOrderStatusInfo;
	private SupervisorUIStub(){
		super(WComponentType.SUPERVISOR_UI);
	}

	public static SupervisorUIStub getInstance(){
		if(sInstance == null)
			sInstance = new SupervisorUIStub();
		return sInstance;
	}

	protected SupervisorUIStub(WComponentType id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void eventHandle(EventMessage event) {
		// TODO Auto-generated method stub
		switch(event.getType()){
		case SYSTEM_READY:

			break;
		case TEST_SUPERVISOR_UI_SEND_WIDGET_CATALOG:
			WidgetCatalog widgetCatalog = (WidgetCatalog)event.getBody();
			sendWidgetCatalog(widgetCatalog);
			break;
		case TEST_SUPERVISOR_UI_REQUEST_ORDER_STATUS:
			requestOrderStatus();
			break;
		case TEST_SUPERVISOR_UI_SEND_WAREHOUSE_INVENTORY_INFO:
			WarehouseInventoryInfo warehouseInventoryInfo = (WarehouseInventoryInfo)event.getBody();
			sendWarehouseInventoryInfo(warehouseInventoryInfo);
			break;
		case RESPONSE_ORDER_STATUS:
			if(event.getBody() instanceof OrderStatusInfo){
				mOrderStatusInfo = (OrderStatusInfo)event.getBody();
			}
			break;
		default:
			logger.info("unhandled event : "+event);
			break;
		}
	}

	@Override
	public void ping() {
		// TODO Auto-generated method stub

	}
	public OrderStatusInfo getOrderStatusInfo(){
		return mOrderStatusInfo;
	}
	public void testSendWidgetCatalog(WidgetCatalog widgetCatalog){
		postEvent(new EventMessage("TestModule", getId().name(), EventMessageType.TEST_SUPERVISOR_UI_SEND_WIDGET_CATALOG, widgetCatalog));
	}

	public void testRequestOrderStatus(){
		postEvent(new EventMessage("TestModule", getId().name(), EventMessageType.TEST_SUPERVISOR_UI_REQUEST_ORDER_STATUS, null));
	}

	public void testSendWarehouseInventoryInfo(WarehouseInventoryInfo info){
		postEvent(new EventMessage("TestModule", getId().name(), EventMessageType.TEST_SUPERVISOR_UI_SEND_WAREHOUSE_INVENTORY_INFO, info));
	}
	@Override
	protected void initBus() {
		// TODO Auto-generated method stub
		addBus(WComponentType.WAREHOUSE_SUPERVISOR);
	}
	public void sendWidgetCatalog(WidgetCatalog widgetCatalog){
		sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.SEND_WIDGET_CATALOG_UPDATE, widgetCatalog);
	}
	public void requestOrderStatus(){
		sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.REQUEST_ORDER_STATUS, null);
	}
	public void sendWarehouseInventoryInfo(WarehouseInventoryInfo info){
		sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.WAREHOUSE_INVENTORY_INFO,info );
	}
	public static void start() {
		new Thread(SupervisorUIStub.getInstance()).start();
	}
}
