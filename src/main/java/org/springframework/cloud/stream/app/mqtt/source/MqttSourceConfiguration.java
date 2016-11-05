package org.springframework.cloud.stream.app.mqtt.source;

import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttMessageConverter;

@EnableBinding(Source.class)
@EnableConfigurationProperties(MqttSourceProperties.class)
public class MqttSourceConfiguration {
	@Autowired
	MqttSourceProperties props;

	@Autowired
	Source source;

	@Bean
	MqttMessageConverter mqttMessageConverter() {
		DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
		converter.setPayloadAsBytes(props.isBinary());
		return converter;
	}

	@Bean
	MqttPahoClientFactory mqttPahoClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setUserName(props.getUsername());
		factory.setPassword(props.getPassword());
		factory.setServerURIs(props.getUrl());
		factory.setCleanSession(props.isCleanSession());
		factory.setConnectionTimeout(props.getConnectionTimeout());
		factory.setKeepAliveInterval(props.getKeepAliveInterval());
		switch (props.getPersistence()) {
		case MEMORY:
			factory.setPersistence(new MemoryPersistence());
			break;
		case FILE:
			factory.setPersistence(
					new MqttDefaultFilePersistence(props.getPersistenceDirectory()));
			break;
		}
		return factory;
	}

	@Bean
	MqttPahoMessageDrivenChannelAdapter mqttPahoMessageDrivenChannelAdapter() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
				props.getClientId(), mqttPahoClientFactory(), props.getTopics());
		adapter.setAutoStartup(false);
		adapter.setOutputChannel(source.output());
		adapter.setQos(props.getQos());
		adapter.setRecoveryInterval(props.getRecoveryInterval());
		adapter.setConverter(mqttMessageConverter());
		return adapter;
	}
}
