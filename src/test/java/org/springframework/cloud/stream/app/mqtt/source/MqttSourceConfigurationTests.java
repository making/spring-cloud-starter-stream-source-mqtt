package org.springframework.cloud.stream.app.mqtt.source;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.Bindings;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.messaging.Message;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DirtiesContext
public abstract class MqttSourceConfigurationTests {

	@Autowired
	@Bindings(MqttSourceConfiguration.class)
	protected Source source;

	@Autowired
	protected MessageCollector messageCollector;

	@Autowired
	protected MqttPahoClientFactory mqttPahoClientFactory;

	@Autowired
	protected MqttSourceProperties props;

	protected IMqttClient client;

	@Before
	public void setUp() throws Exception {
		client = new DefaultMqttPahoClientFactory().getClientInstance(props.getUrl()[0],
				"test-client");
		client.connect();
	}

	@After
	public void tearDown() throws Exception {
		client.disconnect();
		client.close();
	}

	@SpringBootTest(classes = MqttSourceConfigurationTests.MqttSourceApplication.class, properties = {
			"mqtt.url=tcp://iot.eclipse.org:1883",
			"logging.level.org.eclipse.paho.client=DEBUG",
			"logging.level.org.springframework.integration.mqtt=DEBUG" })
	public static class TextMessageTest extends MqttSourceConfigurationTests {
		@Test
		public void contextLoads() throws Exception {
			MqttMessage mqttMessage = new MqttMessage();
			mqttMessage.setPayload("MQTT!".getBytes());
			client.publish(props.getTopics()[0], mqttMessage);

			Message<?> received = messageCollector.forChannel(source.output()).poll(5,
					TimeUnit.SECONDS);

			assertThat(received).isNotNull();
			assertThat(received.getPayload()).isEqualTo("MQTT!");
			assertThat(received.getHeaders().get("mqtt_topic"))
					.isEqualTo("scdf.mqtt.test");
			assertThat(received.getHeaders().get("mqtt_qos")).isEqualTo(0);
		}
	}

	@SpringBootTest(classes = MqttSourceConfigurationTests.MqttSourceApplication.class, properties = {
			"mqtt.url=tcp://iot.eclipse.org:1883", "mqtt.binary=true",
			"logging.level.org.eclipse.paho.client=DEBUG",
			"logging.level.org.springframework.integration.mqtt=DEBUG" })
	public static class ByteMessageTest extends MqttSourceConfigurationTests {
		@Test
		public void contextLoads() throws Exception {
			MqttMessage mqttMessage = new MqttMessage();
			mqttMessage.setPayload(new byte[] { 0, 1, 2, 3 });
			client.publish(props.getTopics()[0], mqttMessage);

			Message<?> received = messageCollector.forChannel(source.output()).poll(5,
					TimeUnit.SECONDS);

			assertThat(received).isNotNull();
			assertThat(received.getPayload()).isEqualTo(new byte[] { 0, 1, 2, 3 });
			assertThat(received.getHeaders().get("mqtt_topic"))
					.isEqualTo("scdf.mqtt.test");
			assertThat(received.getHeaders().get("mqtt_qos")).isEqualTo(0);
		}
	}

	@SpringBootTest(classes = MqttSourceConfigurationTests.MqttSourceApplication.class, properties = {
			"mqtt.url=tcp://iot.eclipse.org:1883", "mqtt.topics=test",
			"logging.level.org.eclipse.paho.client=DEBUG",
			"logging.level.org.springframework.integration.mqtt=DEBUG" })
	public static class ChangeTopicTest extends MqttSourceConfigurationTests {
		@Test
		public void contextLoads() throws Exception {
			MqttMessage mqttMessage = new MqttMessage();
			mqttMessage.setPayload("MQTT!".getBytes());
			client.publish(props.getTopics()[0], mqttMessage);

			Message<?> received = messageCollector.forChannel(source.output()).poll(5,
					TimeUnit.SECONDS);

			assertThat(received).isNotNull();
			assertThat(received.getPayload()).isEqualTo("MQTT!");
			assertThat(received.getHeaders().get("mqtt_topic")).isEqualTo("test");
			assertThat(received.getHeaders().get("mqtt_qos")).isEqualTo(0);
		}
	}

	@SpringBootApplication
	public static class MqttSourceApplication {
	}
}
