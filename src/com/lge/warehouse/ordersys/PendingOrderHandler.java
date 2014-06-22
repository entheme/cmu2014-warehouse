/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.ordersys;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.MsgInterface;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseContext;
import com.lge.warehouse.util.Order;
import org.apache.log4j.Logger;

/**
 *
 * @author seuki77
 */
public class PendingOrderHandler {
    static Logger logger = Logger.getLogger(PendingOrderHandler.class);
    private MsgInterface mMsgInf;
    private boolean mPendingOrderRequested = false;
    public PendingOrderHandler(MsgInterface msgInf){
        mMsgInf = msgInf;
    }
    public void requestPendingOrder(){
        Order order = PendingOrderQueue.getInstance().getOrder();
        if(order==null)
            mPendingOrderRequested = true;
        else{
            mMsgInf.sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.RESPONSE_PENDING_ORDER, order);
        }
    }
    public void pendingOrderReady(){
        if(mPendingOrderRequested == true){
            Order order = PendingOrderQueue.getInstance().getOrder();
            if (order == null){
                String errMsg = "Received Pending Order Ready, but Pending Order Queue is empty";
                if(WarehouseContext.DEBUG_WITH_RUNTIME_EXCEPTION)
                    throw new RuntimeException(errMsg);
                else
                    logger.debug(errMsg);
            }
            mMsgInf.sendMsg(WComponentType.WAREHOUSE_SUPERVISOR, EventMessageType.RESPONSE_PENDING_ORDER, order);
            mPendingOrderRequested = false;
        }
    }
}
