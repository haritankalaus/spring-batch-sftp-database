package com.example.batchdemo.domain.repository;

import com.example.batchdemo.domain.model.LoanRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRecordRepository extends JpaRepository<LoanRecord, Long> {
}
