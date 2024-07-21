package com.natwest.reportGen.controller;

import com.natwest.reportGen.services.FilesUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    public FilesUploadService filesUploadService;

    @PostMapping(path="/file", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFiles(@RequestParam("inputFile") MultipartFile inputFile, @RequestParam("referenceFile") MultipartFile referenceFile) {
        try {
            if (inputFile == null || inputFile.isEmpty()) {
                return ResponseEntity.badRequest().body("Input file is missing or empty");
            }
            if (referenceFile == null || referenceFile.isEmpty()) {
                return ResponseEntity.badRequest().body("Reference file is missing or empty");
            }
            filesUploadService.saveFiles(inputFile, referenceFile);
            return ResponseEntity.ok("Files uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
