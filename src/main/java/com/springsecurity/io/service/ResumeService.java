package com.springsecurity.io.service;

import java.io.IOException;
import java.util.Map;

public interface ResumeService {
    Map<String, Object> loadResumeData() throws IOException;
    byte[] generateResumePdf(Map<String, Object> resumeData, String profile) throws IOException;
    int calculateAtsScore(Map<String, Object> resumeData, String profile);
}
