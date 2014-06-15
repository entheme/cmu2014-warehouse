/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.common.bus;

import javax.jms.MessageListener;
/**
 *
 * @author seuki77
 */


public interface IMessageReceiver {
	public void setMessageListener(MessageListener listener);
	public void stop();
}
