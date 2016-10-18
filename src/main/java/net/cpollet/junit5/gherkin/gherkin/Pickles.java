package net.cpollet.junit5.gherkin.gherkin;

import gherkin.pickles.Compiler;
import gherkin.pickles.Pickle;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by cpollet on 18.10.16.
 */
public class Pickles {
    private final GherkinDocument gherkinDocument;
    private final Compiler compiler;
    private List<Pickle> pickles;

    public Pickles(GherkinDocument gherkinDocument) {
        this.gherkinDocument = gherkinDocument;
        this.compiler = new Compiler();
    }

    public void compile() throws FileNotFoundException {
        gherkinDocument.parse();
        pickles = compiler.compile(gherkinDocument.content(), gherkinDocument.path());
    }

    public List<Pickle> pickles() {
        if (pickles == null) {
            throw new IllegalStateException("Pickles not compiled");
        }
        return pickles;
    }

    public Stream<Pickle> stream() {
        return pickles().stream();
    }
}
