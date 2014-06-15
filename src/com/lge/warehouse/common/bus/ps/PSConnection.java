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
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.QueueSession;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public final class PSConnection {
	private static Context mContext = null;
	private static TopicConnectionFactory mFactory = null;
	private static TopicConnection mConnection = null;
	
	static Logger logger = Logger.getLogger(PSConnection.class);

	static { 
		initContext();
	}
	
	public static void initContext(){
		try {
			mContext = new InitialContext();
			mFactory = (TopicConnectionFactory)mContext.lookup("ConnectionFactory");
			mConnection = mFactory.createTopicConnection();
			mConnection.start();
		}catch (NamingException e){
			e.printStackTrace();
		}catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static PSPublisher createPublisher(String topicName) {
		try {
			TopicSession session = mConnection.createTopicSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			Topic topic = (Topic)mContext.lookup(topicName);
                        if (topic == null) topic = session.createTopic(topicName);
			TopicPublisher publisher = session.createPublisher(topic);
			return new PSPublisher(session, publisher);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static PSSubscriber createSubscriber(String topicName) {
		try {
			TopicSession session = mConnection.createTopicSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			Topic topic = (Topic)mContext.lookup(topicName);
                        if(topic == null) topic = session.createTopic(topicName);
			TopicSubscriber subscriber = session.createSubscriber(topic);
			return new PSSubscriber(session, subscriber);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
