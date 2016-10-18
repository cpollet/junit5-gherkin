package net.cpollet.junit5gherkin;


import net.cpollet.junit5gherkin.annotations.Bindings;
import net.cpollet.junit5gherkin.annotations.FeatureFilePath;
import net.cpollet.junit5gherkin.bindings.StepBindings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Created by cpollet on 18.10.16.
 */
public class AllTests {
    @Test
    public void bootstrap() {
        // just to be able to run suite in IntelliJ
    }

    @Nested
    @DisplayName("Feature 1")
    @FeatureFilePath("classpath:/features/maths.feature")
    @Bindings(StepBindings.class)
    class Feature1 extends GherkinSuite {
    }

    @Nested
    @DisplayName("Feature 2")
    @Bindings(StepBindings.class)
    class Feature2 extends GherkinSuite {
    }
}
