package net.cpollet.junit5gherkin;

import net.cpollet.junit5gherkin.annotations.Bindings;
import net.cpollet.junit5gherkin.annotations.Given;
import net.cpollet.junit5gherkin.annotations.Step;
import net.cpollet.junit5gherkin.annotations.Then;
import net.cpollet.junit5gherkin.annotations.When;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by cpollet on 17.10.16.
 */
public abstract class GherkinSuite {
    private static List<String> steps;

    @BeforeAll
    public static void setUpSteps() {
        steps = new ArrayList<>();
        steps.addAll(Arrays.asList(
                "Given: two integers 1 and 1",
                "When: add the two integers",
                "Then: the result is 2"
        ));
    }

    @DisplayName("Scenarios")
    @TestFactory
    Collection<DynamicTest> testSuite() {
        try {
            Class[] bindingClasses = getBindingClasses();

            // loop {
            Map<Class, Object> bindings = instantiateBindings(bindingClasses);

            List<StepDescriptor> stepDescriptors = steps.stream()
                    .map((description) -> findMethodInBindings(bindings, description))
                    .collect(Collectors.toList());
            // }

            return Collections.singletonList(buildTest(stepDescriptors));
        } catch (Throwable t) {
            return suiteFailure(t);
        }
    }

    private Class[] getBindingClasses() {
        Bindings bindingsAnnotation = getClass().getAnnotation(Bindings.class);
        if (bindingsAnnotation == null) {
            throw new IllegalArgumentException("No bindings found");
        }
        return bindingsAnnotation.value();
    }

    private Map<Class, Object> instantiateBindings(Class[] bindingClasses) {
        return Arrays.stream(bindingClasses)
                .collect(Collectors.toMap(clazz -> clazz, clazz -> {
                    try {
                        return clazz.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }));
    }

    private List<DynamicTest> suiteFailure(Throwable t) {
        return Collections.singletonList(scenarioFailure(t));
    }

    private DynamicTest scenarioFailure(Throwable t) {
        return DynamicTest.dynamicTest("?", () -> Assertions.fail("Unable to launch: " + extractMessage(t)));
    }

    private String extractMessage(Throwable t) {
        if (t.getMessage() != null && t.getMessage().trim().length() > 0) {
            return t.getMessage();
        }

        if (t.getCause() != null) {
            return extractMessage(t.getCause());
        }

        return "(no description)";
    }

    private StepDescriptor findMethodInBindings(Map<Class, Object> bindings, String description) {
        for (Class clazz : bindings.keySet()) {
            Method method;
            if ((method = findMethodInClass(clazz, description)) != null) {
                return new StepDescriptor(bindings.get(clazz), method, description);
            }
        }
        return StepDescriptor.fail(description);
    }

    private Method findMethodInClass(Class clazz, String description) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (hasMatchingAnnotation(method, description)) {
                return method;
            }
        }
        return null;
    }

    private boolean hasMatchingAnnotation(Method method, String description) {
        return hasMatchingGivenAnnotation(method, description)
                || hasMatchingWhenAnnotation(method, description)
                || hasMatchingThenAnnotation(method, description)
                || hasMatchingStepAnnotation(method, description);
    }

    private boolean hasMatchingGivenAnnotation(Method method, String description) {
        return method.isAnnotationPresent(Given.class) && method.getAnnotation(Given.class).value().equals(description);
    }

    private boolean hasMatchingWhenAnnotation(Method method, String description) {
        return method.isAnnotationPresent(When.class) && method.getAnnotation(When.class).value().equals(description);
    }

    private boolean hasMatchingThenAnnotation(Method method, String description) {
        return method.isAnnotationPresent(Then.class) && method.getAnnotation(Then.class).value().equals(description);
    }

    private boolean hasMatchingStepAnnotation(Method method, String description) {
        return method.isAnnotationPresent(Step.class) && method.getAnnotation(Step.class).value().equals(description);
    }

    private DynamicTest buildTest(List<StepDescriptor> stepDescriptors) {
        String scenario = "Scenario: ...";

        return DynamicTest.dynamicTest(scenario, () -> {
            System.out.println(colorize(scenario));
            for (StepDescriptor stepDescriptor : stepDescriptors) {
                System.out.println(colorize(stepDescriptor.description));
                stepDescriptor.method.invoke(stepDescriptor.instance);
            }
        });
    }

    private String colorize(String string) {
        return new ColorizedString(ColorizedString.COLOR_BLUE, string).toString();
    }
}
