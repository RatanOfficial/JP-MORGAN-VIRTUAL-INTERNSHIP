package com.jpmc.midascore.kafka;

import com.jpmc.midascore.foundation.Transaction;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class TransactionProducer {
    public static void main(String[] args) {
        String topic = "transactions";

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, Transaction> producer = new KafkaProducer<>(props);

        Transaction transaction = new Transaction(101, 202, 500.75f);
        ProducerRecord<String, Transaction> record = new ProducerRecord<String, Transaction>(topic, "txn-key",  transaction);

        producer.send(record);
        producer.close();

        System.out.println("Transaction sent successfully");
    }
}
