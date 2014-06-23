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

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import com.lge.warehouse.common.app.WBus;
import com.lge.warehouse.common.app.WarehouseContext;

public final class P2PConnection{
	private static Context mContext = null;
	private static ConnectionFactory mFactory = null;
	private static Connection mConnection = null;

	static Logger logger = Logger.getLogger(P2PConnection.class);

	static {
		initContext();
	}

	static void initContext(){
		try {
			if(WarehouseContext.TEST_MODE){
				
				mFactory = new ActiveMQConnectionFactory(
						"vm://localhost?broker.persistent=false");

				mConnection = mFactory.createConnection();
			}else{
				Properties props = new Properties();
				props.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");

				props.setProperty(Context.PROVIDER_URL,"tcp://localhost:61616");
				
				mContext = new InitialContext(props);
				mFactory = (QueueConnectionFactory) mContext
						.lookup("ConnectionFactory");
				//            mFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
				mConnection = mFactory.createConnection();
			}
			mConnection.start();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static P2PSender createSender(WBus type) {
		return createSender(type.name());
	}
	public static P2PSender createSender(String destination) {
		try {
			Session session = mConnection.createSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			Destination dest = null;
			try{
				dest = (Destination)mContext.lookup(destination);

			}catch (NamingException ex) {
				dest = session.createQueue(destination);
			}

			MessageProducer sender = session.createProducer(dest);
			return new P2PSender(session, sender);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static P2PReceiver createReceiver(WBus type) {
		return createReceiver(type.name());
	}
	public static P2PReceiver createReceiver(String destination) {

		try {
			Session session = mConnection.createSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			Destination dest = null;

			try {
				dest = (Destination)mContext.lookup(destination);
			} catch (NamingException ex) {
				dest = session.createQueue(destination);
			}

			MessageConsumer receiver = session.createConsumer(dest);

			return new P2PReceiver(session, receiver);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
