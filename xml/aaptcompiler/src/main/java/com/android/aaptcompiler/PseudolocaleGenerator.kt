package com.android.aaptcompiler

import com.android.aaptcompiler.android.ResTableConfig

/**
 * The struct that represents both [Span] and [UntranslatableSection] objects.
 *
 * @param tag The tag description if and only if the UnifiedSpan represents a [Span], otherwise a
 *   value of {@code null} if the UnifiedSpan represents a [UntranslatableSection]
 * @param firstChar The UTF-16 index into the string where this span starts.
 * @param lastChar The UTF-16 index into the string where this span ends, inclusive.
 */
data class UnifiedSpan(
  val tag: String?, var firstChar: Int, var lastChar: Int): Comparable<UnifiedSpan> {

  constructor(span: Span): this(span.name, span.firstChar, span.lastChar)

  constructor(span: StringPool.Span): this(span.name.value(), span.firstChar, span.lastChar)

  constructor(section: UntranslatableSection): this(null, section.startIndex, section.endIndex - 1)

  fun isSpan() = tag != null

  fun isUntranslatable() = tag == null

  fun toSpan() = if (isSpan()) Span(tag!!, firstChar, lastChar) else null

  override fun compareTo(other: UnifiedSpan) =
    when {
      firstChar != other.firstChar -> firstChar.compareTo(other.firstChar)
      lastChar != other.lastChar -> other.lastChar.compareTo(lastChar)
      else -> 0
    }
}

internal fun mergeSpans(string: StyledString): List<UnifiedSpan> {
  val unifiedSpans = mutableListOf<UnifiedSpan>()
  string.spans().forEach {
    unifiedSpans.add(UnifiedSpan(it))
  }
  string.untranslatableSections.forEach {
    unifiedSpans.add(UnifiedSpan(it))
  }
  unifiedSpans.sort()
  return unifiedSpans
}

internal fun pseudolocalizeStyledString(
  original: StyledString, method: Pseudolocalizer.Method, pool: StringPool
): StyledString {

  val localizer = Pseudolocalizer(method)

  // Collect the spans and untranslatable sections into one set of spans, sorted by first_char.
  // This will effectively subdivide the string into multiple sections that can be individually
  // pseudolocalized, while keeping the span indices synchronized.
  val mergedSpans = mergeSpans(original)

  // The new string to be created.
  val newText = StringBuilder(localizer.start())

  // Stack keeping track of the indices of what nested Span we're in.
  val spanStack = mutableListOf<Int>()

  val originalText = original.ref.value()

  // The current position in the original text.
  var cursor = 0

  // We assume no nesting of untranslatable section, as XLIFF does not allow it.
  var inTranslatable = true

  var currentSpanId = 0
  while (currentSpanId < mergedSpans.size || spanStack.isNotEmpty()) {
    val currentSpan = mergedSpans.getOrNull(currentSpanId)
    val parentSpan = if (spanStack.isEmpty()) null else mergedSpans[spanStack.last()]

    if (currentSpan != null) {
      if (parentSpan == null || parentSpan.lastChar >= currentSpan.lastChar) {
        // There is no parent, or this span is the child of the parent.
        // Pseudolocalize all the text until this span.
        val substr = originalText.substring(cursor, currentSpan.firstChar)
        cursor = currentSpan.firstChar

        // Pseudolocalize the substring
        val newSubstr = if (inTranslatable) localizer.text(substr) else substr

        newText.append(newSubstr)

        // rewrite the first char
        currentSpan.firstChar = newText.length
        if (currentSpan.isUntranslatable()) {
          // An untranslatable section has begun.
          inTranslatable = false
        }
        spanStack.add(currentSpanId)
        ++currentSpanId
        continue
      }
    }

    if (parentSpan != null) {
      // There is a parent, and either this span is not a child of it, or there are no more spans.
      // As such, we can finish up with the parent span.
      val substr = originalText.substring(cursor, parentSpan.lastChar + 1)
      cursor = parentSpan.lastChar + 1

      // Pseudolocalize the substring
      val newSubstring = if (inTranslatable) localizer.text(substr) else substr

      newText.append(newSubstring)

      // rewrite the last char
      parentSpan.lastChar = newText.length - 1

      if (parentSpan.isUntranslatable()) {
        // Exiting an untranslatable section.
        inTranslatable = true
      }
      spanStack.removeAt(spanStack.lastIndex)
    }
  }

  // Finish the pseudolocalization at the end of the string.
  newText.append(localizer.text(originalText.substring(cursor)))
  newText.append(localizer.end())

  val spans = mutableListOf<Span>()
  mergedSpans.forEach {
    if (it.isSpan()) {
      spans.add(it.toSpan()!!)
    }
  }

  val styleString = StyleString(newText.toString(), spans)
  return StyledString(pool.makeRef(styleString), listOf())
}

internal fun pseudolocalizeBasicString(
  original: BasicString, method: Pseudolocalizer.Method, pool: StringPool
): BasicString {
  try {
    val localizer = Pseudolocalizer(method)

    val originalText = original.ref.value()
    val newText = StringBuilder(localizer.start())

    // Pseudolocalize only the translatable sections.
    var start = 0
    for (section in original.untranslatables) {
      // Pseudolocalize the content before the untranslatable section.
      if (section.startIndex != start) {
        newText.append(localizer.text(originalText.substring(start, section.startIndex)))
      }

      // Copy in the untranslatable content.
      newText.append(originalText.substring(section.startIndex, section.endIndex))
      start = section.endIndex
    }

    // Pseudolocalize the content after the last untranslatable section.
    if (start != originalText.length) {
      newText.append(localizer.text(originalText.substring(start)))
    }
    newText.append(localizer.end())

    return BasicString(pool.makeRef(newText.toString()))
  } catch (e: Exception) {
    // TODO: add to list of errors
    error("Failed to pseudo-localize string: $original")
  }
}

internal fun pseudolocalizePlural(
  original: Plural, method: Pseudolocalizer.Method, pool: StringPool
): Plural {

  val localizedPlural = Plural()

  for (i in original.values.indices) {
    val value = original.values[i]

    if (value != null) {
      localizedPlural.values[i] = when (value) {
        is BasicString -> pseudolocalizeBasicString(value, method, pool)
        is StyledString -> pseudolocalizeStyledString(value, method, pool)
        else -> value.clone(pool)
      }
    }
  }
  return localizedPlural
}

class PseudolocaleGenerator{

  private fun modifyForLocale(
    config: ConfigDescription, method: Pseudolocalizer.Method): ConfigDescription {

    val modified = ConfigDescription(config)
    when (method) {
      Pseudolocalizer.Method.ACCENT -> {
        modified.language[0] = 'e'.code.toByte()
        modified.language[1] = 'n'.code.toByte()
        modified.country[0] = 'X'.code.toByte()
        modified.country[1] = 'A'.code.toByte()
      }
      Pseudolocalizer.Method.BIDI -> {
        modified.language[0] = 'a'.code.toByte()
        modified.language[1] = 'r'.code.toByte()
        modified.country[0] = 'X'.code.toByte()
        modified.country[1] = 'B'.code.toByte()
      }
      else -> {}
    }
    return modified
  }

  // A value is pseudolocalizable if it does not define a locale (or is the default locale) and is
  // translatable.
  private fun isPseudolocalizable(configValue: ResourceConfigValue): Boolean {
    val diff = configValue.config.diff(ConfigDescription())
    if ((diff and ResTableConfig.CONFIG_LOCALE) != 0) {
      return false
    }
    return configValue.value!!.translatable
  }

  private fun pseudolocalizeIfNeeded(
    method: Pseudolocalizer.Method,
    originalValue: ResourceConfigValue,
    pool: StringPool,
    entry: ResourceEntry) {

    val valueToLocalize = originalValue.value
    valueToLocalize ?: return

    val localizedValue = when (valueToLocalize) {
      is BasicString -> pseudolocalizeBasicString(valueToLocalize, method, pool)
      is StyledString -> pseudolocalizeStyledString(valueToLocalize, method, pool)
      is Plural -> pseudolocalizePlural(valueToLocalize, method, pool)
      else -> return
    }

    localizedValue.source = valueToLocalize.source
    localizedValue.weak = valueToLocalize.weak

    val modifiedConfig = modifyForLocale(originalValue.config, method)
    val newConfigValue = entry.findOrCreateValue(modifiedConfig, originalValue.product)

    // Only use auto-generated pseudo-localization if none is defined.
    if (newConfigValue.value == null) {
      newConfigValue.value = localizedValue
    }
  }

  fun consume (table: ResourceTable) {
    for (resourcePackage in table.packages) {
      for (resourceGroup in resourcePackage.groups) {
        for (idToEntry in resourceGroup.entries.values) {
          for (entry in idToEntry.values) {
            val valuesToLocalize = entry.values.filter { isPseudolocalizable(it) }
            for (value in valuesToLocalize) {
              pseudolocalizeIfNeeded(Pseudolocalizer.Method.ACCENT, value, table.stringPool, entry)
              pseudolocalizeIfNeeded(Pseudolocalizer.Method.BIDI, value, table.stringPool, entry)
            }
          }
        }
      }
    }
  }
}
