package com.preclinical.platform.preclinicaldataplatform.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "preclinical.platform")
@Data
public class PreclinicalPlatformProperties {
    
    private Study study = new Study();
    private Patient patient = new Patient();
    private AdverseEvent adverseEvent = new AdverseEvent();
    private Email email = new Email();
    
    @Data
    public static class Study {
        private int maxEnrollment = 1000;
        private int defaultStudyDuration = 365; // days
        private boolean autoActivateAfterCreation = false;
        private List<String> requiredApprovals = List.of("IRB", "FDA", "SPONSOR");
    }
    
    @Data
    public static class Patient {
        private int minAge = 18;
        private int maxAge = 100;
        private boolean requireConsentBeforeEnrollment = true;
        private int maxWithdrawalDays = 30;
    }
    
    @Data
    public static class AdverseEvent {
        private boolean autoReportSeriousEvents = true;
        private int saeReportingTimeframe = 24; // hours
        private List<String> saeNotificationEmails = new ArrayList<>();
    }
    
    @Data
    public static class Email {
        private boolean enabled = true;
        private String fromAddress = "noreply@preclinical-platform.com";
        private String regulatoryTeamEmail = "regulatory@company.com";
        private String safetyTeamEmail = "safety@company.com";
    }
}
