package com.springbatchkafka.producer.config;

import com.springbatchkafka.producer.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
@RequiredArgsConstructor
class KafkaProducerConfig {
    private final KafkaProperties properties;

    @Bean
    public ProducerFactory<Long, TransactionDto> producerFactory() {
        return new DefaultKafkaProducerFactory<>(this.properties.buildProducerProperties());
    }

    @Bean
    public KafkaTemplate<Long, TransactionDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
