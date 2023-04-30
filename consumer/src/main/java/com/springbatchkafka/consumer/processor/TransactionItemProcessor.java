package com.springbatchkafka.consumer.processor;

import com.springbatchkafka.consumer.dto.TransactionDto;
import com.springbatchkafka.consumer.entity.FinTransaction;
import com.springbatchkafka.consumer.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;


@RequiredArgsConstructor
public class TransactionItemProcessor implements ItemProcessor<TransactionDto, FinTransaction> {

    private final TransactionRepository transactionRepository;

    @Override
    public FinTransaction process(TransactionDto input) throws Exception {
        FinTransaction transaction =  transactionRepository.findById(input.getTransactionId()).orElse(null);
        return transaction;

    }

}