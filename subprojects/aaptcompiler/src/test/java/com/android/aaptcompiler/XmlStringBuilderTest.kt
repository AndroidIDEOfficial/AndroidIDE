package com.android.aaptcompiler

import com.google.common.truth.Truth
import org.junit.Test

class XmlStringBuilderTest {
  @Test
  fun testStringBuilderWhitespaceRemoval() {
    // XmlStringBuilder is aware of removing spaces on the ends of the build string when the string
    // has no spans. Thus we need to add spans to shift the xml string.
    val stringBuilder = XmlStringBuilder()
    var spanString = stringBuilder
      .startSpan("hi")
      .append("    hey guys ")
      .append(" this is so cool ")
      .endSpan()
      .getFlattenedXml()
    Truth.assertThat(stringBuilder.error).isEmpty()
    Truth.assertThat(spanString.styleString.str).isEqualTo(" hey guys this is so cool ")

    stringBuilder.clear()
    spanString = stringBuilder
      .startSpan("hi")
      .append(" \" wow,  so many \t")
      .append("spaces. \"what?  ")
      .endSpan()
      .getFlattenedXml()
    Truth.assertThat(stringBuilder.error).isEmpty()
    Truth.assertThat(spanString.styleString.str)
      .isEqualTo("  wow,  so many \tspaces. what? ")

    stringBuilder.clear()
    spanString = stringBuilder
      .startSpan("hi")
      .append("  where \t ")
      .append(" \nis the pie?")
      .endSpan()
      .getFlattenedXml()
    Truth.assertThat(stringBuilder.error).isEmpty()
    Truth.assertThat(spanString.styleString.str).isEqualTo(" where is the pie?")

    // If the string has no spans then the spaces at the end are trimmed. (up to the first/last
    // quotation).
    stringBuilder.clear()
    var basicString = stringBuilder
      .append("   \tOh   where")
      .append(" is \t  my   hairbrush?\n \t")
      .getFlattenedXml()
    Truth.assertThat(stringBuilder.error).isEmpty()
    Truth.assertThat(basicString.styleString.str).isEqualTo("Oh where is my hairbrush?")

    stringBuilder.clear()
    basicString = stringBuilder
      .append(" \" \tHey there.  \"  ")
      .getFlattenedXml()
    Truth.assertThat(stringBuilder.error).isEmpty()
    Truth.assertThat(basicString.styleString.str).isEqualTo(" \tHey there.  ")

    stringBuilder.clear()
    basicString =
      stringBuilder.append("""   before Quote " inside quote " after Quote   """).getFlattenedXml()
    Truth.assertThat(stringBuilder.error).isEmpty()
    Truth.assertThat(basicString.styleString.str)
      .isEqualTo(" before Quote  inside quote  after Quote ")

    stringBuilder.clear()
    basicString =
      stringBuilder.append("""a"b"c""").getFlattenedXml()
    Truth.assertThat(stringBuilder.error).isEmpty()
    Truth.assertThat(basicString.styleString.str)
      .isEqualTo("abc")

    stringBuilder.clear()
    basicString =
      stringBuilder.append("""a"bc""").getFlattenedXml()
    Truth.assertThat(stringBuilder.error).isEmpty()
    Truth.assertThat(basicString.styleString.str)
      .isEqualTo("abc")

    stringBuilder.clear()
    basicString =
      stringBuilder.append("""ab"c""").getFlattenedXml()
    Truth.assertThat(stringBuilder.error).isEmpty()
    Truth.assertThat(basicString.styleString.str)
      .isEqualTo("abc")

    stringBuilder.clear()
    basicString =
      stringBuilder.append("""""""").getFlattenedXml()
    Truth.assertThat(stringBuilder.error).isEmpty()
    Truth.assertThat(basicString.styleString.str)
      .isEqualTo("")
  }

  @Test
  fun testStringBuilderEscaping() {
    val stringBuilder = XmlStringBuilder()
    var basicString = stringBuilder
      .append("hey guys\\n ")
      .append(" this \\t is so\\\\ cool")
      .getFlattenedXml()
    Truth.assertThat(stringBuilder.error).isEmpty()
    Truth.assertThat(basicString.styleString.str)
      .isEqualTo("hey guys\n this \t is so\\ cool")

    stringBuilder.clear()
    basicString = stringBuilder
      .append("\\@\\?\\#\\\\\\'")
      .getFlattenedXml()
    Truth.assertThat(stringBuilder.error).isEmpty()
    Truth.assertThat(basicString.styleString.str).isEqualTo("@?#\\\'")
  }

  @Test
  fun testStringBuilderMisplacedQuote() {
    val stringBuilder = XmlStringBuilder()
    stringBuilder.append("they're coming")
    Truth.assertThat(stringBuilder.error).isNotEmpty()
  }

  @Test
  fun testStringBuilderUnicodeCodes() {
    val stringBuilder = XmlStringBuilder()
    val basicString = stringBuilder.append("\\u00AF\\u0AF0 whoa").getFlattenedXml()
    Truth.assertThat(stringBuilder.error).isEmpty()
    Truth.assertThat(basicString.styleString.str).isEqualTo("\u00AF\u0AF0 whoa")

    stringBuilder.clear()
    stringBuilder.append("\\u00 yo")
    Truth.assertThat(stringBuilder.error).isNotEmpty()
  }

  @Test
  fun testStringBuilderPreserveSpacesRecordsDoubleQuotes() {
    val stringBuilder = XmlStringBuilder(true)
    val basicString = stringBuilder.append("\"").getFlattenedXml()
    Truth.assertThat(stringBuilder.error).isEmpty()
    Truth.assertThat(basicString.styleString.str).isEqualTo("\"")
  }

  @Test
  fun testUnfinishedSpansCauseError() {
    val stringBuilder = XmlStringBuilder(false)
    val spanAttempt = stringBuilder.startSpan("b").append("hi").getFlattenedXml()
    Truth.assertThat(stringBuilder.error).isNotEmpty()
    Truth.assertThat(spanAttempt.success).isFalse()

    stringBuilder.clear()
    val spanAttempt2 = stringBuilder
      .startSpan("b")
      .append("how ")
      .startSpan("i")
      .append("are you?")
      .endSpan()
      .getFlattenedXml()
    Truth.assertThat(stringBuilder.error).isNotEmpty()
    Truth.assertThat(spanAttempt2.success).isFalse()
  }

  @Test
  fun testFinishingMissingSpanCausesError() {
    val stringBuilder = XmlStringBuilder(false)
    val spanAttempt = stringBuilder.append("hi").endSpan().getFlattenedXml()
    Truth.assertThat(stringBuilder.error).isNotEmpty()
    Truth.assertThat(spanAttempt.success).isFalse()
  }

  @Test
  fun testSpanCreated() {
    val stringBuilder = XmlStringBuilder(false)
    val spanAttempt = stringBuilder
      .append("Hello, ")
      .startSpan("b")
      .append("my name is Bob.")
      .endSpan()
      .getFlattenedXml()
    Truth.assertThat(stringBuilder.error).isEmpty()
    Truth.assertThat(spanAttempt.styleString.str).isEqualTo("Hello, my name is Bob.")
    val spans = spanAttempt.styleString.spans
    Truth.assertThat(spans).hasSize(1)
    Truth.assertThat(spans[0].name).isEqualTo("b")
    Truth.assertThat(spans[0].firstChar).isEqualTo(7)
    // spans are inclusive of their last index.
    Truth.assertThat(spans[0].lastChar).isEqualTo(21)

    stringBuilder.clear()
    val spanAttempt2 = stringBuilder
      .startSpan("i")
      .append("I\\'m running out of things ")
      .startSpan("b")
      .append("to type!")
      .endSpan()
      .append("...")
      .endSpan()
      .getFlattenedXml()
    Truth.assertThat(stringBuilder.error).isEmpty()
    Truth.assertThat(spanAttempt2.styleString.str)
      .isEqualTo("I'm running out of things to type!...")
    val spans2 = spanAttempt2.styleString.spans
    Truth.assertThat(spans2).hasSize(2)
    val firstSpan = spans2[0]
    val lastSpan = spans2[1]
    Truth.assertThat(firstSpan.name).isEqualTo("i")
    Truth.assertThat(firstSpan.firstChar).isEqualTo(0)
    Truth.assertThat(firstSpan.lastChar).isEqualTo(36)
    Truth.assertThat(lastSpan.name).isEqualTo("b")
    Truth.assertThat(lastSpan.firstChar).isEqualTo(26)
    Truth.assertThat(lastSpan.lastChar).isEqualTo(33)
  }

  @Test
  fun testUntranslatableSectionsCannotBeNested() {
    val stringBuilder = XmlStringBuilder(false)
    val untranslatableAttempt = stringBuilder
      .startUntranslatable()
      .append("This ")
      .startUntranslatable()
      .append("is ")
      .endUntranslatable()
      .endUntranslatable()
      .append("an ")
      .startUntranslatable()
      .append("attempt")
      .endUntranslatable()
      .getFlattenedXml()
    Truth.assertThat(stringBuilder.error).isNotEmpty()
    Truth.assertThat(untranslatableAttempt.success).isFalse()
  }

  @Test
  fun testUnfinishedUntranslatableSections() {
    val stringBuilder = XmlStringBuilder(false)
    val untranslatableAttempt = stringBuilder
      .startUntranslatable()
      .append("Well, how about this?")
      .getFlattenedXml()
    Truth.assertThat(stringBuilder.error).isNotEmpty()
    Truth.assertThat(untranslatableAttempt.success).isFalse()
  }

  @Test
  fun testUntranslatableSections() {
    val stringBuilder = XmlStringBuilder(false)
    val untranslatableAttempt = stringBuilder
      .append("This ")
      .startUntranslatable()
      .append("is ")
      .endUntranslatable()
      .append("an ")
      .startUntranslatable()
      .append("attempt.")
      .endUntranslatable()
      .getFlattenedXml()
    Truth.assertThat(stringBuilder.error).isEmpty()
    Truth.assertThat(untranslatableAttempt.styleString.str).isEqualTo("This is an attempt.")
    val untranslatables = untranslatableAttempt.untranslatableSections
    Truth.assertThat(untranslatables).hasSize(2)
    Truth.assertThat(untranslatables[0].startIndex).isEqualTo(5)
    // untranslatable sections are exclusive of their last index.
    Truth.assertThat(untranslatables[0].endIndex).isEqualTo(8)
    Truth.assertThat(untranslatables[1].startIndex).isEqualTo(11)
    Truth.assertThat(untranslatables[1].endIndex).isEqualTo(19)
  }

    @Test
    fun testHandleEscapeQuotationsBetweenTextChunks() {
        val stringBuilder = XmlStringBuilder(false)
        val flattendSample = stringBuilder
            .append("[[198745]] You\\")
            .append("'ve blocked. Tap to report and share more details.")
        Truth.assertThat(stringBuilder.error).isEmpty()
        Truth.assertThat(flattendSample.getFlattenedXml().styleString.str)
            .isEqualTo("[[198745]] You've blocked. Tap to report and share more details.")
    }
}
