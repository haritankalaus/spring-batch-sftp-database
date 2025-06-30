package com.example.batchdemo.reader;

import org.springframework.stereotype.Component;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

@Component
public class CsvFileReader implements FileReader {
    
    @Override
    public String[] read(InputStream inputStream) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            // Read header row
            String[] header = reader.readNext();
            if (header == null) { // Empty file
                return new String[0];
            }
            
            if (header.length != 4) { // We expect name,age,email,address
                throw new RuntimeException("Invalid CSV format: Header must have exactly 4 columns");
            }
            
            // Read data row
            String[] data = reader.readNext();
            if (data != null && data.length != header.length) {
                throw new RuntimeException("Invalid CSV format: Data row must have same number of columns as header");
            }
            return data != null ? data : new String[0];
        } catch (IOException | CsvException e) {
            throw new RuntimeException("Failed to read CSV file", e);
        }
    }

    @Override
    public boolean supports(String filename) {
        return filename.toLowerCase().endsWith(".csv");
    }
}
