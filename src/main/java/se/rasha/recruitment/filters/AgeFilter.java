package se.rasha.recruitment.filters;

import se.rasha.recruitment.domain.Candidate;

public class AgeFilter implements CandidateFilter {
    private final int minAge;
    private final int maxAge;

    public AgeFilter(int minAge, int maxAge) {
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    @Override
    public boolean matches(Candidate candidate) {
        return candidate.getAge() >= minAge && candidate.getAge() <= maxAge;
    }
}
