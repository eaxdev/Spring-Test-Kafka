package com.testtool.springtestkafka;

import java.lang.annotation.*;

/**
 * Created on 2019-08-16
 * <p>
 * Start a kafka docker container with a Spring Application context
 *
 * @author eaxdev
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(KafkaTestContainers.class)
public @interface KafkaTestContainer {

    /**
     * @return In this property will be set the value for establishing the initial
     * connections to the Kafka cluster after start a container
     */
    String kafkaBootstrapServersTargetProperty() default "spring.kafka.bootstrap-servers";

}