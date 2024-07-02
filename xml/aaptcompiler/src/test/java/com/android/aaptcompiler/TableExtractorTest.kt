package com.android.aaptcompiler

import com.android.aaptcompiler.android.ResValue
import com.android.aapt.Resources
import com.android.aaptcompiler.AaptResourceType.RAW
import com.itsaky.androidide.layoutlib.resources.ResourceVisibility
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

const val XML_PREAMBLE = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"

class TableExtractorTest {

  lateinit var table: ResourceTable

  @Before
  fun setup() {
    table = ResourceTable()
  }

  private fun testParse(
      input: String,
      config: ConfigDescription = ConfigDescription(),
      mockLogger: BlameLoggerTest.MockLogger = BlameLoggerTest.MockLogger()
  ): Boolean {
    val parseInput =
      """$XML_PREAMBLE
      <resources>
      $input
      </resources>
    """.trimIndent()

    val blameLogger = getMockBlameLogger(mockLogger)

    val extractor =
      TableExtractor(table, Source("test.xml"), config, TableExtractorOptions(), blameLogger)

      try {
          extractor.extract(parseInput.byteInputStream())
      } catch (e: Exception) {
          return false
      }
      return true
  }

  private fun getValue(
    resName: String,
    config: ConfigDescription = ConfigDescription(),
    productName: String = "") =
    getValue(table, resName, config, productName)

  @Test
  fun testParseEmptyInput() {
    val input = "$XML_PREAMBLE\n"

    val mockLogger = BlameLoggerTest.MockLogger()

    val extractor =
      TableExtractor(
          table,
          Source("test.xml"),
          ConfigDescription(),
          TableExtractorOptions(),
          getMockBlameLogger(mockLogger))

    extractor.extract(input.byteInputStream())
    assertThat(mockLogger.errors).isEmpty()
    assertThat(mockLogger.warnings).isEmpty()
  }

  @Test
  fun failToParseWithNoRoot() {
    val input = """$XML_PREAMBLE<attr name="foo"/>"""
    val mockLogger = BlameLoggerTest.MockLogger()
    val blameLogger = getMockBlameLogger(mockLogger)
    val extractor =
      TableExtractor(
          table, Source("test.xml"), ConfigDescription(), TableExtractorOptions(), blameLogger)

    val extractorException = assertThrows(Exception::class.java) {
      extractor.extract(input.byteInputStream())
    }
    assertThat(extractorException).isNotNull()
    assertThat(extractorException.message)
      .contains("Root xml element of resource table not labeled 'resources' (test.xml.rewritten:0:1:).")
  }

  @Test
  fun failToParseDuplicates() {
    val input = """
    <attr name="foo">
        <enum name="bar" value="0"/>
        <enum name="bar" value="1"/>
    </attr>
    """.trimIndent()

      val mockLogger = BlameLoggerTest.MockLogger()
      assertThat(testParse(input, mockLogger = mockLogger)).isFalse()
      assertThat(mockLogger.errors).hasSize(1)
      val errorMsg = mockLogger.errors.single().first

      assertThat(errorMsg).contains(
          "test.xml.rewritten:7:1: Duplicate symbol 'id/bar' defined here:")
      assertThat(errorMsg).contains(
          "test.xml.rewritten:7:1:  and here:")
      assertThat(errorMsg)
          .contains("test.xml.rewritten:6:1")
  }

  @Test
  fun testParseBoolean() {
    val input = """
      <bool name="a">true</bool>
      <bool name="b">false</bool>""".trimIndent()

    assertThat(testParse(input)).isTrue()

    val boolA = getValue("bool/a") as? BinaryPrimitive
    assertThat(boolA).isNotNull()
    assertThat(boolA!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_BOOLEAN)
    assertThat(boolA.resValue.data).isEqualTo(-1)

    val boolB = getValue("bool/b") as? BinaryPrimitive
    assertThat(boolB).isNotNull()
    assertThat(boolB!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_BOOLEAN)
    assertThat(boolB.resValue.data).isEqualTo(0)
  }

  @Test
  fun testParseColor() {
    val input = """
      <color name="a">#7fa87f</color>
      <color name="b">@android:color/black</color>
    """.trimIndent()

    assertThat(testParse(input)).isTrue()

    val colorA = getValue("color/a") as? BinaryPrimitive
    assertThat(colorA).isNotNull()
    assertThat(colorA!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_COLOR_RGB8)
    assertThat(colorA.resValue.data).isEqualTo(0xff7fa87f.toInt())

    val colorB = getValue("color/b") as? Reference
    assertThat(colorB).isNotNull()
    assertThat(colorB!!.name)
      .isEqualTo(parseResourceName("android:color/black")!!.resourceName)
  }

  @Test
  fun parseDimen() {
    val input =
      """
        <dimen name="a">16dp</dimen>
        <dimen name="b">@dimen/abc_control_padding_material</dimen>
        <item name="c" type="dimen">10%</item>
      """.trimIndent()

    assertThat(testParse(input)).isTrue()

    val dimenA = getValue("dimen/a") as? BinaryPrimitive
    assertThat(dimenA).isNotNull()
    assertThat(dimenA!!.resValue.dataType).isEqualTo(ResValue.DataType.DIMENSION)
    assertThat(dimenA.resValue.data).isEqualTo(0x1001)

    val dimenB = getValue("dimen/b") as? Reference
    assertThat(dimenB).isNotNull()
    assertThat(dimenB!!.name)
      .isEqualTo(parseResourceName("dimen/abc_control_padding_material")!!.resourceName)

    val dimenC = getValue("dimen/c") as? BinaryPrimitive
    assertThat(dimenC).isNotNull()
    assertThat(dimenC!!.resValue.dataType).isEqualTo(ResValue.DataType.FRACTION)
    // 0           00011001100110011001101     0011                        0000
    // ^ positive  ^ 10% or .1 with rounding.  ^ Radix 0p23 (all fraction) ^ non-parent fraction
    // i.e. 0x0ccccd30
    assertThat(dimenC.resValue.data).isEqualTo(0x0ccccd30)
  }

  @Test
  fun testParseDrawable() {
    assertThat(testParse("""<drawable name="foo">#0cffffff</drawable>""")).isTrue()

    val drawable = getValue("drawable/foo") as? BinaryPrimitive
    assertThat(drawable).isNotNull()
    assertThat(drawable!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_COLOR_ARGB8)
    assertThat(drawable.resValue.data).isEqualTo(0xcffffff)
  }

  @Test
  fun testParseInteger() {
    val input = """
      <integer name="a">10</integer>
      <integer name="b">0x10</integer>
      <item name="c" type="integer">0xA</item>
    """.trimIndent()

    assertThat(testParse(input)).isTrue()

    val integerA = getValue("integer/a") as? BinaryPrimitive
    assertThat(integerA).isNotNull()
    assertThat(integerA!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_DEC)
    assertThat(integerA.resValue.data).isEqualTo(10)

    val integerB = getValue("integer/b") as? BinaryPrimitive
    assertThat(integerB).isNotNull()
    assertThat(integerB!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_HEX)
    assertThat(integerB.resValue.data).isEqualTo(16)

    val integerC = getValue("integer/c") as? BinaryPrimitive
    assertThat(integerC).isNotNull()
    assertThat(integerC!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_HEX)
    assertThat(integerC.resValue.data).isEqualTo(10)
  }

  @Test
  fun testParseId() {
    assertThat(testParse("""<item name="foo" type="id"/>""")).isTrue()
    assertThat(getValue("id/foo") as? Id).isNotNull()
  }

  @Test
  fun testParsingNonItemId() {
      assertThat(testParse("""<id name="foo"/>""")).isTrue()
      assertThat(getValue("id/foo") as? Id).isNotNull()
  }

  @Test
  fun testParseQuotedString() {

    assertThat(testParse("""<string name="foo">   "  hey there " </string>""")).isTrue()
    var str = getValue("string/foo") as BasicString
    assertThat(str.toString()).isEqualTo("  hey there ")
    assertThat(str.untranslatables).isEmpty()

    assertThat(testParse("""<string name="bar">Isn\'t it cool?</string>""")).isTrue()
    str = getValue("string/bar") as BasicString
    assertThat(str.toString()).isEqualTo("Isn't it cool?")

    assertThat(testParse("""<string name="baz">"Isn't it cool?"</string>""")).isTrue()
    str = getValue("string/baz") as BasicString
    assertThat(str.toString()).isEqualTo("Isn't it cool?")
  }

  @Test
  fun testParseCDataString() {
      assertThat(testParse("""<string name="foo"><![CDATA[basic]]></string>""")).isTrue()
      var str = getValue("string/foo") as BasicString
      assertThat(str.toString()).isEqualTo("basic")
      assertThat(str.untranslatables).isEmpty()

      assertThat(testParse("""
          <string name="bar"><![CDATA[<span>Try span</span>]]></string>""")).isTrue()
      str = getValue("string/bar") as BasicString
      assertThat(str.toString()).isEqualTo("<span>Try span</span>")

      // Testing multiple CDATA spans and whitespace behavior across spans.
      assertThat(testParse("""
          <string name="baz"><![CDATA[
            <t>trial</t>]]>multiple <![CDATA[ <t>trials</t>]]></string>
      """.trimIndent())).isTrue()
      str = getValue("string/baz") as BasicString
      assertThat(str.toString()).isEqualTo("<t>trial</t>multiple <t>trials</t>")

      // Quotes are handled as expected.
      assertThat(testParse("""
          <string name="bat">"  <![CDATA[Let's go!]]>  "</string>
      """.trimIndent()))
      str = getValue("string/bat") as BasicString
      assertThat(str.toString()).isEqualTo("  Let's go!  ")

      // Quotes are handle the same inside or out of CDATA.
      assertThat(testParse("""
          <string name="bat2"><![CDATA["  Let's go!  "]]></string>
      """.trimIndent())).isTrue()
      str = getValue("string/bat2") as BasicString
      assertThat(str.toString()).isEqualTo("  Let's go!  ")

      // Or across the CDATA border.
      assertThat(testParse("""
          <string name="bat3">" <![CDATA[ Let's go! ]]> "</string>
      """.trimIndent())).isTrue()
      str = getValue("string/bat3") as BasicString
      assertThat(str.toString()).isEqualTo("  Let's go!  ")

      // Invalid xml in CDATA is okay.
      assertThat(testParse("""
          <string name="bax"><![CDATA[<invalid>xml]]></string>
      """.trimIndent())).isTrue()
      str = getValue("string/bax") as BasicString
      assertThat(str.toString()).isEqualTo("<invalid>xml")

      assertThat(testParse("""
          <string name="bav"><![CDATA[\"  QUOTE TIME  \"]]></string>
      """.trimIndent())).isTrue()
      str = getValue("string/bav") as BasicString
      assertThat(str.toString()).isEqualTo("\" QUOTE TIME \"")
  }

  @Test
  fun testParseEscapedString() {
    assertThat(testParse("""<string name="foo">\?123</string>""")).isTrue()
    var str = getValue("string/foo") as BasicString
    assertThat(str.toString()).isEqualTo("?123")
    assertThat(str.untranslatables).isEmpty()

    assertThat(testParse("""<string name="bar">This isn\'t a bad string</string>"""))
    str = getValue("string/bar") as BasicString
    assertThat(str.toString()).isEqualTo("This isn't a bad string")
  }

  @Test
  fun testParseFormattedString() {
    val mockLogger = BlameLoggerTest.MockLogger()
    assertThat(
        testParse("""<string name="foo">%1${"$"}d %2${"$"}s</string>""", mockLogger = mockLogger))
        .isTrue()
    assertThat(mockLogger.errors).isEmpty()
    assertThat(mockLogger.warnings).isEmpty()

    assertThat(
        testParse("""<string name="foo">%d %s</string>""", mockLogger = mockLogger)).isFalse()
    assertThat(mockLogger.errors).hasSize(1)
    assertThat(mockLogger.errors.single().first).contains(
        "test.xml.rewritten:5:1: Multiple substitutions specified in non-positional format of " +
                "string resource string/foo. ")
    assertThat(mockLogger.warnings).isEmpty()

  }

  @Test
  fun testParseUnformattedString() {
      assertThat(testParse("""<string name="tested">%10</string>""")).isTrue()
  }

  @Test
  fun testNonAsciiString() {
      assertThat(testParse("""<string name="theme_light_default">डिफ़ॉल्ट</string>""")).isTrue()
  }

  @Test
  fun testParseStyledString() {
    val input =
      "<string name=\"foo\">This is my aunt\u2019s <b>fickle <small>string</small></b></string>"
    assertThat(testParse(input)).isTrue()

    val str = getValue("string/foo") as StyledString

    assertThat(str.toString()).isEqualTo("This is my aunt\u2019s fickle string")
    assertThat(str.spans()).hasSize(2)
    assertThat(str.untranslatableSections).isEmpty()

    val span0 = str.spans()[0]
    assertThat(span0.name.value()).isEqualTo("b")
    assertThat(span0.firstChar).isEqualTo(18)
    assertThat(span0.lastChar).isEqualTo(30)

    val span1 = str.spans()[1]
    assertThat(span1.name.value()).isEqualTo("small")
    assertThat(span1.firstChar).isEqualTo(25)
    assertThat(span1.lastChar).isEqualTo(30)
  }

  @Test
  fun testParseStringWithWhitespace() {
    assertThat(testParse("""<string name="foo">  This is what   I think  </string>"""))

    var str = getValue("string/foo") as BasicString
    assertThat(str.toString()).isEqualTo("This is what I think")
    assertThat(str.untranslatables).isEmpty()

    assertThat(
      testParse("""<string name="foo2">"  This is what   I think  "</string>"""))

    str = getValue("string/foo2") as BasicString
    assertThat(str.toString()).isEqualTo("  This is what   I think  ")
    assertThat(str.untranslatables).isEmpty()

    assertThat(
      testParse("""<string name="foo3">"\nHello world\n\n"</string>"""))

    str = getValue("string/foo3") as BasicString
    assertThat(str.toString()).isEqualTo("\nHello world\n\n")
    assertThat(str.untranslatables).isEmpty()
  }

  @Test
  fun testParseStringWithExplicitNewLines() {
    assertThat(testParse("""<string name="foo">\n\nTest\n\n</string>"""))

    var str = getValue("string/foo") as BasicString
    assertThat(str.toString()).isEqualTo("\n\nTest\n\n")
    assertThat(str.untranslatables).isEmpty()

    assertThat(testParse("""<string name="foo2">      \n      </string>"""))

    str = getValue("string/foo2") as BasicString
    assertThat(str.toString()).isEqualTo("\n")
    assertThat(str.untranslatables).isEmpty()

     assertThat(testParse("""<string name="foo3">\n\n\n\n\n\n\n\n\n</string>"""))

    str = getValue("string/foo3") as BasicString
    assertThat(str.toString()).isEqualTo("\n\n\n\n\n\n\n\n\n")
    assertThat(str.untranslatables).isEmpty()
  }

  @Test
  fun testParseStringWithBasicLineBreaking() {
    assertThat(testParse("""
    <string name="foo">

          Hello
          world


    </string>""".trimMargin()))

    val str = getValue("string/foo") as BasicString
    assertThat(str.toString()).isEqualTo("Hello world")
    assertThat(str.untranslatables).isEmpty()
  }

  @Test
  fun testStringOfWhitespaces() {
    assertThat(testParse("""<string name="foo">      </string>"""))

    var str = getValue("string/foo") as BasicString
    assertThat(str.toString()).isEmpty()
    assertThat(str.untranslatables).isEmpty()

    assertThat(testParse("""
        <string name="foo2">



        </string>
    """.trimMargin()))

    str = getValue("string/foo2") as BasicString
    assertThat(str.toString()).isEmpty()
    assertThat(str.untranslatables).isEmpty()
  }

  @Test
  fun testEmptyString() {
    assertThat(testParse("""<string name="foo"></string>"""))

    val str = getValue("string/foo") as BasicString
    assertThat(str.toString()).isEmpty()
    assertThat(str.untranslatables).isEmpty()
  }

  @Test
  fun testShortStrings() {
    assertThat(testParse("""<string name="foo">a</string>"""))

    var str = getValue("string/foo") as BasicString
    assertThat(str.toString()).isEqualTo("a")
    assertThat(str.untranslatables).isEmpty()

    assertThat(testParse("""<string name="foo2">\n</string>"""))

    str = getValue("string/foo2") as BasicString
    assertThat(str.toString()).isEqualTo("\n")
    assertThat(str.untranslatables).isEmpty()

    assertThat(testParse("""<string name="foo3">"</string>"""))

    str = getValue("string/foo3") as BasicString
    //AAPT2 removes the single quotes, so should be empty.
    assertThat(str.toString()).isEmpty()
    assertThat(str.untranslatables).isEmpty()
  }

  @Test
  fun testStringWithSlash() {
    assertThat(testParse("""<string name="foo">\</string>"""))

    var str = getValue("string/foo") as BasicString
    assertThat(str.toString()).isEqualTo("")
    assertThat(str.untranslatables).isEmpty()

    assertThat(testParse("""<string name="foo2">\woo\</string>"""))

    str = getValue("string/foo2") as BasicString
    assertThat(str.toString()).isEqualTo("woo")
    assertThat(str.untranslatables).isEmpty()

    assertThat(testParse("""<string name="foo3">\@\'\\</string>"""))

    str = getValue("string/foo3") as BasicString
    assertThat(str.toString()).isEqualTo("@'\\")
    assertThat(str.untranslatables).isEmpty()
  }

  @Test
  fun testIgnoreXliffTagsOtherThanG() {
    val input = """
      <string name="foo" xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2">
          There are <xliff:source>no</xliff:source> apples</string>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    var str = getValue("string/foo") as? BasicString
    assertThat(str).isNotNull()
    str = str!!
    assertThat(str.toString()).isEqualTo("There are no apples")
    assertThat(str.untranslatables).isEmpty()
  }

  @Test
  fun failToParseNestedXliffGTags() {
    val input = """
      <string name="foo" xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2">
          Do not <xliff:g>translate <xliff:g>this</xliff:g></xliff:g></string>
    """.trimIndent()
    assertThat(testParse(input)).isFalse()
  }

  @Test
  fun testParseUntranslatableSections() {
    val input = """
      <string name="foo" xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2">
          There are <xliff:g id="count">%1${"$"}d</xliff:g> apples</string>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    val str = getValue("string/foo") as? BasicString
    assertThat(str).isNotNull()
    str!!
    assertThat(str.toString()).isEqualTo("There are %1\$d apples")
    assertThat(str.untranslatables).hasSize(1)

    val untranslatables0 = str.untranslatables[0]
    assertThat(untranslatables0.startIndex).isEqualTo(10)
    assertThat(untranslatables0.endIndex).isEqualTo(14)
  }

  @Test
  fun testParseUntranslatablesInStyledString() {
    val input = """
      <string name="foo" xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2">
          There are <b><xliff:g id="count">%1${"$"}d</xliff:g></b> apples</string>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    val str = getValue("string/foo") as? StyledString
    assertThat(str).isNotNull()
    str!!
    assertThat(str.toString()).isEqualTo(" There are %1\$d apples")
    assertThat(str.spans()).hasSize(1)
    assertThat(str.untranslatableSections).hasSize(1)

    val untranslatables0 = str.untranslatableSections[0]
    assertThat(untranslatables0.startIndex).isEqualTo(11)
    assertThat(untranslatables0.endIndex).isEqualTo(15)

    val span0 = str.spans()[0]
    assertThat(span0.name.value()).isEqualTo("b")
    assertThat(span0.firstChar).isEqualTo(11)
    assertThat(span0.lastChar).isEqualTo(14)
  }

  @Test
  fun testParseNull() {
    val input = """<integer name="foo">@null</integer>"""
    assertThat(testParse(input)).isTrue()

    // The Android runtime treats a value of android::Res_value::TYPE_NULL as a non-existing value,
    // and this causes problems in styles when trying to resolve an attribute. Null values must be
    // encoded as android::Res_value::TYPE_REFERENCE with a data value of 0.
    val nullRef = getValue("integer/foo") as? Reference
    assertThat(nullRef).isNotNull()
    nullRef!!
    assertThat(nullRef.name).isEqualTo(ResourceName("", RAW, ""))
    assertThat(nullRef.id).isNull()
    assertThat(nullRef.referenceType).isEqualTo(Reference.Type.RESOURCE)
  }

  @Test
  fun testParseEmptyValue() {
    val input = """<integer name="foo">@empty</integer>"""
    assertThat(testParse(input)).isTrue()

    val integer = getValue("integer/foo") as? BinaryPrimitive
    assertThat(integer).isNotNull()
    integer!!
    assertThat(integer.resValue.dataType).isEqualTo(ResValue.DataType.NULL)
    assertThat(integer.resValue.data).isEqualTo(ResValue.NullFormat.EMPTY)
  }

  @Test
  fun testParseAttr() {
    val input = """
      <attr name="foo" format="string"/>
      <attr name="bar"/>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    val attr1 = getValue("attr/foo") as? AttributeResource
    assertThat(attr1).isNotNull()
    attr1!!
    assertThat(attr1.typeMask).isEqualTo(Resources.Attribute.FormatFlags.STRING_VALUE)

    val attr2 = getValue("attr/bar") as? AttributeResource
    assertThat(attr2).isNotNull()
    attr2!!
    assertThat(attr2.typeMask).isEqualTo(Resources.Attribute.FormatFlags.ANY_VALUE)
  }

  // Old AAPT allowed attributes to be defined under different configurations, but ultimately
  // stored them with the default configuration. Check that we have the same behavior.
  @Test
  fun testParseAttrAndDeclareStyleableUnderConfigButRecordAsNoConfig() {
    val watchConfig = parse("watch")
    val input = """
      <attr name="foo" />
      <declare-styleable name="bar">
        <attr name="baz" />
      </declare-styleable>
    """.trimIndent()
    assertThat(testParse(input, watchConfig)).isTrue()

    assertThat(getValue("attr/foo", watchConfig)).isNull()
    assertThat(getValue("attr/baz", watchConfig)).isNull()
    assertThat(getValue("styleable/bar", watchConfig)).isNull()

    assertThat(getValue("attr/foo")).isNotNull()
    assertThat(getValue("attr/baz")).isNotNull()
    assertThat(getValue("styleable/bar")).isNotNull()

    val styleable = table.packages[0].groups[1].entries.asIterable().first()
    assertThat(styleable).isNotNull()

    val styleableItem = table.packages[0].groups[1].getStyleable(styleable)
    assertThat(styleableItem).isNotNull()
    assertThat(styleableItem.entries.size).isEqualTo(1)
    assertThat(styleableItem.entries[0].name.entry).isEqualTo("baz")
  }

  @Test
  fun testEmptyStyleable() {
    val input = """
      <declare-styleable name="bar"/>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()
    assertThat(getValue("styleable/bar")).isNotNull()

    assertThat(table.packages.size).isEqualTo(1)
    val pck = table.packages[0]

    assertThat(pck.groups.size).isEqualTo(1)
    val group = pck.groups[0]

    assertThat(group.entries.size).isEqualTo(1)
    group.entries.forEach {
        assertThat(it.key).isEqualTo("bar")
        assertThat(group.type.tagName).isEqualTo("styleable")
        val styleableContainer = it.value.values.first().values
        assertThat(styleableContainer.size).isEqualTo(1)
        assertThat(styleableContainer[0].value).isInstanceOf(Styleable::class.java)
        assertThat(styleableContainer[0].value).isEqualTo(group.getStyleable(it))
    }
  }

  @Test
  fun testParseAttrWithMinMax() {
    val input = """<attr name="foo" min="10" max="23" format="integer"/>"""
    assertThat(testParse(input)).isTrue()

    val attr = getValue("attr/foo") as? AttributeResource
    assertThat(attr).isNotNull()
    attr!!
    assertThat(attr.typeMask).isEqualTo(Resources.Attribute.FormatFlags.INTEGER_VALUE)
    assertThat(attr.minInt).isEqualTo(10)
    assertThat(attr.maxInt).isEqualTo(23)
  }

  @Test
  fun failToParseAttrWithMinMaxButNotInteger() {
    val input = """<attr name="foo" min="10" max="23" format="string"/>"""
    assertThat(testParse(input)).isFalse()
  }

  @Test
  fun testParseUseAndDeclarationOfAttr() {
    val input = """
      <declare-styleable name="Styleable">
        <attr name="foo" />
      </declare-styleable>
      <attr name="foo" format="string"/>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    val attr = getValue("attr/foo") as? AttributeResource
    assertThat(attr).isNotNull()
    attr!!
    assertThat(attr.typeMask).isEqualTo(Resources.Attribute.FormatFlags.STRING_VALUE)
  }

  @Test
  fun testParseDoubleUseOfAttr() {
    val input = """
      <declare-styleable name="Theme">
        <attr name="foo" />
      </declare-styleable>
      <declare-styleable name="Window">
        <attr name="foo" format="boolean"/>
      </declare-styleable>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    val attr = getValue("attr/foo") as? AttributeResource
    assertThat(attr).isNotNull()
    attr!!
    assertThat(attr.typeMask).isEqualTo(Resources.Attribute.FormatFlags.BOOLEAN_VALUE)
  }

  @Test
  fun testParseEnumAttr() {
    val input = """
      <attr name="foo">
        <enum name="bar" value="0"/>
        <enum name="bat" value="1"/>
        <enum name="baz" value="2"/>
      </attr>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    val attr = getValue("attr/foo") as? AttributeResource
    assertThat(attr).isNotNull()
    attr!!
    assertThat(attr.typeMask).isEqualTo(Resources.Attribute.FormatFlags.ENUM_VALUE)
    assertThat(attr.symbols).hasSize(3)

    val symbol0 = attr.symbols[0]
    assertThat(symbol0.symbol.name.entry).isEqualTo("bar")
    assertThat(symbol0.value).isEqualTo(0)

    val symbol1 = attr.symbols[1]
    assertThat(symbol1.symbol.name.entry).isEqualTo("bat")
    assertThat(symbol1.value).isEqualTo(1)

    val symbol2 = attr.symbols[2]
    assertThat(symbol2.symbol.name.entry).isEqualTo("baz")
    assertThat(symbol2.value).isEqualTo(2)
  }

  @Test
  fun testParseFlagAttr() {
    val input = """
      <attr name="foo">
        <flag name="bar" value="0"/>
        <flag name="bat" value="1"/>
        <flag name="baz" value="2"/>
      </attr>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    val attr = getValue("attr/foo") as? AttributeResource
    assertThat(attr).isNotNull()
    attr!!
    assertThat(attr.typeMask).isEqualTo(Resources.Attribute.FormatFlags.FLAGS_VALUE)
    assertThat(attr.symbols).hasSize(3)

    val symbol0 = attr.symbols[0]
    assertThat(symbol0.symbol.name.entry).isEqualTo("bar")
    assertThat(symbol0.value).isEqualTo(0)

    val symbol1 = attr.symbols[1]
    assertThat(symbol1.symbol.name.entry).isEqualTo("bat")
    assertThat(symbol1.value).isEqualTo(1)

    val symbol2 = attr.symbols[2]
    assertThat(symbol2.symbol.name.entry).isEqualTo("baz")
    assertThat(symbol2.value).isEqualTo(2)

    val flagValue = tryParseFlagSymbol(attr, "baz | bat")
    assertThat(flagValue).isNotNull()
    flagValue!!
    assertThat(flagValue.resValue.data).isEqualTo(1 or 2)
  }

  @Test
  fun failParseEnumNonUniqueKeys() {
    val input = """
      <attr name="foo">
        <enum name="bar" value="0"/>
        <enum name="bat" value="1"/>
        <enum name="bat" value="2"/>
      </attr>
    """.trimIndent()
    assertThat(testParse(input)).isFalse()
  }

  @Test
  fun testParseStyle() {
    val input = """
      <style name="foo" parent="@style/fu">
        <item name="bar">#ffffffff</item>
        <item name="bat">@string/hey</item>
        <item name="baz"><b>hey</b></item>
      </style>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    val style = getValue("style/foo") as? Style
    assertThat(style).isNotNull()
    style!!
    assertThat(style.parent).isNotNull()
    assertThat(style.parent!!.name)
      .isEqualTo(parseResourceName("style/fu")!!.resourceName)
    assertThat(style.entries).hasSize(3)

    assertThat(style.entries[0].key.name)
      .isEqualTo(parseResourceName("attr/bar")!!.resourceName)
    assertThat(style.entries[1].key.name)
      .isEqualTo(parseResourceName("attr/bat")!!.resourceName)
    assertThat(style.entries[2].key.name)
      .isEqualTo(parseResourceName("attr/baz")!!.resourceName)
  }

  @Test
  fun testParseStyleWithShorthandParent() {
    assertThat(testParse("""<style name="foo" parent="com.app:Theme"/>""")).isTrue()

    val style = getValue("style/foo") as? Style
    assertThat(style).isNotNull()
    style!!
    assertThat(style.parent).isNotNull()
    assertThat(style.parent!!.name)
      .isEqualTo(parseResourceName("com.app:style/Theme")!!.resourceName)
  }

  @Test
  fun testParseStyleWithPackageAliasedParent() {
    val input = """
      <style xmlns:app="http://schemas.android.com/apk/res/android"
          name="foo" parent="app:Theme"/>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    val style = getValue("style/foo") as? Style
    assertThat(style).isNotNull()
    style!!
    assertThat(style.parent).isNotNull()
    assertThat(style.parent!!.name)
      .isEqualTo(parseResourceName("android:style/Theme")!!.resourceName)
  }

  @Test
  fun testParseStyleWithPackageAliasedItems() {
    val input = """
      <style xmlns:app="http://schemas.android.com/apk/res/android" name="foo">
        <item name="app:bar">0</item>
      </style>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    val style = getValue("style/foo") as? Style
    assertThat(style).isNotNull()
    style!!
    assertThat(style.entries).hasSize(1)
    assertThat(style.entries[0].key.name)
      .isEqualTo(parseResourceName("android:attr/bar")!!.resourceName)
  }

  @Test
  fun testParseStyleWithInferredParent() {
    assertThat(testParse("""<style name="foo.bar"/>""")).isTrue()

    val style = getValue("style/foo.bar") as? Style
    assertThat(style).isNotNull()
    style!!
    assertThat(style.parent).isNotNull()
    assertThat(style.parentInferred).isTrue()
    assertThat(style.parent!!.name)
      .isEqualTo(parseResourceName("style/foo")!!.resourceName)
  }

  @Test
  fun testParseStyleWithOverwrittenInferredParent() {
    assertThat(testParse("""<style name="foo.bar" parent=""/>""")).isTrue()

    val style = getValue("style/foo.bar") as? Style
    assertThat(style).isNotNull()
    style!!
    assertThat(style.parent).isNull()
    assertThat(style.parentInferred).isFalse()
  }

  @Test
  fun testParseStyleWithPrivateParent() {
    assertThat(
      testParse("""<style name="foo" parent="*android:style/bar" />""")).isTrue()

    val style = getValue("style/foo") as? Style
    assertThat(style).isNotNull()
    style!!
    assertThat(style.parent).isNotNull()
    assertThat(style.parent!!.isPrivate).isTrue()
  }

  @Test
  fun testParseAutoGeneratedId() {
    assertThat(testParse("""<string name="foo">@+id/bar</string>""")).isTrue()
    assertThat(getValue("id/bar")).isNotNull()
  }

  @Test
  fun testParseAttributesInDeclareStyleable() {
    val input = """
      <declare-styleable name="foo">
        <attr name="bar" />
        <attr name="bat" format="string|reference"/>
        <attr name="baz">
          <enum name="foo" value="1"/>
        </attr>
      </declare-styleable>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    val tableResult = table.findResource(parseResourceName("styleable/foo")!!.resourceName)
    assertThat(tableResult).isNotNull()
    assertThat(tableResult!!.entry.visibility.level).isEqualTo(ResourceVisibility.PUBLIC)

    val attr1 = getValue("attr/bar") as? AttributeResource
    assertThat(attr1).isNotNull()
    assertThat(attr1!!.weak).isTrue()

    val attr2 = getValue("attr/bat") as? AttributeResource
    assertThat(attr2).isNotNull()
    assertThat(attr2!!.weak).isTrue()

    val attr3 = getValue("attr/baz") as? AttributeResource
    assertThat(attr3).isNotNull()
    assertThat(attr3!!.weak).isTrue()
    assertThat(attr3.symbols).hasSize(1)

    assertThat(getValue("id/foo")).isNotNull()

    val styleable = getValue("styleable/foo") as? Styleable
    assertThat(styleable).isNotNull()
    styleable!!
    assertThat(styleable.entries).hasSize(3)

    assertThat(styleable.entries[0].name)
      .isEqualTo(parseResourceName("attr/bar")!!.resourceName)
    assertThat(styleable.entries[1].name)
      .isEqualTo(parseResourceName("attr/bat")!!.resourceName)
    assertThat(styleable.entries[2].name)
      .isEqualTo(parseResourceName("attr/baz")!!.resourceName)
  }

  @Test
  fun testParsePrivateAttributesDeclareStyleable() {
    val input = """
      <declare-styleable xmlns:privAndroid="http://schemas.android.com/apk/prv/res/android"
          name="foo">
        <attr name="*android:bar" />
        <attr name="privAndroid:bat" />
      </declare-styleable>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    val styleable = getValue("styleable/foo") as? Styleable
    assertThat(styleable).isNotNull()
    styleable!!
    assertThat(styleable.entries).hasSize(2)

    val attr0 = styleable.entries[0]
    assertThat(attr0.isPrivate).isTrue()
    assertThat(attr0.name.pck).isEqualTo("android")

    val attr1 = styleable.entries[1]
    assertThat(attr1.isPrivate).isTrue()
    assertThat(attr1.name.pck).isEqualTo("android")
  }

  @Test
  fun testParseArray() {
    val input = """
      <array name="foo">
        <item>@string/ref</item>
        <item>hey</item>
        <item>23</item>
      </array>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    val array = getValue("array/foo") as? ArrayResource
    assertThat(array).isNotNull()
    array!!
    assertThat(array.elements).hasSize(3)

    val item0 = array.elements[0] as? Reference
    assertThat(item0).isNotNull()
    val item1 = array.elements[1] as? BasicString
    assertThat(item1).isNotNull()
    val item2 = array.elements[2] as? BinaryPrimitive
    assertThat(item2).isNotNull()
  }

  @Test
  fun testParseStringArray() {
    val input = """
      <string-array name="foo">
        <item>"Werk"</item>"
      </string-array>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    val array = getValue("array/foo") as? ArrayResource
    assertThat(array).isNotNull()
    assertThat(array!!.elements).hasSize(1)
  }

  @Test
  fun testParseArrayWithFormat() {
    val input = """
      <array name="foo" format="string">
        <item>100</item>
      </array>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    val array = getValue("array/foo") as? ArrayResource
    assertThat(array).isNotNull()
    array!!
    assertThat(array.elements).hasSize(1)

    val str = array.elements[0] as? BasicString
    assertThat(str).isNotNull()
    assertThat(str!!.toString()).isEqualTo("100")
  }

  @Test
  fun testParseArrayWithBadFormat() {
    val input = """
      <array name="foo" format="integer">
        <item>Hi</item>
      </array>
    """.trimIndent()
    assertThat(testParse(input)).isFalse()
  }

  @Test
  fun testParsePlural() {
    val input = """
      <plurals name="foo">
        <item quantity="other">apples</item>
        <item quantity="one">apple</item>
      </plurals>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    val plural = getValue("plurals/foo") as? Plural
    assertThat(plural).isNotNull()
    plural!!
    assertThat(plural.values[Plural.Type.ZERO.ordinal]).isNull()
    assertThat(plural.values[Plural.Type.TWO.ordinal]).isNull()
    assertThat(plural.values[Plural.Type.FEW.ordinal]).isNull()
    assertThat(plural.values[Plural.Type.MANY.ordinal]).isNull()

    assertThat(plural.values[Plural.Type.ONE.ordinal]).isNotNull()
    assertThat(plural.values[Plural.Type.OTHER.ordinal]).isNotNull()
  }

  @Test
  fun testParseCommentsWithResource() {
    val input = """
      <!--This is a comment-->
      <string name="foo">Hi</string>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    val value = getValue("string/foo") as? BasicString
    assertThat(value).isNotNull()
    assertThat(value!!.comment).isEqualTo("This is a comment")
  }

  @Test
  fun testDoNotCombineMultipleComments() {
    val input = """
      <!--One-->
      <!--Two-->
      <string name="foo">Hi</string>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    val value = getValue("string/foo") as? BasicString
    assertThat(value).isNotNull()
    assertThat(value!!.comment).isEqualTo("Two")
  }

  @Test
  fun testIgnoreCommentBeforeEndTag() {
    val input = """
      <!--One-->
      <string name="foo">
        Hi
      <!--Two-->
      </string>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    val value = getValue("string/foo") as? BasicString
    assertThat(value).isNotNull()
    assertThat(value!!.comment).isEqualTo("One")
  }

  @Test
  fun testParseNestedComments() {
    // We only care about declare-styleable and enum/flag attributes because comments from those end
    // up in R.java
    val input = """
      <declare-styleable name="foo">
        <!-- The name of the bar -->
        <attr name="barName" format="string|reference" />
      </declare-styleable>

      <attr name="foo">
        <!-- The very first -->
        <enum name="one" value="1" />
      </attr>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    val styleable = getValue("styleable/foo") as? Styleable
    assertThat(styleable).isNotNull()
    assertThat(styleable!!.entries).hasSize(1)
    assertThat(styleable.entries[0].comment).isEqualTo("The name of the bar")

    val attr = getValue("attr/foo") as? AttributeResource
    assertThat(attr).isNotNull()
    assertThat(attr!!.symbols).hasSize(1)
    assertThat(attr.symbols[0].symbol.comment).isEqualTo("The very first")
  }

  @Test
  fun testParsePublicIdAsDefinition() {
    // Declaring an id as public should not require a separate definition (as an id has no value)
    assertThat(testParse("""<public type="id" name="foo"/>""")).isTrue()
    assertThat(getValue("id/foo") as? Id).isNotNull()
  }

  @Test
  fun testKeepAllProducts() {
    val input = """
      <string name="foo" product="phone">hi</string>
      <string name="foo" product="no-sdcard">ho</string>
      <string name="bar" product="">wee</string>
      <string name="baz">woo</string>
      <string name="bit" product="phablet">hoot</string>
      <string name="bot" product="default">yes</string>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    assertThat(getValue("string/foo", productName = "phone") as? BasicString)
      .isNotNull()
    assertThat(getValue("string/foo", productName = "no-sdcard") as? BasicString)
      .isNotNull()
    assertThat(getValue("string/bar") as? BasicString).isNotNull()
    assertThat(getValue("string/baz") as? BasicString).isNotNull()
    assertThat(getValue("string/bit", productName = "phablet") as? BasicString)
      .isNotNull()
    assertThat(getValue("string/bot", productName = "default") as? BasicString)
      .isNotNull()
  }

  @Test
  fun testAutoIncrementIdsInPublicGroup() {
    val input = """
      <public-group type="attr" first-id="0x01010040">
        <public name="foo" />
        <public name="bar" />
      </public-group>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    val tableResult0 = table.findResource(parseResourceName("attr/foo")!!.resourceName)
    assertThat(tableResult0).isNotNull()
    tableResult0!!
    assertThat(tableResult0.tablePackage.id).isNotNull()
    assertThat(tableResult0.group.id).isNotNull()
    assertThat(tableResult0.entry.id).isNotNull()
    val actualId0 = resourceIdFromParts(
      tableResult0.tablePackage.id!!,
      tableResult0.group.id!!,
      tableResult0.entry.id!!)
    assertThat(actualId0).isEqualTo(0x01010040)

    val tableResult1 = table.findResource(parseResourceName("attr/bar")!!.resourceName)
    assertThat(tableResult1).isNotNull()
    tableResult1!!
    assertThat(tableResult1.tablePackage.id).isNotNull()
    assertThat(tableResult1.group.id).isNotNull()
    assertThat(tableResult1.entry.id).isNotNull()
    val actualId1 = resourceIdFromParts(
      tableResult1.tablePackage.id!!,
      tableResult1.group.id!!,
      tableResult1.entry.id!!)
    assertThat(actualId1).isEqualTo(0x01010041)
  }

  @Test
  fun testStrongestSymbolVisibilityWins() {
    val input = """
      <public type="string" name="foo" id="0x01020000" />
      <string name="foo" />
      <java-symbol type="string" name="bar"/>
      <string name="bar" />

      <string name="foo_rev"/>
      <public name="foo_rev" type="string"/>
      <string name="bar_rev"/>
      <java-symbol name="bar_rev" type="string"/>
    """.trimIndent()
    assertThat(testParse(input)).isTrue()

    val fooResult = table.findResource(parseResourceName("string/foo")!!.resourceName)
    assertThat(fooResult).isNotNull()
    assertThat(fooResult!!.entry.visibility.level).isEqualTo(ResourceVisibility.PUBLIC)

    val barResult = table.findResource(parseResourceName("string/bar")!!.resourceName)
    assertThat(barResult).isNotNull()
    assertThat(barResult!!.entry.visibility.level).isEqualTo(ResourceVisibility.PRIVATE)

    val fooRevResult = table.findResource(parseResourceName("string/foo_rev")!!.resourceName)
    assertThat(fooRevResult).isNotNull()
    assertThat(fooRevResult!!.entry.visibility.level).isEqualTo(ResourceVisibility.PUBLIC)

    val barRevResult = table.findResource(parseResourceName("string/bar_rev")!!.resourceName)
    assertThat(barRevResult).isNotNull()
    assertThat(barRevResult!!.entry.visibility.level).isEqualTo(ResourceVisibility.PRIVATE)
  }

  @Test
  fun testVisibilityConflict() {
    val input = """
      <java-symbol type="string" name="foo" />
      <public type="string" name="foo" id="0x01020000" />
    """.trimIndent()
    assertThat(testParse(input)).isFalse()
  }

  @Test
  fun testExternalTypesShouldBeReferences() {
    assertThat(
      testParse("""<item type="layout" name="foo">@layout/bar</item>""")).isTrue()
    assertThat(
      testParse("""<item type="layout" name="bar">"this is a string"</item>""")).isFalse()
  }

  @Test
  fun testAddResourcesElementShouldAddEntryWithUndefinedSymbol() {
    assertThat(testParse("""<add-resource name="bar" type="string" />""")).isTrue()

    val tableResult = table.findResource(parseResourceName("string/bar")!!.resourceName)
    assertThat(tableResult).isNotNull()
    val entry = tableResult!!.entry
    assertThat(entry.visibility.level).isEqualTo(ResourceVisibility.UNDEFINED)
    assertThat(entry.allowNew).isNotNull()
  }

  @Test
  fun testParseItemElementWithFormat() {
    assertThat(
      testParse("""<item name="foo" type="integer" format="float">0.3</item>""")).isTrue()

    val primitive = getValue("integer/foo") as? BinaryPrimitive
    assertThat(primitive).isNotNull()
    assertThat(primitive!!.resValue.dataType).isEqualTo(ResValue.DataType.FLOAT)

    assertThat(
      testParse("""<item name="bar" type="integer" format="fraction">100</item>""")).isFalse()
  }

  @Test
  fun testParseItemElementValueOnNewLineWithQuotations() {
    val sample =
        """<style name="Widget.ImageButton.Custom" parent="android:style/Widget.ImageButton">
                <item name="customAttr">
                  "some value"
                </item>
           </style>"""
    assertThat(testParse(sample)).isTrue()
    val style = getValue("style/Widget.ImageButton.Custom") as Style
    val item = style.entries[0].value as RawString
    assertThat(item.value.value()).isEqualTo("some value")
  }

  @Test
  fun testParseItemElementWithValueOnNewLineWithNoQuotations() {
      val sample =
         """<style name="Widget.ImageButton.Custom" parent="android:style/Widget.ImageButton">
                <item name="customAttr">
                    some value
                </item>
            </style>"""
      assertThat(testParse(sample)).isTrue()
      val style = getValue("style/Widget.ImageButton.Custom") as Style
      val item = style.entries[0].value as RawString
      assertThat(item.value.value()).isEqualTo("some value")
  }

  @Test
  fun testMacro() {
      val mockLogger = BlameLoggerTest.MockLogger()

      val macros = """
          <macro name="m_string">@string/foo</macro>
          <macro name="m_int">@integer/foo</macro>
          <macro name="m_attr">?attr/foo</macro>
          <macro name="m_macro">@macro/foo</macro>
          <macro name="m_text_value">Hello world</macro>
          <macro name="m_int_value">123</macro>
          """.trimIndent()

      val result = testParse(macros, mockLogger = mockLogger)
      assertThat(result).isTrue()
      assertThat(mockLogger.errors).isEmpty()
      assertThat(mockLogger.warnings).isEmpty()

      val stringRefMacro = getValue("macro/m_string") as Macro
      assertThat(stringRefMacro).isNotNull()
      assertThat(stringRefMacro.rawValue).isEqualTo("@string/foo")

      val intRefMacro = getValue("macro/m_int") as Macro
      assertThat(intRefMacro).isNotNull()
      assertThat(intRefMacro.rawValue).isEqualTo("@integer/foo")

      val maybeAttrRefMacro = getValue("macro/m_attr") as Macro
      assertThat(maybeAttrRefMacro).isNotNull()
      assertThat(maybeAttrRefMacro.rawValue).isEqualTo("?attr/foo")

      val macroRefMacro = getValue("macro/m_macro") as Macro
      assertThat(macroRefMacro).isNotNull()
      assertThat(macroRefMacro.rawValue).isEqualTo("@macro/foo")

      val rawStringValueMacro = getValue("macro/m_text_value") as Macro
      assertThat(rawStringValueMacro).isNotNull()
      assertThat(rawStringValueMacro.untranslatables).isEmpty()
      assertThat(rawStringValueMacro.rawValue).isEqualTo("Hello world")

      // Any text that isn't a reference is held as a string in a macro
      val rawIntValueMacro = getValue("macro/m_int_value") as Macro
      assertThat(rawIntValueMacro).isNotNull()
      assertThat(rawIntValueMacro.untranslatables).isEmpty()
      assertThat(rawIntValueMacro.rawValue).isEqualTo("123")
  }

  @Test
  fun testMacroMissingName() {
      val mockLogger = BlameLoggerTest.MockLogger()

      val result = testParse("<macro/>", mockLogger = mockLogger)
      assertThat(result).isFalse()
      assertThat(mockLogger.errors).hasSize(1)
      assertThat(mockLogger.errors.single().first).contains("<macro> is missing the 'name' attribute.")
  }

}
