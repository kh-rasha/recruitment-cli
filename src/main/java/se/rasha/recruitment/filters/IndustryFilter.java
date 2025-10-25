package se.rasha.recruitment.filters;

import se.rasha.recruitment.domain.Candidate;

public class IndustryFilter implements CandidateFilter {
    private final String industry;

    public IndustryFilter(String industry) {
        this.industry = industry;
    }

    @Override
    public boolean matches(Candidate candidate) {
        return candidate.getIndustry().equalsIgnoreCase(industry);
    }
}
