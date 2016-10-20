package net.cpollet.junit5.gherkin;

import net.cpollet.junit5.gherkin.steps.ExecutableStep;

import java.util.List;

/**
 * Created by cpollet on 20.10.16.
 */
class Scenario {
    private final String name;
    private final List<ExecutableStep> steps;

    public Scenario(String name, List<ExecutableStep> steps) {
        this.name = name;
        this.steps = steps;
    }

    public void displayHeader() {
        System.out.println(colorize("Scenario: " + name));
        for (ExecutableStep step : steps) {
            System.out.println(colorize("  " + step.toString()));
        }
    }

    private String colorize(String string) {
        return new ColorizedString(ColorizedString.Color.BLUE, string).toString();
    }

    public void execute() {
        steps.forEach(ExecutableStep::execute);
    }

    public String name() {
        return name;
    }
}
