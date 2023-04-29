package com.springbatchkafka.producer.service;

import com.springbatchkafka.producer.entity.FinTransaction;
import com.springbatchkafka.producer.enums.TransactionType;
import com.springbatchkafka.producer.repository.FinTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class CustomerTransactionServiceImpl implements CustomerTransactionService {

    private static final Double amountStartRange = 1000D;
    private static final Double amountEndRange = 100000D;

    private static final List<String> accountNumberList = Arrays.asList("10000000017",
            "10000000051",
            "10000000062",
            "10000000073",
            "100411925205",
            "100461449349",
            "100482675577",
            "100608312706",
            "100636783104",
            "100653709593");

    private static final List<String> debitCreditTypeList = Arrays.asList("DEBIT",
            "CREDIT");
    private static final List<TransactionType> typeList = Arrays.stream(TransactionType.values()).collect(Collectors.toList());

    private final FinTransactionRepository finTransactionRepository;
    private final KafkaTemplate<Long, FinTransaction> kafkaTemplate;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUsersInBatch(Integer transactionCount, String topic) {
        List<FinTransaction> finTransactions = this.finTransactionRepository.findAll();
        long currentSeq = finTransactions.stream().mapToLong(FinTransaction::getSequence).max().orElse(0);
        for (int index = 0; index < transactionCount; index++) {
            FinTransaction finTransaction = buildFinTransaction();
            finTransaction.setSequence(currentSeq + index + 1);
            this.finTransactionRepository.save(finTransaction);
            kafkaTemplate.send(topic, finTransaction.getSequence(), finTransaction);
            log.info("Message sent in kafka on topic:{}, message:{}", topic, finTransaction);
        }
    }



    private FinTransaction buildFinTransaction() {
        TransactionType type = this.getTransactionType();
        FinTransaction finTransaction = new FinTransaction();
        finTransaction.setAccountNumber(getRandomAccountNumber());
        finTransaction.setTransactionDate(createRandomCalendar().getTime());
        finTransaction.setTransactionId(UUID.randomUUID().toString());
        finTransaction.setAmount(getAmount());
        finTransaction.setType(getType());
        finTransaction.setTransactionCode(type.getValue());
        return finTransaction;
    }



    private String getRandomAccountNumber() {
        return this.getRandomStringFromList(accountNumberList);
    }


    private TransactionType getTransactionType() {
        return this.getRandomStringFromList(typeList);
    }

    private String getType() {
        return this.getRandomStringFromList(debitCreditTypeList);
    }

    private <T> T getRandomStringFromList(List<T> data) {
        Random random = new Random();
        int value = (random.nextInt() % (data.size() - 1));
        return data.get(Math.abs(value));
    }

    private Double getAmount() {
        Random r = new Random();
        return amountStartRange + (amountEndRange - amountStartRange) * r.nextDouble();
    }

    private Calendar createRandomCalendar() {
        int year = 2022;
        Calendar cal = Calendar.getInstance();
        Random random = new Random();

        cal.set(Calendar.HOUR, random.nextInt() % 24);
        cal.set(Calendar.MINUTE, random.nextInt() % 60);
        cal.set(Calendar.SECOND, random.nextInt() % 60);
        cal.set(Calendar.MONTH, random.nextInt() % 6);
        cal.set(Calendar.DAY_OF_MONTH, random.nextInt() % 28);
        cal.set(Calendar.YEAR, year);
        return cal;
    }
}
