# spring-batch-kafka

* Introduction : Here we're trying to process messages from kafka from particular topic through Spring batch. The producer produces messages in Kafka topic and the consumer consumes messages from topic in batch and process them.

## Modules
1. A generic Kafka producer (producer)
    * Configurations
        * JPA
            * MySQL Datasource 
            * JpaAuditing   
            * JPARepository for entities.
        * ProducerFactory and KafkaTemplate
2. Kafka consumer with spring batch and Spring boot
    * Configuration 
        * JPA
            * MySQL Datasource 
            * JPARepository for entities.
        * Batch processing
            * Job 
            * Job Steps
            * Kafka Item Reader
            * Item Processor
            * Item Writer
        * Scheduler


## Flow
1. There is an API in producer module that create random transactions in bulk and store it in **transaction_master** table and produce messages in kafka in provided topic.
```cURL
curl --location 'http://localhost:8080' \
--form 'transactionCount="10000"' \
--form 'topic="transaction_insertion"'
```
2. The consumer module has one scheduler with fixed delay of 5 seconds. This scheduler executes a spring batch job that read messages from the topic and calculate and update charges in **transaction_master** table.



## References
1. https://www.youtube.com/watch?v=UJesCn731G4
2. https://www.youtube.com/watch?v=enqqp2ZIEyE&t=250s
3. http://selftuts.in/install-kafka-and-kafka-manger-using-docker-compose/
4. https://stackoverflow.com/questions/74227651/spring-batch-kafkaitemreader-reads-from-offset-0-if-the-jvm-is-not-restarted