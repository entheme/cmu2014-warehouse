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
    
    P2P_CustomerServiceManager_PendingOrderManager(WComponentType.CUSTOMER_SERVICE_MANAGER, WComponentType.PENDING_ORDER_MANAGER),
    P2P_PendingOrderManager_CustomerServiceManager(WComponentType.PENDING_ORDER_MANAGER, WComponentType.CUSTOMER_SERVICE_MANAGER),
    
    P2P_CustomerServiceManager_WarehouseSupervisor(WComponentType.CUSTOMER_SERVICE_MANAGER, WComponentType.WAREHOUSE_SUPERVISOR),
    P2P_WarehouseSupervisor_CustomerServiceManager(WComponentType.WAREHOUSE_SUPERVISOR, WComponentType.CUSTOMER_SERVICE_MANAGER),
  
    P2P_PendingOrderManager_WarehouseSupervisor(WComponentType.PENDING_ORDER_MANAGER, WComponentType.WAREHOUSE_SUPERVISOR),
    P2P_WarehouseSupervisor_PendingOrderManager(WComponentType.WAREHOUSE_SUPERVISOR, WComponentType.PENDING_ORDER_MANAGER),
    
    P2P_WarehouseSupervisor_SupervisorUI(WComponentType.WAREHOUSE_SUPERVISOR, WComponentType.SUPERVISOR_UI),
    P2P_SupervisorUI_WarehouseServiceManager(WComponentType.SUPERVISOR_UI, WComponentType.WAREHOUSE_SUPERVISOR),
    
    P2P_WarehouseSupervisor_WmMsgHandler(WComponentType.WAREHOUSE_SUPERVISOR, WComponentType.WM_MSG_HANDLER),
    P2P_WmMsgHandler_WarehouseSupervisor(WComponentType.WM_MSG_HANDLER, WComponentType.WAREHOUSE_SUPERVISOR),
    
    P2P_WmMsgHandler_WarehouseManagerController(WComponentType.WM_MSG_HANDLER, WComponentType.WAREHOUSE_MANAGER_CONTROLLER),
    P2P_WarehouseManagerController_WmMsgHandler(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, WComponentType.WM_MSG_HANDLER),
    
    P2P_WarehouseManagerController_RobotOutputMgr(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, WComponentType.ROBOT_OUTPUT_MGR),
    P2P_RobotOutputMgr_WarehouseManagerController(WComponentType.ROBOT_OUTPUT_MGR, WComponentType.WAREHOUSE_MANAGER_CONTROLLER),
    
    P2P_WarehouseManagerController_RobotInputMgr(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, WComponentType.ROBOT_INPUT_MGR),
    P2P_RobotInputMngr_WarehouseManagerController(WComponentType.ROBOT_INPUT_MGR, WComponentType.WAREHOUSE_MANAGER_CONTROLLER),
    
    P2P_WarehouseManagerController_WarehouseOutputMgr(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, WComponentType.WAREHOUSE_OUTPUT_MGR),
    P2P_WarehouseOutputMgr_WarehouseManagerController(WComponentType.WAREHOUSE_OUTPUT_MGR, WComponentType.WAREHOUSE_MANAGER_CONTROLLER),
    
    P2P_WarehouseManagerController_WarehouseInputMgr(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, WComponentType.WAREHOUSE_INPUT_MGR),
    P2P_WarehouseInputMgr_WarehouseManagerController(WComponentType.WAREHOUSE_INPUT_MGR, WComponentType.WAREHOUSE_MANAGER_CONTROLLER),
    
    P2P_CustomerIF_SYSTEM(WComponentType.CUSTOMER_INF, WComponentType.SYSTEM),
    P2P_SYSTEM_CustomerIF(WComponentType.SYSTEM, WComponentType.CUSTOMER_INF),
    
    P2P_SupervisorUI_SYSTEM(WComponentType.SUPERVISOR_UI, WComponentType.SYSTEM),
    P2P_SYSTEM_SupervisorUI(WComponentType.SYSTEM, WComponentType.SUPERVISOR_UI),
    
    P2P_CustomerServiceManager_SYSTEM(WComponentType.CUSTOMER_SERVICE_MANAGER, WComponentType.SYSTEM),
    P2P_SYSTEM_CustomerServiceManager(WComponentType.SYSTEM, WComponentType.CUSTOMER_SERVICE_MANAGER),
    
    P2P_PendingOrderManager_SYSTEM(WComponentType.PENDING_ORDER_MANAGER, WComponentType.SYSTEM),
    P2P_SYSTEM_PendingOrderManager(WComponentType.SYSTEM, WComponentType.PENDING_ORDER_MANAGER),
    
    P2P_WarehouseSupervisor_SYSTEM(WComponentType.WAREHOUSE_SUPERVISOR, WComponentType.SYSTEM ),
    P2P_SYSTEM_WarehouseSupervisor(WComponentType.SYSTEM, WComponentType.WAREHOUSE_SUPERVISOR),
    
    P2P_WmMsgHandler_SYSTEM(WComponentType.WM_MSG_HANDLER, WComponentType.SYSTEM),
    P2P_SYSTEM_WmMsgHandler(WComponentType.SYSTEM, WComponentType.WM_MSG_HANDLER),
    
    P2P_WarehouseManagerController_SYSTEM(WComponentType.WAREHOUSE_MANAGER_CONTROLLER, WComponentType.SYSTEM),
    P2P_SYSTEM_WarehouseManagerControlelr(WComponentType.SYSTEM, WComponentType.WAREHOUSE_MANAGER_CONTROLLER),
    
    P2P_RobotOutputMgr_SYSTEM(WComponentType.ROBOT_OUTPUT_MGR, WComponentType.SYSTEM),
    P2P_SYSTEM_RobotOutputMgr(WComponentType.SYSTEM, WComponentType.ROBOT_OUTPUT_MGR),
    
    P2P_RobotInputMgr_SYSTEM(WComponentType.ROBOT_INPUT_MGR, WComponentType.SYSTEM),
    P2P_SYSTEM_RobotInputMgr(WComponentType.SYSTEM, WComponentType.ROBOT_INPUT_MGR),
    
    P2P_WarehouseOutputMgr_SYSTEM(WComponentType.WAREHOUSE_OUTPUT_MGR, WComponentType.SYSTEM),
    P2P_SYSTEM_WarehouseOutputMgr(WComponentType.SYSTEM, WComponentType.WAREHOUSE_OUTPUT_MGR),
    
    P2P_WarehouseInputMgr_SYSTEM(WComponentType.WAREHOUSE_INPUT_MGR, WComponentType.SYSTEM),
    P2P_SYSTEM_WarehouseInputMgr(WComponentType.SYSTEM, WComponentType.WAREHOUSE_INPUT_MGR)
    
    ;
    
    
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
