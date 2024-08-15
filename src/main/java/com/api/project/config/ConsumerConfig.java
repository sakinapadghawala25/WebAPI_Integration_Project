package com.api.project.config;



import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class ConsumerConfig {

    private final static String BOOTSTRAP_SERVERS = "localhost:9092";
    private final static String GROUP_ID = "teams_group";
    private static final Object BOOTSTRAP_SERVERS_CONFIG = "";
    private static final Object GROUP_ID_CONFIG = "";
    private static final Object KEY_DESERIALIZER_CLASS_CONFIG = "";
    private static final Object VALUE_DESERIALIZER_CLASS_CONFIG = "";

    public static Properties getConsumerProps() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return props;
    }
}