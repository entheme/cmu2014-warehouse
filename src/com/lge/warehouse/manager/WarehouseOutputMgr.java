/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.manager;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.common.app.WarehouseRunnable;
import com.lge.warehouse.common.bus.EventMessage;
import org.apache.log4j.Logger;

/**
 *
 * @author seuki77
 */
public class WarehouseOutputMgr extends WarehouseRunnable{
    private static WarehouseOutputMgr sInstance = null;
    static Logger logger = Logger.getLogger(WarehouseOutputMgr.class);
    ArduinoConnector mArduinoConForWarehouse = null;
       
    private WarehouseOutputMgr() {
        super(WComponentType.WAREHOUSE_OUTPUT_MGR, false);
    }
    
    public static WarehouseOutputMgr getInstance() {
        if (sInstance == null) {
            sInstance = new WarehouseOutputMgr();
        }
        return sInstance;
    }
    
    protected void setArduinoConnector(ArduinoConnector arduinoCon) {
        mArduinoConForWarehouse = arduinoCon;
    }

    @Override
    protected void eventHandle(EventMessage event) {
		switch(event.getType()){
		case SYSTEM_READY:
                    break;
                case INIT_WAREHOUSE:
                    logger.info("Call INIT_WAREHOUSE");
                    mArduinoConForWarehouse.writeData("I");
                    break;
                case REQUEST_LOAD_STATUS:
                    logger.info("REQUEST_LOAD_STATUS");
                    mArduinoConForWarehouse.writeData("L");
                    break;
                case REQUST_POS_STATUS:
                    logger.info("REQUST_POS_STATUS");
                    mArduinoConForWarehouse.writeData("P");
                    break;    
                case REQUEST_WAREHOUSE_RECOVERY:
                    logger.info("REQUEST_WAREHOUSE_RECOVERY");
                    mArduinoConForWarehouse.writeData("R");
                    break;    
		default:
                    logger.info("unhandled event :"+event);
                    break;
		}
    }
    
     public static void start(ArduinoConnector arduinoCon) {
        logger.info("WarehouseOutputMgr start");
        getInstance().setArduinoConnector(arduinoCon);
        new Thread(getInstance()).start();
    }  

    @Override
    protected void initBus() {
        addBus(WComponentType.WAREHOUSE_MANAGER_CONTROLLER);
    }

    @Override
    public void ping() {
        sendMsg(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, EventMessageType.COMPONENT_HELLO,null);
    }
}
