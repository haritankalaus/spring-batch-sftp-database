package com.example.batchdemo.service;

import java.io.InputStream;
import java.util.List;

public interface SftpService extends AutoCloseable {
    void connect();
    List<String> listFiles(String remoteDirectory);
    InputStream downloadFile(String remoteFilePath);
    @Override
    void close();
}
