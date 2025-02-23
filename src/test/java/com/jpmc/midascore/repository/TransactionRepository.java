package com.jpmc.midascore.repository;

import com.jpmc.midascore.model.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionRecord, Long> {
}
