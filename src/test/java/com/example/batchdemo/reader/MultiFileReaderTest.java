package com.example.batchdemo.reader;

import com.example.batchdemo.service.SftpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MultiFileReaderTest {

    @Mock
    private SftpService sftpService;

    @Mock
    private FileReaderFactory fileReaderFactory;

    @Mock
    private FileReader fileReader;

    private MultiFileReader multiFileReader;
    private final String remoteDirectory = "/test";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        multiFileReader = new MultiFileReader(sftpService, fileReaderFactory, remoteDirectory);
    }

    @Test
    void read_Success() throws Exception {
        // Arrange
        when(sftpService.listFiles(remoteDirectory))
                .thenReturn(Arrays.asList("file1.csv", "file2.xlsx"));
        when(fileReaderFactory.getReader(anyString())).thenReturn(fileReader);
        
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        when(sftpService.downloadFile(anyString())).thenReturn(inputStream);
        
        String[] expectedData = {"col1", "col2", "col3"};
        when(fileReader.read(any(InputStream.class))).thenReturn(expectedData);

        // Act
        String[] result1 = multiFileReader.read();
        String[] result2 = multiFileReader.read();
        String[] result3 = multiFileReader.read();

        // Assert
        assertArrayEquals(expectedData, result1);
        assertArrayEquals(expectedData, result2);
        assertNull(result3); // No more files

        verify(sftpService, times(1)).listFiles(remoteDirectory);
        verify(sftpService, times(2)).downloadFile(anyString());
        verify(fileReader, times(2)).read(any(InputStream.class));
    }

    @Test
    void read_NoFiles() throws Exception {
        // Arrange
        when(sftpService.listFiles(remoteDirectory)).thenReturn(Arrays.asList());

        // Act
        String[] result = multiFileReader.read();

        // Assert
        assertNull(result);
        verify(sftpService, times(1)).listFiles(remoteDirectory);
        verify(sftpService, never()).downloadFile(anyString());
        verify(fileReader, never()).read(any(InputStream.class));
    }

    @Test
    void read_ErrorListingFiles() {
        // Arrange
        when(sftpService.listFiles(remoteDirectory))
                .thenThrow(new RuntimeException("List failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> multiFileReader.read());
    }

    @Test
    void read_ErrorDownloadingFile() {
        // Arrange
        when(sftpService.listFiles(remoteDirectory))
                .thenReturn(Arrays.asList("file1.csv"));
        when(fileReaderFactory.getReader(anyString())).thenReturn(fileReader);
        when(sftpService.downloadFile(anyString()))
                .thenThrow(new RuntimeException("Download failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> multiFileReader.read());
    }

    @Test
    void read_ErrorReadingFile() {
        // Arrange
        when(sftpService.listFiles(remoteDirectory))
                .thenReturn(Arrays.asList("file1.csv"));
        when(fileReaderFactory.getReader(anyString())).thenReturn(fileReader);
        
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        when(sftpService.downloadFile(anyString())).thenReturn(inputStream);
        
        when(fileReader.read(any(InputStream.class)))
                .thenThrow(new RuntimeException("Read failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> multiFileReader.read());
    }
}
