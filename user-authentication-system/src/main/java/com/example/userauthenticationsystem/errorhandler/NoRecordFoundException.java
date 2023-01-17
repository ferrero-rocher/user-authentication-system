package com.example.userauthenticationsystem.errorhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class NoRecordFoundException extends RuntimeException{
    private String message;
    public NoRecordFoundException(String message)
    {
        super();
        this.message=message;
    }
}
