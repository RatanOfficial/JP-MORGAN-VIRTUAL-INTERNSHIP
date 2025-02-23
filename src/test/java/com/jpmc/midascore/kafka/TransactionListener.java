package com.jpmc.midascore.kafka;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.model.TransactionRecord;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TransactionListener {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core-group")
    @Transactional
    public void listen(Transaction transaction) {
        Optional<UserRecord> senderOpt = Optional.ofNullable(userRepository.findById(transaction.getSenderId()));
        Optional<UserRecord> recipientOpt = Optional.ofNullable(userRepository.findById(transaction.getRecipientId()));

        if (senderOpt.isPresent() && recipientOpt.isPresent()) {
            UserRecord sender = senderOpt.get();
            UserRecord recipient = recipientOpt.get();

            if (sender.getBalance() >= transaction.getAmount()) {
                // Deduct from sender and add to recipient
                sender.setBalance(sender.getBalance() - transaction.getAmount());
                recipient.setBalance(recipient.getBalance() + transaction.getAmount());

                // Save updated users
                userRepository.save(sender);
                userRepository.save(recipient);

                // Save transaction record
                TransactionRecord record = new TransactionRecord(sender, recipient, transaction.getAmount());
                transactionRepository.save(record);

                System.out.println("✅ Transaction recorded: " + record.getSender() + " Balance: " + transaction.getAmount());
            } else {
                System.out.println("❌ Transaction failed: Insufficient balance for sender ID " + sender.getId());
            }
        } else {
            System.out.println("❌ Transaction failed: Invalid sender or recipient ID.");
        }
    }
}
