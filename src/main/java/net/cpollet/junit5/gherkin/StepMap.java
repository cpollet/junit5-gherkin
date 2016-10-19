package net.cpollet.junit5.gherkin;

/**
 * Created by cpollet on 19.10.16.
 */
public interface StepMap {
    ExecutableStep step(String scenarioLine);
}
