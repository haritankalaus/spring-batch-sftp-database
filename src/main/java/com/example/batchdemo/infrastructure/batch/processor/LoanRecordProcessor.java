package com.example.batchdemo.infrastructure.batch.processor;

import com.example.batchdemo.domain.model.LoanRecord;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class LoanRecordProcessor implements ItemProcessor<String[], LoanRecord> {
    
    @Override
    public LoanRecord process(String[] items) {
        if (items == null || items.length < 3) {
            return null;
        }
        
        try {
            LoanRecord record = new LoanRecord();
            record.setId(Long.parseLong(items[0]));
            record.setName(items[1]);
            record.setAmount(Double.parseDouble(items[2]));
            return record;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
