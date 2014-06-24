/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.supervisor;

import org.apache.log4j.Logger;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.util.NewWidgetInfo;
import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.OrderStatusInfo;
import com.lge.warehouse.util.WarehouseInventoryInfo;
import com.lge.warehouse.util.WarehouseStatus;
import com.lge.warehouse.util.WidgetCatalog;
import com.lge.warehouse.util.WidgetCatalogRepository;

/**
 *
 * @author kihyung2.lee
 */
public final class WarehouseSupervisor extends WarehouseRunnable {
	private static WarehouseSupervisor sInstance = null;
	static Logger logger = Logger.getLogger(WarehouseProxyHandler.class);
	private WarehouseProxyHandler mWarehouseProxyHandler;
	private OrderProvider mOrderProvider;

	private WarehouseSupervisor() {
		super(WComponentType.WAREHOUSE_SUPERVISOR);
		mWarehouseProxyHandler = new WarehouseProxyHandler(this,this);
		mOrderProvider = new OrderProvider(this, mWarehouseProxyHandler);
	}

	public static WarehouseSupervisor getInstance() {
		if (sInstance == null) {
			sInstance = new WarehouseSupervisor();
		}
		return sInstance;
	}

	@Override
	protected void eventHandle(EventMessage event) {
		switch(event.getType()){
		case SYSTEM_READY:
			break;
		case WAREHOUSE_ADD_REQUEST: 	//from WmMsgHandler
		mWarehouseProxyHandler.handleWarehouseAddRequest();
			doNextFillOrder();
		break;
		case RESPONSE_PENDING_ORDER:	//from PendingOrderManager
			if (event.getBody() instanceof Order){
				Order order = (Order)event.getBody();
				if(mWarehouseProxyHandler.hasInventory(order)){
					mWarehouseProxyHandler.requestFillOrder(order);
				}else{
					logger.info("No inventory, push BackOrder");
					mOrderProvider.pushBackOrder(order);
				}
				updateOrderStatus();
			} else{
				handleBodyError(event);
			}
			break;
		case WAREHOUSE_INVENTORY_INFO:	//from SupervisorUI
			if (event.getBody() instanceof WarehouseInventoryInfo){
				WarehouseInventoryInfo warehouseInventoryInfo = (WarehouseInventoryInfo)event.getBody();
				mWarehouseProxyHandler.updateInventory(warehouseInventoryInfo);
				doNextFillOrder();
			}else{
				handleBodyError(event);
			}
			break;
		case FINISH_FILL_ORDER:
			if (event.getBody() instanceof Order){
				Order order = (Order)event.getBody();
				mWarehouseProxyHandler.finishFillOrder(event.getSrc(), order);
				doNextFillOrder();
				updateOrderStatus();
			}else {
				handleBodyError(event);
			}
			break;
		case REQUEST_CATAGORY_FROM_CUSTOMER_SERVICE_MANAGER:
			WidgetCatalog widgetCatalog1 = WidgetCatalogRepository.getInstance().getWidgetCatalog();
			widgetCatalog1.dump();
			
			sendMsg(WComponentType.CUSTOMER_SERVICE_MANAGER, EventMessageType.RESPONSE_CATAGORY_TO_CUSTOMER_SERVICE_MANAGER, widgetCatalog1);
			break;
		case REQUEST_CATAGORY_FROM_SUPERVISOR_UI:
			WidgetCatalog widgetCatalog2 = WidgetCatalogRepository.getInstance().getWidgetCatalog();
			widgetCatalog2.dump();
			
			sendMsg(WComponentType.CUSTOMER_SERVICE_MANAGER, EventMessageType.RESPONSE_CATAGORY_TO_SUPERVISOR_UI, widgetCatalog2);
			break;
		case SEND_WIDGET_CATALOG_UPDATE:
			if (event.getBody() instanceof NewWidgetInfo){
				NewWidgetInfo widgetCatalog = (NewWidgetInfo) event.getBody();
				WidgetCatalogRepository.getInstance().addNewWidget(widgetCatalog);
				sendMsg(WComponentType.CUSTOMER_SERVICE_MANAGER, EventMessageType.RESPONSE_CATAGORY_TO_CUSTOMER_SERVICE_MANAGER, WidgetCatalogRepository.getInstance().getWidgetCatalog());
			}else {
				handleBodyError(event);
			}
			break;
		case REQUEST_ORDER_STATUS:
			updateOrderStatus();
			break;
		case RESPONSE_PENDING_ORDER_STATUS:
			if(event.getBody() instanceof OrderStatusInfo){
				OrderStatusInfo orderStatusInfo = (OrderStatusInfo) event.getBody();
				for(Order order : mOrderProvider.getBackOrderList()){
					orderStatusInfo.addBackOrder(order);
				}
				for(Order order : mWarehouseProxyHandler.getCompletedOrderList()){
					orderStatusInfo.addCompleteOrder(order);
				}
				for(Order order : mWarehouseProxyHandler.getInProgressOrderList()){
					orderStatusInfo.addInProgressOrder(order);
				}
				sendMsg(WComponentType.SUPERVISOR_UI, EventMessageType.RESPONSE_ORDER_STATUS, orderStatusInfo);
			}else {
				handleBodyError(event);
			}
			break;
		case UPDATE_WAREHOUSE_STATUS:
			if(event.getBody() instanceof WarehouseStatus){
				WarehouseStatus warehouseStatus = (WarehouseStatus)event.getBody();
				mWarehouseProxyHandler.updateWarehouseStatus(event.getSrc(), warehouseStatus);
				sendMsg(WComponentType.SUPERVISOR_UI, EventMessageType.UPDATE_WAREHOUSE_STATUS, warehouseStatus);
			}else {
				handleBodyError(event);
			}
			break;
		default:
			logger.info("unhandled event :"+event);
			break;
		}
	}
	private void updateOrderStatus(){
		sendMsg(WComponentType.PENDING_ORDER_MANAGER, EventMessageType.REQUEST_PENDING_ORDER_STATUS, null);
	}
	private void doNextFillOrder(){
		Order order = mOrderProvider.getOrder();
		if(order == null){
			logger.info("No ready backorder, request pending order to PendingOrderManager");
			sendMsg(WComponentType.PENDING_ORDER_MANAGER, EventMessageType.REQUEST_PENDING_ORDER, null);
		}else {
			logger.info("back order is ready, request filling order to WmMsgHandler");
			sendMsg(WComponentType.WM_MSG_HANDLER, EventMessageType.FILL_ORDER, order);
		}
	}
	public static void start() {
		logger.info("WarehouseSupervisor start");
		new Thread(getInstance()).start();
	}

	@Override
	protected void initBus() {
		addBus(WComponentType.CUSTOMER_SERVICE_MANAGER);
		addBus(WComponentType.PENDING_ORDER_MANAGER);
		addBus(WComponentType.SUPERVISOR_UI);
		addBus(WComponentType.WM_MSG_HANDLER);
	}

	@Override
	public void ping() {
		sendMsg(WComponentType.CUSTOMER_SERVICE_MANAGER, EventMessageType.COMPONENT_HELLO, null);
		sendMsg(WComponentType.PENDING_ORDER_MANAGER, EventMessageType.COMPONENT_HELLO, null);
		sendMsg(WComponentType.SUPERVISOR_UI, EventMessageType.COMPONENT_HELLO, null);
	}
}
