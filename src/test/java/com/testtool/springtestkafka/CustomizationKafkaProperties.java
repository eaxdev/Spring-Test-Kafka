package com.testtool.springtestkafka;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 2019-08-17
 *
 * @author eaxdev
 */
@SpringBootTest
@EnableKafkaTestContainer(kafkaBootstrapServersTargetProperty = "my.bootstrap.servers")
class CustomizationKafkaProperties {

    @Test
    void testPropertiesAfterStartContext() {
        assertThat(System.getProperty("my.bootstrap.servers")).isNotEmpty();
        assertThat(System.getProperty("my.bootstrap.servers")).contains("PLAINTEXT://localhost:");
    }
}
