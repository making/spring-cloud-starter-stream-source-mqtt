package org.springframework.cloud.stream.app.mqtt.source;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("mqtt")
public class MqttSourceProperties {
	private int recoveryInterval = 10000;
	private boolean binary = false;
	private String charset = "UTF-8";
	private boolean cleanSession = true;
	private String clientId = "scdf.mqtt.client.id.src";
	private int connectionTimeout = 30;
	private int keepAliveInterval = 60;
	private String password = "guest";
	private PersistenceType persistence = PersistenceType.MEMORY;
	private String persistenceDirectory = "/tmp/paho";
	private String[] topics = { "scdf.mqtt.test" };
	private int[] qos = { 0 };
	private String[] url = { "tcp://localhost:1883" };
	private String username = "guest";

	public static enum PersistenceType {
		MEMORY, FILE
	}

	public int getRecoveryInterval() {
		return recoveryInterval;
	}

	public void setRecoveryInterval(int recoveryInterval) {
		this.recoveryInterval = recoveryInterval;
	}

	public boolean isBinary() {
		return binary;
	}

	public void setBinary(boolean binary) {
		this.binary = binary;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public boolean isCleanSession() {
		return cleanSession;
	}

	public void setCleanSession(boolean cleanSession) {
		this.cleanSession = cleanSession;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getKeepAliveInterval() {
		return keepAliveInterval;
	}

	public void setKeepAliveInterval(int keepAliveInterval) {
		this.keepAliveInterval = keepAliveInterval;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public PersistenceType getPersistence() {
		return persistence;
	}

	public void setPersistence(PersistenceType persistence) {
		this.persistence = persistence;
	}

	public String getPersistenceDirectory() {
		return persistenceDirectory;
	}

	public void setPersistenceDirectory(String persistenceDirectory) {
		this.persistenceDirectory = persistenceDirectory;
	}

	public String[] getTopics() {
		return topics;
	}

	public void setTopics(String[] topics) {
		this.topics = topics;
	}

	public int[] getQos() {
		return qos;
	}

	public void setQos(int[] qos) {
		this.qos = qos;
	}

	public String[] getUrl() {
		return url;
	}

	public void setUrl(String[] url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
