package net.cpollet.junit5.gherkin.steps;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by cpollet on 19.10.16.
 */
public class Target {
    private static final Map<Class<?>, Function<String, ?>> converters = new HashMap<>();

    static {
        converters.put(String.class, Function.identity());
        converters.put(Object.class, Function.identity());
        converters.put(Integer.class, Integer::valueOf);
    }

    private final Object instance;
    private final Method method;

    public Target(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
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
            castParameters[i] = converters.get(parameterTypes[i]).apply(parameters.get(i));
        }

        return castParameters;
    }
}
