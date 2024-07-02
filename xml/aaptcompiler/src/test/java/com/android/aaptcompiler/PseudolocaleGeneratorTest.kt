package com.android.aaptcompiler

import com.google.common.truth.Truth
import org.junit.Test

class PseudolocaleGeneratorTest {

  @Test
  fun testPseudolocalizeBasicString() {
    val pool = StringPool()
    val original =
      BasicString(pool.makeRef("Hello world!"), listOf(UntranslatableSection(1, 4)))

    val newString1 = pseudolocalizeBasicString(original, Pseudolocalizer.Method.NONE, pool)

    Truth.assertThat(newString1.ref.value()).isEqualTo(original.ref.value())

    val newString2 = pseudolocalizeBasicString(original, Pseudolocalizer.Method.ACCENT, pool)

    Truth.assertThat(newString2.ref.value()).isEqualTo("[Ĥellö ŵöŕļð¡ one two]")

    val newString3 = pseudolocalizeBasicString(original, Pseudolocalizer.Method.BIDI, pool)

    Truth.assertThat(newString3.ref.value())
      .isEqualTo("${bidiWordStart}H${bidiWordEnd}ell${bidiWordStart}o$bidiWordEnd " +
        "${bidiWordStart}world!$bidiWordEnd")
  }

  @Test
  fun testPseudolocalizeStyledString() {
    val pool = StringPool()
    val spans = listOf(
      Span("i", 1, 10),
      Span("b", 2, 3),
      Span("b", 6, 7)
    )
    val original = StyledString(pool.makeRef(StyleString("Hello world!", spans)), listOf())

    val newString1 = pseudolocalizeStyledString(original, Pseudolocalizer.Method.NONE, pool)

    Truth.assertThat(newString1.ref.value()).isEqualTo(original.ref.value())
    Truth.assertThat(newString1.spans().size).isEqualTo(original.spans().size)

    val spans1 = newString1.spans()
    Truth.assertThat(spans1[0].name.value()).isEqualTo("i")
    Truth.assertThat(spans1[0].firstChar).isEqualTo("H".length)
    Truth.assertThat(spans1[0].lastChar).isEqualTo("Hello worl".length)

    Truth.assertThat(spans1[1].name.value()).isEqualTo("b")
    Truth.assertThat(spans1[1].firstChar).isEqualTo("He".length)
    Truth.assertThat(spans1[1].lastChar).isEqualTo("Hel".length)

    Truth.assertThat(spans1[2].name.value()).isEqualTo("b")
    Truth.assertThat(spans1[2].firstChar).isEqualTo("Hello ".length)
    Truth.assertThat(spans[2].lastChar).isEqualTo("Hello w".length)

    val newString2 = pseudolocalizeStyledString(original, Pseudolocalizer.Method.ACCENT, pool)

    Truth.assertThat(newString2.ref.value()).isEqualTo("[Ĥéļļö ŵöŕļð¡ one two]")
    Truth.assertThat(newString2.spans().size).isEqualTo(original.spans().size)

    val spans2 = newString2.spans()
    Truth.assertThat(spans2[0].name.value()).isEqualTo("i")
    Truth.assertThat(spans2[0].firstChar).isEqualTo("[Ĥ".length)
    Truth.assertThat(spans2[0].lastChar).isEqualTo("[Ĥéļļö ŵöŕļ".length)

    Truth.assertThat(spans2[1].name.value()).isEqualTo("b")
    Truth.assertThat(spans2[1].firstChar).isEqualTo("[Ĥé".length)
    Truth.assertThat(spans2[1].lastChar).isEqualTo("[Ĥéļ".length)

    Truth.assertThat(spans2[2].name.value()).isEqualTo("b")
    Truth.assertThat(spans2[2].firstChar).isEqualTo("[Ĥéļļö ".length)
    Truth.assertThat(spans2[2].lastChar).isEqualTo("[Ĥéļļö ŵ".length)
  }

  @Test
  fun testPseudolocalizeAdjacentNestedTags() {
    val pool = StringPool()
    val originalSpans = listOf(Span("b", 0, 3), Span("i", 0, 3))

    val original = StyledString(pool.makeRef(StyleString("bold", originalSpans)), listOf())

    val newString = pseudolocalizeStyledString(original, Pseudolocalizer.Method.ACCENT, pool)

    Truth.assertThat(newString.ref.value()).isEqualTo("[ɓöļð one]")
    Truth.assertThat(newString.spans().size).isEqualTo(original.spans().size)

    val spans = newString.spans()

    Truth.assertThat(spans[0].name.value()).isEqualTo("b")
    Truth.assertThat(spans[0].firstChar).isEqualTo("[".length)
    Truth.assertThat(spans[0].lastChar).isEqualTo("[ɓöļ".length)

    Truth.assertThat(spans[1].name.value()).isEqualTo("i")
    Truth.assertThat(spans[1].firstChar).isEqualTo("[".length)
    Truth.assertThat(spans[1].lastChar).isEqualTo("[ɓöļ".length)
  }

  @Test
  fun testPseudolocalizeUnsorted() {
    val pool = StringPool()
    val originalSpans = listOf(Span("i", 2, 3), Span("b", 0, 1))
    val original = StyledString(pool.makeRef(StyleString("bold", originalSpans)), listOf())

    val newString = pseudolocalizeStyledString(original, Pseudolocalizer.Method.ACCENT, pool)

    Truth.assertThat(newString.ref.value()).isEqualTo("[ɓöļð one]")
    Truth.assertThat(newString.spans().size).isEqualTo(original.spans().size)

    val spans = newString.spans()

    Truth.assertThat(spans[0].name.value()).isEqualTo("b")
    Truth.assertThat(spans[0].firstChar).isEqualTo("[".length)
    Truth.assertThat(spans[0].lastChar).isEqualTo("[ɓ".length)

    Truth.assertThat(spans[1].name.value()).isEqualTo("i")
    Truth.assertThat(spans[1].firstChar).isEqualTo("[ɓö".length)
    Truth.assertThat(spans[1].lastChar).isEqualTo("[ɓöļ".length)
  }

  @Test
  fun testPseudolocalizeNestedAndAdjacentTags() {
    val pool = StringPool()
    val originalSpans = listOf(
      Span("b", 16, 19),
      Span("em", 29, 47),
      Span("i", 38, 40),
      Span("b", 44, 47)
    )
    val originalText = "This sentence is not what you think it is at all."
    val original = StyledString(pool.makeRef(StyleString(originalText, originalSpans)), listOf())

    val newString = pseudolocalizeStyledString(original, Pseudolocalizer.Method.ACCENT, pool)

    Truth.assertThat(newString.ref.value())
      .isEqualTo("[Ţĥîš šéñţéñçé îš ñöţ ŵĥåţ ýöû ţĥîñķ îţ îš åţ åļļ. one two three four five six]")
    Truth.assertThat(newString.spans().size).isEqualTo(original.spans().size)

    val spans = newString.spans()
    Truth.assertThat(spans[0].name.value()).isEqualTo("b")
    Truth.assertThat(spans[0].firstChar).isEqualTo("[Ţĥîš šéñţéñçé îš".length)
    Truth.assertThat(spans[0].lastChar).isEqualTo("[Ţĥîš šéñţéñçé îš ñö".length)

    Truth.assertThat(spans[1].name.value()).isEqualTo("em")
    Truth.assertThat(spans[1].firstChar).isEqualTo("[Ţĥîš šéñţéñçé îš ñöţ ŵĥåţ ýöû".length)
    Truth.assertThat(spans[1].lastChar)
      .isEqualTo("[Ţĥîš šéñţéñçé îš ñöţ ŵĥåţ ýöû ţĥîñķ îţ îš åţ åļ".length)

    Truth.assertThat(spans[2].name.value()).isEqualTo("i")
    Truth.assertThat(spans[2].firstChar).isEqualTo("[Ţĥîš šéñţéñçé îš ñöţ ŵĥåţ ýöû ţĥîñķ îţ".length)
    Truth.assertThat(spans[2].lastChar)
      .isEqualTo("[Ţĥîš šéñţéñçé îš ñöţ ŵĥåţ ýöû ţĥîñķ îţ î".length)

    Truth.assertThat(spans[3].name.value()).isEqualTo("b")
    Truth.assertThat(spans[3].firstChar)
      .isEqualTo("[Ţĥîš šéñţéñçé îš ñöţ ŵĥåţ ýöû ţĥîñķ îţ îš åţ".length)
    Truth.assertThat(spans[3].lastChar)
      .isEqualTo("[Ţĥîš šéñţéñçé îš ñöţ ŵĥåţ ýöû ţĥîñķ îţ îš åţ åļ".length)
  }

  @Test
  fun testPseudolocalizePartsOfStyledString() {
    val pool = StringPool()
    val originalSpans = listOf(Span("em", 4, 14), Span("i", 18, 33))
    val untranslatables = listOf(UntranslatableSection(11, 15))
    val originalText = "This should NOT be pseudolocalized."
    val original =
      StyledString(pool.makeRef(StyleString(originalText, originalSpans)), untranslatables)

    val newString = pseudolocalizeStyledString(original, Pseudolocalizer.Method.ACCENT, pool)

    Truth.assertThat(newString.ref.value())
      .isEqualTo("[Ţĥîš šĥöûļð NOT ɓé þšéûðöļöçåļîžéð. one two three four]")
    Truth.assertThat(newString.spans().size).isEqualTo(original.spans().size)

    val spans = newString.spans()
    Truth.assertThat(spans[0].name.value()).isEqualTo("em")
    Truth.assertThat(spans[0].firstChar).isEqualTo("[Ţĥîš".length)
    Truth.assertThat(spans[0].lastChar).isEqualTo("[Ţĥîš šĥöûļð NO".length)

    Truth.assertThat(spans[1].name.value()).isEqualTo("i")
    Truth.assertThat(spans[1].firstChar).isEqualTo("[Ţĥîš šĥöûļð NOT ɓé".length)
    Truth.assertThat(spans[1].lastChar).isEqualTo("[Ţĥîš šĥöûļð NOT ɓé þšéûðöļöçåļîžé".length)
  }

  @Test
  fun testPseudolocalizeOnlyDefaultConfigs() {
    val table = ResourceTable()

    val string1 = BasicString(table.stringPool.makeRef("one"))
    table.addResource(
      parseResourceName("android:string/one")!!.resourceName,
      ConfigDescription(),
      "",
      string1)

    val string2 = BasicString(table.stringPool.makeRef("two"))
    table.addResource(
      parseResourceName("android:string/two")!!.resourceName,
      parse("en"),
      "",
      string2)

    val string3Default = BasicString(table.stringPool.makeRef("three"))
    table.addResource(
      parseResourceName("android:string/three")!!.resourceName,
      ConfigDescription(),
      "",
      string3Default)

    val string3Override = BasicString(table.stringPool.makeRef("three next"))
    table.addResource(
      parseResourceName("android:string/three")!!.resourceName,
      parse("en-rXA"),
      "",
      string3Override)

    val string4 = BasicString(table.stringPool.makeRef("four"))
    table.addResource(
      parseResourceName("android:string/four")!!.resourceName,
      ConfigDescription(),
      "",
      string4)

    val resource4 = getValue(table, "android:string/four")
    // sanity check
    Truth.assertThat(resource4).isNotNull()
    resource4!!.translatable = false

    val generator = PseudolocaleGenerator()
    generator.consume(table)

    // Normal pseudolocalization should take place.
    Truth.assertThat(getValue(table, "android:string/one", parse("en-rXA"))).isNotNull()
    Truth.assertThat(getValue(table, "android:string/one", parse("ar-rXB"))).isNotNull()

    // No default config for android:string/two, so no pseudlocales should exist.
    Truth.assertThat(getValue(table, "android:string/two", parse("en-rXA"))).isNull()
    Truth.assertThat(getValue(table, "android:string/two", parse("ar-rXB"))).isNull()

    // Check that we didn't override manual pseudolocalization.
    val resource3Override = getValue(table, "android:string/three", parse("en-rXA"))

    Truth.assertThat(resource3Override).isNotNull()
    Truth.assertThat((resource3Override as BasicString).ref.value()).isEqualTo("three next")

    // Check that four's translatable marker was honored.
    Truth.assertThat(getValue(table, "android:string/four", parse("en-rXA"))).isNull()
    Truth.assertThat(getValue(table, "android:string/four", parse("ar-rXB"))).isNull()
  }

  @Test
  fun testPluralsArePseudolocalized() {
    val table = ResourceTable()

    val plural = Plural()
    plural.setValue(Plural.Type.ZERO, BasicString(table.stringPool.makeRef("zero")))
    plural.setValue(Plural.Type.ONE, BasicString(table.stringPool.makeRef("one")))

    table.addResource(
      parseResourceName("com.pkg:plurals/foo")!!.resourceName, ConfigDescription(), "", plural)

    val expected = Plural()
    expected.setValue(Plural.Type.ZERO, BasicString(table.stringPool.makeRef("[žéŕö one]")))
    expected.setValue(Plural.Type.ONE, BasicString(table.stringPool.makeRef("[öñé one]")))

    val generator = PseudolocaleGenerator()
    generator.consume(table)

    val actual = getValue(table, "com.pkg:plurals/foo", parse("en-rXA"))

    Truth.assertThat(actual).isEqualTo(expected)
  }

  @Test
  fun testUntranslatableSections() {
    val table = ResourceTable()

    val originalText = "Hello world!"
    val originalSpans = listOf(Span("i", 1, 10), Span("b", 2, 3), Span("b", 6, 7))
    val originalStyle = StyledString(
      table.stringPool.makeRef(StyleString(originalText, originalSpans)),
      listOf(UntranslatableSection(6, 8), UntranslatableSection(8, 11)))

    val originalString = BasicString(
      table.stringPool.makeRef(originalText),
      listOf(UntranslatableSection(6, 11)))

    table.addResource(
      parseResourceName("android:string/foo")!!.resourceName,
      ConfigDescription(),
      "",
      originalStyle)
    table.addResource(
      parseResourceName("android:string/bar")!!.resourceName,
      ConfigDescription(),
      "",
      originalString)

    val generator = PseudolocaleGenerator()
    generator.consume(table)

    val newStyle = getValue(table, "android:string/foo", parse("en-rXA"))
    Truth.assertThat(newStyle).isNotNull()

    // "world" should be untranslated.
    Truth.assertThat((newStyle as StyledString).ref.value()).contains("world")

    val newString = getValue(table, "android:string/bar", parse("en-rXA"))
    Truth.assertThat(newString).isNotNull()

    // "world" should be untranslated.
    Truth.assertThat((newString as BasicString).ref.value()).contains("world")
  }

    // Regression test for b/188963894.
    @Test
    fun canPseudolocalizeNestedTagWithSpace() {
        val pool = StringPool()
        val originalSpans = listOf(
            Span("b", 0, 4),
                    Span("i", 0, 10)
        )
        val originalText = "hello world"
        val original = StyledString(pool.makeRef(StyleString(originalText, originalSpans)), listOf())

        val newString = pseudolocalizeStyledString(original, Pseudolocalizer.Method.ACCENT, pool)
        val spans = newString.spans()
        Truth.assertThat(spans[0].name.value()).isEqualTo("i")
        Truth.assertThat(spans[0].firstChar).isEqualTo(1)
        Truth.assertThat(spans[0].lastChar).isEqualTo("ĥéļļö ŵöŕļð".length)

        Truth.assertThat(spans[1].name.value()).isEqualTo("b")
        Truth.assertThat(spans[1].firstChar).isEqualTo(1)
        Truth.assertThat(spans[1].lastChar).isEqualTo("ĥéļļö".length)
    }
}
