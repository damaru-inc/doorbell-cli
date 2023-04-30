
package com.damaru.doorbell;
import java.util.HashMap;

public class DataMessage {


	// topic and messageId: These fields allow the client to see the topic
	// and messageId of a received messages. It is not necessary to set these 
	// when publishing.

	private String topic;

	public String getTopic() {
		return topic;
	}

	public DataMessage setTopic(String topic) {
		this.topic = topic;
		return this;
	}

	private String messageId;

	public String getMessageId() {
		return messageId;
	}

	public DataMessage setMessageId(String messageId) {
		this.messageId = messageId;
		return this;
	}

	// Headers with their getters and setters.
	private HashMap<String, Object> headers = new HashMap<>();

	// Payload


	private String message;

	public String getPayload() {
		return message;
	}

	public DataMessage setPayload(String message) {
		this.message = message;
		return this;
	}

	// Listers

	public interface SubscribeListener {
		public void onReceive(DataMessage dataMessage);
		public void handleException(Exception exception);
	}
}
