package com.itsaky.lsp.java;

import androidx.annotation.NonNull;

import com.google.common.reflect.ClassPath;
import com.itsaky.androidide.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import jdk.internal.jrtfs.JrtFileSystemProvider;

class ScanClassPath {

    // TODO delete this and implement findPublicTypeDeclarationInJdk some other way
    /** All exported modules that are present in JDK 10 or 11 */
    static String[] JDK_MODULES = {
        "java.activation",
        "java.base",
        "java.compiler",
        "java.corba",
        "java.datatransfer",
        "java.desktop",
        "java.instrument",
        "java.jnlp",
        "java.logging",
        "java.management",
        "java.management.rmi",
        "java.naming",
        "java.net.http",
        "java.prefs",
        "java.rmi",
        "java.scripting",
        "java.se",
        "java.se.ee",
        "java.security.jgss",
        "java.security.sasl",
        "java.smartcardio",
        "java.sql",
        "java.sql.rowset",
        "java.transaction",
        "java.transaction.xa",
        "java.xml",
        "java.xml.bind",
        "java.xml.crypto",
        "java.xml.ws",
        "java.xml.ws.annotation",
        "javafx.base",
        "javafx.controls",
        "javafx.fxml",
        "javafx.graphics",
        "javafx.media",
        "javafx.swing",
        "javafx.web",
        "jdk.accessibility",
        "jdk.aot",
        "jdk.attach",
        "jdk.charsets",
        "jdk.compiler",
        "jdk.crypto.cryptoki",
        "jdk.crypto.ec",
        "jdk.dynalink",
        "jdk.editpad",
        "jdk.hotspot.agent",
        "jdk.httpserver",
        "jdk.incubator.httpclient",
        "jdk.internal.ed",
        "jdk.internal.jvmstat",
        "jdk.internal.le",
        "jdk.internal.opt",
        "jdk.internal.vm.ci",
        "jdk.internal.vm.compiler",
        "jdk.internal.vm.compiler.management",
        "jdk.jartool",
        "jdk.javadoc",
        "jdk.jcmd",
        "jdk.jconsole",
        "jdk.jdeps",
        "jdk.jdi",
        "jdk.jdwp.agent",
        "jdk.jfr",
        "jdk.jlink",
        "jdk.jshell",
        "jdk.jsobject",
        "jdk.jstatd",
        "jdk.localedata",
        "jdk.management",
        "jdk.management.agent",
        "jdk.management.cmm",
        "jdk.management.jfr",
        "jdk.management.resource",
        "jdk.naming.dns",
        "jdk.naming.rmi",
        "jdk.net",
        "jdk.pack",
        "jdk.packager.services",
        "jdk.rmic",
        "jdk.scripting.nashorn",
        "jdk.scripting.nashorn.shell",
        "jdk.sctp",
        "jdk.security.auth",
        "jdk.security.jgss",
        "jdk.snmp",
        "jdk.unsupported",
        "jdk.unsupported.desktop",
        "jdk.xml.dom",
        "jdk.zipfs",
    };

    static Set<String> classPathTopLevelClasses(Set<Path> classPath) {
        LOG.info(String.format(Locale.getDefault (), "Searching for top-level classes in %d classpath locations", classPath.size()));

        URL[] urls = classPath.stream().map(ScanClassPath::toUrl).toArray(URL[]::new);
        ClassLoader classLoader = new URLClassLoader(urls, null);
        
        ClassPath scanner;
        
        try {
            scanner = ClassPath.from(classLoader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        Set<String> classes = new HashSet<String>();
        for (ClassPath.ClassInfo c : scanner.getTopLevelClasses()) {
            classes.add(c.getName());
        }

        LOG.info(String.format(Locale.ROOT, "Found %d classes in classpath", classes.size()));

        return classes;
    }

    private static URL toUrl(@NonNull Path p) {
        try {
            return p.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Logger LOG = Logger.instance ("ScanClassPath");
}
