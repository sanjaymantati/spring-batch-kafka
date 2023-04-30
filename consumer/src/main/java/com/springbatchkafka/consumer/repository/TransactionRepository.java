package com.springbatchkafka.consumer.repository;

import com.springbatchkafka.consumer.entity.FinTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<FinTransaction, Long> {
}
