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
import static com.lge.warehouse.manager.WarehouseManagerController.logger;
import com.lge.warehouse.util.InventoryName;
import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.QuantifiedWidget;
import com.lge.warehouse.util.WarehouseInventoryInfo;
import com.lge.warehouse.util.WarehouseStatus;
import org.apache.log4j.Logger;
import com.lge.warehouse.manager.ArduinoWriter;

/**
 *
 * @author seuki77
 */
public class RobotOutputMgr extends WarehouseRunnable{
    private static RobotOutputMgr sInstance = null;
    static Logger logger = Logger.getLogger(RobotOutputMgr.class);
    ArduinoWriter mArduinoWriter = new ArduinoWriter("tcp://localhost");
    
    private RobotOutputMgr() {
        super(WComponentType.ROBOT_OUTPUT_MGR);
        mArduinoWriter.setPortNum(504);
    }
    
    public static RobotOutputMgr getInstance() {
        if (sInstance == null) {
            sInstance = new RobotOutputMgr();
        }
        return sInstance;
    }

    @Override
    protected void eventHandle(EventMessage event) {
		switch(event.getType()){
		case SYSTEM_READY:
                    break;
                case MOVE_NEXT_INV:
                    mArduinoWriter.writeData("M");
                    break;
                case REQUEST_ROBOT_RECOVERY:
                    mArduinoWriter.writeData("R");
                    break;
		default:
			logger.info("unhandled event :"+event);
			break;
		}
    }

    public static void start() {
        logger.info("RobotOutputMgr start");
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
