package com.testtool.springtestkafka.junit4;

import com.testtool.springtestkafka.KafkaTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@KafkaTestContainer
@TestPropertySource(properties = {
        "spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer",
        "spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer",
        "spring.kafka.consumer.key-serializer=org.apache.kafka.common.serialization.StringDeserializer",
        "spring.kafka.consumer.value-serializer=org.apache.kafka.common.serialization.StringDeserializer",
        "spring.kafka.consumer.group-id=test-group-id",
        "spring.kafka.consumer.auto-offset-reset=earliest",
        "spring.kafka.consumer.properties.spring.json.trusted.packages=*"
})
class KafkaTestContainerTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void readWriteValueByRedisTemplate() {
        System.out.println("spring.kafka.bootstrap-servers: " + System.getProperty("spring.kafka.bootstrap-servers"));
        kafkaTemplate.send("testTopic", "testcontainers rulezzzz");
    }

    @KafkaListener(topics = {"testTopic"}, concurrency = "1", id = "kafka-test")
    public void consumer(String message) {
        System.out.println("Message: " + message);
    }

}