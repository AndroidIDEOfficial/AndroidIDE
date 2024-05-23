package com.android.aaptcompiler

import com.android.aaptcompiler.android.parseHex
import java.util.PrimitiveIterator
import kotlin.math.max
import kotlin.math.min

/**
 * A string flattened from an XML hierarchy, which maintains tags and untranslatable sections in
 * parallel data structures.
 *
 * @property rawString The raw string of the flattened xml.
 *   <p> This string will contain all removed spaces and escape characters. For the processed
 *   string see {@code styleString.str}.
 * @property styleString The processed string with all spans generated.
 * @property untranslatableSections all marked untranslatable sections from the xml.
 * @property success Whether or not this resource was parsed without error.
 */
data class FlattenedXml(
  val rawString: String,
  val styleString: StyleString,
  val untranslatableSections: List<UntranslatableSection>,
  val success: Boolean)

/**
 * Flattens an XML hierarchy into a FlattenedXml string, formatting the text, escaping characters,
 * and removing whitespace, all while keeping the untranslatable sections and spans in sync with the
 * transformations.
 *
 * <p> Specifically, the StringBuilder will handle escaped characters like \t, \n, \\, \', etc.
 * Single quotes *must* be escaped, unless within a pair of double-quotes.
 * Pairs of double-quotes disable whitespace stripping of the enclosed text.
 * Unicode escape codes (\u0049) are interpreted and the represented Unicode character is inserted.
 *
 * <p> A NOTE ON WHITESPACE:
 *
 * <p> When preserve_spaces is false, and when text is not enclosed within double-quotes,
 * StringBuilder replaces a series of whitespace with a single space character. This happens at the
 * start and end of the string as well, so leading and trailing whitespace is possible, but only if
 * the flattened xml being built contains spans.
 *
 * <p> When a Span is started or stopped, the whitespace counter is reset, meaning if whitespace
 * is encountered directly after the span, it will be emitted. This leads to situations like the
 * following: "This <b> is </b> spaced" -> "This  is  spaced". Without spans, this would be properly
 * compressed: "This  is  spaced" -> "This is spaced".
 *
 * <p> Untranslatable sections do not have the same problem:
 * "This <xliff:g> is </xliff:g> not spaced" -> "This is not spaced".
 *
 * <p> NOTE: This is all the way it is because AAPT1 did it this way. Maintaining backwards
 * compatibility is important.
 *
 * @property preserveSpaces If true, whitespace removal is not performed.
 *   <p> Single quotations can be used without escaping when {@code preservedSpaces} is set to true
 *   as well.
 */
class XmlStringBuilder(private val preserveSpaces: Boolean = false) {
  var inQuote = preserveSpaces
    private set
  var lastCodepointWasSpace = false
    private set
  private var lastCodepointWasBackslash = false
  private var firstQuote = -1
  private var lastQuote = -1
  private var firstChar = -1
  private var lastChar = -1
  private var rawStringBuilder: StringBuilder = StringBuilder()
  private var textBuilder: StringBuilder = StringBuilder()

  private val spans = mutableListOf<Span>()
  private val spanStack = mutableListOf<Span>()

  private val untranslatableSections = mutableListOf<UntranslatableSection>()
  private var inUntranslatable = false

  /** @property error the last error found while constructing the flattened xml. */
  var error = ""
    private set


  /** Resets the XmlStringBuilder state to an empty string. */
  fun clear() {
    inQuote = preserveSpaces
    lastCodepointWasSpace = false
    lastCodepointWasBackslash = false
    firstQuote = -1
    lastQuote = -1
    firstChar = -1
    lastChar = -1
    rawStringBuilder = StringBuilder()
    textBuilder = StringBuilder()

    spans.clear()
    spanStack.clear()

    untranslatableSections.clear()
    inUntranslatable = false

    error = ""
  }

  /** Transform escaped codepoint into expected codepoint. */
  fun handleEscape(
      codePoint: Int,
      textBuilder: StringBuilder,
      codePoints: PrimitiveIterator.OfInt,
      str: String
  ): Boolean {
    when (codePoint) {
      't'.code -> textBuilder.append('\t')
      'n'.code -> textBuilder.append('\n')
      '#'.code, '@'.code, '?'.code, '"'.code, '\''.code, '\\'.code ->
          textBuilder.appendCodePoint(codePoint)
      'u'.code -> {
          if (!appendUnicodeEscapeSequence(codePoints, textBuilder)) {
              error = "Invalid unicode escape sequence in string\n\"$str\""
              return false
          }
      }
      // Can ignore the escape character and just include the code point.
      else -> textBuilder.appendCodePoint(codePoint)
    }
    return true
  }

  /** Appends a chunk of text to the xml string. */
  fun append(str : String) : XmlStringBuilder {
    rawStringBuilder.append(str)

    val codePoints = str.codePoints().iterator()
    while (codePoints.hasNext()) {
      var codePoint = codePoints.nextInt()

      if (!Character.isValidCodePoint(codePoint)) {
        error = "Invalid unicode code point in string\n\"$str\""
        return this
      }

      if (Character.isWhitespace(codePoint) && !preserveSpaces && !inQuote) {
        if (!lastCodepointWasSpace) {
          // emit a space if it is the first
          textBuilder.append(' ')
          lastCodepointWasSpace = true
          lastCodepointWasBackslash = false
        }

        continue
      }

      if (firstChar == -1) {
          firstChar = textBuilder.length
      }

      lastCodepointWasSpace = false
      when {
        lastCodepointWasBackslash -> {
          if (!handleEscape(codePoint, textBuilder, codePoints, str)) {
            return this
          }
          lastChar = textBuilder.length
          lastCodepointWasBackslash = false
        }
        codePoint == '\\'.code -> {
            if (codePoints.hasNext()) {
                codePoint = codePoints.nextInt()
                if (!handleEscape(codePoint, textBuilder, codePoints, str)) {
                    return this
                }
                lastChar = textBuilder.length
            } else {
                lastCodepointWasBackslash = true
            }
        }
        codePoint == '\"'.code && !preserveSpaces -> {
          // only toggle quote when we are not preserving spaces.
          inQuote = !inQuote
          if (firstQuote == -1) {
              firstQuote = textBuilder.length
          }
          lastQuote = textBuilder.length
          lastChar = textBuilder.length
        }
        codePoint == '\''.code && !preserveSpaces && !inQuote -> {
          // this should be escaped when we are not preserving spaces
          error = "Invalid unicode escape sequence in string\n\"{str}\""
          return this
        }
        else -> {
          textBuilder.appendCodePoint(codePoint)
          lastChar = textBuilder.length
        }
      }
    }

    return this
  }

  /**
   * Starts a Span (tag) with the given name.
   * <p> The name is expected to be of the form:
   * "tag_name;attr1=value;attr2=value"
   * <p> Which is how Spans are encoded in the ResStringPool. A corresponding call to
   * [endSpan] is needed to close the span.
   */
  fun startSpan(name : String): XmlStringBuilder {
    if (error.isNotEmpty()) {
      return this
    }
    // When a span is started, all state associated with whitespace truncation and quotation is
    // ended.
    resetTextState()
    val span = Span(name, textBuilder.length)
    spans.add(span)
    spanStack.add(span)

    return this
  }

  /** Ends the last span (tag) that was started with a call to [startSpan]. */
  fun endSpan(): XmlStringBuilder {
    if (error.isNotEmpty()) {
      return this
    }
    if (spanStack.isEmpty()) {
      error = "Unmatched span end."
      return this
    }
    // When a span is ended, all state associated with whitespace truncation and quotation is ended.
    resetTextState()
    val lastSpan = spanStack.removeAt(spanStack.lastIndex)
    lastSpan.lastChar = textBuilder.length - 1

    return this
  }

  /**
   * Starts an Untranslatable section.
   * <p> To end the section, call the [endUntranslatable] method. No more than one translatable
   * section can be open at one time.
   */
  fun startUntranslatable(): XmlStringBuilder {
    if (error.isNotEmpty()) {
      return this
    }
    if (inUntranslatable) {
      error = "Attempt to nest untranslatable sections in flattened xml, which is not allowed."
      return this
    }

    val section = UntranslatableSection(textBuilder.length)
    untranslatableSections.add(section)
    inUntranslatable = true

    return this
  }

  /** Ends the current untranslatable section started with a call to [startUntranslatable] */
  fun endUntranslatable(): XmlStringBuilder {
    if (error.isNotEmpty()) {
      return this
    }
    if (!inUntranslatable) {
      error = "Attempting to end untranslatable section while not in an untranslatable sections."
      return this
    }

    untranslatableSections.last().endIndex = textBuilder.length
    inUntranslatable = false

    return this
  }

  /**
   * Returns the flattened XML string, with all spans and untranslatable sections encoded as
   * parallel data structures.
   */
  fun getFlattenedXml() : FlattenedXml {
    if (inUntranslatable) {
      error = "Attempting to flatten xml with unfinished untranslatable section."
    }
    if (spanStack.isNotEmpty()) {
      error = "Attempting to flatten xml with unfinished spans."
    }
    var untranslatables: List<UntranslatableSection> = untranslatableSections
    var text = textBuilder.toString()
    if (spans.isEmpty()) {

      var firstChar = getFirstChar()
      if (untranslatableSections.isNotEmpty()) {
        firstChar = min(firstChar, untranslatableSections.first().startIndex)
      }

      var lastChar = getLastChar(text)
      if (untranslatableSections.isNotEmpty()) {
        lastChar = max(lastChar, untranslatableSections.last().endIndex)
      }
      untranslatables = untranslatableSections.map {
        it.shift(-firstChar)
      }

      // If we had a string made fully of whitespace, then firstChar > lastChar. Since we're
      // removing leading and trailing whitespaces, handle this as an empty string.
      text = if (firstChar >= lastChar) "" else textBuilder.substring(firstChar, lastChar)
    }

    val raw = rawStringBuilder.toString()
    return FlattenedXml(
        raw, StyleString(text, spans.toList()), untranslatables.toList(), error.isEmpty())
  }

  private fun getFirstChar(): Int {
    // If there were no spans, we treat this string a little differently (according to AAPT).
    // We must strip the leading and trailing whitespace.
    if (firstQuote == -1) {
      if (firstChar != -1) {
        // If there are no quotes, remove all whitespace before first character.
        return firstChar
      }
      // If there are no characters in the string, it's just whitespaces, the resulting string
      // string should be empty.
      return textBuilder.length
    }
    // If there are quotes we need to check if there are any non-whitespace characters before
    // the quote. If there are any, the string should be preserved.
    if (firstChar != -1 && firstChar < firstQuote)
      return 0
    return firstQuote
  }

  private fun getLastChar(text: String): Int {
    if (lastQuote == -1) {
      // If we had no quotes then just trim the whitespace at the end of the string.
      if (lastChar != -1) {
          return lastChar
      }
      // If there were no characters, it's all whitespace, and the resulting string should be empty.
      return 0
    }
    // If we had quotes, only trim the string if there are exclusively whitespaces after the
    // last quote.
    if (lastChar != -1 && lastChar > lastQuote)
      return text.length
    return lastQuote
  }

  private fun appendUnicodeEscapeSequence(iter : Iterator<Int>, text: StringBuilder) : Boolean {
    var code = 0
    for (i in 1..4) {
      if (!iter.hasNext()) {
        break
      }
      val codePoint = iter.next()
      val value = parseHex(codePoint)
      if (value == -1) return false

      code = (code shl 4) or value
    }
    if (!Character.isValidCodePoint(code)) {
      return false
    }
    text.appendCodePoint(code)
    return true
  }

  private fun resetTextState() {
    inQuote = preserveSpaces
    lastCodepointWasSpace = false
    lastCodepointWasBackslash = false
  }

}
