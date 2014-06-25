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
import com.lge.warehouse.util.WarehouseStatus;
import com.lge.warehouse.util.WidgetCatalog;

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
		super(WComponentType.WAREHOUSE_SUPERVISOR, false);
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
					doNextFillOrder();
					updateOrderStatus();
				}
			} else{
				handleBodyError(event);
			}
			break;
		case FILL_INVENTORY_WIDGET:	//from SupervisorUI
			if (event.getBody() instanceof WarehouseInventoryInfo){
				WarehouseInventoryInfo warehouseInventoryInfo = (WarehouseInventoryInfo)event.getBody();
				mWarehouseProxyHandler.fillInventoryWidget(warehouseInventoryInfo);
				doNextFillOrder();
				warehouseInventoryInfo = mWarehouseProxyHandler.getInventoryInfo(); 
				sendMsg(WComponentType.SUPERVISOR_UI, EventMessageType.WAREHOUSE_INVENTORY_INFO, warehouseInventoryInfo);
			}else{
				handleBodyError(event);
			}
			updateOrderStatus();
			break;
		case FINISH_FILL_ORDER:
			if (event.getBody() instanceof Order){
				Order order = (Order)event.getBody();
				mWarehouseProxyHandler.finishFillOrder(event.getSrc(), order);
				doNextFillOrder();
			}else {
				handleBodyError(event);
			}
			updateOrderStatus();
			break;
		case REQUEST_CATAGORY_FROM_CUSTOMER_SERVICE_MANAGER:
			sendWidgetCatalog(WComponentType.CUSTOMER_SERVICE_MANAGER, EventMessageType.RESPONSE_CATAGORY_TO_CUSTOMER_SERVICE_MANAGER);
			break;
		case REQUEST_CATAGORY_FROM_SUPERVISOR_UI:
			sendWidgetCatalog(WComponentType.SUPERVISOR_UI, EventMessageType.RESPONSE_CATAGORY_TO_SUPERVISOR_UI);
			WarehouseInventoryInfo warehouseInventoryInfo = mWarehouseProxyHandler.getInventoryInfo(); 
			sendMsg(WComponentType.SUPERVISOR_UI, EventMessageType.WAREHOUSE_INVENTORY_INFO, warehouseInventoryInfo);
			updateOrderStatus();
			WarehouseStatus warehouseStatusToSupervisorUI = mWarehouseProxyHandler.getWarehouseStatus();
			if(warehouseStatusToSupervisorUI != null){
				sendMsg(WComponentType.SUPERVISOR_UI, EventMessageType.UPDATE_WAREHOUSE_STATUS, warehouseStatusToSupervisorUI);
			}
			break;
		case ADD_NEW_WIDGET_ITEM:
			if (event.getBody() instanceof NewWidgetInfo){
				NewWidgetInfo widgetCatalog = (NewWidgetInfo) event.getBody();
				WidgetCatalogRepository.getInstance().addNewWidget(widgetCatalog);
				sendWidgetCatalog(WComponentType.CUSTOMER_SERVICE_MANAGER, EventMessageType.RESPONSE_CATAGORY_TO_CUSTOMER_SERVICE_MANAGER);
				sendWidgetCatalog(WComponentType.SUPERVISOR_UI, EventMessageType.RESPONSE_CATAGORY_TO_SUPERVISOR_UI);
				mWarehouseProxyHandler.sendWidgetCatalog();
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
			updateOrderStatus();
			break;
		default:
			logger.info("unhandled event :"+event);
			break;
		}
	}

	private void sendWidgetCatalog(WComponentType dest, EventMessageType message) {
		WidgetCatalog widgetCatalog = WidgetCatalogRepository.getInstance().getWidgetCatalog();
		widgetCatalog.dump();

		sendMsg(dest, message, widgetCatalog);
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
			//sendMsg(WComponentType.WM_MSG_HANDLER, EventMessageType.FILL_ORDER, order);
			mWarehouseProxyHandler.requestFillOrder(order);
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
