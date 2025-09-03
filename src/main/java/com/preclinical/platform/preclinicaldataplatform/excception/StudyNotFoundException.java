package com.preclinical.platform.preclinicaldataplatform.excception;

public class StudyNotFoundException extends RuntimeException {
    public StudyNotFoundException(String message) {
        super(message);
    }
}
