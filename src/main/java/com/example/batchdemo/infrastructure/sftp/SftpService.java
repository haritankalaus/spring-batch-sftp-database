package com.example.batchdemo.infrastructure.sftp;

import java.io.InputStream;
import java.util.List;

public interface SftpService {
    List<String> listFiles(String remoteDirectory);
    InputStream downloadFile(String remoteFilePath);
}
