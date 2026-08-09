package com.screener.resumescreener.controller;

import com.screener.resumescreener.model.MatchResult;
import com.screener.resumescreener.service.DocumentParserService;
import com.screener.resumescreener.service.ScoringService;
import com.screener.resumescreener.service.SkillExtractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/screen")
public class ScreeningController {

    @Autowired private DocumentParserService parserService;
    @Autowired private SkillExtractorService skillService;
    @Autowired private ScoringService scoringService;

    @PostMapping
    public MatchResult screenResume(
            @RequestParam("resume") MultipartFile resume,
            @RequestParam("jobDescription") String jobDescription) throws IOException {

        String resumeText = parserService.extractText(resume);

        Set<String> resumeSkills = skillService.extractSkills(resumeText);
        Set<String> jdSkills = skillService.extractSkills(jobDescription);

        Set<String> matched = new HashSet<>(resumeSkills);
        matched.retainAll(jdSkills);

        Set<String> missing = new HashSet<>(jdSkills);
        missing.removeAll(resumeSkills);

        double similarityScore = scoringService.computeSimilarity(resumeText, jobDescription);

        return new MatchResult(similarityScore, matched, missing);
    }
}