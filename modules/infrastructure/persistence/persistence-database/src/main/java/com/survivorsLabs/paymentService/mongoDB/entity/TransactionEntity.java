package com.survivorsLabs.paymentService.mongoDB.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("transactions")
public class TransactionMongoEntity {
    @Id
    private String id;
    private Date creationDate;
    private PayerEntity payer;
    private CardEntity card;
    private double value;
    private String currency;
    private String deviceSessionId;
    private String ipAddress;
    private String cookie;
    private String userAgent;
}



