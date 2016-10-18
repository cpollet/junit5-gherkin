package net.cpollet.junit5gherkin;


import net.cpollet.junit5gherkin.annotations.Bindings;
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
    @Bindings(StepBindings.class)
    class Feature1 extends GherkinSuite {
    }
}
