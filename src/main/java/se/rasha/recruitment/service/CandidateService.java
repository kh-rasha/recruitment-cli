package se.rasha.recruitment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.rasha.recruitment.domain.Candidate;
import se.rasha.recruitment.filters.CandidateFilter;
import se.rasha.recruitment.repository.CandidateRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;


// Tjänstelager: använder Repository (DIP) + Streams för filter/sortering.
// OCP för filter uppnås genom att ta emot Predicates (eller egna filter-interface i nästa steg).
public class CandidateService {

    private static final Logger log = LoggerFactory.getLogger(CandidateService.class);

    private final CandidateRepository repository;
    private final ValidationService validation;

    public CandidateService(CandidateRepository repository, ValidationService validation) {
        this.repository = Objects.requireNonNull(repository);
        this.validation = Objects.requireNonNull(validation);
    }

    public Candidate add(Candidate candidate) {
        validation.validateCandidate(candidate);
        Candidate saved = repository.save(candidate);
        log.info("Added candidate: {}", saved.getId());
        return saved;
    }

    public boolean remove(String id) {
        boolean ok = repository.deleteById(id);
        log.info("Removed candidate id={} result={}", id, ok);
        return ok;
    }

    public List<Candidate> listAll() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Candidate::getFirstName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    // --- Stream-baserade operationer ---
    public List<Candidate> filterByIndustry(String industry) {
        return applyFilter(c -> c.getIndustry().equalsIgnoreCase(industry));
    }

    public List<Candidate> filterExperienceAtLeast(int years) {
        return applyFilter(c -> c.getYearsExperience() >= years);
    }

    public List<Candidate> sortByFirstName() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Candidate::getFirstName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public List<Candidate> applyFilter(Predicate<Candidate> predicate) {
        List<Candidate> result = repository.findAll().stream()
                .filter(predicate)
                .collect(Collectors.toList());
        log.info("Filtered candidates count={}", result.size());
        return result;
    }
    public List<Candidate> applyFilters(List<CandidateFilter> filters) {
        return repository.findAll().stream()
                .filter(candidate ->
                        filters.stream().allMatch(f -> f.matches(candidate)))
                .toList();
    }
}
