package com.natwest.reportGen.contoller;

import com.natwest.reportGen.controller.ReportController;
import com.natwest.reportGen.services.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportControllerUnitTest {

    private ReportController reportController;

    @Mock
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reportController = new ReportController();
        reportController.reportService = reportService;
    }

    @Test
    void generateReport_Success() {
        ResponseEntity<String> response = reportController.generateReport();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Report generation request received and is being processed.", response.getBody());
        verify(reportService, times(1)).generateReport();
    }

    @Test
    void generateReport_Exception() {
        doThrow(new RuntimeException("Test error")).when(reportService).generateReport();

        ResponseEntity<String> response = reportController.generateReport();

        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Error starting report generation: Test error"));
    }

    @Test
    void fetchReport_Success() throws Exception {
        byte[] mockContent = "test content".getBytes();
        when(reportService.readFileContent()).thenReturn(mockContent);

        ResponseEntity<byte[]> response = reportController.fetchReport();

        assertEquals(200, response.getStatusCodeValue());
        assertArrayEquals(mockContent, response.getBody());
        assertEquals("text/csv", response.getHeaders().getContentType().toString());
    }

    @Test
    void fetchReport_FileNotFound() throws Exception {
        when(reportService.readFileContent()).thenThrow(new Exception("File not found"));

        ResponseEntity<byte[]> response = reportController.fetchReport();

        assertEquals(404, response.getStatusCodeValue());
        assertArrayEquals("File not found".getBytes(), response.getBody());
    }

    @Test
    void fetchReport_OtherException() throws Exception {
        when(reportService.readFileContent()).thenThrow(new Exception("Other error"));

        ResponseEntity<byte[]> response = reportController.fetchReport();

        assertEquals(500, response.getStatusCodeValue());
        assertArrayEquals("Other error".getBytes(), response.getBody());
    }
}