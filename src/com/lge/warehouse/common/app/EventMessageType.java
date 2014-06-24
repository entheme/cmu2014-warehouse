/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.common.app;

/**
 *
 * @author seuki77
 */
public enum EventMessageType {
    COMPONENT_END,
    COMPONENT_HELLO,
    
    WAREHOUSE_ADD_REQUEST,
    WAREHOUSE_ADD_ACCEPT,
    
    
    PLACE_ORDER,            //Customer interface ->CustomerServiceManager for placing order
    PENDING_ORDER_READY,     //CustomerServiceManager ->PendingOrderManager for notifying pending order ready
    
    REQUEST_PENDING_ORDER, //PendingOrderManager->WarehouseSupervisor for requesting pending order.
    RESPONSE_PENDING_ORDER,
    
    WAREHOUSE_INVENTORY_INFO, //SupervisorUI->WarehouseSupervisor->WmMsgHandler for sending each warehouse inventory info
    
    FILL_ORDER, //WarehouseSupervisor ->WmMsgHandler for filling order
    FINISH_FILL_ORDER, //WmMsgHandler->WarehouseSupervisor for notifying filling order is done.
    
    READY_TO_OPERATE,	//All thread -> System
    SYSTEM_READY, //System -> All thread
    
    REQUEST_CATAGORY_FROM_CUSTOMER_IF, 	//CustomerIF -> CustomerServiceManager to request Widget Catalog list
    RESPONSE_CATAGORY_TO_CUSTOMER_IF,	//CustomerServiceManager -> CustomerIF to send WidgetCatalog list
    
    REQUEST_CATAGORY_FROM_CUSTOMER_SERVICE_MANAGER,	//CustomerServiceManager -> WarehouseSupervisor to request Widget Catalog list
    RESPONSE_CATAGORY_TO_CUSTOMER_SERVICE_MANAGER,	//WarehouseSupervisor -> CustomerServiceManager to send Widget Catalog list
    
    REQUEST_CATAGORY_FROM_SUPERVISOR_UI, //SupervisorUI -> WarehouseSupervisor to request Widget Catalog list
    RESPONSE_CATAGORY_TO_SUPERVISOR_UI, //WarehouseSupervisor -> SupervisorUI to send Widget Catalog list
    
    SEND_WIDGET_CATALOG_UPDATE,	//SupervisorUI -> WarehouseSupervisor to send Widget Catalog 
    
    REQUEST_ORDER_STATUS,	//SupervisorUI -> WarehouseSupervisor to request order status
    RESPONSE_ORDER_STATUS,		//WarehouseSupervisor -> WarehouseSupervisor to send order status
    
    REQUEST_PENDING_ORDER_STATUS, //WarehouseSupervisor -> PendingOrderManager to request pending order status
    RESPONSE_PENDING_ORDER_STATUS, //PendingOrderManager -> WarehouseSupervisor to send pending order status
    
    
    TEST_SUPERVISOR_UI_SEND_WIDGET_CATALOG, TEST_SUPERVISOR_UI_REQUEST_ORDER_STATUS, TEST_SUPERVISOR_UI_SEND_WAREHOUSE_INVENTORY_INFO,
    ;
    

}
