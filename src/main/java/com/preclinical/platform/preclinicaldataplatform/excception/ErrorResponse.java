package com.preclinical.platform.preclinicaldataplatform.excception;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {
    private String message;
    private String errorCode;
    private int status;
    private LocalDateTime timestamp;
    private String path;
    private Map<String, String> validationErrors;
    
    // Default constructor
    public ErrorResponse() {}
    
    // Constructor with all fields
    public ErrorResponse(String message, String errorCode, int status, 
                        LocalDateTime timestamp, String path, 
                        Map<String, String> validationErrors) {
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.timestamp = timestamp;
        this.path = path;
        this.validationErrors = validationErrors;
    }
    
    // Static builder method
    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }
    
    // Builder class
    public static class ErrorResponseBuilder {
        private String message;
        private String errorCode;
        private int status;
        private LocalDateTime timestamp;
        private String path;
        private Map<String, String> validationErrors;
        
        public ErrorResponseBuilder message(String message) {
            this.message = message;
            return this;
        }
        
        public ErrorResponseBuilder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }
        
        public ErrorResponseBuilder status(int status) {
            this.status = status;
            return this;
        }
        
        public ErrorResponseBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public ErrorResponseBuilder path(String path) {
            this.path = path;
            return this;
        }
        
        public ErrorResponseBuilder validationErrors(Map<String, String> validationErrors) {
            this.validationErrors = validationErrors;
            return this;
        }
        
        public ErrorResponse build() {
            return new ErrorResponse(message, errorCode, status, timestamp, path, validationErrors);
        }
    }
    
    // Getters and setters
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }
    
    public void setValidationErrors(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }
}