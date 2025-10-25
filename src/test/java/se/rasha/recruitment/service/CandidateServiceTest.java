package se.rasha.recruitment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.rasha.recruitment.domain.Candidate;
import se.rasha.recruitment.repository.CandidateRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class) // Aktiverar Mockito i JUnit 5
class CandidateServiceTest {

    @Mock
    CandidateRepository repository;   // DIP: vi mockar gränssnittet

    @Mock
    ValidationService validation;     // Separat validering (SRP) mockas

    @InjectMocks
    CandidateService service;         // System under test (SUT)

    Candidate c1, c2, c3;

    @BeforeEach
    void setup() {
        // Testdata
        c1 = new Candidate("Ali",  "Hassan", 28, "IT",      4);
        c2 = new Candidate("Sara", "Lind",   32, "Finance", 7);
        c3 = new Candidate("Omar", "K.",     25, "IT",      2);
    }

    @Test
    void add_ShouldValidateAndSaveCandidate() {
        //  arrange
        when(repository.save(c1)).thenReturn(c1);

        // act
        Candidate saved = service.add(c1);

        // assert
        assertNotNull(saved);
        assertEquals(c1.getId(), saved.getId());

        // Verifiera att validering och sparande anropades
        verify(validation, times(1)).validateCandidate(c1);
        verify(repository, times(1)).save(c1);
        verifyNoMoreInteractions(validation, repository);
    }

    @Test
    void remove_ShouldReturnTrueWhenRepositoryDeletes() {
        String id = "any-id";
        when(repository.deleteById(id)).thenReturn(true);

        boolean result = service.remove(id);

        assertTrue(result);
        verify(repository).deleteById(id);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void filterByIndustry_ShouldReturnOnlyMatchingIndustry() {
        // arrange
        when(repository.findAll()).thenReturn(List.of(c1, c2, c3));

        // act
        var itOnly = service.filterByIndustry("IT");

        // assert
        assertEquals(2, itOnly.size());
        assertTrue(itOnly.stream().allMatch(c -> c.getIndustry().equalsIgnoreCase("IT")));
        verify(repository, times(1)).findAll();
    }

    @Test
    void sortByFirstName_ShouldReturnAlphabeticalOrder() {
        // arrange: oordnad lista
        when(repository.findAll()).thenReturn(List.of(c3, c2, c1)); // Omar, Sara, Ali

        // act
        var sorted = service.sortByFirstName();

        // assert: alfabetisk på förnamn: Ali, Omar, Sara
        assertEquals(List.of("Ali", "Omar", "Sara"),
                sorted.stream().map(Candidate::getFirstName).toList());

        verify(repository, times(1)).findAll();
    }
    @Test
    void applyFilters_ShouldCombineMultipleConditions() {
        when(repository.findAll()).thenReturn(List.of(c1, c2, c3));

        var filters = List.of(
                new se.rasha.recruitment.filters.IndustryFilter("IT"),
                new se.rasha.recruitment.filters.ExperienceFilter(3)
        );

        var result = service.applyFilters(filters);

        assertEquals(1, result.size());
        assertEquals("Ali", result.get(0).getFirstName());
    }
    @Test
    void add_ShouldThrowWhenValidationFails_AndNotCallRepository() {
        // arrange
        Candidate bad = new Candidate("", "Hassan", 15, "IT", -1);

        doThrow(new IllegalArgumentException("invalid candidate"))
                .when(validation).validateCandidate(bad);

        // act + assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.add(bad));
        assertEquals("invalid candidate", ex.getMessage());

        //  om valideringen faller ska repo inte anropas alls.
        verify(validation, times(1)).validateCandidate(bad);
        verifyNoInteractions(repository);
    }

}
