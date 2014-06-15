/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.common.bus.ps;

/**
 *
 * @author seuki77
 */
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import com.lge.warehouse.common.bus.IMessageReceiver;

public class PSSubscriber implements IMessageReceiver {
	private TopicSubscriber mSubscriber;
	private TopicSession mSession;
	
	public PSSubscriber(TopicSession session, TopicSubscriber subscriber) {
		mSubscriber = subscriber;
		mSession = session;
	}
	@Override
	public void setMessageListener(MessageListener listener) {
		// TODO Auto-generated method stub
		try {
			mSubscriber.setMessageListener(listener);
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
