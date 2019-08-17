package com.testtool.springtestkafka;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableKafkaTestContainer
@TestPropertySource(properties = {
        "spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer",
        "spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer",
        "spring.kafka.consumer.key-serializer=org.apache.kafka.common.serialization.StringDeserializer",
        "spring.kafka.consumer.value-serializer=org.apache.kafka.common.serialization.StringDeserializer",
        "spring.kafka.consumer.group-id=test-group-id",
        "spring.kafka.consumer.auto-offset-reset=earliest"
})
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
class KafkaTestContainerTest {

    private final CountDownLatch latch = new CountDownLatch(1);

    private final List<String> consumedMessages = new ArrayList<>();

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void testKafkaFunctionality() {
        kafkaTemplate.send("testTopic", "testcontainers rulezzzz");
        Unreliables.retryUntilTrue(10, TimeUnit.SECONDS, () -> {
            if (latch.getCount() > 0) {
                return false;
            }

            assertThat(consumedMessages)
                    .hasSize(1)
                    .extracting(s -> s)
                    .containsExactly("testcontainers rulezzzz");

            return true;
        });
    }

    @KafkaListener(topics = {"testTopic"}, concurrency = "1", id = "kafka-test")
    public void consumer(String message) {
        consumedMessages.add(message);
        latch.countDown();
    }
}