package com.example.batchdemo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JobControllerTest {

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private Job sftpCsvToDbJob;

    @InjectMocks
    private JobController jobController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void triggerSftpImport_Success() throws Exception {
        // Arrange
        JobExecution jobExecution = mock(JobExecution.class);
        when(jobLauncher.run(eq(sftpCsvToDbJob), any(JobParameters.class)))
                .thenReturn(jobExecution);

        // Act
        ResponseEntity<String> response = jobController.triggerSftpImport();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Job triggered successfully", response.getBody());
        verify(jobLauncher).run(eq(sftpCsvToDbJob), any(JobParameters.class));
    }

    @Test
    void triggerSftpImport_Error() throws Exception {
        // Arrange
        when(jobLauncher.run(eq(sftpCsvToDbJob), any(JobParameters.class)))
                .thenThrow(new RuntimeException("Test error"));

        // Act
        ResponseEntity<String> response = jobController.triggerSftpImport();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Error triggering job"));
        verify(jobLauncher).run(eq(sftpCsvToDbJob), any(JobParameters.class));
    }
}
