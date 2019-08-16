package com.testtool.springtestkafka;

import java.lang.annotation.*;

/**
 * Created on 2019-08-16
 * <p>
 * This annotation provide an ability to use @{@link KafkaTestContainer}
 * annotation as repeatable annotation.
 *
 * @author eaxdev
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface KafkaTestContainers {

    KafkaTestContainer[] value();

}
