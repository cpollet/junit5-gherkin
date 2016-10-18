package net.cpollet.junit5.gherkin.document;

import java.io.FileNotFoundException;

/**
 * Created by cpollet on 18.10.16.
 */
public interface GherkinDocument {
    void parse() throws FileNotFoundException;

    gherkin.ast.GherkinDocument content();

    String path();
}
