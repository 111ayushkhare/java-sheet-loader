package com.example.sheetloader.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto {
    String message;
    short statusCode;

    public ResponseDto(String message, short statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
