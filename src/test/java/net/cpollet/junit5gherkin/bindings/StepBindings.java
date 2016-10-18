package net.cpollet.junit5gherkin.bindings;

import net.cpollet.junit5gherkin.annotations.Given;
import org.junit.jupiter.api.Assertions;

/**
 * Created by cpollet on 18.10.16.
 */
public class StepBindings {
    private TestContext testContext = new TestContext();

    @Given("Given: two integers 1 and 1")
    public void prepare() {
        System.out.println("Let a=1 and b=1");
        testContext.a = 1;
        testContext.b = 1;
    }

    @Given("When: add the two integers")
    public void add() {
        System.out.println("Let result = a + b");
        testContext.result = testContext.a + testContext.b;
    }

    @Given("Then: the result is 2")
    public void result() {
        System.out.println("Result should be 2");
        Assertions.assertEquals(2, testContext.result);
    }

    private class TestContext {
        int a;
        int b;
        int result;
    }
}
