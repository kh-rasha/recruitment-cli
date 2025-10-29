package se.rasha.recruitment.repository;

import se.rasha.recruitment.domain.Candidate;

import java.util.List;
import java.util.Optional;


// Repository-gränssnitt: DIP - tjänstelagret beror på gränssnitt, inte konkreta implementationer.
public interface CandidateRepository {
    Candidate save(Candidate candidate);
    boolean deleteById(String id);
    Optional<Candidate> findById(String id);
    List<Candidate> findAll();
}