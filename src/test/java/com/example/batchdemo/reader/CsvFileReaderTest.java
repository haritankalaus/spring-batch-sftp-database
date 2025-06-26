package com.example.batchdemo.reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class CsvFileReaderTest {

    private CsvFileReader csvFileReader;

    @BeforeEach
    void setUp() {
        csvFileReader = new CsvFileReader();
    }

    @Test
    void read_Success() {
        // Arrange
        String csvContent = "name,age,email\nJohn,30,john@test.com";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        // Act
        String[] result = csvFileReader.read(inputStream);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("John", result[0]);
        assertEquals("30", result[1]);
        assertEquals("john@test.com", result[2]);
    }

    @Test
    void read_EmptyFile() {
        // Arrange
        String csvContent = "";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        // Act
        String[] result = csvFileReader.read(inputStream);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void read_InvalidFormat() {
        // Arrange
        String csvContent = "invalid,csv,format\nwith\nmissing,columns";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> csvFileReader.read(inputStream));
    }
}
