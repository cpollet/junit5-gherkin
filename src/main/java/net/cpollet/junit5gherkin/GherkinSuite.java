package net.cpollet.junit5gherkin;

import net.cpollet.junit5gherkin.annotations.Bindings;
import net.cpollet.junit5gherkin.annotations.Given;
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
            Map<Class, Object> bindings = instantiateBindings(bindingClasses);

            List<StepDescriptor> stepDescriptors = steps.stream()
                    .map((description) -> findMethod(bindings, description))
                    .collect(Collectors.toList());

            return Collections.singletonList(buildTest(stepDescriptors));
        } catch (Throwable t) {
            return Collections.singletonList(
                    DynamicTest.dynamicTest("?", () -> {
                                Assertions.fail("Unable to launch: " + t.getMessage());
                            }
                    ));
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
                .collect(Collectors.toMap(e -> e, e -> {
                    try {
                        return e.newInstance();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }));
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

    private StepDescriptor findMethod(Map<Class, Object> bindings, String description) {
        for (Class clazz : bindings.keySet()) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Given.class)) {
                    if (method.getAnnotation(Given.class).value().equals(description)) {
                        return new StepDescriptor(bindings.get(clazz), method, description);
                    }
                }
            }
        }

        return StepDescriptor.fail(description);
    }
}
