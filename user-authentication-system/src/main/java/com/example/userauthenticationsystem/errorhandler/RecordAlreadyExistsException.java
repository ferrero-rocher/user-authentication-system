package com.example.userauthenticationsystem.errorhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordAlreadyExistsException extends RuntimeException{
    private String message;
}
