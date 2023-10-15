package dev.maxuz.vas3ksanta.model;

import java.util.HashMap;
import java.util.Map;

public class FindSantaResult {
    private final Map<GrandchildEntity, GrandchildEntity> matchedMap = new HashMap<>();
    private final Map<GrandchildEntity, String> unmatchedMap = new HashMap<>();

    public void addMatched(GrandchildEntity from, GrandchildEntity to) {
        matchedMap.put(from, to);
    }

    public void addUnmatched(GrandchildEntity grandchild, String reason) {
        unmatchedMap.put(grandchild, reason);
    }

    public Map<GrandchildEntity, GrandchildEntity> getMatchedMap() {
        return matchedMap;
    }

    public Map<GrandchildEntity, String> getUnmatchedMap() {
        return unmatchedMap;
    }
}
