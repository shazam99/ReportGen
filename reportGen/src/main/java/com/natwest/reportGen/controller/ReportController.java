package com.natwest.reportGen.controller;

import com.natwest.reportGen.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
public class ReportController {



    @Autowired
    public ReportService reportService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateReport() {
        try {
            reportService.generateReport();
            return ResponseEntity.ok("Report generation request received and is being processed.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error starting report generation: " + e.getMessage());
        }
    }

    @GetMapping("/fetch")
    public ResponseEntity<byte[]> fetchReport() {

        try {
            byte[] content = reportService.readFileContent();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", "report.csv");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (Exception e) {
            if (e.getMessage().equals("File not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage().getBytes());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage().getBytes());
            }
        }
    }
}