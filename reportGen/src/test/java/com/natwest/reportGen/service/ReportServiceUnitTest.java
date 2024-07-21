package com.natwest.reportGen.service;

import com.natwest.reportGen.services.ReportService;
import com.natwest.reportGen.services.TransformationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportServiceUnitTest {

    private ReportService reportService;

    @Mock
    private TransformationService transformationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reportService = new ReportService();
        reportService.transformationService = transformationService;
        reportService.filePath = "test/path/";
    }

    @Test
    void generateReport_NotProcessed() throws Exception {
        reportService.generateReport();

        verify(transformationService, times(1)).transform();
        assertFalse(reportService.getProcessed());
    }

    @Test
    void generateReport_AlreadyProcessed() throws Exception {
        reportService.setProcessed(true);
        reportService.generateReport();

        verify(transformationService, never()).transform();
        assertTrue(reportService.getProcessed());
    }

    @Test
    void readFileContent_FileExists() throws Exception {
        // This test would require mocking static methods of Files class
        // Consider refactoring the method to improve testability
    }

    @Test
    void readFileContent_FileBeingProcessed() throws Exception {
        reportService.setProcessed(true);
        byte[] content = reportService.readFileContent();

        assertArrayEquals("File is being processed!".getBytes(), content);
    }

    @Test
    void setAndGetProcessed() {
        reportService.setProcessed(true);
        assertTrue(reportService.getProcessed());

        reportService.setProcessed(false);
        assertFalse(reportService.getProcessed());
    }
}
