package com.lge.warehouse.common.app;

import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Level;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

import com.lge.warehouse.common.bus.EventMessage;
import com.lge.warehouse.common.bus.p2p.P2PConnection;
import com.lge.warehouse.common.bus.p2p.P2PReceiver;
import com.lge.warehouse.common.bus.p2p.P2PSender;
import com.lge.warehouse.supervisor.CustomerServiceManager;

public abstract class WarehouseComponent implements MessageListener, MsgInterface{
	static Logger logger = Logger.getLogger(WarehouseComponent.class);
	protected WComponentType mId;
	protected HashMap<String, P2PSender> mP2PSenderMap = new HashMap<String, P2PSender>();
	protected HashMap<String, P2PReceiver> mP2PReceiverMap = new HashMap<String, P2PReceiver>();
	protected String mAlias = null;

	public WarehouseComponent(WComponentType id) {
		mId = id;
		
		if((getId() == WComponentType.CUSTOMER_SERVICE_MANAGER)||
				(getId() == WComponentType.BACKORDER_MANAGER)||
				(getId() == WComponentType.WAREHOUSE_SUPERVISOR) ||
				((WarehouseContext.TEST_MODE==true)&&(getId() == WComponentType.WM_MSG_HANDLER))
				){
			addBus(WComponentType.SYSTEM);
		}else if((getId() == WComponentType.WM_MSG_HANDLER)||
				(getId() == WComponentType.WAREHOUSE_MANAGER_CONTROLLER)||
				(getId() == WComponentType.ROBOT_OUTPUT_MGR)||
				(getId() == WComponentType.WAREHOUSE_OUTPUT_MGR)){
			addBus(WComponentType.MANAGER_SYSTEM);
		}
		initBus();
	}

	public WComponentType getId() {
		return mId;
	}

	protected abstract void initBus();

	public void setAlias(String alias) {
		mAlias = alias;
	}

	public boolean addBus(WComponentType dest) {
		WBus bus= WBus.getBus(getId(), dest);
		if (bus== WBus.P2P_NONE){
			return false;
		}
		P2PSender sender = P2PConnection.createSender(bus);
		if ( sender == null)
			return false;
		mP2PSenderMap.put(dest.name(), sender);
		bus = WBus.getBus(dest, getId());
		P2PReceiver receiver = P2PConnection.createReceiver(bus);
		if (receiver == null)
			return false;
		receiver.setMessageListener(this);
		mP2PReceiverMap.put(dest.name(), receiver);
		return true;
	}

	public void removeBus(WComponentType dest) {
		P2PSender sender = mP2PSenderMap.remove(dest.name());
		sender.stop();

		P2PReceiver receiver = mP2PReceiverMap.remove(dest.name());
		receiver.stop();
	}

	@Override
	public void sendMsg(WComponentType dest, EventMessageType type, Serializable body) {
		logger.debug("sendMsg "+dest.name()+", "+type.name());
		P2PSender sender = mP2PSenderMap.get(dest.name());
		EventMessage em = new EventMessage(mAlias==null?getId().name():mAlias, dest.name(), type, body);
		sender.sendObject(em);
	}
	public void sendObject(EventMessage em){
		P2PSender sender = mP2PSenderMap.get(em.getDest());
		sender.sendObject(em);
	}
	protected void handleBodyError(EventMessage event){
		String errLog = event.getType()+" Has Wrong Body";
		if(WarehouseContext.DEBUG_WITH_RUNTIME_EXCEPTION)
			throw new RuntimeException(errLog);
		else
			logger.info(errLog);
	}

	public abstract void onMessage(Message message);
	protected abstract void eventHandle(EventMessage event);
}