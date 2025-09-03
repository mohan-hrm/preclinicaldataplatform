package com.preclinical.platform.preclinicaldataplatform.excception;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(String message) {
        super(message);
    }
}
