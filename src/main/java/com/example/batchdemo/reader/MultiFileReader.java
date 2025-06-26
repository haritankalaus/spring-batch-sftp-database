package com.example.batchdemo.reader;

import com.example.batchdemo.service.SftpService;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;

import java.util.Queue;
import java.util.LinkedList;

public class MultiFileReader implements ItemReader<String[]> {
    private final SftpService sftpService;
    private final String remoteDirectory;
    private final FileReaderFactory fileReaderFactory;
    private Queue<String> pendingFiles;
    private String currentFile;
    private Queue<String[]> currentFileRows;

    public MultiFileReader(SftpService sftpService,
                         FileReaderFactory fileReaderFactory,
                         @Value("${sftp.input.directory}") String remoteDirectory) {
        this.sftpService = sftpService;
        this.fileReaderFactory = fileReaderFactory;
        this.remoteDirectory = remoteDirectory;
    }

    @Override
    public String[] read() throws Exception {
        if (pendingFiles == null) {
            initialize();
        }

        if (currentFileRows == null || currentFileRows.isEmpty()) {
            if (!openNextFile()) {
                return null; // No more files to process
            }
        }

        return currentFileRows.poll();
    }

    private void initialize() {
        pendingFiles = new LinkedList<>(sftpService.listFiles(remoteDirectory));
        currentFileRows = new LinkedList<>();
    }

    private boolean openNextFile() {
        currentFile = pendingFiles.poll();
        if (currentFile == null) {
            return false;
        }

        try {
            var inputStream = sftpService.downloadFile(remoteDirectory + "/" + currentFile);
            var reader = fileReaderFactory.getReader(currentFile);
            String[] row = reader.read(inputStream);
            if (row != null) {
                currentFileRows.add(row);
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to process file: " + currentFile, e);
        }
    }
}
