package net.cpollet.junit5.gherkin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Iterator;

/**
 * Created by cpollet on 18.10.16.
 */
public class ClasspathPath implements Path {
    private final String path;
    private Path cachedPath;

    public ClasspathPath(String path) {
        this.path = path;
    }

    private Path get() throws FileNotFoundException {
        if (cachedPath == null) {
            cachedPath = Paths.get(absolutePath());
        }
        return cachedPath;
    }

    private String absolutePath() throws FileNotFoundException {
        if (path.startsWith("classpath:")) {
            URL resource = getClass().getClassLoader().getResource(path.replaceFirst("classpath:/*", ""));
            if (resource == null) {
                throw new FileNotFoundException("File does not exist: " + path);
            }
            return resource.getPath();
        }

        return path;
    }

    @Override
    public FileSystem getFileSystem() {
        try {
            return get().getFileSystem();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isAbsolute() {
        try {
            return get().isAbsolute();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Path getRoot() {
        try {
            return get().getRoot();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Path getFileName() {
        try {
            return get().getFileName();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Path getParent() {
        try {
            return get().getParent();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getNameCount() {
        try {
            return get().getNameCount();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Path getName(int index) {
        try {
            return get().getName(index);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Path subpath(int beginIndex, int endIndex) {
        try {
            return get().subpath(beginIndex, endIndex);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean startsWith(Path other) {
        try {
            return get().startsWith(other);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean startsWith(String other) {
        try {
            return get().startsWith(other);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean endsWith(Path other) {
        try {
            return get().endsWith(other);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean endsWith(String other) {
        try {
            return get().endsWith(other);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Path normalize() {
        try {
            return get().normalize();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Path resolve(Path other) {
        try {
            return get().resolve(other);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Path resolve(String other) {
        try {
            return get().resolve(other);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Path resolveSibling(Path other) {
        try {
            return get().resolveSibling(other);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Path resolveSibling(String other) {
        try {
            return get().resolveSibling(other);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Path relativize(Path other) {
        try {
            return get().relativize(other);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public URI toUri() {
        try {
            return get().toUri();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Path toAbsolutePath() {
        try {
            return get().toAbsolutePath();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Path toRealPath(LinkOption... options) throws IOException {
        return get().toRealPath(options);
    }

    @Override
    public File toFile() {
        try {
            return get().toFile();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) throws IOException {
        return get().register(watcher, events, modifiers);
    }

    @Override
    public WatchKey register(WatchService watcher, WatchEvent.Kind<?>... events) throws IOException {
        return get().register(watcher, events);
    }

    @Override
    public Iterator<Path> iterator() {
        try {
            return get().iterator();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int compareTo(Path other) {
        try {
            return get().compareTo(other);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object other) {
        try {
            return get().equals(other);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int hashCode() {
        try {
            return get().hashCode();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        try {
            return get().toString();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
