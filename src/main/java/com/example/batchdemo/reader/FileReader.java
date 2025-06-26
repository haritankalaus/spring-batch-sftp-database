package com.example.batchdemo.reader;

import java.io.InputStream;

public interface FileReader {
    String[] read(InputStream inputStream);
    boolean supports(String filename);
}
