package net.cpollet.junit5.gherkin;

import net.cpollet.junit5.gherkin.annotations.Given;
import net.cpollet.junit5.gherkin.annotations.Step;
import net.cpollet.junit5.gherkin.annotations.Then;
import net.cpollet.junit5.gherkin.annotations.When;
import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cpollet on 18.10.16.
 */
public class StepDescriptor {
    final Object instance;
    final Method method;
    final String description;

    public StepDescriptor(Object instance, Method method, String description) {
        this.instance = instance;
        this.method = method;
        this.description = description;
    }

    public static StepDescriptor fail(String message) {
        try {
            FailFunction failFunction = () -> Assertions.fail(String.format("Unable to find binding for '%s'", message));
            return new StepDescriptor(failFunction, failFunction.getClass().getMethod("fail"), message);
        } catch (NoSuchMethodException e) {
            // this cannot happen since the method we get exists by design
            throw new Error(e);
        }
    }

    public List<String> parameters() {
        String annotation = stepAnnotation();
        Pattern pattern = Pattern.compile(annotation);
        Matcher matcher = pattern.matcher(description);

        if (matcher.find()) {
            List<String> parameters = new ArrayList<>(matcher.groupCount());
            for (int i = 1; i <= matcher.groupCount(); i++) {
                parameters.add(matcher.group(i));
            }
            return parameters;
        }

        return Collections.emptyList();
    }

    private String stepAnnotation() {
        if (method.getAnnotation(Given.class) != null) {
            return method.getAnnotation(Given.class).value();
        }
        if (method.getAnnotation(When.class) != null) {
            return method.getAnnotation(When.class).value();
        }
        if (method.getAnnotation(Then.class) != null) {
            return method.getAnnotation(Then.class).value();
        }
        if (method.getAnnotation(Step.class) != null) {
            return method.getAnnotation(Step.class).value();
        }
        throw new IllegalStateException("");
    }

    @FunctionalInterface
    private interface FailFunction {
        @SuppressWarnings("unused")
        void fail();
    }
}
