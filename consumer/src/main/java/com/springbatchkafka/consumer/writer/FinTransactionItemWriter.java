package com.springbatchkafka.consumer.writer;

import com.springbatchkafka.consumer.entity.FinTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
public class FinTransactionItemWriter implements ItemWriter<FinTransaction> {
    @Override
    public void write(List<? extends FinTransaction> list) {
        for (FinTransaction item : list) {
            log.info("Item : {}", item.toString());
        }
    }
}
