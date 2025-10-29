
##  Kommande funktioner
- Lägg till/ta bort/visa kandidater
- Filtrering via Streams & Lambda
- SLF4J-loggning
- JUnit-testning med Mockito

##  För VG
Refaktorisering för att följa:
- Single Responsibility
- Open/Closed
- Dependency Inversion

# Use Cases

### 1️⃣ Lägg till kandidat
**Som:** Rekryterare  
**Vill jag:** Mata in kandidatdata (namn, ålder, bransch, erfarenhet)  
**Så att:** Jag kan spara kandidaten i systemet.

### 2️⃣ Ta bort kandidat
**Som:** Rekryterare  
**Vill jag:** Ange ett ID för en kandidat  
**Så att:** Jag kan ta bort den från listan.

### 3️⃣ Visa kandidater
**Som:** Rekryterare  
**Vill jag:** Se alla kandidater på skärmen  
**Så att:** Jag får en översikt.

### 4️⃣ Filtrera kandidater
**Som:** Rekryterare  
**Vill jag:** Filtrera baserat på bransch eller erfarenhet  
**Så att:** Jag snabbt hittar rätt profiler.

- ## Prompt-engineering (LLM)
I denna uppgift använde jag en LLM (ChatGPT) som “par-programmerare” för att snabba upp design och test.

**Mål med prompterna**
- Säkerställa korrekt tillämpning av SOLID (SRP, OCP, DIP).
- Få exempel på Stream-baserad filtrering/sortering.
- Generera JUnit/Mockito-tester som isolerar beroenden.

**Exempel på prompt (förkortad)**
> “Jag bygger en rekrytering-CLI i Java. Jag vill följa SRP/OCP/DIP.  
> Ge mig ett `CandidateRepository`-interface, en `InMemoryCandidateRepository` med SLF4J-loggning,
> och en `CandidateService` som använder Streams för filter och sortering.  
> Lägg svenska kommentarer i koden för läraren.”

**Hur jag itererade prompterna**
- Började med en grov struktur (Domain/Repository/Service).
- Bad om förbättringar: lägga till `ValidationService` (SRP) och `CandidateFilter`-interface (OCP) för att kunna lägga till nya filter utan att ändra befintlig kod.
- Bad om JUnit 5 + Mockito-tester (isolerad testning via mocks), inklusive ett test för valideringsfel.

**Designbeslut (motivation)**
- **SRP**: `ValidationService` separerar validering från affärslogik i `CandidateService`.
- **DIP**: `CandidateService` beror på `CandidateRepository` (interface), inte en konkret klass.
- **OCP**: Nya filter implementeras via `CandidateFilter` (t.ex. `IndustryFilter`, `ExperienceFilter`) utan att ändra `CandidateService`.
- **Streams**: Filtrering och sortering implementeras med Stream API för tydlighet och uttrycksfullhet.
- **SLF4J**: `slf4j-simple` för enkel loggning till konsol.

**Verifiering**
- Enhetstester:
    - `add()` validerar och sparar korrekt (verifiering av ordning och anrop).
    - `remove()` returnerar rätt värde från repository.
    - `filterByIndustry()` و `sortByFirstName()` Streams.
    - `applyFilters()` (OCP).
    - **Validering**: `add()` repository.

