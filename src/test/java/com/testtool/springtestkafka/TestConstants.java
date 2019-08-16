package com.testtool.springtestkafka;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class TestConstants {

    public static String[] getBaseKafkaProperties() {
        List<String> props = new ArrayList<>();
        props.add("spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer");
        props.add("spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer");
        props.add("spring.kafka.consumer.key-serializer=org.apache.kafka.common.serialization.StringDeserializer");
        props.add("spring.kafka.consumer.value-serializer=org.apache.kafka.common.serialization.StringDeserializer");
        props.add("spring.kafka.consumer.group-id=test-group-id");
        props.add("spring.kafka.consumer.auto-offset-reset=earliest");
        props.add("spring.kafka.consumer.properties.spring.json.trusted.packages=*");
        return props.toArray(new String[0]);
    }
}
