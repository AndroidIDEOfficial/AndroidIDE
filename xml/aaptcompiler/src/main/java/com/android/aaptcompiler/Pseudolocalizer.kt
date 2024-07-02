package com.android.aaptcompiler

import com.android.aaptcompiler.android.isTruthy

// Placeholder marks
private const val PLACEHOLDER_OPEN = "\u00bb"
private const val PLACEHOLDER_CLOSE = "\u00ab"

// Special unicode characters to override directionality of the words
private const val RLM = "\u200f"
private const val RLO = "\u202e"
private const val PDF = "\u202c"

// Argument markers for text
private const val ARG_START = '{'
private const val ARG_END = '}'

private fun Char.pseudoLocalize() = when (this) {
  'a' -> "\u00e5"
  'b' -> "\u0253"
  'c' -> "\u00e7"
  'd' -> "\u00f0"
  'e' -> "\u00e9"
  'f' -> "\u0192"
  'g' -> "\u011d"
  'h' -> "\u0125"
  'i' -> "\u00ee"
  'j' -> "\u0135"
  'k' -> "\u0137"
  'l' -> "\u013c"
  'm' -> "\u1e3f"
  'n' -> "\u00f1"
  'o' -> "\u00f6"
  'p' -> "\u00fe"
  'q' -> "\u0051"
  'r' -> "\u0155"
  's' -> "\u0161"
  't' -> "\u0163"
  'u' -> "\u00fb"
  'v' -> "\u0056"
  'w' -> "\u0175"
  'x' -> "\u0445"
  'y' -> "\u00fd"
  'z' -> "\u017e"
  'A' -> "\u00c5"
  'B' -> "\u03b2"
  'C' -> "\u00c7"
  'D' -> "\u00d0"
  'E' -> "\u00c9"
  /* No 'F' */
  'G' -> "\u011c"
  'H' -> "\u0124"
  'I' -> "\u00ce"
  'J' -> "\u0134"
  'K' -> "\u0136"
  'L' -> "\u013b"
  'M' -> "\u1e3e"
  'N' -> "\u00d1"
  'O' -> "\u00d6"
  'P' -> "\u00de"
  'Q' -> "\u0071"
  'R' -> "\u0154"
  'S' -> "\u0160"
  'T' -> "\u0162"
  'U' -> "\u00db"
  'V' -> "\u03bd"
  'W' -> "\u0174"
  'X' -> "\u00d7"
  'Y' -> "\u00dd"
  'Z' -> "\u017d"
  '!' -> "\u00a1"
  '?' -> "\u00bf"
  '$' -> "\u20ac"
  else -> null
}

private fun Char.isPossiblePlaceHolderEnd() = when (this) {
  's', 'S',
  'c', 'C',
  'd',
  'o',
  'x', 'X',
  'f',
  'e', 'E',
  'g', 'G',
  'a', 'A',
  'b', 'B',
  'h', 'H',
  '%',
  'n' -> true
  else -> false
}

abstract class PseudoMethodImpl {
  open fun start() = ""
  open fun end() = ""
  abstract fun text(originalText: String): String
  abstract fun placeholder(originalText: String): String
}

object PseudoMethodNone : PseudoMethodImpl() {
  override fun text(originalText: String) = originalText
  override fun placeholder(originalText: String) = originalText
}

object PseudoMethodBidi: PseudoMethodImpl() {
  private const val ESCAPE_CHAR = '\\'

  override fun text(originalText: String): String {
    val result = StringBuilder()
    var lastSpace = true
    var space = true
    var escape = false

    for (i in originalText.indices) {
      val currentChar = originalText[i]

      if (!escape && currentChar == ESCAPE_CHAR) {
        escape = true
        continue
      }

      space = (!escape && currentChar.isWhitespace()) ||
        (escape && (currentChar == 'n' || currentChar == 't'))
      if (lastSpace && !space) {
        // Word start.
        result.append(RLM + RLO)
      } else if (!lastSpace && space) {
        // Word end.
        result.append(PDF + RLM)
      }
      lastSpace = space
      if (escape) {
        result.append(ESCAPE_CHAR)
      }
      result.append(currentChar)
    }
    if (!lastSpace) {
      // End of last word
      result.append(PDF + RLM)
    }
    return result.toString()
  }

  override fun placeholder(originalText: String): String =
    // Surround a placeholder with directionality change sequence
    RLM + RLO + originalText + PDF + RLM
}

class PseudoMethodAccent: PseudoMethodImpl() {
  var depth = 0
  var wordCount = 0
  var length = 0

  override fun start(): String {
    var result = ""
    if (depth == 0) {
      result = "["
    }
    wordCount = 0
    length = 0
    ++depth
    return result
  }

  override fun end(): String {
    val result = StringBuilder()
    if (length.isTruthy()) {
      result.append(' ')
      result.append(pseudoGenerateExpansion(if (wordCount > 3) length else length/2))
    }
    wordCount = 0
    length = 0
    --depth
    if (depth == 0) {
      result.append("]")
    }
    return result.toString()
  }

  /**
   * Converts characters so they look like they've been localized.
   *
   * Note: This leaves placeholder syntax untouched.
   */
  override fun text(originalText: String): String {
    val result = StringBuilder()
    val size = originalText.length
    var lastSpace = true
    var currentIndex = 0
    while (currentIndex < size) {
      val currentChar = originalText[currentIndex]
      when (currentChar) {
        '%' -> {
          // Placeholder syntax, no need to pseudolocalize.
          currentIndex = processMaybePlaceholder(result, originalText, currentIndex)
        }
        '<', '&' -> {
          // Html syntax, no need to pseudolocalize
          currentIndex = processHtml(result, originalText, currentIndex)
        }
        else -> {
          // This is a pure text that should be pseudolocalized.
          val localized = currentChar.pseudoLocalize()
          if (localized != null) {
            result.append(localized)
          } else {
            val space = currentChar.isWhitespace()
            if (lastSpace && !space) {
              ++wordCount
            }
            lastSpace = space
            result.append(currentChar)
          }
          // Count only pseudolocalizable chars and delimiters
          ++length
        }
      }
      ++currentIndex
    }
    return result.toString()
  }

  override fun placeholder(originalText: String): String =
    // Surround a placeholder with brackets
    PLACEHOLDER_OPEN + originalText + PLACEHOLDER_CLOSE

  private fun pseudoGenerateExpansion(length: Int): String {
    val result = StringBuilder(EXPANSION_STRING)
    while (result.length < length) {
      result.append(" ")
      result.append(EXPANSION_STRING)
    }
    val lastSpace = result.indexOf(' ', length + 1)

    return result.substring(0, if (lastSpace == -1) result.length else lastSpace)
  }

  private fun processMaybePlaceholder(
    partial: StringBuilder, source: String, startIndex: Int) : Int {

    val size = source.length
    var currentIndex = startIndex
    var currentChar = source[currentIndex]

    val chunk = StringBuilder().append(currentChar)
    while (currentIndex + 1 < size) {
      ++currentIndex
      currentChar = source[currentIndex]
      chunk.append(currentChar)

      if (currentChar.isPossiblePlaceHolderEnd()) {
        break
      }

      if (currentIndex + 1 < size && currentChar == 't') {
        ++currentIndex
        currentChar = source[currentIndex]
        chunk.append(currentChar)
        break
      }
    }

    // Treat chunk as a placeholder unless it ends with '%'.
    partial.append(if(currentChar == '%') chunk else placeholder(chunk.toString()))
    return currentIndex
  }

  private fun processHtml(sourcePartial: StringBuilder, source: String, startIndex: Int): Int {
    val size = source.length
    var currentIndex = startIndex
    var currentChar = source[currentIndex]
    val htmlPartial = StringBuilder()

    @Suppress("KotlinConstantConditions")
    htmlTag@ while (currentIndex < size) {

      if (currentChar == '&') {
        val escapeText = StringBuilder().append(currentChar)
        var htmlCodePosition = currentIndex

        htmlCode@ while (htmlCodePosition + 1 < size) {
          ++htmlCodePosition
          currentChar = source[htmlCodePosition]
          escapeText.append(currentChar)

          when (currentChar) {
            ';' -> {
              // valid html code.
              currentIndex = htmlCodePosition
              break@htmlCode
            }
            '#', in 'a'..'z', in 'A'..'Z', in '0'..'9' -> {
              // Valid html, skip.
            }
            else -> {
              // Wrong html code.
              break@htmlCode
            }
          }
        }
        htmlPartial.append(escapeText)
        if (escapeText.toString() != "&lt;") {
          break@htmlTag
        }
        continue
      }

      if (currentChar == '>') {
        htmlPartial.append('>')
        break@htmlTag
      }

      htmlPartial.append(currentChar)
      ++currentIndex
      if (currentIndex >= size) {
        // we didn't find the closing bracket, it wasn't valid HTML. Needs to be pseudolocalized, so
        // return starting index, and append the first character to the partial string builder.
        sourcePartial.append(source[startIndex])
        return startIndex
      }
      currentChar = source[currentIndex]
    }
    sourcePartial.append(htmlPartial.toString())
    return currentIndex
  }

  // Yes, "fiveteen".
  private val EXPANSION_STRING = "one two three four five six seven eight nine ten eleven " +
    "twelve thirteen fourteen fiveteen sixteen seventeen nineteen twenty"

}


class Pseudolocalizer(method: Method) {
  private var implementation: PseudoMethodImpl
  var lastDepth = 0
    private set

  enum class Method {
    NONE,
    ACCENT,
    BIDI;

    fun getMethod() = when (this) {
      Method.NONE -> PseudoMethodNone
      Method.ACCENT -> PseudoMethodAccent()
      Method.BIDI -> PseudoMethodBidi
    }
  }

  init {
    implementation = method.getMethod()
  }

  fun setMethod(method: Method) {
    implementation = method.getMethod()
  }
  fun start() = implementation.start()
  fun end() = implementation.end()
  fun text(originalText: String): String {
    val out = StringBuilder()
    var depth = lastDepth
    var lastPos = 0
    var pos = 0
    val size = originalText.length
    var escaped = false
    while (pos < size) {
      val currentChar = originalText[pos]
      if (escaped) {
        escaped = false
        ++pos
        continue
      }
      if (currentChar == '\'') {
        escaped = true
        ++pos
        continue
      }

      if (currentChar == ARG_START) {
        ++depth
      } else if (currentChar == ARG_END && depth.isTruthy()) {
        --depth
      }

      if (lastDepth != depth || pos == size - 1) {
        val pseudo = (lastDepth % 2) == 0
        var nextPos = pos
        if (!pseudo || depth == lastDepth) {
          ++nextPos
        }
        val chunkSize = nextPos - lastPos
        if (chunkSize.isTruthy()) {
          var chunk = originalText.substring(lastPos, nextPos)
          if (pseudo) {
            chunk = implementation.text(chunk)
          } else if (originalText[lastPos] == ARG_START && originalText[nextPos - 1] == ARG_END) {
            chunk = implementation.placeholder(chunk)
          }
          out.append(chunk)
        }
        if (pseudo && depth < lastDepth) {
          out.append(implementation.end())
        } else if (!pseudo && depth > lastDepth) {
          out.append(implementation.start())
        }
        lastPos = nextPos
        lastDepth = depth
      }
      ++pos
    }
    return out.toString()
  }
}