package com.springsecurity.io.controller;

import com.itextpdf.html2pdf.HtmlConverter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/resume")
public class ResumeController {

    @Autowired
    private SpringTemplateEngine templateEngine;

    @GetMapping("/generate")
    public ResponseEntity<byte[]> generateResume() throws IOException {
        // Create a Thymeleaf context
        Context context = new Context();

        // Create a map to hold the resume data
        Map<String, Object> resumeData = loadResumeData();

        // Add the resume data to the context
        context.setVariable("resume", resumeData);

        // Calculate ATS score
        int atsScore = calculateAtsScore(resumeData);
        resumeData.put("atsScore", atsScore);

        // Process the Thymeleaf template
        String html = templateEngine.process("resume", context);

        // Generate the PDF from the HTML
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(html, outputStream);

        // Set the response headers
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename("resume.pdf")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(contentDisposition);

        // Return the PDF as a byte array
        return ResponseEntity.ok()
                .headers(headers)
                .body(outputStream.toByteArray());
    }

    private Map<String, Object> loadResumeData() throws IOException {
      Properties properties = new Properties();
      try (InputStream is = getClass().getClassLoader().getResourceAsStream("resume.txt")) {
          if (is == null) {
              throw new IOException("resume.txt not found");
          }
          properties.load(is);
      }

      Map<String, Object> resumeData = new HashMap<>();
      for (String key : properties.stringPropertyNames()) {
          if (key.equals("skills")) {
              String skillsString = properties.getProperty(key);
              List<String> skillsList = new ArrayList<>();
              if (skillsString != null && !skillsString.isEmpty()) {
                  String[] skillsArray = skillsString.split("[,:]");
                  for (String skill : skillsArray) {
                      skillsList.add(skill.trim());
                  }
              }
              resumeData.put(key, skillsList);
          } else if (!key.startsWith("projects.") && !key.startsWith("education.")) { // Exclude project and education properties from being added directly
              resumeData.put(key, properties.getProperty(key));
          }
      }
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

          String description = properties.getProperty("projects." + i + ".description");
          String techStackKey1 = "projects." + i + ".tech";
          String techStackKey2 = "projects." + i + ".Tech Stack";

          String techStack = null;
          if (properties.containsKey(techStackKey1)) {
              techStack = properties.getProperty(techStackKey1);
          } else if (properties.containsKey(techStackKey2)) {
              techStack = properties.getProperty(techStackKey2);
          }

          if (techStack != null) {
              description += "\n\n" + techStack;
          }
          project.put("description", description);

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

    private int calculateAtsScore(Map<String, Object> resumeData) {
        int score = 0;
        String summary = (String) resumeData.get("summary");
        List<String> skills = (List<String>) resumeData.get("skills");

        String combinedText = (summary + " " + String.join(" ", skills)).toLowerCase();

        if (combinedText.contains("java")) score += 20;
        if (combinedText.contains("spring boot")) score += 20;
        if (combinedText.contains("spring security")) score += 15;
        if (combinedText.contains("hibernate")) score += 10;
        if (combinedText.contains("jpa")) score += 10;
        if (combinedText.contains("microservices") || combinedText.contains("micro services")) score += 20;
        if (combinedText.contains("rest api") || combinedText.contains("restful api")) score += 10;
        if (combinedText.contains("kafka")) score += 15;
        if (combinedText.contains("mysql")) score += 5;
        if (combinedText.contains("postgresql")) score += 5;
        if (combinedText.contains("jenkins")) score += 10;
        if (combinedText.contains("git")) score += 5;
        if (combinedText.contains("agile")) score += 5;
        if (combinedText.contains("docker") || combinedText.contains("dockers")) score += 10;
        if (combinedText.contains("j2ee")) score += 5;
        if (combinedText.contains("junit")) score += 5;
        if (combinedText.contains("mockito")) score += 5;
        if (combinedText.contains("ci/cd")) score += 15;
        if (combinedText.contains("azure")) score += 10;
        if (combinedText.contains("aws")) score += 10;
        if (combinedText.contains("gcp")) score += 10;
        if (combinedText.contains("kubernetes")) score += 10;

        score = Math.max(score, 90);
        score = Math.min(score, 100);

        return score;
    }

    @PostMapping("/upload")
    public ResponseEntity<byte[]> generateResumeFromPdf(@RequestParam("file") MultipartFile file) {
        try {
            // Extract text from the PDF
            PDDocument document = PDDocument.load(file.getInputStream());
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            document.close();

            // Parse the text to create resume data (this is a simplified approach)
            Map<String, Object> resumeData = parseResumeText(text);

            // Calculate ATS score
            int atsScore = calculateAtsScore(resumeData);
            resumeData.put("atsScore", atsScore);

            // Create a Thymeleaf context
            Context context = new Context();
            context.setVariable("resume", resumeData);

            // Process the Thymeleaf template
            String html = templateEngine.process("resume", context);

            // Generate the PDF from the HTML
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            HtmlConverter.convertToPdf(html, outputStream);

            // Set the response headers
            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename("resume_with_ats.pdf")
                    .build();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(contentDisposition);

            // Return the PDF as a byte array
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            // Handle exceptions
            return ResponseEntity.status(500).build();
        }
    }

    private Map<String, Object> parseResumeText(String text) {
        // Sanitize the text by removing non-ASCII characters
        String sanitizedText = text.replaceAll("[^\\x00-\\x7F]", "");

        Map<String, Object> resumeData = new HashMap<>();

        // Basic parsing using regular expressions
        resumeData.put("name", extractValue(sanitizedText, "(?i)(^[A-Z][a-zA-Z]+(?: [A-Z][a-zA-Z]+)+)"));
        resumeData.put("email", extractValue(sanitizedText, "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}"));
        resumeData.put("phone", extractValue(sanitizedText, "\\+?[0-9. ()-]{10,25}"));
        resumeData.put("linkedin", extractValue(sanitizedText, "(?i)(linkedin.com/in/[a-zA-Z0-9-]+)"));

        // Extract sections
        resumeData.put("summary", extractSection(sanitizedText, "Summary", "Experience"));
        String skillsSection = extractSection(sanitizedText, "Skills", "Experience");
        List<String> skillsList = new ArrayList<>();
        if (skillsSection != null && !skillsSection.isEmpty()) {
            String[] skillsArray = skillsSection.split("[,:\\n]+");
            for (String skill : skillsArray) {
                String trimmedSkill = skill.trim();
                if (!trimmedSkill.isEmpty()) {
                    skillsList.add(trimmedSkill);
                }
            }
        }
        resumeData.put("skills", skillsList);

        // Parse Experience section
        String experienceSection = extractSection(sanitizedText, "Experience", "Education");
        List<Map<String, String>> experiences = new ArrayList<>();
        if (experienceSection != null && !experienceSection.isEmpty()) {
            // This is a very simplified approach to parsing experiences.
            // A more robust solution would require more advanced NLP techniques.
            String[] experienceEntries = experienceSection.split("\n\n"); // Assuming double newline separates entries
            for (String entry : experienceEntries) {
                Map<String, String> experience = new HashMap<>();
                String[] lines = entry.split("\n");
                if (lines.length > 0) {
                    experience.put("title", lines[0]);
                }
                if (lines.length > 1) {
                    experience.put("company", lines[1]);
                }
                if (lines.length > 2) {
                    experience.put("duration", lines[2]);
                }
                if (lines.length > 3) {
                    experience.put("description", lines[3]);
                }
                experiences.add(experience);
            }
        }
        // This will be used to populate the projects in the template
        resumeData.put("projects", experiences);


        // Parse Education section
        String educationSection = extractSection(sanitizedText, "Education", "Skills");
        List<Map<String, String>> educations = new ArrayList<>();
        if (educationSection != null && !educationSection.isEmpty()) {
            String[] educationEntries = educationSection.split("\n\n"); // Assuming double newline separates entries
            for (String entry : educationEntries) {
                Map<String, String> education = new HashMap<>();
                String[] lines = entry.split("\n");
                if (lines.length > 0) {
                    education.put("degree", lines[0]);
                }
                if (lines.length > 1) {
                    education.put("university", lines[1]);
                }
                if (lines.length > 2) {
                    education.put("duration", lines[2]);
                }
                educations.add(education);
            }
        }
        resumeData.put("educations", educations);


        return resumeData;
    }

    private String extractValue(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    private String extractSection(String text, String startKeyword, String endKeyword) {
        try {
            Pattern startPattern = Pattern.compile("(?i)" + startKeyword);
            Matcher startMatcher = startPattern.matcher(text);

            if (startMatcher.find()) {
                int startIndex = startMatcher.end();

                if (endKeyword != null && !endKeyword.isEmpty()) {
                    Pattern endPattern = Pattern.compile("(?i)" + endKeyword);
                    Matcher endMatcher = endPattern.matcher(text);

                    if (endMatcher.find(startIndex)) {
                        return text.substring(startIndex, endMatcher.start()).trim();
                    } else {
                        return text.substring(startIndex).trim();
                    }
                } else {
                    return text.substring(startIndex).trim();
                }
            }
        } catch (Exception e) {
            // Log the exception
        }
        return "";
    }

}
