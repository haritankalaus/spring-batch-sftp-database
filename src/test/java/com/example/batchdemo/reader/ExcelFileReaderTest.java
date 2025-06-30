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
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        
        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("name");
        headerRow.createCell(1).setCellValue("age");
        headerRow.createCell(2).setCellValue("email");
        headerRow.createCell(3).setCellValue("address");
        
        // Create data row
        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue("John");
        dataRow.createCell(1).setCellValue("30");
        dataRow.createCell(2).setCellValue("john@test.com");
        dataRow.createCell(3).setCellValue("123 Main St");
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        
        InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());

        // Act
        String[] result = excelFileReader.read(inputStream);

        // Assert
        assertNotNull(result);
        assertEquals(4, result.length);
        assertEquals("John", result[0]);
        assertEquals("30", result[1]);
        assertEquals("john@test.com", result[2]);
        assertEquals("123 Main St", result[3]);
    }

    @Test
    void read_EmptyFile() throws Exception {
        // Arrange
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        
        // Create header row only
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("name");
        headerRow.createCell(1).setCellValue("age");
        headerRow.createCell(2).setCellValue("email");
        headerRow.createCell(3).setCellValue("address");
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        
        InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());

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

    @Test
    void read_InvalidHeader() throws Exception {
        // Arrange
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        
        // Create header row with wrong number of columns
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("name");
        headerRow.createCell(1).setCellValue("age");
        headerRow.createCell(2).setCellValue("email");
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        
        InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> excelFileReader.read(inputStream),
                "Should throw exception for invalid header");
    }
}
