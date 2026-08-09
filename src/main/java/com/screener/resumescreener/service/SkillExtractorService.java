package com.screener.resumescreener.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SkillExtractorService {

    private Set<String> skillsTaxonomy = new HashSet<>();

    @PostConstruct
    public void loadSkills() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = new ClassPathResource("skills-taxonomy.json").getInputStream()) {
            JsonNode root = mapper.readTree(is);
            JsonNode skillsNode = root.get("skills");
            for (JsonNode skill : skillsNode) {
                skillsTaxonomy.add(skill.asText());
            }
        }
    }

    public Set<String> extractSkills(String text) {
        String normalized = text.toLowerCase();
        return skillsTaxonomy.stream()
                .filter(skill -> normalized.contains(skill.toLowerCase()))
                .collect(Collectors.toSet());
    }
}