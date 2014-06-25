package com.lge.warehouse.customer.ui;

import java.util.logging.Level;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.ordersys.CustomerServiceManager;
import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.WidgetCatalog;

public class OrderSysUiController extends WarehouseRunnable {
	private static Logger logger = Logger.getLogger(OrderSysUiController.class);
	public OrderSysUiController() {
		super(WComponentType.CUSTOMER_INF, true);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initBus() {
		// TODO Auto-generated method stub
		addBus(WComponentType.CUSTOMER_SERVICE_MANAGER);
	}

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		if(message instanceof ObjectMessage){
			try {
				EventMessage em = (EventMessage)((ObjectMessage)message).getObject();
				//eventHandle(em);
				postEvent(em);
			} catch (JMSException ex) {
				java.util.logging.Logger.getLogger(CustomerServiceManager.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	@Override
	protected void eventHandle(EventMessage event) {
		// TODO Auto-generated method stub
		logger.info("Received : "+event);
		switch(event.getType()){
		case RESPONSE_CATAGORY_TO_CUSTOMER_IF:
			if(event.getBody() instanceof WidgetCatalog){
				WidgetCatalog widgetCatalog = (WidgetCatalog) event.getBody();
				OrderSysWidgetCart.setWidgetCatalog(widgetCatalog);
			}
			break;
		}
	}
	
	public void requestWidgetCatalog(){
		//sendMsg(WComponentType.CUSTOMER_SERVICE_MANAGER, EventMessageType.REQUEST_CATAGORY_FROM_CUSTOMER_IF, null);
		sendMsgOnContext(new EventMessage(getId().name(), WComponentType.CUSTOMER_SERVICE_MANAGER.name(), EventMessageType.REQUEST_CATAGORY_FROM_CUSTOMER_IF, null));
	}
	public void requestPlaceOrder(Order order){
//		sendMsg(WComponentType.CUSTOMER_SERVICE_MANAGER, EventMessageType.PLACE_ORDER, order);
		sendMsgOnContext(new EventMessage(getId().name(), WComponentType.CUSTOMER_SERVICE_MANAGER.name(), EventMessageType.PLACE_ORDER, order));
	}

	@Override
	public void ping() {
		// TODO Auto-generated method stub
		
	}
}
