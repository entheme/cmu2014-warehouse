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
public enum WBus {
    P2P_NONE(WComponentType.NONE, WComponentType.NONE),
    P2P_CustomerIF_CustomerServiceManager(WComponentType.CUSTOMER_INF, WComponentType.CUSTOMER_SERVICE_MANAGER),
    P2P_CustomerServiceManager_CustomerIF(WComponentType.CUSTOMER_SERVICE_MANAGER, WComponentType.CUSTOMER_INF),
    
    P2P_CustomerServiceManager_OrderProvider(WComponentType.CUSTOMER_SERVICE_MANAGER, WComponentType.ORDER_PROVIDER),
    P2P_OrderProvider_CustomerServiceManager(WComponentType.ORDER_PROVIDER, WComponentType.CUSTOMER_SERVICE_MANAGER),
  
    P2P_PendingOrderManager_OrderProvider(WComponentType.PENDING_ORDER_MANAGER, WComponentType.ORDER_PROVIDER),
    P2P_OrderProvider_PendingOrderManager(WComponentType.ORDER_PROVIDER, WComponentType.PENDING_ORDER_MANAGER),
    
    P2P_OrderProvider_SupervisorUI(WComponentType.ORDER_PROVIDER, WComponentType.SUPERVISOR_UI),
    P2P_SupervisorUI_OrderProvider(WComponentType.SUPERVISOR_UI, WComponentType.ORDER_PROVIDER),
    
    P2P_OrderProvider_WarehouseServiceManager(WComponentType.ORDER_PROVIDER, WComponentType.WAREHOUSE_SERVICE_MANAGER),
    P2P_WarehouseServiceManager_OrderProvider(WComponentType.WAREHOUSE_SERVICE_MANAGER, WComponentType.ORDER_PROVIDER),
    
    P2P_WarehouseServiceManager_SupervisorUI(WComponentType.WAREHOUSE_SERVICE_MANAGER, WComponentType.SUPERVISOR_UI),
    P2P_SupervisorUI_WarehouseServiceManager(WComponentType.SUPERVISOR_UI, WComponentType.WAREHOUSE_SERVICE_MANAGER),
    
    P2P_WarehouseServiceManager_WmMsgHandler(WComponentType.WAREHOUSE_SERVICE_MANAGER, WComponentType.WM_MSG_HANDLER),
    P2P_WmMsgHandler_WarehouseServiceManager(WComponentType.WM_MSG_HANDLER, WComponentType.WAREHOUSE_SERVICE_MANAGER),
    
    P2P_WmMsgHandler_WarehouseManagerController(WComponentType.WM_MSG_HANDLER, WComponentType.WAREHOUSE_MANAGER_CONTROLLER),
    P2P_WarehouseManagerController_WmMsgHandler(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, WComponentType.WM_MSG_HANDLER),
    
    P2P_WarehouseManagerController_RobotOutputMgr(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, WComponentType.ROBOT_OUTPUT_MGR),
    P2P_RobotOutputMgr_WarehouseManagerController(WComponentType.ROBOT_OUTPUT_MGR, WComponentType.WAREHOUSE_MANAGER_CONTROLLER),
    
    P2P_WarehouseManagerController_RobotInputMgr(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, WComponentType.ROBOT_INPUT_MGR),
    P2P_RobotInputMngr_WarehouseManagerController(WComponentType.ROBOT_INPUT_MGR, WComponentType.WAREHOUSE_MANAGER_CONTROLLER),
    
    P2P_WarehouseManagerController_WarehouseOutputMgr(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, WComponentType.WAREHOUSE_OUTPUT_MGR),
    P2P_WarehouseOutputMgr_WarehouseManagerController(WComponentType.WAREHOUSE_OUTPUT_MGR, WComponentType.WAREHOUSE_MANAGER_CONTROLLER),
    
    P2P_WarehouseManagerController_WarehouseInputMgr(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, WComponentType.WAREHOUSE_INPUT_MGR),
    P2P_WarehouseInputMgr_WarehouseManagerController(WComponentType.WAREHOUSE_INPUT_MGR, WComponentType.WAREHOUSE_MANAGER_CONTROLLER);
    
    WComponentType mSrc;
    WComponentType mDest;
    WBus(WComponentType src, WComponentType dest){
        mSrc = src;
        mDest = dest;
    }
    public WComponentType getSrc(){
        return mSrc;
    }
    public WComponentType getDest() {
        return mDest;
    }
    public static WBus getBus(WComponentType src, WComponentType dest){
        for(WBus bus : WBus.values()){
            if(src==bus.getSrc()&&dest==bus.getDest())
                return bus;
        }
        return P2P_NONE;
    }

}
