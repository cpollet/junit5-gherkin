package net.cpollet.junit5gherkin;

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
import java.util.List;
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
        List<StepDescriptor> stepDescriptors = steps.stream()
                .map(this::findMethod)
                .collect(Collectors.toList());

        List<DynamicTest> tests = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            String scenario = String.format("Scenario %d: ...", i);


            tests.add(DynamicTest.dynamicTest(scenario, () -> {
                        System.out.println(colorize(scenario));
                        for (StepDescriptor stepDescriptor : stepDescriptors) {
                            System.out.println(colorize(stepDescriptor.description));
                            stepDescriptor.method.invoke(stepDescriptor.instance);
                        }
                    })
            );
        }

        return tests;
    }

    private String colorize(String string) {
        return "\u001b["       // Prefix
                + "0"          // Brightness
                + ";"          // Separator
                + "32"         // Red foreground
                + "m"          // Suffix
                + string       // the text to output
                + "\u001b[m "; // Prefix + Suffix to reset color
    }

    private StepDescriptor findMethod(String description) {
        for (Method method : getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Given.class)) {
                if (method.getAnnotation(Given.class).value().equals(description)) {
                    return new StepDescriptor(this, method, description);
                }
            }
        }

        try {
            MakeItFail makeItFail = fail(description);

            return new StepDescriptor(makeItFail, makeItFail.getClass().getMethod("fail"), description);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private MakeItFail fail(final String description) {
        return () -> Assertions.fail(String.format("Unable to find binding for '%s'", description));
    }


    @FunctionalInterface
    interface MakeItFail {
        void fail();
    }

    class StepDescriptor {
        final Object instance;
        final Method method;
        final String description;

        StepDescriptor(Object instance, Method method, String description) {
            this.instance = instance;
            this.method = method;
            this.description = description;
        }
    }


}
