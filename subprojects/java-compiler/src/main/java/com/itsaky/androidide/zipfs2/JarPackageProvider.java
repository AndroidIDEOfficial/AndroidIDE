/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.itsaky.androidide.zipfs2;

import java.nio.file.Path;
import java.util.Map;

/**
 * @author itsaky
 */
public interface JarPackageProvider {
    
    /**
     * Get the package map from the given archive path.
     * @return The cached package entries. Should return null or empty map to walk the archive file tree instead.
     *         The keys must be <code>openjdk.tools.javac.file.RelativePath.RelativeDirectory</code>
     */
    public Map<? extends Object, Path> getPackages(Path archivePath);
}
