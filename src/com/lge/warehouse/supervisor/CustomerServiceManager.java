/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.supervisor;

import java.util.List;

import org.apache.log4j.Logger;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.OrderStatusInfo;
import com.lge.warehouse.util.QuantifiedWidget;
import com.lge.warehouse.util.WidgetCatalog;

/**
 *
 * @author seuki77
 */
public final class CustomerServiceManager extends WarehouseRunnable{
	private static CustomerServiceManager sInstance;
	static Logger logger = Logger.getLogger(CustomerServiceManager.class);
	private static long sOrderCnt=0;
	private CustomerServiceManager() {
		super(WComponentType.CUSTOMER_SERVICE_MANAGER, false);
	}
	public static CustomerServiceManager getInstance() {
		if(sInstance == null) {
			sInstance = new CustomerServiceManager();
		}
		return sInstance;
	}
	@Override
	protected void eventHandle(EventMessage event) {
		switch(event.getType()){
		case SYSTEM_READY:
			break;
		case PLACE_ORDER:
			if (event.getBody() instanceof Order){
				Order order = (Order)event.getBody();
				order.setOrderId(++sOrderCnt);
				if(WarehouseInventoryInfoRepository.getInstance().hasInventoryForOrder(order)){
					logger.info("New Order Place - inventory is enough, send to WarehouseSupervisor");
					sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.NEW_ORDER, order);
				}else{
					logger.info("New Order Place - inventory is not engouth, put in BackOrderQueue");
					handleBackOrder(order);
					reportOrderStatus();
				}
			}else {
				handleBodyError(event);
			}
			break;
		case REQUEST_CATAGORY_FROM_CUSTOMER_IF:
		case NOTIFY_WIDGET_CATALOG_CHANGED:
			WidgetCatalog widgetCatalog = WidgetCatalogRepository.getInstance().getWidgetCatalog();
			sendMsg(WComponentType.CUSTOMER_INF, EventMessageType.RESPONSE_CATAGORY_TO_CUSTOMER_IF, widgetCatalog);
			break;
		
		default:
			logger.info("unhandled event :"+event);
			break;
		}
	}
	private void reportOrderStatus(){
		OrderStatusInfo orderStatusInfo = new OrderStatusInfo();
		for(Order backOrder : BackOrderQueue.getInstance().getBackOrderList())
			orderStatusInfo.addBackOrder(backOrder);
		sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.RESPONSE_ORDER_STATUS,orderStatusInfo);
	}
	public void handleBackOrder(Order order){
        List<QuantifiedWidget> list = order.getItemList();
        for(QuantifiedWidget qw : list){
            logger.info(qw.getWidget()+" : cnt("+qw.getQuantity()+")");
        }
        BackOrderQueue.getInstance().putOrder(order);
    }
	@Override
	protected void initBus() {
		addBus(WComponentType.CUSTOMER_INF);
		addBus(WComponentType.WAREHOUSE_SUPERVISOR);
		addBus(WComponentType.BACKORDER_MANAGER);
	}

	public static void start() {
		logger.info("CustomerServiceManager start");
		new Thread(getInstance()).start();
	}

	@Override
	public void ping() {
		sendMsg(WComponentType.CUSTOMER_INF, EventMessageType.COMPONENT_HELLO, null);
		sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.COMPONENT_HELLO, null);
		sendMsg(WComponentType.BACKORDER_MANAGER, EventMessageType.COMPONENT_HELLO, null);
	}
}
