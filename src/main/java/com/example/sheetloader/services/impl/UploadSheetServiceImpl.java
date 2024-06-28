package com.example.sheetloader.services.impl;

import com.example.sheetloader.dto.ResponseDto;
import com.example.sheetloader.repositories.UploadSheetRepository;
import com.example.sheetloader.services.UploadSheetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
public class UploadSheetServiceImpl implements UploadSheetService {

    private static final Logger log = LoggerFactory.getLogger(UploadSheetServiceImpl.class);

    @Autowired
    private UploadSheetRepository uploadSheetRepository;

    @Override
    public ResponseDto readSingleFile(MultipartFile spreadsheet) {
        try {
            Workbook workbook = new XSSFWorkbook(spreadsheet.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            Row row;

            int totalRows = sheet.getLastRowNum() - 1;
            int lastFilledRows = 0;

            for (int i = 1; i < totalRows; i++) {
                boolean isCellEmpty = true;
                row = sheet.getRow(i);

                for (Cell cell : row) {
                    if (cell != null && cell.getCellType() != CellType.BLANK) {
                        isCellEmpty = false;
                        break;
                    }
                }

                if (isCellEmpty) {
                    lastFilledRows = i;
                    break;
                }
            }

            if (this.uploadSheetRepository.checkIfTableExists(sheet.getSheetName())) {
                return new ResponseDto(
                        "This sheet is already dumped in the database. Kindly go for UPDATE sheet if needed",
                        (short) 400
                );
            } else {
                if (this.uploadSheetRepository.createTable(sheet)) {
                    this.uploadSheetRepository.insertSheetDataIntoDb(sheet, lastFilledRows);
                } else {
                    return new ResponseDto("FAILURE: Could not dump sheet data into the database", (short) 406);
                }
            }

            return new ResponseDto("SUCCESS: Sheet data dump into the database", (short) 201);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseDto("FAILURE: Sheet data dump into the database" + e.getMessage(), (short) 406);
        }

    }

    // TODO: To be implemented later
    @Override
    public ResponseDto readMultipleFiles() {
        return new ResponseDto("", (byte) 201);
    }
}
