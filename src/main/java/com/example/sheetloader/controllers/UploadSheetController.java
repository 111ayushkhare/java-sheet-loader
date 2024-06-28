package com.example.sheetloader.controllers;

import com.example.sheetloader.dto.ResponseDto;
import com.example.sheetloader.services.UploadSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/upload")
public class UploadSheetController {

    @Autowired
    private UploadSheetService uploadSheetService;

    @PostMapping(value = "/single")
    public ResponseDto readSingleFile(@RequestParam("file") MultipartFile spreadsheet) {
        return this.uploadSheetService.readSingleFile(spreadsheet);
    }

    // TODO: To be implemented in future
    @PostMapping(value = "/multiple")
    public ResponseDto readMultipleFiles() {
        return this.uploadSheetService.readMultipleFiles();
    }
}
