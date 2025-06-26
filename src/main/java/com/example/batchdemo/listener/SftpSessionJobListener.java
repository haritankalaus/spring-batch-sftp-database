package com.example.batchdemo.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.batchdemo.service.SftpService;

@Component
public class SftpSessionJobListener implements JobExecutionListener {

    private final SftpService sftpService;

    @Autowired
    public SftpSessionJobListener(SftpService sftpService) {
        this.sftpService = sftpService;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        try {
            sftpService.connect();
        } catch (Exception e) {
            jobExecution.setStatus(BatchStatus.FAILED);
            throw new RuntimeException("Failed to connect to SFTP server: " + e.getMessage(), e);
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        try {
            sftpService.close();
        } catch (Exception e) {
            // Log error but don't rethrow as we don't want to fail the job just because of connection close issues
            jobExecution.getExecutionContext().put("sftp.close.warning", "Failed to close SFTP connection: " + e.getMessage());
            jobExecution.setStatus(BatchStatus.COMPLETED);  // Still mark as complete since the job itself succeeded
        }
    }
}
