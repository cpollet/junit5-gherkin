package net.cpollet.junit5.gherkin.bindings;

import net.cpollet.junit5.gherkin.annotations.Given;
import net.cpollet.junit5.gherkin.annotations.Then;
import net.cpollet.junit5.gherkin.annotations.When;
import org.junit.jupiter.api.Assertions;

/**
 * Created by cpollet on 18.10.16.
 */
public class StepBindings {
    private TestContext testContext = new TestContext();

    @Given("two integers (\\d) and (\\d)")
    public void prepare(Integer a, Integer b) {
        System.out.println("Let a=1 and b=1");
        testContext.a = a;
        testContext.b = b;
    }

    @When("add the two integers")
    public void add() {
        System.out.println("Let result = a + b");
        testContext.result = testContext.a + testContext.b;
    }

    @Then("the result is (\\d)")
    public void result(Integer r) {
        System.out.println("Result should be 2");
        Assertions.assertEquals((int) r, testContext.result);
    }

    private class TestContext {
        int a;
        int b;
        int result;
    }
}
