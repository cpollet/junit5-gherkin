package net.cpollet.junit5.gherkin;

import net.cpollet.junit5.gherkin.annotations.Given;
import net.cpollet.junit5.gherkin.annotations.Step;
import net.cpollet.junit5.gherkin.annotations.Then;
import net.cpollet.junit5.gherkin.annotations.When;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by cpollet on 19.10.16.
 */
public class DefaultStepMap implements StepMap {
    private final Map<Class, Object> bindings;
    private final Map<Method, Pattern> methodPatterns;

    public DefaultStepMap(Map<Class, Object> bindings) {
        this.bindings = bindings;
        this.methodPatterns = new HashMap<>();
    }

    @Override
    public ExecutableStep step(String scenarioLine) {
        for (Class clazz : bindings.keySet()) {
            Method method;
            if ((method = method(clazz, scenarioLine)) != null) {
                return new ExecutableStep(new Target(bindings.get(clazz), method), scenarioLine, methodPatterns.get(method));
            }
        }
        return ExecutableStep.fail(scenarioLine);
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
        return method.isAnnotationPresent(Given.class)
                && matches(method, method.getAnnotation(Given.class).value(), description);
    }

    private boolean hasMatchingWhenAnnotation(Method method, String description) {
        return method.isAnnotationPresent(When.class)
                && matches(method, method.getAnnotation(When.class).value(), description);
    }

    private boolean hasMatchingThenAnnotation(Method method, String description) {
        return method.isAnnotationPresent(Then.class)
                && matches(method, method.getAnnotation(Then.class).value(), description);
    }

    private boolean hasMatchingStepAnnotation(Method method, String description) {
        return method.isAnnotationPresent(Step.class)
                && matches(method, method.getAnnotation(Step.class).value(), description);
    }

    private boolean matches(Method method, String annotation, String description) {
        if (!methodPatterns.containsKey(method)) {
            methodPatterns.put(method, Pattern.compile(annotation));
        }

        return methodPatterns.get(method).matcher(description).matches();
    }
}
