/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.ordersys;

import java.util.List;

import org.apache.log4j.Logger;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.MsgInterface;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.QuantifiedWidget;

/**
 *
 * @author seuki77
 */
public class PlaceOrderHandler {
    private MsgInterface mMsgInf;
    static Logger logger = Logger.getLogger(PlaceOrderHandler.class);
    private static long sOrderCnt = 0;
    public PlaceOrderHandler(MsgInterface inf){
        mMsgInf = inf;
    }
    public void handleOrder(Order order){
        logger.info("handleOrder "+(sOrderCnt+1));
        List<QuantifiedWidget> list = order.getItemList();
        for(QuantifiedWidget qw : list){
            logger.info(qw.getWidget()+" : cnt("+qw.getQuantity()+")");
        }
        order.setOrderId(++sOrderCnt);
        order.setOrderStatus(Order.Status.ORDER_PENDING);
        PendingOrderQueue.getInstance().putOrder(order);
        mMsgInf.sendMsg(WComponentType.PENDING_ORDER_MANAGER, EventMessageType.PENDING_ORDER_READY, null);
    }
}
