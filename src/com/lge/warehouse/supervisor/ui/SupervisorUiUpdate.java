/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.supervisor.ui;

import com.lge.warehouse.util.WidgetCatalog;

/**
 *
 * @author Jihun
 */
public interface SupervisorUiUpdate {
    void updateCatalog(WidgetCatalog widgetCatalog);
    void updateOrderStatus(String orderStatus);
    void updateRobotStatus(String robotStatus);
    void updateInvenetoryStatus(String inventoryStatus);
}