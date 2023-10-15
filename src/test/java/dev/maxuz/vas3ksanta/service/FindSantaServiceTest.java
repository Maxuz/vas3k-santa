package dev.maxuz.vas3ksanta.service;

import dev.maxuz.vas3ksanta.model.CountryEntity;
import dev.maxuz.vas3ksanta.model.FindSantaResult;
import dev.maxuz.vas3ksanta.model.GrandchildEntity;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class FindSantaServiceTest {
    private static final Random random = new Random();

    private final FindSantaService service = new FindSantaService();

    private static CountryEntity getCountry(String name) {
        CountryEntity country = new CountryEntity();
        country.setName(name);
        return country;
    }

    private static GrandchildEntity getGrandchild(String country, List<String> destCountries) {
        var grandchild = new GrandchildEntity();
        grandchild.setId(random.nextLong());
        grandchild.setCountry(getCountry(country));
        grandchild.setRecipientCountries(destCountries.stream().map(FindSantaServiceTest::getCountry).collect(Collectors.toList()));
        return grandchild;
    }

    private static void verifySenderAndRecipient(FindSantaResult result) {
        for (Map.Entry<GrandchildEntity, GrandchildEntity> entry : result.getMatchedMap().entrySet()) {
            assertNotEquals(entry.getKey().getId(), entry.getValue().getId());
        }
    }

    @Test
    void findSanta_TwoGrandchild_NoUnmatched() {
        List<GrandchildEntity> grandchildren = List.of(
            getGrandchild("Germany", List.of("Germany")),
            getGrandchild("Germany", List.of("Germany"))
        );

        FindSantaResult result = service.findSanta(grandchildren);

        assertEquals(0, result.getUnmatchedMap().size());
        assertEquals(2, result.getMatchedMap().size());
        verifySenderAndRecipient(result);
    }

    @Test
    void findSanta_ThreeGrandchild_NoUnmatched() {
        List<GrandchildEntity> grandchildren = List.of(
            getGrandchild("France", List.of("France")),
            getGrandchild("France", List.of("France")),
            getGrandchild("France", List.of("France"))
        );

        FindSantaResult result = service.findSanta(grandchildren);

        assertEquals(0, result.getUnmatchedMap().size());
        assertEquals(3, result.getMatchedMap().size());
        verifySenderAndRecipient(result);
    }

    @Test
    void findSanta_ThreeGrandchildWithMultipleDestCountries_NoUnmatched() {
        List<GrandchildEntity> grandchildren = List.of(
            getGrandchild("Germany", List.of("Germany", "France")),
            getGrandchild("Germany", List.of("France", "Germany")),
            getGrandchild("Germany", List.of("Germany", "France"))
        );

        FindSantaResult result = service.findSanta(grandchildren);

        assertEquals(0, result.getUnmatchedMap().size());
        assertEquals(3, result.getMatchedMap().size());
        verifySenderAndRecipient(result);
    }

    @Test
    void findSanta_SingleChild_NoMatched() {
        List<GrandchildEntity> grandchildren = List.of(
            getGrandchild("Zimbabwe", List.of("Zimbabwe"))
        );

        FindSantaResult result = service.findSanta(grandchildren);

        assertEquals(0, result.getMatchedMap().size());
        assertEquals(1, result.getUnmatchedMap().size());
        assertEquals("Не смог найти Санту для ребенка", result.getUnmatchedMap().get(grandchildren.get(0)));
    }

    @Test
    void findSanta_ThreeFromDifferentCountries_AllUnmatched() {
        List<GrandchildEntity> grandchildren = List.of(
            getGrandchild("France", List.of("France")),
            getGrandchild("Germany", List.of("Germany")),
            getGrandchild("China", List.of("China"))
        );

        FindSantaResult result = service.findSanta(grandchildren);

        assertEquals(3, result.getUnmatchedMap().size());
        assertEquals(0, result.getMatchedMap().size());
    }

    @Test
    void findSanta_ThreeGrandchildSequentiallyMatched_NoUnmatched() {
        List<GrandchildEntity> grandchildren = List.of(
            getGrandchild("Germany", List.of("Germany", "France")),
            getGrandchild("France", List.of("France", "Germany")),
            getGrandchild("France", List.of("France"))
        );

        FindSantaResult result = service.findSanta(grandchildren);

        assertEquals(0, result.getUnmatchedMap().size());
        assertEquals(3, result.getMatchedMap().size());
        verifySenderAndRecipient(result);
    }

    @Test
    void findSanta_ThreeGrandchildSequentiallyMatched2_NoUnmatched() {
        List<GrandchildEntity> grandchildren = List.of(
            getGrandchild("Mexico", List.of("Mexico", "Peru", "Serbia")),
            getGrandchild("Mexico", List.of("Mexico", "Peru")),
            getGrandchild("Serbia", List.of("Serbia", "Mexico"))
        );

        FindSantaResult result = service.findSanta(grandchildren);

        assertEquals(0, result.getUnmatchedMap().size());
        assertEquals(3, result.getMatchedMap().size());
        verifySenderAndRecipient(result);
    }

    @Test
    void findSanta_TwoMatchOneDoesNot() {
        List<GrandchildEntity> grandchildren = List.of(
            getGrandchild("Germany", List.of("Germany", "France")),
            getGrandchild("Germany", List.of("France", "Germany")),
            getGrandchild("Peru", List.of("Peru"))
        );

        FindSantaResult result = service.findSanta(grandchildren);

        assertEquals(1, result.getUnmatchedMap().size());
        assertEquals(2, result.getMatchedMap().size());
        verifySenderAndRecipient(result);
    }

    @Test
    void findSanta_MultipleSendersSingleRecipient_OneMatchesTwoDoNot() {
        List<GrandchildEntity> grandchildren = List.of(
            getGrandchild("Mexico", List.of("Peru")),
            getGrandchild("Mexico", List.of("Peru")),
            getGrandchild("Peru", List.of("Peru"))
        );

        FindSantaResult result = service.findSanta(grandchildren);

        assertEquals(2, result.getUnmatchedMap().size());
        assertEquals(1, result.getMatchedMap().size());
        verifySenderAndRecipient(result);
    }
}
