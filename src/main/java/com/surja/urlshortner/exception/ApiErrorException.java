package com.surja.urlshortner.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class ApiErrorException extends Exception{
    private String message;

    public ApiErrorException(String message) {
        super(message);
    }
    public ApiErrorException(){super();}
}
