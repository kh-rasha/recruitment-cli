package se.rasha.recruitment.domain;

import java.util.Objects;
import java.util.UUID;

// Kandidat-domänklass med oföränderliga fält (immutability) för enkelhet och säkerhet.
// SRP: ansvarar endast för att bära data om en kandidat.
public final class Candidate {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final int age;
    private final String industry;          // t.ex. "IT", "Finance"
    private final int yearsExperience;      // antal år erfarenhet

    public Candidate(String firstName, String lastName, int age, String industry, int yearsExperience) {
        this(UUID.randomUUID().toString(), firstName, lastName, age, industry, yearsExperience);
    }

    public Candidate(String id, String firstName, String lastName, int age, String industry, int yearsExperience) {
        this.id = Objects.requireNonNull(id, "id");
        this.firstName = Objects.requireNonNull(firstName, "fiirstName");
        this.lastName = Objects.requireNonNull(lastName, "lastName");
        this.industry = Objects.requireNonNull(industry, "industry");
        this.age = age;
        this.yearsExperience = yearsExperience;
    }

    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getAge() { return age; }
    public String getIndustry() { return industry; }
    public int getYearsExperience() { return yearsExperience; }

    public String getFullName() { return firstName + " " + lastName; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Candidate)) return false;
        Candidate that = (Candidate) o;
        return id.equals(that.id);
    }
    @Override public int hashCode() { return Objects.hash(id); }

    @Override public String toString() {
        return "Candidate{" +
                "id='" + id + '\'' +
                ", name='" + getFullName() + '\'' +
                ", age=" + age +
                ", industry='" + industry + '\'' +
                ", yearsExperience=" + yearsExperience +
                '}';
    }
}
