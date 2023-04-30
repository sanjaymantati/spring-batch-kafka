package com.springbatchkafka.producer.controller;

import com.springbatchkafka.producer.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class TransactionController {


    private final TransactionService TransactionService;

    @PostMapping
    public ResponseEntity<Object> addTransactionsInBulk(@RequestParam Integer transactionCount, @RequestParam String topic){
        this.TransactionService.addTransactionsInBulk(transactionCount, topic);
        return ResponseEntity.ok().build();
    }
}
