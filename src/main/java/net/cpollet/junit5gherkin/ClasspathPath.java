package net.cpollet.junit5gherkin;

import java.io.File;
import java.io.IOException;
import java.net.URI;
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

    private Path get() {
        if (cachedPath == null) {
            cachedPath = Paths.get(absolutePath());
        }
        return cachedPath;
    }

    private String absolutePath() {
        if (path.startsWith("classpath:")) {
            return getClass().getClassLoader().getResource(path.replaceFirst("classpath:/*", "")).getPath();
        }

        return path;
    }

    @Override
    public FileSystem getFileSystem() {
        return get().getFileSystem();
    }

    @Override
    public boolean isAbsolute() {
        return get().isAbsolute();
    }

    @Override
    public Path getRoot() {
        return get().getRoot();
    }

    @Override
    public Path getFileName() {
        return get().getFileName();
    }

    @Override
    public Path getParent() {
        return get().getParent();
    }

    @Override
    public int getNameCount() {
        return get().getNameCount();
    }

    @Override
    public Path getName(int index) {
        return get().getName(index);
    }

    @Override
    public Path subpath(int beginIndex, int endIndex) {
        return get().subpath(beginIndex, endIndex);
    }

    @Override
    public boolean startsWith(Path other) {
        return get().startsWith(other);
    }

    @Override
    public boolean startsWith(String other) {
        return get().startsWith(other);
    }

    @Override
    public boolean endsWith(Path other) {
        return get().endsWith(other);
    }

    @Override
    public boolean endsWith(String other) {
        return get().endsWith(other);
    }

    @Override
    public Path normalize() {
        return get().normalize();
    }

    @Override
    public Path resolve(Path other) {
        return get().resolve(other);
    }

    @Override
    public Path resolve(String other) {
        return get().resolve(other);
    }

    @Override
    public Path resolveSibling(Path other) {
        return get().resolveSibling(other);
    }

    @Override
    public Path resolveSibling(String other) {
        return get().resolveSibling(other);
    }

    @Override
    public Path relativize(Path other) {
        return get().relativize(other);
    }

    @Override
    public URI toUri() {
        return get().toUri();
    }

    @Override
    public Path toAbsolutePath() {
        return get().toAbsolutePath();
    }

    @Override
    public Path toRealPath(LinkOption... options) throws IOException {
        return get().toRealPath(options);
    }

    @Override
    public File toFile() {
        return get().toFile();
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
        return get().iterator();
    }

    @Override
    public int compareTo(Path other) {
        return get().compareTo(other);
    }

    @Override
    public boolean equals(Object other) {
        return get().equals(other);
    }

    @Override
    public int hashCode() {
        return get().hashCode();
    }

    @Override
    public String toString() {
        return get().toString();
    }
}
