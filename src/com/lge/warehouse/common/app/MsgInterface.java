/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.common.app;

import java.io.Serializable;

/**
 *
 * @author seuki77
 */
public interface MsgInterface {

    void sendMsg(WComponentType dest, EventMessageType type, Serializable body);
    
}
