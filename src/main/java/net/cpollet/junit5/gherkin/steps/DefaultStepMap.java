package net.cpollet.junit5.gherkin.steps;

import net.cpollet.junit5.gherkin.Converter;
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
    private final Converter converter;
    private final Map<Method, Pattern> methodPatterns;

    public DefaultStepMap(Map<Class, Object> bindings, Converter converter) {
        this.bindings = bindings;
        this.converter = converter;
        this.methodPatterns = new HashMap<>();
    }

    @Override
    public ExecutableStep step(String stepText) {
        for (Class clazz : bindings.keySet()) {
            Method method;
            if ((method = method(clazz, stepText)) != null) {
                return new ExecutableStep(new Target(bindings.get(clazz), method, converter), stepText, methodPatterns.get(method));
            }
        }
        return ExecutableStep.fail(stepText);
    }

    private Method method(Class clazz, String stepText) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (hasMatchingAnnotation(method, stepText)) {
                return method;
            }
        }
        return null;
    }

    private boolean hasMatchingAnnotation(Method method, String stepText) {
        return hasMatchingGivenAnnotation(method, stepText)
                || hasMatchingWhenAnnotation(method, stepText)
                || hasMatchingThenAnnotation(method, stepText)
                || hasMatchingStepAnnotation(method, stepText);
    }

    private boolean hasMatchingGivenAnnotation(Method method, String stepText) {
        return method.isAnnotationPresent(Given.class)
                && matches(method, method.getAnnotation(Given.class).value(), stepText);
    }

    private boolean hasMatchingWhenAnnotation(Method method, String stepText) {
        return method.isAnnotationPresent(When.class)
                && matches(method, method.getAnnotation(When.class).value(), stepText);
    }

    private boolean hasMatchingThenAnnotation(Method method, String stepText) {
        return method.isAnnotationPresent(Then.class)
                && matches(method, method.getAnnotation(Then.class).value(), stepText);
    }

    private boolean hasMatchingStepAnnotation(Method method, String stepText) {
        return method.isAnnotationPresent(Step.class)
                && matches(method, method.getAnnotation(Step.class).value(), stepText);
    }

    private boolean matches(Method method, String annotation, String stepText) {
        if (!methodPatterns.containsKey(method)) {
            methodPatterns.put(method, Pattern.compile(annotation));
        }

        return methodPatterns.get(method).matcher(stepText).matches();
    }
}
