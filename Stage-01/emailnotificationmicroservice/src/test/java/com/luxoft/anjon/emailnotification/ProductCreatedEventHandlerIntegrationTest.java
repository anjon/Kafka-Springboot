package com.luxoft.anjon.emailnotification;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.web.client.RestTemplate;

import com.luxoft.anjon.core.ProductCreatedEvent;
import com.luxoft.anjon.emailnotification.io.ProcessedEventEntity;
import com.luxoft.anjon.emailnotification.io.ProcessedEventRepository;

@EmbeddedKafka
@SpringBootTest(properties = "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}")
public class ProductCreatedEventHandlerIntegrationTest {

    @MockBean
    ProcessedEventRepository processedEventRepository;

    @MockBean
    RestTemplate restTemplate;

    @Test
    public void testProductCreatedEventHandler_OnProductCreated_HandlersEvent() {
        // Arrange
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
        productCreatedEvent.setProductId(UUID.randomUUID().toString());
        productCreatedEvent.setTitle("Test Product");
        productCreatedEvent.setPrice(new BigDecimal(10));
        productCreatedEvent.setQuantity(1);

        String messageId = UUID.randomUUID().toString();
        String messageKey = productCreatedEvent.getProductId();

        ProducerRecord<String, ProductCreatedEvent> record = new ProducerRecord<String, ProductCreatedEvent>(
                "product-created-events-topic", messageKey,
                productCreatedEvent);

        record.headers().add("messageId", messageId.getBytes());
        record.headers().add(KafkaHeaders.RECEIVED_KEY, messageKey.getBytes());

        ProcessedEventEntity processedEventEntity = new ProcessedEventEntity();
        when(processedEventRepository.findByMessageId(anyString())).thenReturn(processedEventEntity);
        when(processedEventRepository.save(any(ProcessedEventEntity.class))).thenReturn(null);

        String responseBody = "{\"key\":\"value\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, headers, HttpStatus.OK);

        when(restTemplate.exchange(any(String.class), 
                any(HttpMethod.class), 
                isNull(), 
                eq(String.class)))
        .thenReturn(responseEntity);
        
        // Act

        // Assert
    }
}
