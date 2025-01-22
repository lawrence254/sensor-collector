package com.lawrence254.datapipeline.exception;

public class DataPipelineException  extends RuntimeException {
    private final ErrorCode errorCode;

    public enum ErrorCode {
        INVALID_SENSOR_DATA,
        PROCESSING_ERROR,
        STORAGE_ERROR,
        COMMUNICATION_ERROR
    }

    public DataPipelineException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    public DataPipelineException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
