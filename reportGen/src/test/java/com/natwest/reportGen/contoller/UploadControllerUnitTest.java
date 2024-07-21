package com.natwest.reportGen.contoller;

import com.natwest.reportGen.controller.UploadController;
import com.natwest.reportGen.services.FilesUploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UploadControllerUnitTest {

    private UploadController uploadController;

    @Mock
    private FilesUploadService filesUploadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        uploadController = new UploadController();
        uploadController.filesUploadService = filesUploadService;
    }

    @Test
    void uploadFiles_Success() throws Exception {
        MockMultipartFile inputFile = new MockMultipartFile("inputFile", "input.csv", "text/csv", "test data".getBytes());
        MockMultipartFile referenceFile = new MockMultipartFile("referenceFile", "reference.csv", "text/csv", "ref data".getBytes());

        ResponseEntity<String> response = uploadController.uploadFiles(inputFile, referenceFile);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Files uploaded successfully", response.getBody());
        verify(filesUploadService, times(1)).saveFiles(inputFile, referenceFile);
    }

    @Test
    void uploadFiles_MissingInputFile() {
        MockMultipartFile referenceFile = new MockMultipartFile("referenceFile", "reference.csv", "text/csv", "ref data".getBytes());

        ResponseEntity<String> response = uploadController.uploadFiles(null, referenceFile);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Input file is missing or empty", response.getBody());
    }

    @Test
    void uploadFiles_MissingReferenceFile() {
        MockMultipartFile inputFile = new MockMultipartFile("inputFile", "input.csv", "text/csv", "test data".getBytes());

        ResponseEntity<String> response = uploadController.uploadFiles(inputFile, null);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Reference file is missing or empty", response.getBody());
    }

    @Test
    void uploadFiles_ServiceException() throws Exception {
        MockMultipartFile inputFile = new MockMultipartFile("inputFile", "input.csv", "text/csv", "test data".getBytes());
        MockMultipartFile referenceFile = new MockMultipartFile("referenceFile", "reference.csv", "text/csv", "ref data".getBytes());

        doThrow(new RuntimeException("Test error")).when(filesUploadService).saveFiles(any(), any());

        ResponseEntity<String> response = uploadController.uploadFiles(inputFile, referenceFile);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Test error", response.getBody());
    }
}