package com.gigwager;

import com.gigwager.model.content.CityRichContent;
import com.gigwager.model.content.PersonaQuote;
import com.gigwager.service.CityRichContentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CityRichContentRepositoryTest {

    @Autowired
    private CityRichContentRepository repository;

    @Test
    public void shouldLoadExpandedCityFiles() {
        Set<String> expectedCities = Set.of(
                "austin",
                "houston",
                "san-francisco",
                "new-york",
                "los-angeles",
                "chicago",
                "miami",
                "seattle",
                "atlanta",
                "dallas",
                "phoenix",
                "washington-dc",
                "boston",
                "denver",
                "las-vegas",
                "philadelphia",
                "san-diego",
                "san-jose",
                "san-antonio",
                "nashville");

        for (String slug : expectedCities) {
            assertTrue(repository.findBySlug(slug).isPresent(), slug + " rich content should load");
        }
        assertTrue(repository.findAll().size() >= 20, "repository should contain at least 20 rich city files");
    }

    @Test
    public void eachWorkLevelShouldHavePersonaTypeDiversity() {
        for (CityRichContent city : repository.findAll()) {
            for (String workLevel : Set.of("part-time", "side-hustle", "full-time")) {
                var content = city.workLevels().get(workLevel);
                assertTrue(content != null, city.citySlug() + " should define " + workLevel);
                assertTrue(content.personaQuotes() != null && content.personaQuotes().size() >= 2,
                        city.citySlug() + " " + workLevel + " should have at least two persona quotes");

                Set<String> personaTypes = content.personaQuotes().stream()
                        .map(PersonaQuote::personaType)
                        .map(Enum::name)
                        .collect(Collectors.toSet());
                assertTrue(personaTypes.size() >= 2,
                        city.citySlug() + " " + workLevel + " should include at least two persona types");
            }
        }
    }
}
