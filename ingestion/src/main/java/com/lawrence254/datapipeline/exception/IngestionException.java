package com.lawrence254.datapipeline.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class IngestionException {
    @ExceptionHandler(DataPipelineException.class)
    public ResponseEntity<Map<String,Object>> handleDataPipelineException(DataPipelineException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", Instant.now());
        response.put("error", ex.getErrorCode());
        response.put("message", ex.getMessage());

        HttpStatus status = switch (ex.getErrorCode()){
            case INVALID_SENSOR_DATA -> HttpStatus.BAD_REQUEST;
            case PROCESSING_ERROR -> HttpStatus.SERVICE_UNAVAILABLE;
            case STORAGE_ERROR -> HttpStatus.BAD_REQUEST;
            case COMMUNICATION_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
        return new ResponseEntity<>(response,status);
    }
}
