package com.natwest.reportGen.service;

import com.natwest.reportGen.model.InputRecord;
import com.natwest.reportGen.model.OutputRecord;
import com.natwest.reportGen.model.ReferenceRecord;
import com.natwest.reportGen.services.FileProcessorService;
import com.natwest.reportGen.services.TransformationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransformationServiceUnitTest {

    private TransformationService transformationService;

    @Mock
    private FileProcessorService fileProcessorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transformationService = new TransformationService();
        transformationService.fileProcessorService = fileProcessorService;
    }

    @Test
    void testTransform() throws Exception {
        // Arrange
        List<ReferenceRecord> referenceRecords = Arrays.asList(
                new ReferenceRecord("key1", "key2", "refdata1", "refdata2", "refdata3", 10.0)
        );
        when(fileProcessorService.readReferenceFile()).thenReturn(referenceRecords);

        // Act
        transformationService.transform();

        // Assert
        verify(fileProcessorService).readReferenceFile();
        verify(fileProcessorService).processFiles(any());
    }


    @Test
    void testCreateReferenceMap() {
        // Arrange
        List<ReferenceRecord> referenceRecords = Arrays.asList(
                new ReferenceRecord("refkey1", "refdata1", "refkey2", "refdata2", "refdata3", 10.0),
                new ReferenceRecord("refkey3", "refdata4", "refkey4", "refdata5", "refdata6", 20.0)
        );

        // Act
        Map<String, List<ReferenceRecord>> result = transformationService.createReferenceMap(referenceRecords);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.containsKey("refkey1:refkey2"));
        assertTrue(result.containsKey("refkey3:refkey4"));
        assertEquals(1, result.get("refkey1:refkey2").size());
        assertEquals(1, result.get("refkey3:refkey4").size());
    }


    @Test
    void testTransformRecord() {
        // Arrange
        InputRecord input = new InputRecord("field1", "field2", "10", "field4", 5.0, "refkey1", "refkey2");
        ReferenceRecord ref = new ReferenceRecord("refkey1", "refdata1", "refkey2", "refdata2", "refdata3", 10.0);
        Map<String, List<ReferenceRecord>> referenceMap = Map.of("refkey1:refkey2", Arrays.asList(ref));

        // Act
        OutputRecord result = transformationService.transformRecord(input, referenceMap);

        // Assert
        assertEquals("field1field2", result.getOutfield1());
        assertEquals("refdata1", result.getOutfield2());
        assertEquals("refdata2refdata3", result.getOutfield3());
        assertEquals(100.0, result.getOutfield4(), 0.001);
        assertEquals(10.0, result.getOutfield5(), 0.001);
    }

}