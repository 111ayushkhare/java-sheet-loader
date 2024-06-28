package com.example.sheetloader.services;

import com.example.sheetloader.dto.ResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface UploadSheetService {
    ResponseDto readSingleFile(MultipartFile spreadsheet);
    ResponseDto readMultipleFiles();
}
