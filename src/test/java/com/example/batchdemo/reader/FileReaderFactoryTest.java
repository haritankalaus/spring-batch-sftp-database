package com.example.batchdemo.reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class FileReaderFactoryTest {

    private FileReaderFactory fileReaderFactory;
    private CsvFileReader csvFileReader;
    private ExcelFileReader excelFileReader;

    @BeforeEach
    void setUp() {
        csvFileReader = new CsvFileReader();
        excelFileReader = new ExcelFileReader();
        fileReaderFactory = new FileReaderFactory(Arrays.asList(csvFileReader, excelFileReader));
    }

    @Test
    void getReader_CSV() {
        // Act
        FileReader reader = fileReaderFactory.getReader("test.csv");

        // Assert
        assertTrue(reader instanceof CsvFileReader);
    }

    @Test
    void getReader_Excel() {
        // Act
        FileReader reader = fileReaderFactory.getReader("test.xlsx");

        // Assert
        assertTrue(reader instanceof ExcelFileReader);
    }

    @Test
    void getReader_UnsupportedFormat() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> fileReaderFactory.getReader("test.txt"));
    }
}
