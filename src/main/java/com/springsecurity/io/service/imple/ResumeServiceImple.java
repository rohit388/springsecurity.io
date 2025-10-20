package com.springsecurity.io.service.imple;

import com.itextpdf.html2pdf.HtmlConverter;
import com.springsecurity.io.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class ResumeServiceImple implements ResumeService {

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public Map<String, Object> loadResumeData() throws IOException {
        Properties properties = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("resume.txt")) {
            if (is == null) {
                throw new IOException("resume.txt not found");
            }
            properties.load(is);
        }

        Map<String, Object> resumeData = new HashMap<>();
        List<String> skillsList = new ArrayList<>();

        for (String key : properties.stringPropertyNames()) {
            if (key.startsWith("skills")) {
                String skillsString = properties.getProperty(key);
                if (skillsString != null && !skillsString.isEmpty()) {
                    String[] skillsArray = skillsString.split("[,:]");
                    for (String skill : skillsArray) {
                        skillsList.add(skill.trim());
                    }
                }
            } else if (!key.startsWith("projects.") && !key.startsWith("education.")) {
                resumeData.put(key, properties.getProperty(key));
            }
        }
        resumeData.put("skills", skillsList);

        if (!resumeData.containsKey("github") || resumeData.get("github").toString().isEmpty()) {
            resumeData.put("github", "Not Available");
        }

        List<Map<String, String>> projects = new ArrayList<>();
        for (int i = 1; ; i++) {
            String titleKey = "projects." + i + ".title";
            if (!properties.containsKey(titleKey)) {
                break;
            }
            Map<String, String> project = new HashMap<>();
            project.put("title", properties.getProperty(titleKey));
            project.put("duration", properties.getProperty("projects." + i + ".duration"));
            project.put("description", properties.getProperty("projects." + i + ".description"));

            String techStackKey1 = "projects." + i + ".tech";
            String techStackKey2 = "projects." + i + ".Tech Stack";

            String techStack = null;
            if (properties.containsKey(techStackKey1)) {
                techStack = properties.getProperty(techStackKey1);
            } else if (properties.containsKey(techStackKey2)) {
                techStack = properties.getProperty(techStackKey2);
            }

            if (techStack != null) {
                project.put("tech", techStack);
            }

            projects.add(project);
        }
        resumeData.put("projects", projects);

        List<Map<String, String>> educations = new ArrayList<>();
        for (int i = 1; ; i++) {
            String degreeKey = "education." + i + ".degree";
            if (!properties.containsKey(degreeKey)) {
                break;
            }
            Map<String, String> education = new HashMap<>();
            education.put("degree", properties.getProperty(degreeKey));
            education.put("university", properties.getProperty("education." + i + ".university"));
            education.put("duration", properties.getProperty("education." + i + ".duration"));
            educations.add(education);
        }
        resumeData.put("educations", educations);

        return resumeData;
    }

    @Override
    public byte[] generateResumePdf(Map<String, Object> resumeData, String profile) throws IOException {
        Context context = new Context();

        List<String> keywordsToHighlight = Arrays.asList("Java", "Spring Boot", "Kafka", "JUnit", "Mockito", "PostgreSQL", "MySQL", "CI/CD", "Tech Stack:", "Cosmos DB", "Microservices", "Jira", "Lambdas", "Streams", "Functional Interfaces");

        String summary = (String) resumeData.get("summary");
        resumeData.put("summary", highlightKeywords(summary, keywordsToHighlight));

        List<String> skills = (List<String>) resumeData.get("skills");
        if (skills != null) {
            List<String> highlightedSkills = new ArrayList<>();
            for (String skill : skills) {
                highlightedSkills.add(highlightKeywords(skill, keywordsToHighlight));
            }
            resumeData.put("skills", highlightedSkills);
        }

        List<Map<String, String>> projects = (List<Map<String, String>>) resumeData.get("projects");
        if (projects != null) {
            for (Map<String, String> project : projects) {
                String description = project.get("description");
                project.put("description", highlightKeywords(description, keywordsToHighlight));
                String tech = project.get("tech");
                project.put("tech", highlightKeywords(tech, keywordsToHighlight));
            }
        }

        context.setVariable("resume", resumeData);

        int atsScore = calculateAtsScore(resumeData, profile);
        resumeData.put("atsScore", atsScore);

        String templateName = "resume";
        if (profile != null) {
            templateName = "resume-" + profile;
        }

        String html = templateEngine.process(templateName, context);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(html, outputStream);
        return outputStream.toByteArray();
    }

    private String highlightKeywords(String text, List<String> keywords) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        String highlightedText = text;
        for (String keyword : keywords) {
            highlightedText = highlightedText.replaceAll("(?i)\\b(" + keyword + ")\\b", "<span class='highlight'>$1</span>");
        }
        return highlightedText;
    }

    public int calculateAtsScore(Map<String, Object> resumeData, String profile) {
        int score = 0;
        String summary = (String) resumeData.get("summary");
        List<String> skills = (List<String>) resumeData.get("skills");

        String combinedText = (summary + " " + String.join(" ", skills)).toLowerCase();

        if ("backend".equals(profile)) {
            if (combinedText.contains("java")) score += 20;
            if (combinedText.contains("spring boot")) score += 20;
            if (combinedText.contains("spring security")) score += 20;
            if (combinedText.contains("hibernate")) score += 20;
            if (combinedText.contains("jpa")) score += 20;
        } else if ("frontend".equals(profile)) {
            if (combinedText.contains("javascript")) score += 20;
            if (combinedText.contains("react")) score += 20;
            if (combinedText.contains("angular")) score += 20;
            if (combinedText.contains("vue")) score += 20;
        } else if ("devops".equals(profile)) {
            if (combinedText.contains("docker")) score += 20;
            if (combinedText.contains("jenkins")) score += 20;
            if (combinedText.contains("kubernetes")) score += 20;
            if (combinedText.contains("aws")) score += 20;
        } else { // fullstack or default
            if (combinedText.contains("java")) score += 15;
            if (combinedText.contains("spring boot")) score += 15;
            if (combinedText.contains("spring security")) score += 15;
            if (combinedText.contains("hibernate")) score += 15;
            if (combinedText.contains("jpa")) score += 15;
            if (combinedText.contains("microservices") || combinedText.contains("micro services")) score += 20;
            if (combinedText.contains("rest api") || combinedText.contains("restful api")) score += 5;
            if (combinedText.contains("kafka")) score += 15;
            if (combinedText.contains("mysql")) score += 5;
            if (combinedText.contains("postgresql")) score += 5;
            if (combinedText.contains("jenkins")) score += 5;
            if (combinedText.contains("git")) score += 5;
            if (combinedText.contains("agile")) score += 5;
            if (combinedText.contains("docker") || combinedText.contains("dockers")) score += 5;
            if (combinedText.contains("j2ee")) score += 5;
            if (combinedText.contains("junit")) score += 5;
            if (combinedText.contains("mockito")) score += 5;
            if (combinedText.contains("ci/cd")) score += 15;
            if (combinedText.contains("azure")) score += 5;
        }

        score = Math.max(score, 85);
        score = Math.min(score, 100);

        return score;
    }
}
