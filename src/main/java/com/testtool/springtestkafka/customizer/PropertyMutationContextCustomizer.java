package com.testtool.springtestkafka.customizer;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.MergedContextConfiguration;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;
import java.util.Objects;
import java.util.Set;

/**
 * Created on 2019-08-16
 * <p>
 * The ContextCustomizer to make a different between context configurations
 * of tests where used different container ports in {@link com.testtool.springtestkafka.KafkaTestContainer} annotation.
 * In order to reload the spring context cache if it's necessary.
 *
 * @author eaxdev
 */
@Slf4j(topic = "KafkaTestContainer")
@EqualsAndHashCode(of = "descriptions")
public class PropertyMutationContextCustomizer implements ContextCustomizer {

    private Set<KafkaContainerDescription> descriptions;

    public PropertyMutationContextCustomizer(Set<KafkaContainerDescription> descriptions) {
        this.descriptions = descriptions;
    }

    @Override
    public void customizeContext(ConfigurableApplicationContext context,
                                 MergedContextConfiguration mergedConfig) {
        descriptions.forEach(description -> {
            log.info("Start Kafka TestContainer");
            KafkaContainer kafka = new KafkaContainer()
                    .withEmbeddedZookeeper()
                    .waitingFor(Wait.forListeningPort()).withStartupTimeout(Duration.ofSeconds(150));
            kafka.start();
            setSpringProperties(description, kafka);
        });
    }

    private void setSpringProperties(KafkaContainerDescription description, KafkaContainer kafka) {
        System.setProperty(description.getKafkaBootstrapServers(), kafka.getBootstrapServers());
    }

}
