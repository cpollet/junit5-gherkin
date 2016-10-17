package net.cpollet.junit5gherkin;

import net.cpollet.junit5gherkin.annotations.Given;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

/**
 * Created by cpollet on 17.10.16.
 */
@DisplayName("Gherkin Suite")
public class Test extends GherkinSuite {
    private TestContext testContext;

    @BeforeEach
    public void setUp() {
        testContext = new TestContext();
    }

    @Given("Given: two integers 1 and 1")
    void prepare() {
        System.out.println("Let a=1 and b=1");
        testContext.a = 1;
        testContext.b = 1;
    }

    @Given("When: add the two integers")
    void add() {
        System.out.println("Let result = a + b");
        testContext.result = testContext.a + testContext.b;
    }

    @Given("Then: the result is 2")
    void result() {
        System.out.println("Result should be 2");
        Assertions.assertEquals(2, testContext.result);
    }

    private class TestContext {
        int a;
        int b;
        int result;
    }
}
