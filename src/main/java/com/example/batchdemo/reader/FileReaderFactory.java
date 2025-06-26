package com.example.batchdemo.reader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class FileReaderFactory {
    
    private final List<FileReader> readers;

    @Autowired
    public FileReaderFactory(List<FileReader> readers) {
        this.readers = readers;
    }

    public FileReader getReader(String filename) {
        return readers.stream()
                .filter(reader -> reader.supports(filename))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No reader found for file: " + filename));
    }
}
