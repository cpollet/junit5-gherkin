package net.cpollet.junit5.gherkin;

import net.cpollet.junit5.gherkin.annotations.Bindings;
import net.cpollet.junit5.gherkin.annotations.FeatureFilePath;
import net.cpollet.junit5.gherkin.document.FileGherkinDocument;
import net.cpollet.junit5.gherkin.document.Pickles;
import net.cpollet.junit5.gherkin.steps.CachedStepMap;
import net.cpollet.junit5.gherkin.steps.DefaultStepMap;
import net.cpollet.junit5.gherkin.steps.ExecutableStep;
import net.cpollet.junit5.gherkin.steps.StepMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

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

            Map<Class, Object> bindings = instantiateBindings(getBindingClasses());
            StepMap stepMap = new CachedStepMap(new DefaultStepMap(bindings));

            return pickles.stream()
                    .map(pickle -> pickle.getSteps().stream()
                            .map((pickleStep) -> stepMap.step(pickleStep.getText()))
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

    private DynamicTest buildTest(List<ExecutableStep> executableSteps) {
        String scenario = "Scenario: ...";

        return DynamicTest.dynamicTest(scenario, () -> {
            System.out.println(colorize(scenario));
            for (ExecutableStep executableStep : executableSteps) {
                System.out.println(colorize(executableStep.toString()));
                executableStep.execute();
            }
        });
    }

    private String colorize(String string) {
        return new ColorizedString(ColorizedString.COLOR_BLUE, string).toString();
    }
}
