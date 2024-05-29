/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 **************************************************************************************/
package com.itsaky.androidide.utils;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.models.SearchResult;
import com.itsaky.androidide.tasks.TaskExecutor;
import io.github.rosemoe.sora.text.CharPosition;
import io.github.rosemoe.sora.text.Content;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides API to search in files recursively
 *
 * @author Akash Yadav
 */
public class RecursiveFileSearcher {

  /**
   * Search the given text in files recursively in given search directories
   *
   * @param text       Text to search
   * @param exts       Extentions of file to search. Maybe null.
   * @param searchDirs Directories to search in. Subdirectories will be included
   * @param callback   A listener that will listen to the search result
   */
  public static void searchRecursiveAsync(
      String text, List<String> exts, List<File> searchDirs, Callback callback) {
    // Cannot search empty or null text
    if (text == null || text.isEmpty()) {
      return;
    }

    // If there is no listener to the search, search is meaningless
    if (callback == null) {
      return;
    }

    // Avoid searching if no directories are specified
    if (searchDirs == null || searchDirs.isEmpty()) {
      return;
    }

    TaskExecutor.executeAsync(new Searcher(text, exts, searchDirs), callback::onResult);
  }

  private static class Searcher implements Callable<Map<File, List<SearchResult>>> {

    private final String query;
    private final List<String> exts;
    private final List<File> dirs;

    public Searcher(String query, List<String> exts, List<File> dirs) {
      this.query = query;
      this.exts = exts;
      this.dirs = dirs;
    }

    @Override
    public Map<File, List<SearchResult>> call() throws Exception {
      final Map<File, List<SearchResult>> result = new HashMap<>();
      for (int i = 0; i < dirs.size(); i++) {
        final File dir = dirs.get(i);
        final List<File> files =
            FileUtils.listFilesInDirWithFilter(dir, new MultiFileFilter(exts), true);
        for (int j = 0; files != null && j < files.size(); j++) {
          final File file = files.get(j);
          if (file.isDirectory()) {
            continue;
          }
          final String text = FileIOUtils.readFile2String(file);
          if (text == null || text.trim().isEmpty()) {
            continue;
          }
          final Content content = new Content(text);
          final List<SearchResult> ranges = new ArrayList<>();
          Matcher matcher = Pattern.compile(Pattern.quote(this.query)).matcher(text);
          while (matcher.find()) {
            final Range range = new Range();
            final CharPosition start = content.getIndexer().getCharPosition(matcher.start());
            final CharPosition end = content.getIndexer().getCharPosition(matcher.end());
            range.setStart(new Position(start.line, start.column));
            range.setEnd(new Position(end.line, end.column));
            String sub =
                "..."
                    .concat(
                        text.substring(
                            Math.max(0, matcher.start() - 30),
                            Math.min(matcher.end() + 31, text.length())))
                    .trim()
                    .concat("...");
            String match =
                content.subContent(start.line, start.column, end.line, end.column).toString();
            ranges.add(new SearchResult(range, file, sub.replaceAll("\\s+", " "), match));
          }
          if (ranges.size() > 0) {
            result.put(file, ranges);
          }
        }
      }
      return result;
    }
  }

  private static class MultiFileFilter implements FileFilter {

    private final List<String> exts;

    public MultiFileFilter(List<String> exts) {
      this.exts = exts;
    }

    @Override
    public boolean accept(File file) {
      boolean accept = false;
      if (exts == null || exts.isEmpty() || file.isDirectory()) {
        accept = true;
      } else {
        for (String ext : exts) {
          if (file.getName().endsWith(ext)) {
            accept = true;
            break;
          }
        }
      }

      return accept && FileUtils.isUtf8(file);
    }
  }

  public static interface Callback {

    void onResult(Map<File, List<SearchResult>> results);
  }
}
