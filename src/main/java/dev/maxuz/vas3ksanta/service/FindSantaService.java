package dev.maxuz.vas3ksanta.service;

import dev.maxuz.vas3ksanta.model.CountryEntity;
import dev.maxuz.vas3ksanta.model.FindSantaResult;
import dev.maxuz.vas3ksanta.model.UserEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindSantaService {

    public FindSantaResult findSanta(List<UserEntity> users) {
        var shuffleResult = new FindSantaResult();

        Map<UserEntity, UserEntity> matched = new HashMap<>();

        for (UserEntity user : users) {
            UserEntity sender = findSender(user, users, matched);
            if (sender == null) {
                shuffleResult.addUnmatched(user, "Не смог найти Санту для ребенка");
            } else {
                matched.put(sender, user);
            }
        }

        matched.forEach(shuffleResult::addMatched);
        return shuffleResult;
    }

    private UserEntity findSender(UserEntity recipient, List<UserEntity> userren, Map<UserEntity, UserEntity> matched) {
        UserEntity result = null;
        for (UserEntity sender : userren) {
            if (!sender.equals(recipient) && !matched.containsKey(sender) && contains(sender.getRecipientCountries(), recipient.getCountry())) {
                result = sender;
                if (!matched.containsKey(recipient) || !matched.get(recipient).equals(sender)) {
                    break;
                }
            }
        }
        return result;
    }

    private static boolean contains(List<CountryEntity> countries, CountryEntity country) {
        if (countries == null || countries.isEmpty()) {
            return false;
        }
        return countries.stream().map(CountryEntity::getName).anyMatch(name -> name.equals(country.getName()));
    }
}
