package se.rasha.recruitment.service;
// TODO: Ansvarar för att validera kandidatdata (ålder, bransch, erfarenhet osv).

import se.rasha.recruitment.domain.Candidate;
public class ValidationService {
    public void validateCandidate(Candidate c ){
        if (c.getFirstName().isBlank()|| c.getLastName().isBlank()){
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if(c.getAge()< 16 || c.getAge()>80){
            throw new IllegalArgumentException("Age must be between 16 and 80");
        }
        if (c.getYearsExperience() < 0 ){
            throw new IllegalArgumentException("Experience cannot be negative");
        }
    }
}
