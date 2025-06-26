package com.example.batchdemo.infrastructure.sftp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Arrays;

@Service
public class SftpServiceImpl implements SftpService {
    
    @Autowired
    private DefaultSftpSessionFactory sftpSessionFactory;

    @Override
    public List<String> listFiles(String remoteDirectory) {
        try {
            var session = sftpSessionFactory.getSession();
            String[] fileArray = session.listNames(remoteDirectory);
            return Arrays.asList(fileArray);
        } catch (Exception e) {
            throw new RuntimeException("Failed to list files from SFTP", e);
        }
    }

    @Override
    public InputStream downloadFile(String remoteFilePath) {
        try {
            var session = sftpSessionFactory.getSession();
            return session.readRaw(remoteFilePath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to download file from SFTP", e);
        }
    }
}
