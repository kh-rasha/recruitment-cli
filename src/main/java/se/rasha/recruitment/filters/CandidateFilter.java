package se.rasha.recruitment.filters;
import se.rasha.recruitment.domain.Candidate;


@FunctionalInterface
public interface CandidateFilter {
    boolean matches(Candidate candidate);
}