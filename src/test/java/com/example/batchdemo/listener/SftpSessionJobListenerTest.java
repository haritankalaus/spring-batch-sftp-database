package com.example.batchdemo.listener;

import com.example.batchdemo.service.SftpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ExecutionContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SftpSessionJobListenerTest {

    @Mock
    private SftpService sftpService;

    @Mock
    private JobExecution jobExecution;

    @Mock
    private ExecutionContext executionContext;

    private SftpSessionJobListener listener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(jobExecution.getExecutionContext()).thenReturn(executionContext);
        listener = new SftpSessionJobListener(sftpService);
    }

    @Test
    void beforeJob_Success() {
        // Act
        listener.beforeJob(jobExecution);

        // Assert
        verify(sftpService).connect();
    }

    @Test
    void beforeJob_Error() {
        // Arrange
        doThrow(new RuntimeException("Connect failed")).when(sftpService).connect();

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> listener.beforeJob(jobExecution));
        assertEquals("Failed to connect to SFTP server: Connect failed", exception.getMessage());
        verify(sftpService).connect();
        verify(jobExecution).setStatus(BatchStatus.FAILED);
    }

    @Test
    void afterJob_Success() {
        // Act
        listener.afterJob(jobExecution);

        // Assert
        verify(sftpService).close();
    }

    @Test
    void afterJob_Error() {
        // Arrange
        doThrow(new RuntimeException("Close failed")).when(sftpService).close();

        // Act
        listener.afterJob(jobExecution);

        // Assert
        verify(sftpService).close();
        verify(executionContext).put("sftp.close.warning", "Failed to close SFTP connection: Close failed");
        verify(jobExecution).setStatus(BatchStatus.COMPLETED);
    }
}
