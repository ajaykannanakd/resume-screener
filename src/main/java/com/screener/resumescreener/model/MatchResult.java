
package com.screener.resumescreener.model;

import java.util.Set;

public class MatchResult {

    private double similarityScore;
    private Set<String> matchedSkills;
    private Set<String> missingSkills;

    public MatchResult(double similarityScore, Set<String> matchedSkills, Set<String> missingSkills) {
        this.similarityScore = similarityScore;
        this.matchedSkills = matchedSkills;
        this.missingSkills = missingSkills;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    public Set<String> getMatchedSkills() {
        return matchedSkills;
    }

    public Set<String> getMissingSkills() {
        return missingSkills;
    }
}