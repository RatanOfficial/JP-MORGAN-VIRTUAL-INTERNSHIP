package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserRecord, Long> {
    UserRecord findById(long id);
}
