package net.cpollet.junit5.gherkin.steps;

import java.lang.reflect.Method;

/**
 * Created by cpollet on 19.10.16.
 */
public class Target {
    private final Object instance;
    private final Method method;

    public Target(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public void invoke(Object[] parameters) {
        try {
            method.invoke(instance, parameters);
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
