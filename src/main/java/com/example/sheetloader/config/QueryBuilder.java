package com.example.sheetloader.config;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class QueryBuilder {
    @Value("${query.table.exists}")
    private String QUERY_TABLE_EXISTS;

    @Value("${query.table.create}")
    private String QUERY_TABLE_CREATE;

    @Value("${query.table.insert}")
    private String QUERY_TABLE_INSERT;

    public String getCheckTableExistQuery(String tableName) {
        return String.format(QUERY_TABLE_EXISTS, tableName.toLowerCase());
    }

    public String getCreateTableQuery(Sheet sheet) {
        return String.format(
                QUERY_TABLE_CREATE,
                sheet.getSheetName(),
                sheet.getRow(0).getCell(0).toString(),
                sheet.getRow(0).getCell(1).toString(),
                sheet.getRow(0).getCell(2).toString(),
                sheet.getRow(0).getCell(3).toString()
        );
    }

    public String getInsertValuesQuery(Sheet sheet, int lastFilledRows) {
        StringBuilder query = new StringBuilder(String.format(QUERY_TABLE_INSERT, sheet.getSheetName()));

        for (int i = 1; i < lastFilledRows; i++) {
            Row row = sheet.getRow(i);

            StringBuilder value = new StringBuilder()
                    .append(" (")
                    .append("'").append(row.getCell(0).getStringCellValue()).append("', ")
                    .append(row.getCell(1).getNumericCellValue()).append(", ")
                    .append("'").append(row.getCell(2).getStringCellValue()).append("', ")
                    .append(row.getCell(3).getBooleanCellValue())
                    .append(")");

            if (i == lastFilledRows - 1) {
                query.append(value);
            } else {
                query.append(value).append(",");
            }
        }

        return query.append(";").toString();
    }
}
