package com.springbatchkafka.producer.controller;

import com.springbatchkafka.producer.service.CustomerTransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class CustomerController {


    private final CustomerTransactionService customerTransactionService;
    @PostMapping
    public ResponseEntity<Object> addTransactions(@RequestParam Integer transactionCount, @RequestParam String topic){
        this.customerTransactionService.insertUsersInBatch(transactionCount, topic);
        return ResponseEntity.ok().build();
    }
}
