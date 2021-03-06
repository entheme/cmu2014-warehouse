/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.supervisor.ui;

import com.lge.warehouse.supervisor.WarehouseInventoryInfo;
import com.lge.warehouse.util.OrderStatusInfo;
import com.lge.warehouse.util.SystemEvent;
import com.lge.warehouse.util.WarehouseStatus;
import com.lge.warehouse.util.WidgetCatalog;

/**
 *
 * @author Jihun
 */
public interface SupervisorUiUpdate {
    void updateCatalog(WidgetCatalog widgetCatalog);
    void updateOrderStatus(OrderStatusInfo orderStatus);
    void updateRobotStatus(WarehouseStatus warehouseStatus);
    void updateInvenetoryStatus(WarehouseInventoryInfo inventoryInfo);
    void updateSystemStatus(SystemEvent systemStatus);
}