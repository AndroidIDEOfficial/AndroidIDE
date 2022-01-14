package com.itsaky.lsp.java;

import java.io.*;
import java.net.URI;
import java.nio.file.Path;
import java.time.Instant;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

public class SourceFileObject implements JavaFileObject {
    /** path is the absolute path to this file on disk */
    final Path path;
    /** contents is the text in this file, or null if we should use the text in FileStore */
    final String contents;
    /** if contents is set, the modified time of contents */
    final Instant modified;

    public SourceFileObject(Path path) {
        this(path, null, Instant.EPOCH);
    }

    public SourceFileObject(Path path, String contents, Instant modified) {
        if (!FileStore.isJavaFile(path)) throw new RuntimeException(path + " is not a java source");
        this.path = path;
        this.contents = contents;
        this.modified = modified;
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() != SourceFileObject.class) return false;
        SourceFileObject that = (SourceFileObject) other;
        return this.path.equals(that.path);
    }

    @Override
    public int hashCode() {
        return this.path.hashCode();
    }

    @Override
    public Kind getKind() {
        String name = path.getFileName().toString();
        return kindFromExtension(name);
    }

    private static Kind kindFromExtension(String name) {
        for (Kind candidate : Kind.values()) {
            if (name.endsWith(candidate.extension)) {
                return candidate;
            }
        }
        return null;
    }

    @Override
    public boolean isNameCompatible(String simpleName, Kind kind) {
        return path.getFileName().toString().equals(simpleName + kind.extension);
    }

    @Override
    public NestingKind getNestingKind() {
        return null;
    }

    @Override
    public Modifier getAccessLevel() {
        return null;
    }

    @Override
    public URI toUri() {
        return path.toUri();
    }

    @Override
    public String getName() {
        return path.toString();
    }

    @Override
    public InputStream openInputStream() {
        if (contents != null) {
            byte[] bytes = contents.getBytes();
            return new ByteArrayInputStream(bytes);
        }
        return FileStore.inputStream(path);
    }

    @Override
    public OutputStream openOutputStream() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Reader openReader(boolean ignoreEncodingErrors) {
        if (contents != null) {
            return new StringReader(contents);
        }
        return FileStore.bufferedReader(path);
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        if (contents != null) {
            return contents;
        }
        return FileStore.contents(path);
    }

    @Override
    public Writer openWriter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getLastModified() {
        if (contents != null) {
            return modified.toEpochMilli();
        }
        return FileStore.modified(path).toEpochMilli();
    }

    @Override
    public boolean delete() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return path.toString();
    }
}
