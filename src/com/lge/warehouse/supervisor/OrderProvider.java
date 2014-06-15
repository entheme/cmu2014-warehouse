/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.supervisor;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author kihyung2.lee
 */
public final class OrderProvider extends WarehouseRunnable {
    private static OrderProvider sInstance = null;
    static Logger logger = Logger.getLogger(OrderProvider.class);
    
    private OrderProvider() {
        super(WComponentType.ORDER_PROVIDER);
    }
    
    public static OrderProvider getInstance() {
        if (sInstance == null) {
            sInstance = new OrderProvider();
        }
        return sInstance;
    }

    @Override
    protected void eventHandle(EventMessage event) {

    }

    public static void start() {
        logger.info("OrderProvider start");
        new Thread(getInstance()).start();
    }

    @Override
    protected void initBus() {
        addBus(WComponentType.CUSTOMER_SERVICE_MANAGER);
        addBus(WComponentType.PENDING_ORDER_MANAGER);
        addBus(WComponentType.WAREHOUSE_SERVICE_MANAGER);
        addBus(WComponentType.SUPERVISOR_UI);
    }

    @Override
    public void ping() {
        sendMsg(WComponentType.CUSTOMER_SERVICE_MANAGER, EventMessageType.COMPONENT_HELLO, null);
        sendMsg(WComponentType.PENDING_ORDER_MANAGER, EventMessageType.COMPONENT_HELLO, null);
        sendMsg(WComponentType.WAREHOUSE_SERVICE_MANAGER, EventMessageType.COMPONENT_HELLO, null);
        sendMsg(WComponentType.SUPERVISOR_UI, EventMessageType.COMPONENT_HELLO, null);
    }
}
