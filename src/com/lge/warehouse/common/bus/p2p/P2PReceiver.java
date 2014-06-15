/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.common.bus.p2p;

/**
 *
 * @author seuki77
 */
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import com.lge.warehouse.common.bus.IMessageReceiver;

public class P2PReceiver implements IMessageReceiver {
	private MessageConsumer mReceiver;
	private Session mSession;
	
	public P2PReceiver(Session session, MessageConsumer consumer) {
		mSession = session;
		mReceiver = consumer;
	}
	
	@Override
	public void setMessageListener(MessageListener listener) {
		// TODO Auto-generated method stub
		try {
			mReceiver.setMessageListener(listener);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		try {
			mSession.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
