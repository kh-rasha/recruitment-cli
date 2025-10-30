package se.rasha.recruitment.cli;

import se.rasha.recruitment.domain.Candidate;
import se.rasha.recruitment.filters.AgeFilter;
import se.rasha.recruitment.filters.CandidateFilter;
import se.rasha.recruitment.filters.ExperienceFilter;
import se.rasha.recruitment.filters.IndustryFilter;
import se.rasha.recruitment.repository.InMemoryCandidateRepository;
import se.rasha.recruitment.service.CandidateService;
import se.rasha.recruitment.service.ValidationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


// Enkel konsol-CLI för rekryterare: lägg till, ta bort, visa, filtrera kandidater.
public class Menu {

    private final CandidateService service;
    private final Scanner scanner;

    public Menu(CandidateService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        var repo = new InMemoryCandidateRepository();
        var service = new CandidateService(repo, new ValidationService());

        // Seed för snabb testning (kan tas bort)
        service.add(new Candidate("Ali", "Hassan", 28, "IT", 4));
        service.add(new Candidate("Sara", "Lind", 32, "Finance", 7));
        service.add(new Candidate("Omar", "Khaled", 25, "IT", 2));

        new Menu(service).run();
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = prompt("Välj ett alternativ: ").trim();
            switch (choice) {
                case "1" -> addCandidate();
                case "2" -> removeCandidate();
                case "3" -> listCandidates();
                case "4" -> filterCandidates();
                case "0" -> {
                    System.out.println("Hejdå!");
                    running = false;
                }
                default -> System.out.println("Ogiltigt val, försök igen.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== Rekrytering CLI ===");
        System.out.println("1) Lägg till kandidat");
        System.out.println("2) Ta bort kandidat (via id)");
        System.out.println("3) Visa kandidater (sorterat på förnamn)");
        System.out.println("4) Filtrera kandidater");
        System.out.println("0) Avsluta");
    }

    // --- Helpers ---

    private String prompt(String msg) {
        System.out.print(msg);
        return scanner.nextLine();
    }

    private int promptInt(String msg, int min, int max) {
        while (true) {
            try {
                String s = prompt(msg);
                int val = Integer.parseInt(s.trim());
                if (val < min || val > max) {
                    System.out.printf("Måste vara mellan %d och %d%n", min, max);
                    continue;
                }
                return val;
            } catch (NumberFormatException ex) {
                System.out.println("Ogiltigt nummer, försök igen.");
            }
        }
    }

    // --- Actions ---

    private void addCandidate() {
        System.out.println("\n[Lägg till kandidat]");
        String first = prompt("Förnamn: ").trim();
        String last  = prompt("Efternamn: ").trim();
        int age      = promptInt("Ålder (16-80): ", 16, 80);
        String industry = prompt("Bransch (t.ex. IT, Finance): ").trim();
        int yearsExp = promptInt("År erfarenhet (0-60): ", 0, 60);

        try {
            Candidate c = new Candidate(first, last, age, industry, yearsExp);
            service.add(c);
            System.out.println("✅ Kandidaten lades till. ID: " + c.getId());
        } catch (IllegalArgumentException ex) {
            System.out.println("❌ Fel vid validering: " + ex.getMessage());
        }
    }

    private void removeCandidate() {
        System.out.println("\n[Ta bort kandidat]");
        String id = prompt("Ange kandidat-ID: ").trim();
        boolean ok = service.remove(id);
        System.out.println(ok ? "✅ Borttagen." : "❌ Hittade ej kandidat med det ID:t.");
    }

    private void listCandidates() {
        System.out.println("\n[Visa alla kandidater]");
        var list = service.listAll();
        if (list.isEmpty()) {
            System.out.println("(tom lista)");
            return;
        }
        list.forEach(c -> System.out.printf("- %s | age=%d | industry=%s | years=%d | id=%s%n",
                c.getFullName(), c.getAge(), c.getIndustry(), c.getYearsExperience(), c.getId()));
    }

    private void filterCandidates() {
        System.out.println("\n[Filtrera kandidater]");
        System.out.println("Välj filter (lägg till flera om du vill):");
        System.out.println("  a) Bransch (Industry)");
        System.out.println("  b) Minsta år erfarenhet");
        System.out.println("  c) Ålder inom intervall");
        System.out.println("  x) Kör filtrering");

        List<CandidateFilter> filters = new ArrayList<>();
        while (true) {
            String opt = prompt("Val [a/b/c/x]: ").trim().toLowerCase();
            switch (opt) {
                case "a" -> {
                    String ind = prompt("Ange bransch: ").trim();
                    filters.add(new IndustryFilter(ind));
                    System.out.println("  + IndustryFilter(" + ind + ") tillagd.");
                }
                case "b" -> {
                    int minY = promptInt("Min år erfarenhet: ", 0, 60);
                    filters.add(new ExperienceFilter(minY));
                    System.out.println("  + ExperienceFilter(" + minY + ") tillagd.");
                }
                case "c" -> {
                    int minAge = promptInt("Min ålder: ", 16, 80);
                    int maxAge = promptInt("Max ålder: ", minAge, 80);
                    filters.add(new AgeFilter(minAge, maxAge));
                    System.out.println("  + AgeFilter(" + minAge + "-" + maxAge + ") tillagd.");
                }
                case "x" -> {
                    // kör filtrering
                    var result = filters.isEmpty()
                            ? service.listAll()
                            : service.applyFilters(filters);
                    if (result.isEmpty()) {
                        System.out.println("(inga kandidater matchar dina filter)");
                    } else {
                        System.out.println("Resultat:");
                        result.forEach(c -> System.out.printf("- %s | age=%d | industry=%s | years=%d | id=%s%n",
                                c.getFullName(), c.getAge(), c.getIndustry(), c.getYearsExperience(), c.getId()));
                    }
                    return;
                }
                default -> System.out.println("Ogiltigt val.");
            }
        }
    }
}
