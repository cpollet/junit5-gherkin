package net.cpollet.junit5.gherkin;


import net.cpollet.junit5.gherkin.annotations.FeatureFilePath;
import net.cpollet.junit5.gherkin.bindings.StepBindings;
import net.cpollet.junit5.gherkin.annotations.Bindings;
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
}
