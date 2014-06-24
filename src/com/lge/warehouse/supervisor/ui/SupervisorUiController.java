package com.lge.warehouse.supervisor.ui;

import java.util.logging.Level;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.ordersys.CustomerServiceManager;
import com.lge.warehouse.util.OrderStatusInfo;
import com.lge.warehouse.util.WarehouseInventoryInfo;
import com.lge.warehouse.util.WidgetCatalog;

public class SupervisorUiController extends WarehouseRunnable {
    private SupervisorUi mSupervisorUi;
	public SupervisorUiController() {
		super(WComponentType.SUPERVISOR_UI);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initBus() {
		// TODO Auto-generated method stub
		addBus(WComponentType.WAREHOUSE_SUPERVISOR);
	}

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		if(message instanceof ObjectMessage){
			try {
				EventMessage em = (EventMessage)((ObjectMessage)message).getObject();
				eventHandle(em);
			} catch (JMSException ex) {
				java.util.logging.Logger.getLogger(CustomerServiceManager.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	@Override
	protected void eventHandle(EventMessage event) {
		// TODO Auto-generated method stub
		if(event.getSrc().equals(getId().name())){
			sendObject(event);
		}
		switch(event.getType()){
		case RESPONSE_ORDER_STATUS:
			if(event.getBody() instanceof OrderStatusInfo){
				OrderStatusInfo statusInfo = (OrderStatusInfo)event.getBody();
                                //System.out.println("got order status");
				
			}
			break;
                case RESPONSE_CATAGORY_TO_SUPERVISOR_UI:
                    //System.out.println("controller:RESPONSE_CATAGORY_TO_SUPERVISOR_UI come");
                    if(event.getBody() instanceof WidgetCatalog){
                        //System.out.println("controller:go");
                        WidgetCatalog widgetCatalog = (WidgetCatalog) event.getBody();
                        mSupervisorUi.updateCatalog(widgetCatalog);
                    }
                    break;
		}
	}
	public void requestWidgetCatalog(){
            //System.out.println("controller:request widget catalog");
		//sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.REQUEST_CATAGORY_FROM_SUPERVISOR_UI, null);
		postEvent(new EventMessage(getId().name(), WComponentType.WAREHOUSE_SUPERVISOR.name(), EventMessageType.REQUEST_CATAGORY_FROM_SUPERVISOR_UI, null));
	}
	public void sendWidgetCatalog(WidgetCatalog widgetCatalog){
		//sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.SEND_WIDGET_CATALOG_UPDATE, widgetCatalog);
		postEvent(new EventMessage(getId().name(), WComponentType.WAREHOUSE_SUPERVISOR.name(), EventMessageType.SEND_WIDGET_CATALOG_UPDATE, widgetCatalog));
	}
	public void requestOrderStatus(){
		//sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.REQUEST_ORDER_STATUS, null);
		postEvent(new EventMessage(getId().name(), WComponentType.WAREHOUSE_SUPERVISOR.name(), EventMessageType.REQUEST_ORDER_STATUS, null));
	}
	public void sendWarehouseInventoryInfo(WarehouseInventoryInfo info){
//		sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.FILL_INVENTORY_WIDGET,info );
		postEvent(new EventMessage(getId().name(), WComponentType.WAREHOUSE_SUPERVISOR.name(), EventMessageType.FILL_INVENTORY_WIDGET, info));
	}

    void setWidgetCatalogUpdateListener(SupervisorUi aThis) {
        mSupervisorUi = aThis;
    }

	@Override
	public void ping() {
		// TODO Auto-generated method stub
		
	}

}
