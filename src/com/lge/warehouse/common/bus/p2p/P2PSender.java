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
import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import com.lge.warehouse.common.bus.IMessageSender;

public class P2PSender implements IMessageSender {
	private MessageProducer mSender;
	private Session mSession;
	
	public P2PSender(Session session, MessageProducer producer){
		mSender = producer;
		mSession = session;
	}
	@Override
	public void sendObject(Serializable obj) {
		// TODO Auto-generated method stub
		ObjectMessage objMsg;
		try {
			objMsg =mSession.createObjectMessage(obj);
			mSender.send(objMsg);
		}catch (JMSException e){
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
