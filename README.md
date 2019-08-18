# Spring Test Kafka

[![Build Status](https://travis-ci.com/eaxdev/Spring-Test-Kafka.svg?branch=master)](https://travis-ci.com/eaxdev/Spring-Test-Kafka)
[![codecov](https://codecov.io/gh/eaxdev/Spring-Test-Kafka/branch/master/graph/badge.svg)](https://codecov.io/gh/eaxdev/Spring-Test-Kafka)

Write your integration tests of Spring Framework with Kafka.

You can start `Apache Kafka` in `docker` (TestContainers) by the using of `@EnableKafkaTestContainer` annotation in tests:

```java
@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableKafkaTestContainer
class KafkaTestContainerTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void testKafkaFunctionality() {
        //some test with kafka
    }
}
```

You can use this annotation to start Kafka container in tests both with `JUnit5` and `JUnit4`. 
The implementation doesn’t depend on some test framework, just on the Spring Framework.
How to use multiple Kafka containers in the one test case:

```java
@SpringBootTest
@EnableKafkaTestContainer
@EnableKafkaTestContainer(kafkaBootstrapServersTargetProperty = "my.bootstrap.servers")
class MultipleContainerInOneTest {
    //some tests
}
```

This code start two `Kafka` containers. First with default `spring.kafka.bootstrap-servers` property, second with `my.bootstrap.servers`.
