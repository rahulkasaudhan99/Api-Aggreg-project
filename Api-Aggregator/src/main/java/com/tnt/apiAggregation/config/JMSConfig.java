package com.tnt.apiAggregation.config;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableJms
public class JMSConfig {

	 @Value("${spring.activemq.broker-url}")
	    String AMQ_URL;
	    @Value("${spring.activemq.user}")
	    String AMQ_USERNAME;
	    @Value("${spring.activemq.password}")
	    String AMQ_PASSWORD;

	    @Bean
	    public ActiveMQConnectionFactory connectionFactory(){
	        ActiveMQConnectionFactory connectionFactory = new  ActiveMQConnectionFactory();
	        connectionFactory.setBrokerURL(AMQ_URL);
	        connectionFactory.setPassword(AMQ_USERNAME);
	        connectionFactory.setUserName(AMQ_PASSWORD);
	        connectionFactory.setTrustAllPackages(true);
	        return connectionFactory;
	    }

	    @Bean
	    public MessageConverter messageConverter() {
	        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
	        converter.setTargetType(MessageType.TEXT);
	        converter.setObjectMapper(objectMapper());
	        return converter;
	    }

	    @Bean
	    public ObjectMapper objectMapper() {
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.registerModule(new JavaTimeModule());
	        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	        return mapper;
	    }
	    
	    @Bean
	    public MessageConverter jacksonJmsMessageConverter() {
	        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
	        converter.setTargetType(MessageType.TEXT);
	        converter.setTypeIdPropertyName("_type");
	        return converter;
	    }

	   
	    @Bean
	    public JmsTemplate jmsTemplate(){
	        JmsTemplate template = new JmsTemplate();
	        template.setConnectionFactory(connectionFactory());
	        template.setPubSubDomain(true);
	        template.setDeliveryPersistent(true);
	        template.setDestinationResolver(destinationResolver());
	        template.setMessageConverter(jacksonJmsMessageConverter());
	        template.setReceiveTimeout(500);
	        return template;
	    }

	    @Bean
	    DynamicDestinationResolver destinationResolver() {
	        return new DynamicDestinationResolver() {
	            @Override
	            public Destination resolveDestinationName(Session session, String destinationName, boolean pubSubDomain) throws JMSException {
	                if(destinationName.endsWith("Topic")) pubSubDomain = true;
	                else pubSubDomain = false;
	                return super.resolveDestinationName(session,destinationName,pubSubDomain);
	            }
	        };
	    }
}
