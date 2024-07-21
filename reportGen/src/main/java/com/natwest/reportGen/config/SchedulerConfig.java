package com.natwest.reportGen.config;

import com.natwest.reportGen.services.ReportService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class SchedulerConfig {

    @Autowired
    private ReportService reportService;

    private static final org.apache.log4j.Logger logger = Logger.getLogger(SchedulerConfig.class.getName());

    @Scheduled(cron = "${report.generation.cron}")
    public void scheduleReportGeneration() {
        try {
            reportService.generateReport();
            logger.info("Report generation completed by CRON");
        } catch (Exception e) {
            logger.error("Error report generation at scheduled CRON", e);
        }
    }
}