package com.springbatchkafka.consumer.writer;

import com.springbatchkafka.consumer.entity.FinTransaction;
import com.springbatchkafka.consumer.enums.TransactionType;
import com.springbatchkafka.consumer.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class TransactionItemWriter implements ItemWriter<FinTransaction> {
    private final TransactionRepository transactionRepository;

    @Override
    public void write(List<? extends FinTransaction> list) {
        Map<String, Float> valuePercentageMap = Arrays.stream(TransactionType.values()).collect(Collectors.toMap(r-> r.getValue(), r-> r.getPercentage()));
        for (FinTransaction finTransaction : list) {
            if (finTransaction.getTxCharge() != null)
                throw new IllegalArgumentException("TxCharge is alread available in : " + finTransaction);
            finTransaction.setTxCharge(finTransaction.getAmount() * valuePercentageMap.getOrDefault(finTransaction.getTransactionCode(), 1f));
            finTransaction.setTxCalculatedAt(Calendar.getInstance().getTime());
            transactionRepository.save(finTransaction);
            log.info("Item : {}", finTransaction);
        }
    }
}
