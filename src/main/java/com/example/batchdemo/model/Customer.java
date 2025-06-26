package com.example.batchdemo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Customer {
    @Id
    private Long id;
    private String name;
    private int age;
    private String email;
    private String address;
}
