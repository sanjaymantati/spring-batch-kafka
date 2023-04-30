package com.springbatchkafka.producer.service;

public interface TransactionService {

    void addTransactionsInBulk(Integer transactionCount, String topic);


}
