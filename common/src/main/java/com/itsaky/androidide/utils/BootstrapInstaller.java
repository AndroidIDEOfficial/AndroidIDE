/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.utils;

import static com.itsaky.androidide.utils.Environment.PREFIX;

import android.content.Context;
import android.system.ErrnoException;
import android.system.Os;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.blankj.utilcode.util.FileUtils;
import com.itsaky.androidide.resources.R;
import com.itsaky.androidide.managers.ToolsManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Installs the bootstraps zip into {@code $SYSROOT}.
 *
 * <p>This class in very much based on Termux's TermuxInstaller.java.
 *
 * @author Akash Yadav
 */
public class BootstrapInstaller {

  private static final ILogger LOG = ILogger.newInstance("BootstrapInstaller");

  @NonNull
  public static CompletableFuture<Void> doInstall(
      final Context context, @Nullable final UpdateListener listener) {
    return CompletableFuture.runAsync(
        () -> {
          var msg = "Opening bootstrap zip input stream...";
          LOG.debug(msg);
          notify(listener, msg);
          try (final var assetIn =
                  context.getAssets().open(ToolsManager.getArchSpecificAsset("bootstrap.zip"));
              final var zip = new ZipInputStream(assetIn)) {

            final var buffer = new byte[8096];
            final var symlinks = new ArrayList<Pair<String, String>>(50);

            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
              msg = "Reading zip entry " + entry.getName();
              notify(listener, msg);
              LOG.verbose(msg);
              if (entry.getName().equals("SYMLINKS.txt")) {
                msg = "Reading SYMLINKS.txt...";
                notify(listener, msg);
                LOG.debug(msg);
                final var symlinksReader = new BufferedReader(new InputStreamReader(zip));
                String line;
                while ((line = symlinksReader.readLine()) != null) {
                  String[] parts = line.split("←");
                  if (parts.length != 2) {
                    final var err = "Malformed symlink line: " + line;
                    LOG.error(err);
                    throw new CompletionException(new InstallationException(err));
                  }
                  String oldPath = parts[0];
                  String newPath = PREFIX.getAbsolutePath() + "/" + parts[1];
                  symlinks.add(Pair.create(oldPath, newPath));

                  final var parentFile = new File(newPath).getParentFile();
                  if (!FileUtils.createOrExistsDir(parentFile)) {
                    LOG.error("Cannot create symlink parent directory.");
                    throw new CompletionException(
                        new InstallationException("Unable to create directory: " + parentFile));
                  }
                }
              } else {
                String zipEntryName = entry.getName();
                File targetFile = new File(PREFIX, zipEntryName);
                boolean isDirectory = entry.isDirectory();

                final var dir = isDirectory ? targetFile : targetFile.getParentFile();
                if (!FileUtils.createOrExistsDir(dir)) {
                  LOG.error("Cannot create target file parent directory");
                  throw new CompletionException(
                      new InstallationException("Unable to create directory: " + dir));
                }

                // If the file exists and it is not a directory
                // Delete that file
                final var targetFilePath = targetFile.toPath();
                if (Files.exists(targetFilePath) && !Files.isDirectory(targetFilePath)) {
                  try {
                    Files.delete(targetFilePath);
                  } catch (Throwable th) {
                    throw new CompletionException(th);
                  }
                }

                if (!isDirectory) {
                  try (final var outStream = new FileOutputStream(targetFile)) {
                    int readBytes;
                    while ((readBytes = zip.read(buffer)) != -1)
                      outStream.write(buffer, 0, readBytes);
                  }

                  if (zipEntryName.startsWith("bin/")
                      || zipEntryName.startsWith("libexec")
                      || zipEntryName.startsWith("lib/apt/apt-helper")
                      || zipEntryName.startsWith("lib/apt/methods")) {

                    notify(listener, context.getString(R.string.msg_chmod_exec));
                    LOG.verbose("chmod 0700", zipEntryName);

                    //noinspection OctalInteger
                    Os.chmod(targetFile.getAbsolutePath(), 0700);
                  }
                }
              }
            }

            if (symlinks.isEmpty()) {
              LOG.error("No SYMLINKS.txt file encountered...");
              throw new CompletionException(
                  new InstallationException("No SYMLINKS.txt encountered"));
            }

            for (Pair<String, String> symlink : symlinks) {
              notify(
                  listener, context.getString(R.string.msg_linking, symlink.second, symlink.first));

              final var target = Paths.get(symlink.second);
              if (Files.exists(target) && !Files.isDirectory(target)) {
                try {
                  Files.delete(target);
                } catch (Throwable throwable) {
                  throw new CompletionException(throwable);
                }
              }
              Os.symlink(symlink.first, symlink.second);
            }

            notify(listener, context.getString(R.string.msg_extracting_hooks));
            ToolsManager.extractLibHooks();

            LOG.info("Bootstrap packages installed successfully.");
          } catch (IOException | ErrnoException e) {
            LOG.error("Failed to extract bootstrap", e);
            throw new RuntimeException(e);
          }
        });
  }

  private static void notify(@Nullable UpdateListener listener, String message) {
    if (listener != null) {
      listener.onUpdate(message);
    }
  }

  public static class InstallationException extends RuntimeException {
    public InstallationException(String message) {
      super(message);
    }
  }

  public interface UpdateListener {
    void onUpdate(String message);
  }
}
