package com.springbatchkafka.producer.repository;

import com.springbatchkafka.producer.entity.FinTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface FinTransactionRepository extends JpaRepository<FinTransaction,Long> {
}
