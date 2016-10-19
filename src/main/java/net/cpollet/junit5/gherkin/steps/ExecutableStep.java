package net.cpollet.junit5.gherkin.steps;

import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cpollet on 18.10.16.
 */
public class ExecutableStep {
    private final Target target;
    private final String scenarioLine;
    private final Pattern pattern;


    private ExecutableStep(Target target, String scenarioLine) {
        this(target, scenarioLine, null);
    }

    public ExecutableStep(Target target, String scenarioLine, Pattern pattern) {
        this.target = target;
        this.scenarioLine = scenarioLine;
        this.pattern = pattern;
    }

    public static ExecutableStep fail(String message) {
        try {
            FailFunction failFunction = () -> Assertions.fail(String.format("Unable to find binding for '%s'", message));
            return new ExecutableStep(new Target(failFunction, failFunction.getClass().getMethod("fail")), message);
        } catch (NoSuchMethodException e) {
            // this cannot happen since the targetBindingMethod we get exists by design
            throw new Error(e);
        }
    }

    private List<String> parameters() {
        Matcher matcher = pattern.matcher(scenarioLine);

        if (matcher.find()) {
            List<String> parameters = new ArrayList<>(matcher.groupCount());
            for (int i = 1; i <= matcher.groupCount(); i++) {
                parameters.add(matcher.group(i));
            }
            return parameters;
        }

        return Collections.emptyList();
    }

    public void execute() {
        target.invoke(parameters().toArray());
    }

    public String toString() {
        return scenarioLine;
    }

    @FunctionalInterface
    private interface FailFunction {
        @SuppressWarnings("unused")
        void fail();
    }
}
