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
    FILL_INVENTORY_WIDGET, //SupervisorUI -> WarehouseSupervisor for fill widget in inventory
    
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
    
    SEND_WDIGET_CATALOG_TO_WM_MSG_HANDLER, //WarehouseSupervisor ->WmMsgHandler to send Widget Catalog list
    
    ADD_NEW_WIDGET_ITEM, 	//WarehouseSupervisor -> SupervisorUI to request adding new widget item
    
    REQUEST_ORDER_STATUS,	//SupervisorUI -> WarehouseSupervisor to request order status
    RESPONSE_ORDER_STATUS,		//WarehouseSupervisor -> WarehouseSupervisor to send order status
    
    REQUEST_PENDING_ORDER_STATUS, //WarehouseSupervisor -> PendingOrderManager to request pending order status
    RESPONSE_PENDING_ORDER_STATUS, //PendingOrderManager -> WarehouseSupervisor to send pending order status
    
    UPDATE_WAREHOUSE_STATUS, //WmMsgHandler ->WarehouseSupervisor ->SupervisorUI
    
    WAREHOUSE_RUNNABLE_SEND_ON_THREAD_CONTEXT, 
    WAREHOUSE_RUNNABLE_HEARTBEAT_MSG,
    
    MOVE_NEXT_INV, //WarehouseMangerController -> RobotOutputMgr
    ROBOT_ERROR_STATUS, //RobotInputMgr -> WarehouseMangerController
    ROBOT_IS_CONNECTED, //RobotInputMgr -> WarehouseMangerController -> WmMsgHandler ->WarehouseSupervisor ->SupervisorUI
    ROBOT_IS_DISCONNECTED, //RobotInputMgr -> WarehouseMangerController -> WmMsgHandler ->WarehouseSupervisor ->SupervisorUI
    REQUEST_ROBOT_RECOVERY, //WarehouseMangerController -> RobotOutputMgr
    INIT_WAREHOUSE, //WarehouseMangerController -> WarehouseOutputMgr
    REQUEST_LOAD_STATUS, //WarehouseMangerController -> WarehouseOutputMgr
    REQUST_POS_STATUS,  //WarehouseMangerController -> WarehouseOutputMgr
    REQUEST_WAREHOUSE_RECOVERY, //WarehouseMangerController -> WarehouseOutputMgr 
    WAREHOUSE_LOAD_STATUS, //WarehouseInputMgr -> WarehouseMangerController
    ROBOT_POSITION_STATUS, //WarehouseInputMgr -> WarehouseMangerController
    WAREHOUSE_IS_CONNECTED, //WarehouseInputMgr -> WarehouseMangerController -> WmMsgHandler ->WarehouseSupervisor ->SupervisorUI
    WAREHOUSE_IS_DISCONNECTED, //WarehouseInputMgr -> WarehouseMangerController -> WmMsgHandler ->WarehouseSupervisor ->SupervisorUI

    TEST_SUPERVISOR_UI_SEND_WIDGET_CATALOG, TEST_SUPERVISOR_UI_REQUEST_ORDER_STATUS, TEST_SUPERVISOR_UI_SEND_WAREHOUSE_INVENTORY_INFO,
    ;
    

}
