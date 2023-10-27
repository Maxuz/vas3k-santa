package dev.maxuz.vas3ksanta.service;

import dev.maxuz.vas3ksanta.model.CountryEntity;
import dev.maxuz.vas3ksanta.model.FindSantaResult;
import dev.maxuz.vas3ksanta.model.UserEntity;
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

    private static UserEntity getUser(String country, List<String> destCountries) {
        var user = new UserEntity();
        user.setId(random.nextLong());
        user.setCountry(getCountry(country));
        user.setRecipientCountries(destCountries.stream().map(FindSantaServiceTest::getCountry).collect(Collectors.toList()));
        return user;
    }

    private static void verifySenderAndRecipient(FindSantaResult result) {
        for (Map.Entry<UserEntity, UserEntity> entry : result.getMatchedMap().entrySet()) {
            assertNotEquals(entry.getKey().getId(), entry.getValue().getId());
        }
    }

    @Test
    void findSanta_TwoUsers_NoUnmatched() {
        List<UserEntity> users = List.of(
            getUser("Germany", List.of("Germany")),
            getUser("Germany", List.of("Germany"))
        );

        FindSantaResult result = service.findSanta(users);

        assertEquals(0, result.getUnmatchedMap().size());
        assertEquals(2, result.getMatchedMap().size());
        verifySenderAndRecipient(result);
    }

    @Test
    void findSanta_ThreeUsers_NoUnmatched() {
        List<UserEntity> users = List.of(
            getUser("France", List.of("France")),
            getUser("France", List.of("France")),
            getUser("France", List.of("France"))
        );

        FindSantaResult result = service.findSanta(users);

        assertEquals(0, result.getUnmatchedMap().size());
        assertEquals(3, result.getMatchedMap().size());
        verifySenderAndRecipient(result);
    }

    @Test
    void findSanta_ThreeUsersWithMultipleDestCountries_NoUnmatched() {
        List<UserEntity> users = List.of(
            getUser("Germany", List.of("Germany", "France")),
            getUser("Germany", List.of("France", "Germany")),
            getUser("Germany", List.of("Germany", "France"))
        );

        FindSantaResult result = service.findSanta(users);

        assertEquals(0, result.getUnmatchedMap().size());
        assertEquals(3, result.getMatchedMap().size());
        verifySenderAndRecipient(result);
    }

    @Test
    void findSanta_SingleChild_NoMatched() {
        List<UserEntity> users = List.of(
            getUser("Zimbabwe", List.of("Zimbabwe"))
        );

        FindSantaResult result = service.findSanta(users);

        assertEquals(0, result.getMatchedMap().size());
        assertEquals(1, result.getUnmatchedMap().size());
        assertEquals("Не смог найти Санту для ребенка", result.getUnmatchedMap().get(users.get(0)));
    }

    @Test
    void findSanta_ThreeFromDifferentCountries_AllUnmatched() {
        List<UserEntity> users = List.of(
            getUser("France", List.of("France")),
            getUser("Germany", List.of("Germany")),
            getUser("China", List.of("China"))
        );

        FindSantaResult result = service.findSanta(users);

        assertEquals(3, result.getUnmatchedMap().size());
        assertEquals(0, result.getMatchedMap().size());
    }

    @Test
    void findSanta_ThreeUsersSequentiallyMatched_NoUnmatched() {
        List<UserEntity> users = List.of(
            getUser("Germany", List.of("Germany", "France")),
            getUser("France", List.of("France", "Germany")),
            getUser("France", List.of("France"))
        );

        FindSantaResult result = service.findSanta(users);

        assertEquals(0, result.getUnmatchedMap().size());
        assertEquals(3, result.getMatchedMap().size());
        verifySenderAndRecipient(result);
    }

    @Test
    void findSanta_ThreeUsersSequentiallyMatched2_NoUnmatched() {
        List<UserEntity> users = List.of(
            getUser("Mexico", List.of("Mexico", "Peru", "Serbia")),
            getUser("Mexico", List.of("Mexico", "Peru")),
            getUser("Serbia", List.of("Serbia", "Mexico"))
        );

        FindSantaResult result = service.findSanta(users);

        assertEquals(0, result.getUnmatchedMap().size());
        assertEquals(3, result.getMatchedMap().size());
        verifySenderAndRecipient(result);
    }

    @Test
    void findSanta_TwoMatchOneDoesNot() {
        List<UserEntity> users = List.of(
            getUser("Germany", List.of("Germany", "France")),
            getUser("Germany", List.of("France", "Germany")),
            getUser("Peru", List.of("Peru"))
        );

        FindSantaResult result = service.findSanta(users);

        assertEquals(1, result.getUnmatchedMap().size());
        assertEquals(2, result.getMatchedMap().size());
        verifySenderAndRecipient(result);
    }

    @Test
    void findSanta_MultipleSendersSingleRecipient_OneMatchesTwoDoNot() {
        List<UserEntity> users = List.of(
            getUser("Mexico", List.of("Peru")),
            getUser("Mexico", List.of("Peru")),
            getUser("Peru", List.of("Peru"))
        );

        FindSantaResult result = service.findSanta(users);

        assertEquals(2, result.getUnmatchedMap().size());
        assertEquals(1, result.getMatchedMap().size());
        verifySenderAndRecipient(result);
    }
}
