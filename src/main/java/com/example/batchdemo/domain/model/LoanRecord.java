package com.example.batchdemo.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class LoanRecord {
    @Id
    private Long id;
    private String name;
    private Double amount;
}
