package com.surja.urlshortner.exception;

public class ApiErrorException extends Exception{
    private String message;

    public ApiErrorException(String message) {
        super(message);
    }
    public ApiErrorException(){super();}
}
