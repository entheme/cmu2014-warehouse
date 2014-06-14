/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.util;
import java.util.List;

/**
 *
 * @author kihyung2.lee
 */
public class Order {
    enum Status {
        ORDER_PENDING,
        ORDER_IN_PROGRESS,
        ORDER_BACK_ORDERED,
        ORDER_COMPLETE
    }

    private int mOrderId;
    private Status mStatus;
    private List<QuantifiedWidget> mOrderItems;
}
