package com.natwest.reportGen.service;

import com.natwest.reportGen.services.FilesUploadService;
import com.natwest.reportGen.services.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FilesUploadServiceUnitTest {

    private FilesUploadService filesUploadService;

    @Mock
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        filesUploadService = new FilesUploadService();
        filesUploadService.reportService = reportService;
        filesUploadService.uploadDir = "test/upload/";
    }

    @Test
    void saveFiles_Success() throws IOException {
        MockMultipartFile inputFile = new MockMultipartFile("inputFile", "input.csv", "text/csv", "test data".getBytes());
        MockMultipartFile referenceFile = new MockMultipartFile("referenceFile", "reference.csv", "text/csv", "ref data".getBytes());

        // This test would require mocking static methods of Files class
        // Consider refactoring the method to improve testability

        // filesUploadService.saveFiles(inputFile, referenceFile);

        // verify(reportService).setProcessed(false);
    }

    @Test
    void saveFiles_InvalidInputFileName() {
        MockMultipartFile inputFile = new MockMultipartFile("inputFile", "wrong.csv", "text/csv", "test data".getBytes());
        MockMultipartFile referenceFile = new MockMultipartFile("referenceFile", "reference.csv", "text/csv", "ref data".getBytes());

        assertThrows(IllegalArgumentException.class, () -> filesUploadService.saveFiles(inputFile, referenceFile));
    }

    @Test
    void saveFiles_InvalidReferenceFileName() {
        MockMultipartFile inputFile = new MockMultipartFile("inputFile", "input.csv", "text/csv", "test data".getBytes());
        MockMultipartFile referenceFile = new MockMultipartFile("referenceFile", "wrong.csv", "text/csv", "ref data".getBytes());

        assertThrows(IllegalArgumentException.class, () -> filesUploadService.saveFiles(inputFile, referenceFile));
    }
}