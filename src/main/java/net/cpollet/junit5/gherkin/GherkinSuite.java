package net.cpollet.junit5.gherkin;

import net.cpollet.junit5.gherkin.annotations.Bindings;
import net.cpollet.junit5.gherkin.annotations.FeatureFilePath;
import net.cpollet.junit5.gherkin.annotations.Given;
import net.cpollet.junit5.gherkin.annotations.Then;
import net.cpollet.junit5.gherkin.annotations.When;
import net.cpollet.junit5.gherkin.document.FileGherkinDocument;
import net.cpollet.junit5.gherkin.document.Pickles;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.lang.reflect.Method;
import java.nio.file.Path;
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
    @DisplayName("Scenarios")
    @TestFactory
    Collection<DynamicTest> testSuite() {
        try {
            Path featurePath = new ClasspathPath(getFeaturePath());

            Pickles pickles = new Pickles(new FileGherkinDocument(featurePath));
            pickles.compile();

            Class[] bindingClasses = getBindingClasses();
            Map<Class, Object> bindings = instantiateBindings(bindingClasses);

            return pickles.stream()
                    .map(pickle -> pickle.getSteps().stream()
                            .map((pickleStep) -> step(bindings, pickleStep.getText()))
                            .collect(Collectors.toList())
                    )
                    .map(this::buildTest)
                    .collect(Collectors.toList());
        } catch (Throwable t) {
            return suiteFailure(t);
        }
    }

    private String getFeaturePath() {
        FeatureFilePath featureFilePath = getClass().getAnnotation(FeatureFilePath.class);
        if (featureFilePath == null) {
            throw new IllegalArgumentException("No feature to execute");
        }
        return featureFilePath.value();
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

        return "(no scenarioLine)";
    }

    private Step step(Map<Class, Object> bindings, String scenarioLine) {
        for (Class clazz : bindings.keySet()) {
            Method method;
            if ((method = method(clazz, scenarioLine)) != null) {
                return new Step(bindings.get(clazz), method, scenarioLine);
            }
        }
        return Step.fail(scenarioLine);
    }

    private Method method(Class clazz, String description) {
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
        return method.isAnnotationPresent(Given.class) && matches(method.getAnnotation(Given.class).value(), description);
    }

    private boolean matches(String annotation, String description) {
        return description.matches(annotation);
    }

    private boolean hasMatchingWhenAnnotation(Method method, String description) {
        return method.isAnnotationPresent(When.class) && matches(method.getAnnotation(When.class).value(), description);
    }

    private boolean hasMatchingThenAnnotation(Method method, String description) {
        return method.isAnnotationPresent(Then.class) && matches(method.getAnnotation(Then.class).value(), description);
    }

    private boolean hasMatchingStepAnnotation(Method method, String description) {
        return method.isAnnotationPresent(net.cpollet.junit5.gherkin.annotations.Step.class) && matches(method.getAnnotation(net.cpollet.junit5.gherkin.annotations.Step.class).value(), description);
    }

    private DynamicTest buildTest(List<Step> steps) {
        String scenario = "Scenario: ...";

        return DynamicTest.dynamicTest(scenario, () -> {
            System.out.println(colorize(scenario));
            for (Step step : steps) {
                System.out.println(colorize(step.scenarioLine));
                step.method.invoke(step.targetBindingInstance, step.parameters().toArray());
            }
        });
    }

    private String colorize(String string) {
        return new ColorizedString(ColorizedString.COLOR_BLUE, string).toString();
    }
}
