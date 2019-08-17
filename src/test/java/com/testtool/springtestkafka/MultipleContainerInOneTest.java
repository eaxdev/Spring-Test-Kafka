package com.testtool.springtestkafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

/**
 * Created on 2019-08-17
 *
 * @author eaxdev
 */
@SpringBootTest
@EnableKafkaTestContainer
@EnableKafkaTestContainer(kafkaBootstrapServersTargetProperty = "my.bootstrap.servers")
@TestPropertySource(properties = {
        "spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer",
        "spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer",
        "spring.kafka.consumer.key-serializer=org.apache.kafka.common.serialization.StringDeserializer",
        "spring.kafka.consumer.value-serializer=org.apache.kafka.common.serialization.StringDeserializer",
        "spring.kafka.consumer.group-id=test-group-id",
        "spring.kafka.consumer.auto-offset-reset=earliest"
})
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
class MultipleContainerInOneTest {

    private final CountDownLatch latch = new CountDownLatch(1);

    private final List<String> consumedMessages = new ArrayList<>();

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    public void testMultiplyKafkaFunctionality() throws Exception {
        try (
                KafkaProducer<String, String> producer = new KafkaProducer<>(
                        ImmutableMap.of(
                                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getProperty("my.bootstrap.servers"),
                                ProducerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString()
                        ),
                        new StringSerializer(),
                        new StringSerializer()
                );

                KafkaConsumer<String, String> consumer = new KafkaConsumer<>(
                        ImmutableMap.of(
                                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getProperty("my.bootstrap.servers"),
                                ConsumerConfig.GROUP_ID_CONFIG, "tc-" + UUID.randomUUID(),
                                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
                        ),
                        new StringDeserializer(),
                        new StringDeserializer()
                )
        ) {
            String topicName = "kafka2";
            consumer.subscribe(Collections.singletonList(topicName));

            producer.send(new ProducerRecord<>(topicName, "testcontainers", "kafka2")).get();

            Unreliables.retryUntilTrue(10, TimeUnit.SECONDS, () -> {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                if (records.isEmpty()) {
                    return false;
                }

                assertThat(records)
                        .hasSize(1)
                        .extracting(ConsumerRecord::topic, ConsumerRecord::key, ConsumerRecord::value)
                        .containsExactly(tuple(topicName, "testcontainers", "kafka2"));

                return true;
            });

            consumer.unsubscribe();
        }

        kafkaTemplate.send("kafka1", "testcontainers kafka1");
        Unreliables.retryUntilTrue(10, TimeUnit.SECONDS, () -> {
            if (latch.getCount() > 0) {
                return false;
            }

            assertThat(consumedMessages)
                    .hasSize(1)
                    .extracting(s -> s)
                    .containsExactly("testcontainers kafka1");

            return true;
        });
    }

    @KafkaListener(topics = {"kafka1"}, concurrency = "1", id = "kafka1-test")
    public void consumer(String message) {
        consumedMessages.add(message);
        latch.countDown();
    }
}
