package com.testtool.springtestkafka.customizer;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Created on 2019-08-16
 * @author eaxdev
 */
@Getter
@EqualsAndHashCode(of = "kafkaBootstrapServers")
public class KafkaContainerDescription {

    private String kafkaBootstrapServers;

    public KafkaContainerDescription(String kafkaBootstrapServers) {
        this.kafkaBootstrapServers = kafkaBootstrapServers;
    }

}
