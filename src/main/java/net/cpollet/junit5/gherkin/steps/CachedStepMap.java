package net.cpollet.junit5.gherkin.steps;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cpollet on 19.10.16.
 */
public class CachedStepMap implements StepMap {
    private final DefaultStepMap stepMap;
    private final Map<String, ExecutableStep> cache;

    public CachedStepMap(DefaultStepMap stepMap) {
        this.stepMap = stepMap;
        this.cache = new HashMap<>();
    }

    @Override
    public ExecutableStep step(String stepText) {
        if (!cache.containsKey(stepText)) {
            cache.put(stepText, stepMap.step(stepText));
        }

        return cache.get(stepText);
    }
}
