package dev.maxuz.vas3ksanta.service;

import dev.maxuz.vas3ksanta.model.CountryEntity;
import dev.maxuz.vas3ksanta.model.FindSantaResult;
import dev.maxuz.vas3ksanta.model.GrandchildEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindSantaService {

    public FindSantaResult findSanta(List<GrandchildEntity> grandchildren) {
        var shuffleResult = new FindSantaResult();

        Map<GrandchildEntity, GrandchildEntity> matched = new HashMap<>();

        for (GrandchildEntity grandchild : grandchildren) {
            GrandchildEntity sender = findSender(grandchild, grandchildren, matched);
            if (sender == null) {
                shuffleResult.addUnmatched(grandchild, "Не смог найти Санту для ребенка");
            } else {
                matched.put(sender, grandchild);
            }
        }

        matched.forEach(shuffleResult::addMatched);
        return shuffleResult;
    }

    private GrandchildEntity findSender(GrandchildEntity recipient, List<GrandchildEntity> grandchildren, Map<GrandchildEntity, GrandchildEntity> matched) {
        GrandchildEntity result = null;
        for (GrandchildEntity sender : grandchildren) {
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
