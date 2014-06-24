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
import static com.lge.warehouse.manager.RobotOutputMgr.logger;
import org.apache.log4j.Logger;

/**
 *
 * @author seuki77
 */
public class WarehouseOutputMgr extends WarehouseRunnable{
    private static WarehouseOutputMgr sInstance = null;
    static Logger logger = Logger.getLogger(WarehouseOutputMgr.class);
     ArduinoWriter mArduinoWriter = new ArduinoWriter("tcp://localhost");
    
    private WarehouseOutputMgr() {
        super(WComponentType.WAREHOUSE_OUTPUT_MGR);
         mArduinoWriter.setPortNum(506);
    }
    
    public static WarehouseOutputMgr getInstance() {
        if (sInstance == null) {
            sInstance = new WarehouseOutputMgr();
        }
        return sInstance;
    }

    @Override
    protected void eventHandle(EventMessage event) {
		switch(event.getType()){
		case SYSTEM_READY:
                    break;
                case INIT_WAREHOUSE:
                    mArduinoWriter.writeData("I");
                    break;
                case REQUEST_LOAD_STATUS:
                    mArduinoWriter.writeData("L");
                    break;
                case REQUST_POS_STATUS:
                    mArduinoWriter.writeData("P");
                    break;    
                case REQUEST_WAREHOUSE_RECOVERY:
                    mArduinoWriter.writeData("R");
                    break;    
		default:
			logger.info("unhandled event :"+event);
			break;
		}
    }

    public static void start() {
        logger.info("WarehouseOutputMgr start");
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
