package com.example.batchdemo.reader;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelFileReader implements FileReader {
    
    @Override
    public String[] read(InputStream inputStream) {
        try {
            Workbook workbook = WorkbookFactory.create(inputStream);
            try {
                Sheet sheet = workbook.getSheetAt(0);
                // Skip header row and read first data row
                Row headerRow = sheet.getRow(0);
                Row dataRow = sheet.getRow(1);
                
                // Validate header
                if (headerRow == null || headerRow.getPhysicalNumberOfCells() != 4) {
                    throw new RuntimeException("Invalid Excel format: Header must have exactly 4 columns");
                }
                
                // Return empty if no data rows
                if (dataRow == null) {
                    return new String[0];
                }

                List<String> rowData = new ArrayList<>();
                for (Cell cell : dataRow) {
                    if (cell != null) {
                        CellType cellType = cell.getCellType();
                        if (cellType == CellType.STRING) {
                            rowData.add(cell.getStringCellValue());
                        } else if (cellType == CellType.NUMERIC) {
                            rowData.add(String.valueOf(cell.getNumericCellValue()));
                        } else {
                            rowData.add("");
                        }
                    } else {
                        rowData.add("");
                    }
                }
                return rowData.toArray(new String[0]);
            } finally {
                workbook.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read Excel file", e);
        }
    }

    @Override
    public boolean supports(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".xlsx") || lower.endsWith(".xls");
    }
}
