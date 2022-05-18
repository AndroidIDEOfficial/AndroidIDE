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

package com.itsaky.lsp.java.utils;

import com.itsaky.androidide.utils.CharSequenceReader;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.lsp.java.FileStore;
import com.itsaky.lsp.util.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.element.TypeElement;

// Translated from https://golang.org/src/strings/search.go

// Search efficiently finds strings in a source text. It's implemented
// using the Boyer-Moore string search algorithm:
// https://en.wikipedia.org/wiki/Boyer-Moore_string_search_algorithm
// https://www.cs.utexas.edu/~moore/publications/fstrpos.pdf (note: this aged
// document uses 1-based indexing)
public class StringSearch {
  // pattern is the string that we are searching for in the text.
  private final byte[] pattern;

  // badCharSkip[b] contains the distance between the last byte of pattern
  // and the rightmost occurrence of b in pattern. If b is not in pattern,
  // badCharSkip[b] is len(pattern).
  //
  // Whenever a mismatch is found with byte b in the text, we can safely
  // shift the matching frame at least badCharSkip[b] until the next time
  // the matching char could be in alignment.
  // TODO 256 is not coloring
  private final int[] badCharSkip = new int[256];

  // goodSuffixSkip[i] defines how far we can shift the matching frame given
  // that the suffix pattern[i+1:] matches, but the byte pattern[i] does
  // not. There are two cases to consider:
  //
  // 1. The matched suffix occurs elsewhere in pattern (with a different
  // byte preceding it that we might possibly match). In this case, we can
  // shift the matching frame to align with the next suffix chunk. For
  // example, the pattern "mississi" has the suffix "issi" next occurring
  // (in right-to-left order) at index 1, so goodSuffixSkip[3] ==
  // shift+len(suffix) == 3+4 == 7.
  //
  // 2. If the matched suffix does not occur elsewhere in pattern, then the
  // matching frame may share part of its prefix with the end of the
  // matching suffix. In this case, goodSuffixSkip[i] will contain how far
  // to shift the frame to align this portion of the prefix to the
  // suffix. For example, in the pattern "abcxxxabc", when the first
  // mismatch from the back is found to be in position 3, the matching
  // suffix "xxabc" is not found elsewhere in the pattern. However, its
  // rightmost "abc" (at position 6) is a prefix of the whole pattern, so
  // goodSuffixSkip[3] == shift+len(suffix) == 6+5 == 11.
  private final int[] goodSuffixSkip;

  StringSearch(String patternSting) {
    this.pattern = patternSting.getBytes();
    this.goodSuffixSkip = new int[pattern.length];

    // last is the index of the last character in the pattern.
    int last = pattern.length - 1;

    // Build bad character table.
    // Bytes not in the pattern can skip one pattern's length.
    Arrays.fill(badCharSkip, pattern.length);
    // The loop condition is < instead of <= so that the last byte does not
    // have a zero distance to itself. Finding this byte out of place implies
    // that it is not in the last position.
    for (int i = 0; i < last; i++) {
      badCharSkip[pattern[i] + 128] = last - i;
    }

    // Build good suffix table.
    // First pass: set each value to the next index which starts a prefix of
    // pattern.
    int lastPrefix = last;
    for (int i = last; i >= 0; i--) {
      if (hasPrefix(pattern, new Slice(pattern, i + 1))) {
        lastPrefix = i + 1;
      }
      // lastPrefix is the shift, and (last-i) is len(suffix).
      goodSuffixSkip[i] = lastPrefix + last - i;
    }
    // Second pass: find repeats of pattern's suffix starting from the front.
    for (int i = 0; i < last; i++) {
      int lenSuffix = longestCommonSuffix(pattern, new Slice(pattern, 1, i + 1));
      if (pattern[i - lenSuffix] != pattern[last - lenSuffix]) {
        // (last-i) is the shift, and lenSuffix is len(suffix).
        goodSuffixSkip[last - lenSuffix] = lenSuffix + last - i;
      }
    }
  }

  int next(String text) {
    return next(text.getBytes());
  }

  private int next(byte[] text) {
    return next(ByteBuffer.wrap(text));
  }

  private int next(ByteBuffer text) {
    return next(text, 0);
  }

  private int next(ByteBuffer text, int startingAfter) {
    int i = startingAfter + pattern.length - 1;
    while (i < text.limit()) {
      // Compare backwards from the end until the first unmatching character.
      int j = pattern.length - 1;
      while (j >= 0 && text.get(i) == pattern[j]) {
        i--;
        j--;
      }
      if (j < 0) {
        return i + 1; // match
      }
      i += Math.max(badCharSkip[text.get(i) + 128], goodSuffixSkip[j]);
    }
    return -1;
  }

  private boolean isWordChar(byte b) {
    char c = (char) (b + 128);
    return Character.isAlphabetic(c) || Character.isDigit(c) || c == '$' || c == '_';
  }

  private boolean startsWord(ByteBuffer text, int offset) {
    if (offset == 0) {
      return true;
    }
    return !isWordChar(text.get(offset - 1));
  }

  private boolean endsWord(ByteBuffer text, int offset) {
    if (offset + 1 >= text.limit()) {
      return true;
    }
    return !isWordChar(text.get(offset + 1));
  }

  private boolean isWord(ByteBuffer text, int offset) {
    return startsWord(text, offset) && endsWord(text, offset + pattern.length - 1);
  }

  private int nextWord(String text) {
    return nextWord(text.getBytes());
  }

  private int nextWord(byte[] text) {
    return nextWord(ByteBuffer.wrap(text));
  }

  private int nextWord(ByteBuffer text) {
    int i = 0;
    while (true) {
      i = next(text, i);
      if (i == -1) {
        return -1;
      }
      if (isWord(text, i)) {
        return i;
      }
      i++;
    }
  }

  private boolean hasPrefix(byte[] s, Slice prefix) {
    for (int i = 0; i < prefix.length(); i++) {
      if (s[i] != prefix.get(i)) {
        return false;
      }
    }
    return true;
  }

  private int longestCommonSuffix(byte[] a, Slice b) {
    int i = 0;
    for (; i < a.length && i < b.length(); i++) {
      if (a[a.length - 1 - i] != b.get(b.length() - 1 - i)) {
        break;
      }
    }
    return i;
  }

  private static class Slice {
    private final byte[] target;
    private final int from;
    private final int until;

    int length() {
      return until - from;
    }

    byte get(int i) {
      return target[from + i];
    }

    Slice(byte[] target, int from) {
      this(target, from, target.length);
    }

    Slice(byte[] target, int from, int until) {
      this.target = target;
      this.from = from;
      this.until = until;
    }
  }

  private static final ByteBuffer SEARCH_BUFFER = ByteBuffer.allocateDirect(1024 * 1024);

  // TODO cache the progress made by searching shorter queries
  public static boolean containsWordMatching(Path java, String query) {
    if (FileStore.activeDocuments().contains(java)) {
      CharSequence text = FileStore.contents(java);
      return matchesTitleCase(text, query);
    }
    try (FileChannel channel = FileChannel.open(java)) {
      // Read up to 1 MB of data from file
      int limit = Math.min((int) channel.size(), SEARCH_BUFFER.capacity());
      SEARCH_BUFFER.position(0);
      SEARCH_BUFFER.limit(limit);
      channel.read(SEARCH_BUFFER);
      SEARCH_BUFFER.position(0);
      CharBuffer chars = StandardCharsets.UTF_8.decode(SEARCH_BUFFER);
      return matchesTitleCase(chars, query);
    } catch (NoSuchFileException e) {
      LOG.warn(e.getMessage());
      return false;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static boolean containsWord(Path java, String query) {
    StringSearch search = new StringSearch(query);
    if (FileStore.activeDocuments().contains(java)) {
      byte[] text = new byte[0];
      try {
        text =
            toByteArray(new CharSequenceReader(FileStore.contents(java)), Charset.defaultCharset());
      } catch (IOException e) {
        LOG.error(e);
      }
      return search.nextWord(text) != -1;
    }
    try (FileChannel channel = FileChannel.open(java)) {
      // Read up to 1 MB of data from file
      int limit = Math.min((int) channel.size(), SEARCH_BUFFER.capacity());
      SEARCH_BUFFER.position(0);
      SEARCH_BUFFER.limit(limit);
      channel.read(SEARCH_BUFFER);
      SEARCH_BUFFER.position(0);
      return search.nextWord(SEARCH_BUFFER) != -1;
    } catch (NoSuchFileException e) {
      LOG.warn(e.getMessage());
      return false;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static boolean containsString(Path java, String query) {
    StringSearch search = new StringSearch(query);
    if (FileStore.activeDocuments().contains(java)) {
      byte[] text = new byte[0];
      try {
        text =
            toByteArray(new CharSequenceReader(FileStore.contents(java)), Charset.defaultCharset());
      } catch (IOException e) {
        LOG.error(e);
      }
      return search.next(text) != -1;
    }
    try (FileChannel channel = FileChannel.open(java)) {
      // Read up to 1 MB of data from file
      int limit = Math.min((int) channel.size(), SEARCH_BUFFER.capacity());
      SEARCH_BUFFER.position(0);
      SEARCH_BUFFER.limit(limit);
      channel.read(SEARCH_BUFFER);
      SEARCH_BUFFER.position(0);
      return search.next(SEARCH_BUFFER) != -1;
    } catch (NoSuchFileException e) {
      LOG.warn(e.getMessage());
      return false;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Check if `candidate` contains all the characters of `find`, in-order, case-insensitive. Matches
   * can be discontinuous if the letters of `find` match the first letters of words in `candidate`
   * For example, fb matches FooBar, but it doesn't match Foobar (exposed for testing)
   */
  public static boolean matchesTitleCase(CharSequence candidate, String find) {
    Objects.requireNonNull(candidate, "candidate is null");
    int i = 0;

    tokenLoop:
    while (i < candidate.length()) {
      i = startOfToken(candidate, i);

      for (char f : find.toCharArray()) {
        // If we have reached the end of candidate without matching all of find, fail
        if (i >= candidate.length()) {
          return false;
        }
        // If the next character in candidate matches, advance i
        else if (Character.toLowerCase(f) == Character.toLowerCase(candidate.charAt(i))) {
          i++;
        } else {
          // Find the start of the next word
          while (i < candidate.length()) {
            char c = candidate.charAt(i);
            // If the next character is not a word, try again with the next token
            if (!isWordChar(c)) {
              continue tokenLoop;
            }
            // TODO match things like fb ~ foo_bar
            boolean isStartOfWord = Character.isUpperCase(c);
            boolean isMatch = Character.toLowerCase(f) == Character.toLowerCase(c);
            i++;
            if (isStartOfWord && isMatch) {
              break;
            }
          }
          if (i >= candidate.length()) {
            return false;
          }
        }
      }
      // All of find was matched!
      return true;
    }
    return false;
  }

  private static int startOfToken(CharSequence candidate, int offset) {
    while (offset < candidate.length()) {
      char c = candidate.charAt(offset);
      if (isWordChar(c)) {
        break;
      }
      offset++;
    }
    return offset;
  }

  private static boolean isWordChar(char c) {
    return Character.isAlphabetic(c) || Character.isDigit(c) || c == '_' || c == '$';
  }

  public static boolean containsType(Path file, TypeElement el) {
    switch (el.getKind()) {
      case INTERFACE:
        return containsInterface(file, el.getSimpleName().toString());
      case CLASS:
        return containsClass(file, el.getSimpleName().toString());
      default:
        throw new RuntimeException("Don't know what to do with " + el.getKind());
    }
  }

  private static Cache<String, Boolean> cacheContainsClass = new Cache<>();

  private static boolean containsClass(Path file, String simpleName) {
    if (cacheContainsClass.needs(file, simpleName)) {
      cacheContainsClass.load(file, simpleName, containsString(file, "class " + simpleName));
      // TODO verify this by actually parsing the file
    }
    return cacheContainsClass.get(file, simpleName);
  }

  private static Cache<String, Boolean> cacheContainsInterface = new Cache<>();

  private static boolean containsInterface(Path file, String simpleName) {
    if (cacheContainsInterface.needs(file, simpleName)) {
      cacheContainsInterface.load(
          file, simpleName, containsString(file, "interface " + simpleName));
      // TODO verify this by actually parsing the file
    }
    return cacheContainsInterface.get(file, simpleName);
  }

  // TODO this doesn't work for inner classes, eliminate
  public static String mostName(String name) {
    int lastDot = name.lastIndexOf('.');
    return lastDot == -1 ? "" : name.substring(0, lastDot);
  }

  // TODO this doesn't work for inner classes, eliminate
  public static String lastName(String name) {
    int i = name.lastIndexOf('.');
    if (i == -1) {
      return name;
    } else {
      return name.substring(i + 1);
    }
  }

  public static String fileName(URI uri) {
    String[] parts = uri.toString().split("/");
    if (parts.length == 0) {
      return "";
    }
    return parts[parts.length - 1];
  }

  public static String packageName(Path file) {
    Pattern packagePattern = Pattern.compile("^package +(.*);");
    Pattern startOfClass = Pattern.compile("^[\\w ]*class +\\w+");
    try (BufferedReader lines = FileStore.lines(file)) {
      for (String line = lines.readLine(); line != null; line = lines.readLine()) {
        if (startOfClass.matcher(line).find()) {
          return "";
        }
        Matcher matchPackage = packagePattern.matcher(line);
        if (matchPackage.matches()) {
          return matchPackage.group(1);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    // TODO fall back on parsing file
    return "";
  }

  public static byte[] toByteArray(final Reader reader, final Charset charset) throws IOException {
    try (final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
      copy(reader, output, charset);
      return output.toByteArray();
    }
  }

  public static void copy(
      final Reader reader, final OutputStream output, final Charset outputCharset)
      throws IOException {
    final OutputStreamWriter writer = new OutputStreamWriter(output, toCharset(outputCharset));
    copy(reader, writer);
    // XXX Unless anyone is planning on rewriting OutputStreamWriter,
    // we have to flush here.
    writer.flush();
  }

  static Charset toCharset(final Charset charset) {
    return charset == null ? Charset.defaultCharset() : charset;
  }

  public static int copy(final Reader reader, final Writer writer) throws IOException {
    final long count = copyLarge(reader, writer, SKIP_CHAR_BUFFER.get());
    if (count > Integer.MAX_VALUE) {
      return -1;
    }
    return (int) count;
  }

  public static long copyLarge(final Reader reader, final Writer writer, final char[] buffer)
      throws IOException {
    long count = 0;
    int n;
    while (-1 != (n = reader.read(buffer))) {
      writer.write(buffer, 0, n);
      count += n;
    }
    return count;
  }

  private static char[] charArray() {
    return new char[8192];
  }

  private static final ThreadLocal<char[]> SKIP_CHAR_BUFFER =
      ThreadLocal.withInitial(StringSearch::charArray);
  private static final ILogger LOG = ILogger.newInstance("StringSearch");
}
