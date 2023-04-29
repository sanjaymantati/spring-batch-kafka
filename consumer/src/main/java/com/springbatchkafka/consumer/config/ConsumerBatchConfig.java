package com.springbatchkafka.consumer.config;

import com.springbatchkafka.consumer.entity.FinTransaction;
import com.springbatchkafka.consumer.writer.FinTransactionItemWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.kafka.KafkaItemReader;
import org.springframework.batch.item.kafka.builder.KafkaItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
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

    @Bean
    public Job finTransactionReportJob() {
        return jobBuilders.get("finTransactionReportJob")
                .incrementer(new RunIdIncrementer())
                .start(startStep())
                .next(chunkStep())
                .build();
    }

    public Step chunkStep() {
        return stepBuilders.get("chunkStep")
                .<FinTransaction, FinTransaction>chunk(1000)
                .reader(kafkaItemReader())
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
    KafkaItemReader<Long, FinTransaction> kafkaItemReader() {
        Properties props = new Properties();
//        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        props.putAll(this.properties.buildConsumerProperties());
        return new KafkaItemReaderBuilder<Long, FinTransaction>()
                .partitions(0,1,2,3,4,5,6,7,8,9)
                .consumerProperties(props)
                .name("customers-reader")
                .saveState(true)
                .topic("fin_transaction_insertion")
                .partitionOffsets(new HashMap<>())
                .build();
    }
    @Bean
    public Step startStep() {
        return stepBuilders.get("startStep")
                .<FinTransaction, FinTransaction>chunk(10)
                .writer(writer())
                .reader(kafkaItemReader())
                .build();
    }

    @Bean
    public ItemWriter<FinTransaction> writer() {
        return new FinTransactionItemWriter();
    }

    @Bean
    public Tasklet tasklet() {
        return (contribution, chunkContext) -> {
            return RepeatStatus.FINISHED;
        };
    }
}
