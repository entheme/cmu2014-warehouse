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

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

import com.lge.warehouse.common.bus.IMessageSender;

public class PSPublisher implements IMessageSender {
	private TopicSession mSession = null;
	private TopicPublisher mPublisher = null;
	
	public PSPublisher(TopicSession session, TopicPublisher publisher){
		mSession = session;
		mPublisher = publisher;
	}
	@Override
	public void sendObject(Serializable obj) {
		// TODO Auto-generated method stub
		ObjectMessage objMsg;
		try {
			objMsg = mSession.createObjectMessage(obj);
			mPublisher.send(objMsg);
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

