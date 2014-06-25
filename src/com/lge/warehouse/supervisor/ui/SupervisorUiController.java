package com.lge.warehouse.supervisor.ui;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.supervisor.CustomerServiceManager;
import com.lge.warehouse.supervisor.WarehouseInventoryInfo;
import com.lge.warehouse.util.NewWidgetInfo;
import com.lge.warehouse.util.OrderStatusInfo;
import com.lge.warehouse.util.WarehouseStatus;
import com.lge.warehouse.util.WidgetCatalog;
import java.util.logging.Level;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import org.apache.log4j.Logger;

public class SupervisorUiController extends WarehouseRunnable {
	private static Logger logger = Logger.getLogger(SupervisorUiController.class);
    private SupervisorUi mSupervisorUi;
	public SupervisorUiController() {
		super(WComponentType.SUPERVISOR_UI, true);
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
				//eventHandle(em);
				postEvent(em);
			} catch (JMSException ex) {
				java.util.logging.Logger.getLogger(CustomerServiceManager.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	@Override
	protected void eventHandle(EventMessage event) {
		logger.info("eventHandle "+event);
		
		switch(event.getType()){
		case RESPONSE_ORDER_STATUS:
			if(event.getBody() instanceof OrderStatusInfo){
				OrderStatusInfo statusInfo = (OrderStatusInfo)event.getBody();
                                //System.out.println("got order status");
                                mSupervisorUi.updateOrderStatus(statusInfo.toString());
				
			}
			break;
                case RESPONSE_CATAGORY_TO_SUPERVISOR_UI:
                    if(event.getBody() instanceof WidgetCatalog){
                        //System.out.println("controller:go");
                        WidgetCatalog widgetCatalog = (WidgetCatalog) event.getBody();
                        mSupervisorUi.updateCatalog(widgetCatalog);
                    }
                    break;
                    
                case WAREHOUSE_INVENTORY_INFO:
                    if(event.getBody() instanceof WarehouseInventoryInfo){
                        //System.out.println("got inventory status");
                        WarehouseInventoryInfo inventoryInfo = (WarehouseInventoryInfo) event.getBody();
                        mSupervisorUi.updateInvenetoryStatus(inventoryInfo.toString());
                    }
                    break;
                    
                case UPDATE_WAREHOUSE_STATUS:
                    if(event.getBody() instanceof WarehouseStatus){
                        //System.out.println("got warehouse status");
                        WarehouseStatus warehouseStatus = (WarehouseStatus) event.getBody();
                        mSupervisorUi.updateRobotStatus(warehouseStatus);
                    }
                    break;
		}
	}
	public void requestAddNewWidgetItem(String widgetName, int price){
		NewWidgetInfo newWidgetInfo = new NewWidgetInfo(widgetName, price);
		sendMsgOnContext(new EventMessage(getId().name(), WComponentType.WAREHOUSE_SUPERVISOR.name(), EventMessageType.ADD_NEW_WIDGET_ITEM, newWidgetInfo));
	}
	public void requestWidgetCatalog(){
            //System.out.println("controller:request widget catalog");
		//sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.REQUEST_CATAGORY_FROM_SUPERVISOR_UI, null);
		sendMsgOnContext(new EventMessage(getId().name(), WComponentType.WAREHOUSE_SUPERVISOR.name(), EventMessageType.REQUEST_CATAGORY_FROM_SUPERVISOR_UI, null));
	}
	
	public void requestOrderStatus(){
		//sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.REQUEST_ORDER_STATUS, null);
		sendMsgOnContext(new EventMessage(getId().name(), WComponentType.WAREHOUSE_SUPERVISOR.name(), EventMessageType.REQUEST_ORDER_STATUS, null));
	}
	public void sendWarehouseInventoryInfo(WarehouseInventoryInfo info){
//		sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.FILL_INVENTORY_WIDGET,info );
		sendMsgOnContext(new EventMessage(getId().name(), WComponentType.WAREHOUSE_SUPERVISOR.name(), EventMessageType.FILL_INVENTORY_WIDGET, info));
	}

    void setWidgetCatalogUpdateListener(SupervisorUi aThis) {
        mSupervisorUi = aThis;
    }

	@Override
	public void ping() {
		// TODO Auto-generated method stub
		
	}

}
