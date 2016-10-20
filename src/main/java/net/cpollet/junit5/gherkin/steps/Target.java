package net.cpollet.junit5.gherkin.steps;

import net.cpollet.junit5.gherkin.Converter;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by cpollet on 19.10.16.
 */
public class Target {
    private final Object instance;
    private final Method method;
    private final Converter converter;

    public Target(Object instance, Method method) {
        this(instance, method, null);
    }

    public Target(Object instance, Method method, Converter converter) {
        this.instance = instance;
        this.method = method;
        this.converter = converter;
    }

    public void invoke(List<String> parameters) {
        Object[] actualParameters = cast(method.getParameterTypes(), parameters);

        try {
            method.invoke(instance, actualParameters);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private Object[] cast(Class<?>[] parameterTypes, List<String> parameters) {
        Object[] castParameters = new Object[parameters.size()];

        for (int i = 0; i < parameters.size(); i++) {
            String parameterValue = parameters.get(i);
            Class<?> parameterType = parameterTypes[i];

            castParameters[i] = converter.convert(parameterValue, parameterType);
        }

        return castParameters;
    }
}
