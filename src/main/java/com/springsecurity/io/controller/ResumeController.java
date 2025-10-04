package com.springsecurity.io.controller;

import com.springsecurity.io.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @GetMapping("/generate")
    public ResponseEntity<byte[]> generateResume() throws IOException {
        Map<String, Object> resumeData = resumeService.loadResumeData();
        byte[] pdfBytes = resumeService.generateResumePdf(resumeData);

        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename("resume.pdf")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(contentDisposition);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
