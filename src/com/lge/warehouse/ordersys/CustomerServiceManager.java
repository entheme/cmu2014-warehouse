/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.lge.warehouse.ordersys;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WBus;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.common.bus.p2p.P2PConnection;
import com.lge.warehouse.common.bus.p2p.P2PReceiver;
import com.lge.warehouse.common.bus.p2p.P2PSender;
import com.lge.warehouse.supervisor.OrderProvider;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author seuki77
 */
public final class CustomerServiceManager extends WarehouseRunnable{
    private static CustomerServiceManager sInstance;
    static Logger logger = Logger.getLogger(CustomerServiceManager.class);
    
    private P2PSender mSenderToClientUI;
    
    private CustomerServiceManager() {
        super(WComponentType.CUSTOMER_SERVICE_MANAGER);
    }
    public static CustomerServiceManager getInstance() {
        if(sInstance == null) {
            sInstance = new CustomerServiceManager();
        }
        return sInstance;
    }
    @Override
    protected void eventHandle(EventMessage event) {
        
    }
    
    @Override
    protected void initBus() {
        addBus(WComponentType.CUSTOMER_INF);
        addBus(WComponentType.ORDER_PROVIDER);
    }
    
    public static void start() {
        logger.info("CustomerServiceManager start");
        new Thread(getInstance()).start();
    }

    @Override
    public void ping() {
        sendMsg(WComponentType.CUSTOMER_INF, EventMessageType.COMPONENT_HELLO, null);
        sendMsg(WComponentType.ORDER_PROVIDER, EventMessageType.COMPONENT_HELLO, null);
    }
}
