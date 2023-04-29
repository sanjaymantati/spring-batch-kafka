package com.springbatchkafka.producer.service;

public interface CustomerTransactionService {

    void insertUsersInBatch(Integer transactionCount, String topic);


}
