package com.springbatchkafka.consumer.config;

import com.springbatchkafka.consumer.dto.TransactionDto;
import com.springbatchkafka.consumer.entity.FinTransaction;
import com.springbatchkafka.consumer.processor.TransactionItemProcessor;
import com.springbatchkafka.consumer.repository.TransactionRepository;
import com.springbatchkafka.consumer.writer.TransactionItemWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.kafka.KafkaItemReader;
import org.springframework.batch.item.kafka.builder.KafkaItemReaderBuilder;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;


@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ConsumerBatchConfig {
    private final JobBuilderFactory jobBuilders;
    private final StepBuilderFactory stepBuilders;
    private final JobLauncher jobLauncher;
    private final KafkaProperties properties;
    private final TransactionRepository transactionRepository;

    @Bean
    public Job finTransactionReportJob() {
        return jobBuilders.get("finTransactionReportJob")
                .incrementer(new RunIdIncrementer())
                .start(startStep())
                .build();
    }

    public Step startStep() {
        return stepBuilders.get("startStep")
                .<TransactionDto, FinTransaction>chunk(1000)
                .reader(kafkaItemReader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Scheduled(fixedDelay = 5000)
    public void run() throws Exception {
        log.info("Scheduler is started at {}", Calendar.getInstance().getTime());

        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();
        JobExecution execution = jobLauncher.run(
                finTransactionReportJob(),
                params
        );
        log.info("Job started at:{} and ended at:{}", execution.getCreateTime(), execution.getEndTime());
        log.info("Scheduler is completed at {}", Calendar.getInstance().getTime());
    }


    @Bean
    @StepScope
    KafkaItemReader<Long, TransactionDto> kafkaItemReader() {
        Properties props = new Properties();
        props.putAll(this.properties.buildConsumerProperties());
        return new KafkaItemReaderBuilder<Long, TransactionDto>()
                .partitions(0,1,2,3,4,5,6,7,8,9)
                .consumerProperties(props)
                .name("customers-reader")
                .saveState(true)
                .topic("transaction_insertion")
                .partitionOffsets(new HashMap<>())
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<TransactionDto, FinTransaction> processor() {
        return new TransactionItemProcessor(transactionRepository);
    }
    @Bean
    public ItemWriter<FinTransaction> writer() {
        return new TransactionItemWriter(transactionRepository);
    }

}
