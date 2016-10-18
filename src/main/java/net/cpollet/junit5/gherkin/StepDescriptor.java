package net.cpollet.junit5.gherkin;

import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Method;

/**
 * Created by cpollet on 18.10.16.
 */
public class StepDescriptor {
    final Object instance;
    final Method method;
    final String description;

    public StepDescriptor(Object instance, Method method, String description) {
        this.instance = instance;
        this.method = method;
        this.description = description;
    }

    public static StepDescriptor fail(String message) {
        try {
            FailFunction failFunction = () -> Assertions.fail(String.format("Unable to find binding for '%s'", message));
            return new StepDescriptor(failFunction, failFunction.getClass().getMethod("fail"), message);
        } catch (NoSuchMethodException e) {
            // this cannot happen since the method we get exists by design
            throw new Error(e);
        }
    }

    @FunctionalInterface
    private interface FailFunction {
        @SuppressWarnings("unused")
        void fail();
    }
}
