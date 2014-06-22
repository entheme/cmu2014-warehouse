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
    RESPONSE_PENDING_ORDER,
    REQUEST_PENDING_ORDER, //PendingOrderManager->WarehouseSupervisor for requesting pending order.
    WAREHOUSE_INVENTORY_INFO, //WmMsgHandler->WarehouseSupervisor for sending each warehouse inventory info
    FILL_ORDER, //WarehouseSupervisor ->WmMsgHandler for filling order
    FINISH_FILL_ORDER, //WmMsgHandler->WarehouseSupervisor for notifying filling order is done.
    READY_TO_OPERATE,	//All thread -> System
    SYSTEM_READY //System -> All thread 
    ;
    

}
