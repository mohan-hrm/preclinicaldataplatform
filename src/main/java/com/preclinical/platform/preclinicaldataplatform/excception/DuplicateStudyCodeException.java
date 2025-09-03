package com.preclinical.platform.preclinicaldataplatform.excception;

public class DuplicateStudyCodeException extends RuntimeException {
    public DuplicateStudyCodeException(String studyCode) {
        super(studyCode);
    }
}
