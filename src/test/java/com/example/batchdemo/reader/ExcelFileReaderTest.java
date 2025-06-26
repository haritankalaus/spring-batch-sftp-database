package com.example.batchdemo.reader;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class ExcelFileReaderTest {

    private ExcelFileReader excelFileReader;

    @BeforeEach
    void setUp() {
        excelFileReader = new ExcelFileReader();
    }

    @Test
    void read_Success() throws Exception {
        // Arrange
        InputStream inputStream = createTestExcelFile(new String[]{"John", "30", "john@test.com"});

        // Act
        String[] result = excelFileReader.read(inputStream);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("John", result[0]);
        assertEquals("30", result[1]);
        assertEquals("john@test.com", result[2]);
    }

    @Test
    void read_EmptyFile() throws Exception {
        // Arrange
        InputStream inputStream = createTestExcelFile(new String[0]);

        // Act
        String[] result = excelFileReader.read(inputStream);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void read_InvalidFormat() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("Not an Excel file".getBytes());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> excelFileReader.read(inputStream));
    }

    private InputStream createTestExcelFile(String[] data) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Test");
        Row row = sheet.createRow(0);

        for (int i = 0; i < data.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(data[i]);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
