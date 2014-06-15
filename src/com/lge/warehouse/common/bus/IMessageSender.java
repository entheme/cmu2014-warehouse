/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.common.bus;

/**
 *
 * @author seuki77
 */
import java.io.Serializable;

public interface IMessageSender {
	public void sendObject(Serializable obj);
	public void stop();
}
