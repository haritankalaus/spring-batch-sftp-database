package com.example.batchdemo.infrastructure.batch.reader;

import com.example.batchdemo.infrastructure.sftp.SftpService;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStreamReader;
import java.util.Queue;
import java.util.LinkedList;

public class MultiFileCsvReader implements ItemReader<String[]> {
    private final SftpService sftpService;
    private final String remoteDirectory;
    private Queue<String> pendingFiles;
    private CSVReader currentReader;

    public MultiFileCsvReader(SftpService sftpService, 
                            @Value("${sftp.input.directory}") String remoteDirectory) {
        this.sftpService = sftpService;
        this.remoteDirectory = remoteDirectory;
    }

    @Override
    public String[] read() throws Exception {
        if (pendingFiles == null) {
            initialize();
        }

        if (currentReader == null && !pendingFiles.isEmpty()) {
            openNextFile();
        }

        String[] nextLine;
        while (true) {
            if (currentReader == null && pendingFiles.isEmpty()) {
                return null; // No more files to process
            }

            nextLine = currentReader.readNext();
            if (nextLine != null) {
                return nextLine;
            }

            // Current file is finished, try next file
            if (!pendingFiles.isEmpty()) {
                openNextFile();
            } else {
                return null;
            }
        }
    }

    private void initialize() {
        pendingFiles = new LinkedList<>(sftpService.listFiles(remoteDirectory));
    }

    private void openNextFile() throws Exception {
        String nextFile = pendingFiles.poll();
        if (nextFile != null) {
            var inputStream = sftpService.downloadFile(remoteDirectory + "/" + nextFile);
            currentReader = new CSVReaderBuilder(new InputStreamReader(inputStream))
                    .withSkipLines(1) // Skip header row
                    .build();
        } else {
            currentReader = null;
        }
    }
}
