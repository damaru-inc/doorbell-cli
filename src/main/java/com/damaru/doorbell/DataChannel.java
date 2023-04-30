
package com.damaru.doorbell;

import com.solacesystems.jcsmp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class DataChannel {

	private static final Logger log = LoggerFactory.getLogger(DataChannel.class);
	// Channel name: temperature/data
	private static final String SUBSCRIBE_TOPIC = "proximity/>";

	@Autowired
	private SolaceSession solaceSession;
	private JCSMPSession jcsmpSession;
	private XMLMessageConsumer consumer;


	@PostConstruct
	public void init() throws Exception {
		jcsmpSession = solaceSession.getSession();
	}

	public void subscribe(DataMessage.SubscribeListener listener) throws Exception {
		System.out.println("got a listener: " + listener);
		MessageListener messageListener = new MessageListener(listener);
		ReconnectHandler reconnectHandler = new ReconnectHandler();
		consumer = jcsmpSession.getMessageConsumer(reconnectHandler, messageListener);
		Topic topic = JCSMPFactory.onlyInstance().createTopic(SUBSCRIBE_TOPIC);
		jcsmpSession.addSubscription(topic);
		consumer.start();
	}


	public void close() {

		if (consumer != null) {
			consumer.close();		
		}

		solaceSession.close();
	}


	class MessageListener implements XMLMessageListener {

		DataMessage.SubscribeListener listener;
		
		public MessageListener(DataMessage.SubscribeListener listener) {
			this.listener = listener;
		}
		
		@Override
		public void onException(JCSMPException exception) {
			listener.handleException(exception);
		}

		@Override
		public void onReceive(BytesXMLMessage bytesXMLMessage) {


			String messageText = null;

			if (bytesXMLMessage instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) bytesXMLMessage;
				messageText = textMessage.getText();
			} else if (bytesXMLMessage instanceof BytesMessage) {
				BytesMessage bytesMessage = (BytesMessage) bytesXMLMessage;
				messageText = new String(bytesMessage.getData());
			}

			//System.out.println("Got a message: " + bytesXMLMessage.getClass() + " " + messageText);
			try {
				DataMessage  dataMessage = new DataMessage();
				dataMessage.setMessageId(bytesXMLMessage.getMessageId());
				dataMessage.setPayload(messageText);
				dataMessage.setTopic(bytesXMLMessage.getDestination().getName());
				listener.onReceive(dataMessage);
			} catch (Exception exception) {
				listener.handleException(exception);
			}			
		}
	}

	class ReconnectHandler implements JCSMPReconnectEventHandler {

		@Override
		public boolean preReconnect() throws JCSMPException {
			return true;
		}

		@Override
		public void postReconnect() throws JCSMPException {
			log.info("postReconnect");
			Topic topic = JCSMPFactory.onlyInstance().createTopic(SUBSCRIBE_TOPIC);
			jcsmpSession.addSubscription(topic);
		}
	}

}

