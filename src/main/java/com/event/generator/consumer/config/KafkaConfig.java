package com.event.generator.consumer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains all the configuration information for Kafka producer factory to be
 * able to create a Kafka template for publishing messages
 */
@Configuration
@Slf4j
public class KafkaConfig {

    private static final String SECURITY_PROTOCOL = "security.protocol";
    private static final String SASL_MECHANISM = "sasl.mechanism";
    private static final String SASL_JAAS_CONFIG = "sasl.jaas.config";
    private static final String SASL_TRUSTSTORE_LOCATION = "ssl.truststore.location";
    private static final String SASL_TRUSTSTORE_PWORD = "ssl.truststore.password";

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${kafka.username:}")
    private String username;
    @Value("${kafka.password:}")
    private String password;
    @Value("${kafka.login-module}")
    private String loginModule;
    @Value("${kafka.sasl-mechanism}")
    private String saslMechanism;
    @Value("${kafka.security-protocol}")
    private String securityProtocol;
    @Value("${kafka.truststore-location:}")
    private String truststoreLocation;
    @Value("${kafka.truststore-password:}")
    private String truststorePassword;
    @Value("${kafka.consumer.group}")
    private String consumerGroup;
    @Value("${kafka.consumer.offset-auto-reset}")
    private String consumerOffsetAutoReset;
    @Value("${kafka.consumer.concurrency}")
    private int consumerConcurrency;
    @Value("${kafka.producer.acks-config:all}")
    private String producerAcksConfig;
    @Value("${kafka.producer.linger:1}")
    private int producerLinger;
    @Value("${kafka.producer.timeout:30000}")
    private int producerRequestTimeout;
    @Value("${kafka.producer.batch-size:16384}")
    private int producerBatchSize;
    @Value("${kafka.producer.send-buffer:131072}")
    private int producerSendBuffer;
    @Value("${kafka.clientId:product-processor-producer}")
    private String kafkaClientId;

    /**
     * This method is used to add SASL properties.
     *
     * @param properties       of type Map.
     * @param saslMechanism    of type String.
     * @param securityProtocol of type String.
     * @param loginModule      of type String.
     * @param username         of type String.
     * @param password         of type String.
     */
    public static void addSaslProperties(Map<String, Object> properties, String saslMechanism, String securityProtocol, String loginModule, String username, String password) {
        if (!ObjectUtils.isEmpty(username)) {
            properties.put(SECURITY_PROTOCOL, securityProtocol);
            properties.put(SASL_MECHANISM, saslMechanism);
            properties.put(SASL_JAAS_CONFIG, String.format("%s required username=\"%s\" password=\"%s\" ;", loginModule, username, password));
        }
    }

    /**
     * This method is used to store trust store properties.
     *
     * @param properties of type Map.
     * @param location   of type String.
     * @param password   of type String.
     */
    private static void addTruststoreProperties(Map<String, Object> properties, String location, String password) {
        if (!ObjectUtils.isEmpty(location)) {
            properties.put(SASL_TRUSTSTORE_LOCATION, location);
            properties.put(SASL_TRUSTSTORE_PWORD, password);
        }
    }

    /**
     * This method provides consumer configuration detail.
     *
     * @return of type ConsumerFactory.
     */
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerOffsetAutoReset);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        addSaslProperties(properties, saslMechanism, securityProtocol, loginModule, username, password);
        addTruststoreProperties(properties, truststoreLocation, truststorePassword);
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, producerLinger);
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, producerRequestTimeout);
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, producerBatchSize);
        properties.put(ProducerConfig.SEND_BUFFER_CONFIG, producerBatchSize);
        properties.put(ProducerConfig.ACKS_CONFIG, producerAcksConfig);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaClientId);
        addSaslProperties(properties, saslMechanism, securityProtocol, loginModule, username, password);
        addTruststoreProperties(properties, truststoreLocation, truststorePassword);

        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public StringJsonMessageConverter stringJsonMessageConverter(ObjectMapper mapper) {
        return new StringJsonMessageConverter(mapper);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            StringJsonMessageConverter messageConverter) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(consumerConcurrency);
        factory.setStatefulRetry(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setSyncCommits(false);
        return factory;
    }

    @Bean
    public KafkaTemplate kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
