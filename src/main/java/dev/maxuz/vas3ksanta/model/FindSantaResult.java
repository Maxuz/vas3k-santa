package dev.maxuz.vas3ksanta.model;

import java.util.HashMap;
import java.util.Map;

public class FindSantaResult {
    private final Map<UserEntity, UserEntity> matchedMap = new HashMap<>();
    private final Map<UserEntity, String> unmatchedMap = new HashMap<>();

    public void addMatched(UserEntity from, UserEntity to) {
        matchedMap.put(from, to);
    }

    public void addUnmatched(UserEntity user, String reason) {
        unmatchedMap.put(user, reason);
    }

    public Map<UserEntity, UserEntity> getMatchedMap() {
        return matchedMap;
    }

    public Map<UserEntity, String> getUnmatchedMap() {
        return unmatchedMap;
    }
}
