package net.cpollet.junit5gherkin.gherkin;

import gherkin.AstBuilder;
import gherkin.Parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;

/**
 * Created by cpollet on 18.10.16.
 */
public class FileGherkinDocument implements GherkinDocument {
    private final Path path;
    private final Parser<gherkin.ast.GherkinDocument> parser;
    private gherkin.ast.GherkinDocument document;

    public FileGherkinDocument(Path path) {
        this.path = path;
        this.parser = new Parser<>(new AstBuilder());
    }

    @Override
    public void parse() throws FileNotFoundException {
        document = parser.parse(new FileReader(path.toFile()));
    }

    @Override
    public gherkin.ast.GherkinDocument content() {
        if (document == null) {
            throw new IllegalStateException("Document not parsed");
        }
        return document;
    }

    @Override
    public String path() {
        return path.toString();
    }

}
