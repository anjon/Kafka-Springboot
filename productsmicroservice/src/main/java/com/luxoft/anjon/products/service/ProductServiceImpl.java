package com.luxoft.anjon.products.service;

import java.util.UUID;
// import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.luxoft.anjon.products.rest.CreateProductRestModel;

@Service
public class ProductServiceImpl implements ProductService{

    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
    private final Logger LOGGGER = LoggerFactory.getLogger(this.getClass());

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(CreateProductRestModel productRestModel) throws Exception{

        String productID = UUID.randomUUID().toString();

        // TODO: Persist product details into database table before publishing an event
        
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productID, 
        productRestModel.getTitle(),
        productRestModel.getPrice(),
        productRestModel.getQuantity());

        // Comented section is for the Asynschoronouus opration
        // CompletableFuture<SendResult<String, ProductCreatedEvent>> future = 
        //     kafkaTemplate.send("product-created-events-topic", productID, productCreatedEvent);

        // future.whenComplete((result, exception) -> {
        //     if(exception != null) {
        //         LOGGGER.error("*** Failed to send message: " + exception.getMessage());
        //     } else {
        //         LOGGGER.info("*** Message sent successfully: " + result.getRecordMetadata());
        //     }
        // });

        LOGGGER.info("Before publishing a ProductCreatedEvent");

        SendResult<String, ProductCreatedEvent> result = 
        kafkaTemplate.send("product-created-events-topic", productID, productCreatedEvent).get();

        LOGGGER.info("Topic: "+ result.getRecordMetadata().topic());
        LOGGGER.info("Partition: " + result.getRecordMetadata().partition());
        LOGGGER.info("Offset: " + result.getRecordMetadata().offset());

        LOGGGER.info("*** Returning Product ID");
        return productID;
    }

}
