package com.testtool.springtestkafka;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 2019-08-16
 * <p>
 * This annotation provide an ability to use @{@link EnableKafkaTestContainer}
 * annotation as repeatable annotation.
 *
 * @author eaxdev
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableKafkaTestContainers {

    EnableKafkaTestContainer[] value();

}
