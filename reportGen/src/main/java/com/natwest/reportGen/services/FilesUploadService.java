package com.natwest.reportGen.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FilesUploadService {

    @Value("${file.upload-dir}")
    public String uploadDir;

    @Autowired
    public ReportService reportService;

    private static final Logger logger = Logger.getLogger(FilesUploadService.class.getName());

    public void saveFiles(MultipartFile inputFile, MultipartFile referenceFile) throws IOException {
        logger.info("Request received for upload files. ");
        validateFileNames(inputFile, referenceFile);
        logger.info("Request validated for upload files");
        Path fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(fileStorageLocation);
            logger.info("Directory found/created for upload files");
        } catch (Exception ex) {
            logger.error("Directory not found for upload files. " + ex.getMessage());
            return;
        }
        emptyUploadDir(fileStorageLocation);
        reportService.setProcessed(false);
        logger.info("Directory cleared for upload files");
        saveFile(inputFile, fileStorageLocation);
        logger.info("Input file uploaded");
        saveFile(referenceFile, fileStorageLocation);
        logger.info("Reference file uploaded");
    }

    private void validateFileNames(MultipartFile inputFile, MultipartFile referenceFile) {
        if (!"input.csv".equals(inputFile.getOriginalFilename())) {
            throw new IllegalArgumentException("Invalid file name for input file. Expected: input.csv");
        }
        if (!"reference.csv".equals(referenceFile.getOriginalFilename())) {
            throw new IllegalArgumentException("Invalid file name for reference file. Expected: reference.csv");
        }
    }

    private void saveFile(MultipartFile file, Path fileStorageLocation) throws IOException {
        Path targetLocation = fileStorageLocation.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), targetLocation);
    }

    private void emptyUploadDir(Path fileStorageLocation) throws IOException {
        Files.walk(fileStorageLocation)
                .filter(path -> !Files.isDirectory(path))
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to delete file: " + path, e);
                    }
                });
    }
}
