package se.rasha.recruitment.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.rasha.recruitment.domain.Candidate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


// Enkel in-memory-implementation. Trådsäker karta för enkelhet.
public class InMemoryCandidateRepository implements CandidateRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryCandidateRepository.class);
    private final Map<String, Candidate> data = new ConcurrentHashMap<>();

    @Override
    public Candidate save(Candidate candidate) {
        data.put(candidate.getId(), candidate);
        log.info("Candidate saved: {}", candidate.getId());
        return candidate;
    }

    @Override
    public boolean deleteById(String id) {
        Candidate removed = data.remove(id);
        log.info("Delete candidate id={} result={}", id, removed != null);
        return removed != null;
    }

    @Override
    public Optional<Candidate> findById(String id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Candidate> findAll() {
        return new ArrayList<>(data.values());
    }
}
