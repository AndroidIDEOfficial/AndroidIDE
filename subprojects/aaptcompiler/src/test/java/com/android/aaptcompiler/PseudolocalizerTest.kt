package com.android.aaptcompiler

import com.google.common.truth.Truth
import org.junit.Test

internal val bidiWordStart = String(byteArrayOf(
  0xe2.toByte(),
  0x80.toByte(),
  0x8f.toByte(),
  0xe2.toByte(),
  0x80.toByte(),
  0xae.toByte()))
internal val bidiWordEnd = String(byteArrayOf(
  0xe2.toByte(),
  0x80.toByte(),
  0xac.toByte(),
  0xe2.toByte(),
  0x80.toByte(),
  0x8f.toByte()))

class PseudolocalizerTest() {
  private fun simpleTest(input: String, expected: String, method: Pseudolocalizer.Method) {
    val localizer = Pseudolocalizer(method)
    val result = localizer.start() + localizer.text(input) + localizer.end()
    Truth.assertThat(result).isEqualTo(expected)
  }

  private fun compoundTest(
    expected: String, method: Pseudolocalizer.Method, vararg inputs: String) {

    val localizer = Pseudolocalizer(method)
    val result = StringBuilder().append(localizer.start())
    for (input in inputs) {
      result.append(localizer.text(input))
    }
    result.append(localizer.end())
    Truth.assertThat(result.toString()).isEqualTo(expected)
  }

  @Test
  fun testNoPseudolocalization() {
    simpleTest("", "", Pseudolocalizer.Method.NONE)
    simpleTest("Hello, world", "Hello, world", Pseudolocalizer.Method.NONE)
    compoundTest("Hello, world", Pseudolocalizer.Method.NONE, "Hello,", " world")
  }

  @Test
  fun testPlainTextAccent() {
    simpleTest("", "[]", Pseudolocalizer.Method.ACCENT)
    simpleTest("Hello, world", "[Ĥéļļö, ŵöŕļð one two]", Pseudolocalizer.Method.ACCENT)
    simpleTest("Hello, %1d", "[Ĥéļļö, »%1d« one two]", Pseudolocalizer.Method.ACCENT)
    simpleTest("Battery %1d%%", "[βåţţéŕý »%1d«%% one two]", Pseudolocalizer.Method.ACCENT)
    simpleTest("^1 %", "[^1 % one]", Pseudolocalizer.Method.ACCENT)
    compoundTest("[]", Pseudolocalizer.Method.ACCENT, "", "", "")
    compoundTest("[Ĥéļļö, ŵöŕļð one two]", Pseudolocalizer.Method.ACCENT, "Hello,", " world", "")
  }

  @Test
  fun testPlainTextBidi() {
    simpleTest("", "", Pseudolocalizer.Method.BIDI)
    simpleTest("word", "${bidiWordStart}word$bidiWordEnd", Pseudolocalizer.Method.BIDI)
    simpleTest("  word  ", "  ${bidiWordStart}word$bidiWordEnd  ", Pseudolocalizer.Method.BIDI)
    simpleTest(
      "hello\n world\n",
      "${bidiWordStart}hello$bidiWordEnd\n ${bidiWordStart}world$bidiWordEnd\n",
      Pseudolocalizer.Method.BIDI)
    compoundTest(
      "${bidiWordStart}hello$bidiWordEnd\n ${bidiWordStart}world$bidiWordEnd\n",
      Pseudolocalizer.Method.BIDI,
      "hello", "\n ", "world\n")
  }

  @Test
  fun testSimpleICU() {
    // Single-fragment messages.
    simpleTest("{placeholder}", "[»{placeholder}«]", Pseudolocalizer.Method.ACCENT)
    simpleTest("{USER} is offline", "[»{USER}« îš öƒƒļîñé one two]", Pseudolocalizer.Method.ACCENT)
    simpleTest(
      "Copy from {path1} to {path2}",
      "[Çöþý ƒŕöḿ »{path1}« ţö »{path2}« one two three]",
      Pseudolocalizer.Method.ACCENT)
    simpleTest(
      "Today is {1,date} {1,time}",
      "[Ţöðåý îš »{1,date}« »{1,time}« one two]",
      Pseudolocalizer.Method.ACCENT)

    // Multi-fragment messages.
    compoundTest(
      "[»{USER}« îš öƒƒļîñé one two]",
      Pseudolocalizer.Method.ACCENT,
      "{USER}", " ", "is offline")
    compoundTest(
      "[Çöþý ƒŕöḿ »{path1}« ţö »{path2}« one two three]",
      Pseudolocalizer.Method.ACCENT,
      "Copy from ", "{path1}", " to", " {path2}")
  }

  @Test
  fun testICUBidi() {
    // Single-fragment messages.
    simpleTest(
      "{placeholder}", "$bidiWordStart{placeholder}$bidiWordEnd", Pseudolocalizer.Method.BIDI)
    simpleTest(
      "{COUNT, plural, one {one} other {other}}",
      "{COUNT, plural, one {${bidiWordStart}one$bidiWordEnd} " +
        "other {${bidiWordStart}other$bidiWordEnd}}",
      Pseudolocalizer.Method.BIDI)
  }

  @Test
  fun testEscaping() {
    // Single-fragment messages.
    simpleTest(
      "'{USER'} is offline", "['{ÛŠÉŔ'} îš öƒƒļîñé one two three]", Pseudolocalizer.Method.ACCENT)

    // Multi-fragment messages.
    compoundTest(
      "['{ÛŠÉŔ} ''îš öƒƒļîñé one two three]",
      Pseudolocalizer.Method.ACCENT,
      "'{USER}", " ", "''is offline")
  }

  @Test
  fun testPluralsAndSelects() {
    simpleTest(
      "{COUNT, plural, one {Delete a file} other {Delete {COUNT} files}}",
      "[{COUNT, plural, one {Ðéļéţé å ƒîļé one two} other {Ðéļéţé »{COUNT}« ƒîļéš one two}}]",
      Pseudolocalizer.Method.ACCENT)
    simpleTest(
      "Distance is {COUNT, plural, one {# mile} other {# miles}}",
      "[Ðîšţåñçé îš {COUNT, plural, one {# ḿîļé one two} other {# ḿîļéš one two}}]",
      Pseudolocalizer.Method.ACCENT)

    simpleTest(
      "{1, select, female {{1} added you} male {{1} added you} other {{1} added you}}",
      "[{1, select, female {»{1}« åððéð ýöû one two} male {»{1}« åððéð ýöû one two} " +
        "other {»{1}« åððéð ýöû one two}}]",
      Pseudolocalizer.Method.ACCENT)

    compoundTest(
      "[{COUNT, plural, one {Ðéļéţé å ƒîļé one two} other {Ðéļéţé »{COUNT}« ƒîļéš one two}}]",
      Pseudolocalizer.Method.ACCENT,
      "{COUNT, plural, one {Delete a file} ", "other {Delete ", "{COUNT}", " files}}")
  }

  @Test
  fun testNestedICU() {
    simpleTest("{person, select, female {{num_circles, plural,=0{{person} didn't add you " +
      "to any of her circles.}=1{{person} added you to one of her circles.}other{{person} added " +
      "you to her # circles.}}}male {{num_circles, plural,=0{{person} didn't add you to any of " +
      "his circles.}=1{{person} added you to one of his circles.}other{{person} added you to his " +
      "# circles.}}}other {{num_circles, plural,=0{{person} didn't add you to any of their " +
      "circles.}=1{{person} added you to one of their circles.}other{{person} added you to their " +
      "# circles.}}}}",
      "[{person, select, female {{num_circles, plural,=0{»{person}« ðîðñ'ţ åðð ýöû ţö åñý öƒ " +
        "ĥéŕ çîŕçļéš. one two three four five}=1{»{person}« åððéð ýöû ţö öñé öƒ ĥéŕ çîŕçļéš. one " +
        "two three four}other{»{person}« åððéð ýöû ţö ĥéŕ # çîŕçļéš. one two three four}}}male " +
        "{{num_circles, plural,=0{»{person}« ðîðñ'ţ åðð ýöû ţö åñý öƒ ĥîš çîŕçļéš. one two three " +
        "four five}=1{»{person}« åððéð ýöû ţö öñé öƒ ĥîš çîŕçļéš. one two three four}other" +
        "{»{person}« åððéð ýöû ţö ĥîš # çîŕçļéš. one two three four}}}other {{num_circles, " +
        "plural,=0{»{person}« ðîðñ'ţ åðð ýöû ţö åñý öƒ ţĥéîŕ çîŕçļéš. one two three four five}" +
        "=1{»{person}« åððéð ýöû ţö öñé öƒ ţĥéîŕ çîŕçļéš. one two three four}other{»{person}« " +
        "åððéð ýöû ţö ţĥéîŕ # çîŕçļéš. one two three four}}}}]",
      Pseudolocalizer.Method.ACCENT)
  }

  @Test
  fun testRedefineMidLocalization() {
    val localizer = Pseudolocalizer(Pseudolocalizer.Method.ACCENT)
    val result = StringBuilder(localizer.text("Hello, "))
    localizer.setMethod(Pseudolocalizer.Method.NONE)
    result.append(localizer.text("world!"))
    Truth.assertThat(result.toString()).isEqualTo("Ĥéļļö, world!")
  }

  @Test
  fun testNonHtmlTags() {
    simpleTest(
      "Started &lt; 1 minute ago",
      "[Šţåŕţéð &ļţ; 1 ḿîñûţé åĝö one two three]",
      Pseudolocalizer.Method.ACCENT)

    simpleTest(
      "Started < 1 minute ago",
      "[Šţåŕţéð < 1 ḿîñûţé åĝö one two three]",
      Pseudolocalizer.Method.ACCENT)

    simpleTest(
      "Started &gt; 1 minute ago",
      "[Šţåŕţéð &gt; 1 ḿîñûţé åĝö one two three]",
      Pseudolocalizer.Method.ACCENT)

    simpleTest(
      "Started > 1 minute ago",
      "[Šţåŕţéð > 1 ḿîñûţé åĝö one two three]",
      Pseudolocalizer.Method.ACCENT)

    simpleTest(
      "Started &lt; 1 minute ago",
      "Started &lt; 1 minute ago",
      Pseudolocalizer.Method.NONE)

    simpleTest(
      "Started < 1 minute ago",
      "Started < 1 minute ago",
      Pseudolocalizer.Method.NONE)

    simpleTest(
      "hello\n &lt;\n world\n",
      "${bidiWordStart}hello$bidiWordEnd\n " +
              "${bidiWordStart}&lt;$bidiWordEnd\n " +
              "${bidiWordStart}world$bidiWordEnd\n",
      Pseudolocalizer.Method.BIDI)

    simpleTest(
      "hello\n <\n world\n",
      "${bidiWordStart}hello$bidiWordEnd\n " +
              "${bidiWordStart}<$bidiWordEnd\n " +
              "${bidiWordStart}world$bidiWordEnd\n",
      Pseudolocalizer.Method.BIDI)
  }

  @Test
  fun testHtmlTags() {
    // Words should be translated, but html tags and their contents should not.
    simpleTest(
      "hello <i>happy</i> world",
      "[ĥéļļö <i>ĥåþþý</i> ŵöŕļð one two three]",
      Pseudolocalizer.Method.ACCENT)

    simpleTest(
      "hello &lt;i&gt;happy&lt;/i&gt; world",
      "[ĥéļļö &lt;;i&gt;ĥåþþý&lt;;/i&gt; ŵöŕļð one two three]",
      Pseudolocalizer.Method.ACCENT)

    // The tag (i.e. "a href="idk"") should not be translated, but the inner content (i.e."world")
    // should be.
    simpleTest(
      "hello <a href=\"idk\"/>world</a>",
      "[ĥéļļö <a href=\"idk\"/>ŵöŕļð</a> one two]",
      Pseudolocalizer.Method.ACCENT)

    // Even if from HTML's point of view the tags are placed incorrectly, the pseudolocalizer should
    // be able to handle them: not translate the tags, and only translate the contents outside and
    // between them.
    simpleTest(
      "Incorrectly <b> nested <i> tags </b> should </i> still <a> work",
      "[Îñçöŕŕéçţļý <b> ñéšţéð <i> ţåĝš </b> šĥöûļð </i> šţîļļ <a> ŵöŕķ one two three four five six]",
      Pseudolocalizer.Method.ACCENT)
  }


}