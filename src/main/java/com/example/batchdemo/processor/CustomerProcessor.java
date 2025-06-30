package com.example.batchdemo.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.batchdemo.model.Customer;

@Component
public class CustomerProcessor implements ItemProcessor<String[], Customer> {
    
    @Override
    public Customer process(String[] items) {
        if (items == null || items.length < 3) {
            return null;
        }
        
        Customer record = new Customer();
        record.setId(System.currentTimeMillis());
        record.setName(items[0]);
        record.setAge((int)Float.parseFloat(items[1])); // Handle both decimal and integer age values
        record.setEmail(items[2]);
        record.setAddress(items[3]);
        return record;
    }
}
