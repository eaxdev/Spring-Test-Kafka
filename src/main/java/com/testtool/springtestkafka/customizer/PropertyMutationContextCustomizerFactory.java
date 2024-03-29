package com.testtool.springtestkafka.customizer;

import com.testtool.springtestkafka.EnableKafkaTestContainer;
import com.testtool.springtestkafka.EnableKafkaTestContainers;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created on 2019-08-16
 * @author eaxdev
 */
public class PropertyMutationContextCustomizerFactory implements ContextCustomizerFactory {

    @Override
    public ContextCustomizer createContextCustomizer(Class<?> testClass,
                                                     List<ContextConfigurationAttributes> configAttributes) {

        Set<EnableKafkaTestContainer> annotations = AnnotationUtils.getRepeatableAnnotations(testClass,
                EnableKafkaTestContainer.class,
                EnableKafkaTestContainers.class);
        Set<KafkaContainerDescription> descriptions = annotations.stream()
                .map(a -> new KafkaContainerDescription(a.kafkaBootstrapServersTargetProperty()))
                .collect(Collectors.toSet());
        return new PropertyMutationContextCustomizer(descriptions);
    }

}
