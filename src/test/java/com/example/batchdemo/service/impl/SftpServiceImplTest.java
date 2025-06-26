package com.example.batchdemo.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpSession;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SftpServiceImplTest {

    @Mock
    private DefaultSftpSessionFactory sftpSessionFactory;

    @Mock
    private SftpSession sftpSession;

    @InjectMocks
    private SftpServiceImpl sftpService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void connect_Success() throws Exception {
        // Arrange
        when(sftpSessionFactory.getSession()).thenReturn(sftpSession);
        when(sftpSession.isOpen()).thenReturn(false);

        // Act
        sftpService.connect();

        // Assert
        verify(sftpSessionFactory).getSession();
    }

    @Test
    void connect_AlreadyConnected() throws Exception {
        // Arrange
        when(sftpSession.isOpen()).thenReturn(true);

        // Act
        sftpService.connect();

        // Assert
        verify(sftpSessionFactory, never()).getSession();
    }

    @Test
    void connect_Error() throws Exception {
        // Arrange
        when(sftpSessionFactory.getSession()).thenThrow(new RuntimeException("Connection failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> sftpService.connect());
    }

    @Test
    void listFiles_Success() throws IOException {
        // Arrange
        String[] files = {"test1.csv", "test2.xlsx", "test3.txt"};
        when(sftpSession.listNames("/test")).thenReturn(files);

        // Act
        List<String> result = sftpService.listFiles("/test");

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains("test1.csv"));
        assertTrue(result.contains("test2.xlsx"));
    }

    @Test
    void listFiles_Error() throws IOException {
        // Arrange
        when(sftpSession.listNames("/test")).thenThrow(new RuntimeException("List failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> sftpService.listFiles("/test"));
    }

    @Test
    void downloadFile_Success() throws IOException {
        // Arrange
        InputStream expectedStream = new ByteArrayInputStream("test data".getBytes());
        when(sftpSession.readRaw("/test/file.csv")).thenReturn(expectedStream);

        // Act
        InputStream result = sftpService.downloadFile("/test/file.csv");

        // Assert
        assertNotNull(result);
        assertEquals(expectedStream, result);
    }

    @Test
    void downloadFile_Error() throws IOException {
        // Arrange
        when(sftpSession.readRaw("/test/file.csv")).thenThrow(new RuntimeException("Download failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> sftpService.downloadFile("/test/file.csv"));
    }

    @Test
    void close_Success() throws Exception {
        // Arrange
        when(sftpSession.isOpen()).thenReturn(true);

        // Act
        sftpService.close();

        // Assert
        verify(sftpSession).close();
    }

    @Test
    void close_NotOpen() {
        // Arrange
        when(sftpSession.isOpen()).thenReturn(false);

        // Act
        sftpService.close();

        // Assert
        verify(sftpSession, never()).close();
    }

    @Test
    void close_Error() {
        // Arrange
        when(sftpSession.isOpen()).thenReturn(true);
        doThrow(new RuntimeException("Close failed")).when(sftpSession).close();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> sftpService.close());
    }
}
