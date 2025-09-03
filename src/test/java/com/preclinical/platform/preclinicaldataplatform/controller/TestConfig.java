package com.preclinical.platform.preclinicaldataplatform.controller;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.preclinical.platform.preclinicaldataplatform.service.AuditService;
import com.preclinical.platform.preclinicaldataplatform.service.EmailNotificationService;

@TestConfiguration
public class TestConfig {
    
    @Bean
    @Primary
    public EmailNotificationService mockEmailService() {
        return Mockito.mock(EmailNotificationService.class);
    }
    
    @Bean
    @Primary
    public AuditService mockAuditService() {
        return Mockito.mock(AuditService.class);
    }
}
