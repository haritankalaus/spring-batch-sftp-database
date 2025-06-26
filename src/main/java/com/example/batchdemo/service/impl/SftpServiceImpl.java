package com.example.batchdemo.service.impl;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpSession;
import org.springframework.stereotype.Service;

import com.example.batchdemo.service.SftpService;


@Service
public class SftpServiceImpl implements SftpService {
    
    @Autowired
    private DefaultSftpSessionFactory sftpSessionFactory;
    private SftpSession session;

    @Override
    public void connect() {
        try {
            if (session == null || !session.isOpen()) {
                session = sftpSessionFactory.getSession();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to SFTP server", e);
        }
    }

    @Override
    public List<String> listFiles(String remoteDirectory) {
        try {
            String[] fileArray = session.listNames(remoteDirectory);
            return Arrays.stream(fileArray)
                    .filter(file -> file.endsWith(".csv") || file.endsWith(".xlsx"))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to list files from SFTP", e);
        }
    }

    @Override
    public InputStream downloadFile(String remoteFilePath) {
        try {
            return session.readRaw(remoteFilePath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to download file from SFTP", e);
        }
    }

    @Override
    public void close() {
        if (session != null && session.isOpen()) {
            try {
                session.close();
            } catch (Exception e) {
                throw new RuntimeException("Failed to close SFTP session", e);
            }
        }
    }
}
