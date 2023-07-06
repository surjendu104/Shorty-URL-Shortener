package com.surja.urlshortner.exception;

import com.surja.urlshortner.payload.ApiResponse;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        String message = e.getMessage();
        ApiResponse apiResponse = new ApiResponse("URL Not Found", false, 400);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> apiExceptionHandler(RuntimeException e) {
        String message = e.getMessage();
        ApiResponse apiResponse = new ApiResponse(message,true,400);
        return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiResponse> malformedJwtExceptionHandler(RuntimeException e) {
        String message = e.getMessage();
        ApiResponse apiResponse = new ApiResponse("Malformed JWT token",true,400);
        return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.BAD_REQUEST);
    }
}
