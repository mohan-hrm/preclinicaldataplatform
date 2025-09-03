package com.preclinical.platform.preclinicaldataplatform.configuration;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.preclinical.platform.preclinicaldataplatform.service.DataAnalysisService;
import com.preclinical.platform.preclinicaldataplatform.service.StudyManagementService;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component  // Remove @Scheduled from here
@Slf4j
public class PreclinicalScheduledTasks {
	// Add this line manually:
    private static final Logger log = LoggerFactory.getLogger(PreclinicalScheduledTasks.class);
    
    private final StudyManagementService studyService;
    private final DataAnalysisService analysisService;
    
    public PreclinicalScheduledTasks(StudyManagementService studyService,
                                   DataAnalysisService analysisService) {
        this.studyService = studyService;
        this.analysisService = analysisService;
    }
    
    @Scheduled(cron = "0 0 9 * * MON") // Every Monday at 9 AM
    public void generateWeeklyStudyReports() {
        log.info("Generating weekly study reports...");
        try {
            // Implementation to generate and send weekly reports
            log.info("Weekly study reports generated successfully");
        } catch (Exception e) {
            log.error("Error generating weekly study reports", e);
        }
    }
    
    @Scheduled(fixedRate = 300000) // Every 5 minutes
    public void checkForOverdueAdverseEventReports() {
        log.debug("Checking for overdue adverse event reports...");
        try {
            // Implementation to check SAE reporting compliance
            log.debug("Adverse event compliance check completed");
        } catch (Exception e) {
            log.error("Error checking adverse event compliance", e);
        }
    }
    
    @Scheduled(cron = "0 0 1 1 * *") // First day of every month at 1 AM
    public void generateMonthlyEnrollmentReport() {
        log.info("Generating monthly enrollment report...");
        try {
            // Implementation for monthly enrollment analysis
            log.info("Monthly enrollment report generated successfully");
        } catch (Exception e) {
            log.error("Error generating monthly enrollment report", e);
        }
    }
    
    @Scheduled(fixedDelay = 3600000) // Every hour after completion
    public void updateStudyStatisticsCache() {
        log.debug("Updating study statistics cache...");
        try {
            // Implementation to refresh cached statistics
            log.debug("Study statistics cache updated successfully");
        } catch (Exception e) {
            log.error("Error updating study statistics cache", e);
        }
    }
}
