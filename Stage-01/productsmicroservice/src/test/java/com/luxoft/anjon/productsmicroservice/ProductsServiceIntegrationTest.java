package com.luxoft.anjon.productsmicroservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")     // application-test.properties
@EmbeddedKafka(partitions=3, count=3, controlledShutdown=true)
@SpringBootTest(properties="spring.kafka.prosucer.bootstrap-servers=${spring.embedded.kafka.brokers}")
public class ProductsServiceIntegrationTest {

    @Test
    void testCreateProduct_whenGivenvalidProductDetails_successfullSendsKafkaMessage() {
        
        // Arrange

        // Act

        // Assert

    }

}
