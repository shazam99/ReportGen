package com.natwest.reportGen.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ReportService {

    private static final Logger logger = Logger.getLogger(ReportService.class.getName());

    private boolean isProcessed = false;

    @Value("${file.upload-dir}")
    public String filePath;

    @Autowired
    public TransformationService transformationService;

    @Async
    public void generateReport() {

        if(getProcessed()){
            logger.info("Report Already Processed");
        }
        else {
            logger.info("Starting report generation");
            try {
                setProcessed(true);
                transformationService.transform();
                setProcessed(false);
                logger.info("Report generation completed successfully");
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }


    public byte[] readFileContent() throws Exception {
        Path path = Paths.get(filePath + "output.csv");
        logger.info("Attempting to read file from path: " + filePath)  ;
        byte[] content;
        if (getProcessed()) {
            content = "File is being processed!".getBytes();
            logger.warn("File is being processed at path: " + filePath);
        } else if (Files.exists(path)) {
            content = Files.readAllBytes(path);
            logger.info("Successfully read file from path: " + filePath);
        }
        else {
            content = "File not found".getBytes();
            logger.error("File not found at path: " + filePath);
        }
        return content;
    }

    public boolean getProcessed() {
        return this.isProcessed;
    }

    public void setProcessed(boolean val) {
        this.isProcessed = val;
    }
}