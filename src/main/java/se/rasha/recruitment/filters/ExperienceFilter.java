package se.rasha.recruitment.filters;

import se.rasha.recruitment.domain.Candidate;

public class ExperienceFilter implements CandidateFilter {
    private final int minYears;

    public ExperienceFilter(int minYears) {
        this.minYears = minYears;
    }

    @Override
    public boolean matches(Candidate candidate) {
        return candidate.getYearsExperience() >= minYears;
    }
}
