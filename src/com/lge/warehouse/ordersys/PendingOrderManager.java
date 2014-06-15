/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.lge.warehouse.ordersys;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.common.bus.p2p.P2PSender;
import com.lge.warehouse.supervisor.OrderProvider;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author seuki77
 */
public final class PendingOrderManager extends WarehouseRunnable{
    static Logger logger = Logger.getLogger(PendingOrderManager.class);
    private static PendingOrderManager sInstance;
    private P2PSender mSenderToSupervisor;
    
    private PendingOrderManager(){
        super(WComponentType.PENDING_ORDER_MANAGER);
        
    }
    
    public static PendingOrderManager getInstance() {
        if (sInstance == null) {
            sInstance = new PendingOrderManager();
        }
        return sInstance;
    }
    
    @Override
    protected void eventHandle(EventMessage event) {
       
    }
    
    public static void start() {
        logger.info("PendingOrderManager start");
        new Thread(PendingOrderManager.getInstance()).start();
    }
    
    @Override
    protected void initBus() {
        addBus(WComponentType.ORDER_PROVIDER);
    }

    @Override
    public void ping() {
        sendMsg(WComponentType.ORDER_PROVIDER, EventMessageType.COMPONENT_HELLO, null);
    }
}
