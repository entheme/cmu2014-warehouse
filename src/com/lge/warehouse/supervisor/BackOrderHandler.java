/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.supervisor;

import java.util.List;

import org.apache.log4j.Logger;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.MsgInterface;
import com.lge.warehouse.common.app.WComponentType;
import com.lge.warehouse.util.Order;
import com.lge.warehouse.util.QuantifiedWidget;

/**
 *
 * @author seuki77
 */
public class BackOrderHandler {
    private MsgInterface mMsgInf;
    static Logger logger = Logger.getLogger(BackOrderHandler.class);
    public BackOrderHandler(MsgInterface inf){
        mMsgInf = inf;
    }
    
}
