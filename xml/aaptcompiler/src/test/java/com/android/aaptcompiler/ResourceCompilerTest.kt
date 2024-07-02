package com.android.aaptcompiler

import com.android.aapt.Resources
import com.android.aaptcompiler.android.ResValue
import com.android.aaptcompiler.proto.deserializeConfigFromPb
import com.android.aaptcompiler.proto.deserializeTableFromPb
import com.android.aaptcompiler.testutils.ContainerReader
import com.android.aaptcompiler.testutils.FileEntry
import com.android.aaptcompiler.testutils.TableEntry
import com.android.aaptcompiler.testutils.parseNameOrFail
import com.android.utils.FileUtils
import com.google.common.truth.Truth
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class ResourceCompilerTest {

    @Rule
    @JvmField
    var tempFolder = TemporaryFolder()

    lateinit var outputDir: File

    @Before
    fun setup() {
        outputDir = tempFolder.newFolder()
    }

    private fun createFile(input: String, name: String, parentFolder: File? = null): File {
        val file = if (parentFolder != null) {
            File(parentFolder, name)
        } else {
            tempFolder.newFile(name)
        }
        file.writeText(input)
        return file
    }

    private fun testValuesFile(
        input: String,
        config: String = "",
        options: ResourceCompilerOptions = ResourceCompilerOptions()
    ): File {
        val valuesFolder = tempFolder.newFolder("values")
        val configSuffix = if (config.isEmpty()) "" else "-$config"
        val file = createFile(input, "test$configSuffix.xml", valuesFolder)
        compileResource(file, outputDir, options, getMockBlameLogger(BlameLoggerTest.MockLogger()))
        val filePath = extractPathData(file)
        // The extension is changed when the table is compiled.
        filePath.extension = "arsc"
        return File(outputDir, filePath.getIntermediateContainerFilename())
    }

    private fun testXmlFile(
      type: AaptResourceType,
      input: String,
      config: String = "",
      options: ResourceCompilerOptions = ResourceCompilerOptions()
    ): File {
        val resourceFolder = tempFolder.newFolder(type.tagName)
        val configSuffix = if (config.isEmpty()) "" else "-$config"
        val file = createFile(input, "test$configSuffix.xml", resourceFolder)
        compileResource(file, outputDir, options, getMockBlameLogger(BlameLoggerTest.MockLogger()))
        val filePath = extractPathData(file)
        return File(outputDir, filePath.getIntermediateContainerFilename())
    }

    private fun testRawFile(input: String, extension: String, config: String = ""): File {
        val resourceFolder = tempFolder.newFolder(AaptResourceType.RAW.tagName)
        val configSuffix = if (config.isEmpty()) "" else "-$config"
        val file = createFile(input, "test$configSuffix.$extension", resourceFolder)
        compileResource(
            file,
            outputDir,
            ResourceCompilerOptions(),
            getMockBlameLogger(BlameLoggerTest.MockLogger())
        )
        val filePath = extractPathData(file)
        return File(outputDir, filePath.getIntermediateContainerFilename())
    }

    private fun testPngFile(
      type: AaptResourceType,
      input: String,
      config: String = "",
      isPatch9: Boolean = false,
      options: ResourceCompilerOptions = ResourceCompilerOptions()
    ): File {
        val resourceFolder = tempFolder.newFolder(type.tagName)
        val configSuffix = if (config.isEmpty()) "" else "-$config"
        val extension = if (isPatch9) "9.png" else "png"
        val file = createFile(input, "test$configSuffix.$extension", resourceFolder)
        compileResource(file, outputDir, options, getMockBlameLogger(BlameLoggerTest.MockLogger()))
        val filePath = extractPathData(file)
        return File(outputDir, filePath.getIntermediateContainerFilename())
    }

    @Test
    fun testEmptyValuesFile() {
        val result = ContainerReader(testValuesFile(""))

        Truth.assertThat(result.entries).hasSize(1)

        val entry = result.entries[0] as? TableEntry
        Truth.assertThat(entry).isNotNull()

        val newTable = ResourceTable()
        Truth.assertThat(deserializeTableFromPb(entry!!.table, newTable, null)).isTrue()
        // compilation package should still be available.
        Truth.assertThat(newTable.packages).hasSize(1)
        val pkg = newTable.packages[0]
        Truth.assertThat(pkg.groups).isEmpty()
        Truth.assertThat(pkg.name).isEqualTo("")
    }

    @Test
    fun failInvalidRoot() {
        try {
            testValuesFile("<resource></resource>")
            fail()
        } catch (e: Exception) {
            // expected.
        }
    }

    @Test
    fun testRootOnlyValuesFile() {
        val result = ContainerReader(testValuesFile("<resources></resources>"))

        Truth.assertThat(result.entries).hasSize(1)

        val entry = result.entries[0] as? TableEntry
        Truth.assertThat(entry).isNotNull()

        val newTable = ResourceTable()
        Truth.assertThat(deserializeTableFromPb(entry!!.table, newTable, null)).isTrue()
        // compilation package should still be available.
        Truth.assertThat(newTable.packages).hasSize(1)
        val pkg = newTable.packages[0]
        Truth.assertThat(pkg.groups).isEmpty()
        Truth.assertThat(pkg.name).isEqualTo("")
    }

    @Test
    fun testStringValuesFile() {
        val input = """
      <?xml version="1.0" encoding="utf-8"?>
      <resources>
        <string name="normal1">Hi there!</string>
        <string name="normal2">    "  Hello there! "    </string>
        <string name="normal3">  Hey
          there!  </string>
        <string name="styled1"> <b>Yo</b>, how are you? </string>
        <string name="styled2"> <i> italicized <b> and bold</b></i></string>
        <string name="styled3"><all><the><spans>Hi</spans></the></all></string>
        <plurals name="plural1">
          <item quantity="one">Found %d puppy!</item>
          <item quantity="other">Found %d puppies!</item>
        </plurals>
      </resources>
    """.trimIndent()

        val result = ContainerReader(testValuesFile(input))
        Truth.assertThat(result.entries).hasSize(1)

        val entry = result.entries[0] as? TableEntry
        Truth.assertThat(entry).isNotNull()

        val newTable = ResourceTable()
        Truth.assertThat(deserializeTableFromPb(entry!!.table, newTable, null)).isTrue()

        val normal1 = getValue(newTable, "string/normal1") as? BasicString
        Truth.assertThat(normal1).isNotNull()
        Truth.assertThat(normal1!!.toString()).isEqualTo("Hi there!")

        val normal2 = getValue(newTable, "string/normal2") as? BasicString
        Truth.assertThat(normal2).isNotNull()
        // only spaces inside the quotes should be preserved from the xml.
        Truth.assertThat(normal2!!.toString()).isEqualTo("  Hello there! ")

        val normal3 = getValue(newTable, "string/normal3") as? BasicString
        Truth.assertThat(normal3).isNotNull()
        // Spaces on the end are skipped for BasicStrings. However white space is compressed
        // inbetween words in the strings (the newline and spaces are treated as a single space.
        Truth.assertThat(normal3!!.toString()).isEqualTo("Hey there!")

        val styled1 = getValue(newTable, "string/styled1") as? StyledString
        Truth.assertThat(styled1).isNotNull()
        // Spaces are preserved on the ends of a styled string.
        Truth.assertThat(styled1!!.toString()).isEqualTo(" Yo, how are you? ")
        val spans1 = styled1.spans()
        Truth.assertThat(spans1).hasSize(1)
        // Spans are firstChar to lastChar inclusive, so the last char ends on 2, not 3.
        Truth.assertThat(spans1[0].firstChar).isEqualTo(1)
        Truth.assertThat(spans1[0].lastChar).isEqualTo(2)
        Truth.assertThat(spans1[0].name.value()).isEqualTo("b")

        val styled2 = getValue(newTable, "string/styled2") as? StyledString
        Truth.assertThat(styled2).isNotNull()
        // Whitespace is only compressed "around" spans but not through spans. I.e.
        // "b <b> a" -> "b  a" with two spaces.
        Truth.assertThat(styled2!!.toString()).isEqualTo("  italicized  and bold")
        val spans2 = styled2.spans()
        Truth.assertThat(spans2).hasSize(2)
        Truth.assertThat(spans2[0].firstChar).isEqualTo(1)
        Truth.assertThat(spans2[0].lastChar).isEqualTo(21)
        Truth.assertThat(spans2[0].name.value()).isEqualTo("i")
        Truth.assertThat(spans2[1].firstChar).isEqualTo(13)
        Truth.assertThat(spans2[1].lastChar).isEqualTo(21)
        Truth.assertThat(spans2[1].name.value()).isEqualTo("b")

        val styled3 = getValue(newTable, "string/styled3") as? StyledString
        Truth.assertThat(styled3).isNotNull()
        Truth.assertThat(styled3!!.toString()).isEqualTo("Hi")
        val spans3 = styled3.spans()
        Truth.assertThat(spans3).hasSize(3)
        Truth.assertThat(spans3[0].firstChar).isEqualTo(0)
        Truth.assertThat(spans3[0].lastChar).isEqualTo(1)
        Truth.assertThat(spans3[0].name.value()).isEqualTo("all")
        Truth.assertThat(spans3[1].firstChar).isEqualTo(0)
        Truth.assertThat(spans3[1].lastChar).isEqualTo(1)
        Truth.assertThat(spans3[1].name.value()).isEqualTo("the")
        Truth.assertThat(spans3[2].firstChar).isEqualTo(0)
        Truth.assertThat(spans3[2].lastChar).isEqualTo(1)
        Truth.assertThat(spans3[2].name.value()).isEqualTo("spans")

        val plurals = getValue(newTable, "plurals/plural1") as? Plural
        Truth.assertThat(plurals).isNotNull()

        val oneValue = plurals!!.values[Plural.Type.ONE.ordinal] as? BasicString
        Truth.assertThat(oneValue).isNotNull()
        Truth.assertThat(oneValue!!.toString()).isEqualTo("Found %d puppy!")

        val manyValue = plurals.values[Plural.Type.OTHER.ordinal] as? BasicString
        Truth.assertThat(manyValue).isNotNull()
        Truth.assertThat(manyValue!!.toString()).isEqualTo("Found %d puppies!")

        Truth.assertThat(plurals.values[Plural.Type.ZERO.ordinal]).isNull()
        Truth.assertThat(plurals.values[Plural.Type.TWO.ordinal]).isNull()
        Truth.assertThat(plurals.values[Plural.Type.FEW.ordinal]).isNull()
        Truth.assertThat(plurals.values[Plural.Type.MANY.ordinal]).isNull()
    }

    @Test
    fun testParseItems() {
        val input = """
      <resources>
        <string name="nullAttempt">@null</string>
        <string name="emptyAttempt">@empty</string>
        <string name="referenceString">@string/nullAttempt</string>
        <color name="color1">#abc</color>
        <color name="color2">#f123</color>
        <color name="red">#ff0000</color>
        <color name="cyan">#ff00ffff</color>
        <bool name="check1">true</bool>
        <integer name="int1">1234</integer>
        <integer name="int2">0x24</integer>
        <item name="pi" type="dimen" format="float">3.14</item>
        <dimen name="itemWidth">12dp</dimen>
        <fraction name="fraction1">100%</fraction>
      </resources>
    """.trimIndent()

        val result = ContainerReader(testValuesFile(input))
        Truth.assertThat(result.entries).hasSize(1)

        val entry = result.entries[0] as? TableEntry
        Truth.assertThat(entry).isNotNull()

        val newTable = ResourceTable()
        Truth.assertThat(deserializeTableFromPb(entry!!.table, newTable, null)).isTrue()

        // Null types are empty references to aapt.
        val nullReference = getValue(newTable, "string/nullAttempt") as? Reference
        Truth.assertThat(nullReference).isNotNull()
        Truth.assertThat(nullReference!!.name).isEqualTo(ResourceName.EMPTY)

        // Empty references are binary primitives with the null format.
        val emptyReference = getValue(newTable, "string/emptyAttempt") as? BinaryPrimitive
        Truth.assertThat(emptyReference).isNotNull()
        Truth.assertThat(emptyReference!!.resValue.dataType).isEqualTo(ResValue.DataType.NULL)
        Truth.assertThat(emptyReference.resValue.data).isEqualTo(ResValue.NullFormat.EMPTY)

        val stringReference = getValue(newTable, "string/referenceString") as? Reference
        Truth.assertThat(stringReference).isNotNull()
        Truth.assertThat(stringReference!!.name).isEqualTo(parseNameOrFail("string/nullAttempt"))

        val color1 = getValue(newTable, "color/color1") as? BinaryPrimitive
        Truth.assertThat(color1).isNotNull()
        Truth.assertThat(color1!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_COLOR_RGB4)
        Truth.assertThat(color1.resValue.data).isEqualTo(0xffaabbcc.toInt())

        val color2 = getValue(newTable, "color/color2") as? BinaryPrimitive
        Truth.assertThat(color2).isNotNull()
        Truth.assertThat(color2!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_COLOR_ARGB4)
        Truth.assertThat(color2.resValue.data).isEqualTo(0xff112233.toInt())

        val color3 = getValue(newTable, "color/red") as? BinaryPrimitive
        Truth.assertThat(color3).isNotNull()
        Truth.assertThat(color3!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_COLOR_RGB8)
        Truth.assertThat(color3.resValue.data).isEqualTo(0xffff0000.toInt())

        val color4 = getValue(newTable, "color/cyan") as? BinaryPrimitive
        Truth.assertThat(color4).isNotNull()
        Truth.assertThat(color4!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_COLOR_ARGB8)
        Truth.assertThat(color4.resValue.data).isEqualTo(0xff00ffff.toInt())

        val bool = getValue(newTable, "bool/check1") as? BinaryPrimitive
        Truth.assertThat(bool).isNotNull()
        Truth.assertThat(bool!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_BOOLEAN)
        Truth.assertThat(bool.resValue.data).isEqualTo(-1)

        val integer1 = getValue(newTable, "integer/int1") as? BinaryPrimitive
        Truth.assertThat(integer1).isNotNull()
        Truth.assertThat(integer1!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_DEC)
        Truth.assertThat(integer1.resValue.data).isEqualTo(1234)

        val integer2 = getValue(newTable, "integer/int2") as? BinaryPrimitive
        Truth.assertThat(integer2).isNotNull()
        Truth.assertThat(integer2!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_HEX)
        Truth.assertThat(integer2.resValue.data).isEqualTo(0x24)

        val floatVal = getValue(newTable, "dimen/pi") as? BinaryPrimitive
        Truth.assertThat(floatVal).isNotNull()
        Truth.assertThat(floatVal!!.resValue.dataType).isEqualTo(ResValue.DataType.FLOAT)
        Truth.assertThat(floatVal.resValue.data).isEqualTo(3.14.toFloat().toRawBits())

        val dimension = getValue(newTable, "dimen/itemWidth") as? BinaryPrimitive
        Truth.assertThat(dimension).isNotNull()
        Truth.assertThat(dimension!!.resValue.dataType).isEqualTo(ResValue.DataType.DIMENSION)
        Truth.assertThat(dimension.resValue.data).isEqualTo(0xc01)

        val fraction = getValue(newTable, "fraction/fraction1") as? BinaryPrimitive
        Truth.assertThat(fraction).isNotNull()
        Truth.assertThat(fraction!!.resValue.dataType).isEqualTo(ResValue.DataType.FRACTION)
        Truth.assertThat(fraction.resValue.data).isEqualTo(0x100)
    }

    @Test
    fun testParseInvalidColorHexCode() {
        val input = """
      <resources>
         <color name="invalid">#0xE1E1E1</color>
      </resources>
    """.trimIndent()

        try {
            testValuesFile(input)
        } catch (e: ResourceCompilationException) {
            Truth.assertThat(e.message).contains("Unable to parse hex color '#0xE1E1E1'.")
        }
    }

    @Test
    fun parseStyleableWithAndroidAttr() {
        val input = """
      <resources>
        <declare-styleable name="oneattr">
          <attr name="android:foo"/>
        </declare-styleable>
      </resources>
    """.trimIndent()

        val result = ContainerReader(testValuesFile(input))
        Truth.assertThat(result.entries).hasSize(1)

        val entry = result.entries[0] as? TableEntry
        Truth.assertThat(entry).isNotNull()

        val newTable = ResourceTable()
        Truth.assertThat(deserializeTableFromPb(entry!!.table, newTable, null)).isTrue()

        val styleable = getValue(newTable, "styleable/oneattr") as? Styleable
        Truth.assertThat(styleable).isNotNull()
        Truth.assertThat(styleable!!.entries).hasSize(1)
        val styleAttribute = styleable.entries[0]
        Truth.assertThat(styleAttribute.name).isEqualTo(parseNameOrFail("android:attr/foo"))
    }

    @Test
    fun parseArrayTypes() {
        val input = """
      <resources>
        <integer-array name="ints">
            <item>0</item>
            <item>1</item>
        </integer-array>
        <string-array name="strings">
          <item>foo</item>
          <item>bar</item>
        </string-array>
        <array name="string_refs">
          <item>@string/s0</item>
          <item>@string/s1</item>
          <item>@string/s2</item>
        </array>
        <array name="colors">
          <item>#FFFF0000</item>
          <item>#FF00FF00</item>
          <item>#FF0000FF</item>
        </array>
      </resources>
    """.trimIndent()

        val result = ContainerReader(testValuesFile(input))
        Truth.assertThat(result.entries).hasSize(1)

        val entry = result.entries[0] as? TableEntry
        Truth.assertThat(entry).isNotNull()

        val newTable = ResourceTable()
        Truth.assertThat(deserializeTableFromPb(entry!!.table, newTable, null)).isTrue()

        val intsArray = getValue(newTable, "array/ints") as? ArrayResource
        Truth.assertThat(intsArray).isNotNull()
        Truth.assertThat(intsArray!!.elements).hasSize(2)
        Truth.assertThat((intsArray.elements[0] as BinaryPrimitive).resValue.data).isEqualTo(0)
        Truth.assertThat((intsArray.elements[1] as BinaryPrimitive).resValue.data).isEqualTo(1)

        val stringsArray1 = getValue(newTable, "array/strings") as? ArrayResource
        Truth.assertThat(stringsArray1).isNotNull()
        Truth.assertThat(stringsArray1!!.elements).hasSize(2)
        Truth.assertThat((stringsArray1.elements[0] as BasicString).toString()).isEqualTo("foo")
        Truth.assertThat((stringsArray1.elements[1] as BasicString).toString()).isEqualTo("bar")

        val stringsArray2 = getValue(newTable, "array/string_refs") as? ArrayResource
        Truth.assertThat(stringsArray2).isNotNull()
        Truth.assertThat(stringsArray2!!.elements).hasSize(3)
        Truth.assertThat((stringsArray2.elements[0] as Reference).name)
            .isEqualTo(parseNameOrFail("string/s0"))
        Truth.assertThat((stringsArray2.elements[1] as Reference).name)
            .isEqualTo(parseNameOrFail("string/s1"))
        Truth.assertThat((stringsArray2.elements[2] as Reference).name)
            .isEqualTo(parseNameOrFail("string/s2"))

        val colorsArray = getValue(newTable, "array/colors") as? ArrayResource
        Truth.assertThat(colorsArray).isNotNull()
        Truth.assertThat(colorsArray!!.elements).hasSize(3)
        Truth.assertThat((colorsArray.elements[0] as? BinaryPrimitive)?.resValue?.data)
            .isEqualTo(0xffff0000.toInt())
        Truth.assertThat((colorsArray.elements[1] as? BinaryPrimitive)?.resValue?.data)
            .isEqualTo(0xff00ff00.toInt())
        Truth.assertThat((colorsArray.elements[2] as? BinaryPrimitive)?.resValue?.data)
            .isEqualTo(0xff0000ff.toInt())
    }

    @Test
    fun parseAttrInStyleable() {
        val input = """
      <resources>
        <declare-styleable name="PieChart">
          <attr name="showText" format="boolean" />
          <attr name="labelPosition" format="enum">
            <enum name="left" value="0"/>
            <enum name="right" value="1"/>
          </attr>
        </declare-styleable>
      </resources>
    """.trimIndent()

        val result = ContainerReader(testValuesFile(input))
        Truth.assertThat(result.entries).hasSize(1)

        val entry = result.entries[0] as? TableEntry
        Truth.assertThat(entry).isNotNull()

        val newTable = ResourceTable()
        Truth.assertThat(deserializeTableFromPb(entry!!.table, newTable, null)).isTrue()

        val styleable = getValue(newTable, "styleable/PieChart") as? Styleable
        Truth.assertThat(styleable).isNotNull()
        Truth.assertThat(styleable!!.entries).hasSize(2)

        val reference1 = styleable.entries[0]
        Truth.assertThat(reference1.name).isEqualTo(parseNameOrFail("attr/showText"))

        val reference2 = styleable.entries[1]
        Truth.assertThat(reference2.name).isEqualTo(parseNameOrFail("attr/labelPosition"))

        val attr1 = getValue(newTable, "attr/showText") as? AttributeResource
        Truth.assertThat(attr1).isNotNull()
        Truth.assertThat(attr1!!.symbols).hasSize(0)

        val attr2 = getValue(newTable, "attr/labelPosition") as? AttributeResource
        Truth.assertThat(attr2).isNotNull()
        Truth.assertThat(attr2!!.symbols).hasSize(2)

        val symbol1 = attr2.symbols[0]
        Truth.assertThat(symbol1.type).isEqualTo(ResValue.DataType.INT_DEC.byteValue)
        Truth.assertThat(symbol1.value).isEqualTo(0)
        Truth.assertThat(symbol1.symbol.name).isEqualTo(parseNameOrFail("id/left"))

        val symbol2 = attr2.symbols[1]
        Truth.assertThat(symbol2.type).isEqualTo(ResValue.DataType.INT_DEC.byteValue)
        Truth.assertThat(symbol2.value).isEqualTo(1)
        Truth.assertThat(symbol2.symbol.name).isEqualTo(parseNameOrFail("id/right"))
    }

    @Test
    fun testCompileRawFileSupported() {
        val input = """
      I'm just writing some text here, because raw files can be anything.
    """.trimIndent()

        val result = ContainerReader(testRawFile(input, "txt"))
        Truth.assertThat(result.numEntries).isEqualTo(1)

        val entry = result.entries[0] as? FileEntry
        Truth.assertThat(entry).isNotNull()

        val header = entry!!.header
        val data = entry.data

        Truth.assertThat(header.resourceName).isEqualTo("raw/test")
        Truth.assertThat(deserializeConfigFromPb(header.config, null).toString())
            .isEqualTo("DEFAULT")
        Truth.assertThat(header.exportedSymbolCount).isEqualTo(0)
        Truth.assertThat(header.type).isEqualTo(Resources.FileReference.Type.UNKNOWN)

        val dataString = String(data)
        Truth.assertThat(dataString).isEqualTo(input)
    }

    @Test
    fun testCompilePatch9ProcessingNotSupported() {
        val input = """
      What is here doesn't really matter, as the files location and extension determines
      whether or not this file is treated as a png.
    """.trimIndent()

        try {
            testPngFile(AaptResourceType.LAYOUT, input, isPatch9 = true)
            fail()
        } catch (e: ResourceCompilationException) {
            Truth.assertThat(e.cause!!.message).contains(
                "Patch 9 PNG processing is not supported with the JVM Android resource compiler."
            )
            // expected
        }
    }

    @Test
    fun testCompilePngCrunchingNotSupported() {
        val input = """
      What is here doesn't really matter, as the files location and extension determines
      whether or not this file is treated as a png.
    """.trimIndent()

        try {
            testPngFile(
                AaptResourceType.LAYOUT,
                input,
                options = ResourceCompilerOptions(requirePngCrunching = true)
            )
            fail()
        } catch (e: ResourceCompilationException) {
            Truth.assertThat(e.cause!!.message).contains(
                "PNG crunching is not supported with the JVM Android resource compiler."
            )
            // expected
        }
    }

    @Test
    fun testCompilePngWithoutCrunchingSupported() {
        val input = """
      What is here doesn't really matter, as the files location and extension determines
      whether or not this file is treated as a png.
    """.trimIndent()

        val result = ContainerReader(testPngFile(AaptResourceType.LAYOUT, input))
        Truth.assertThat(result.numEntries).isEqualTo(1)

        val entry = result.entries[0] as? FileEntry
        Truth.assertThat(entry).isNotNull()

        val header = entry!!.header
        val data = entry.data

        Truth.assertThat(header.resourceName).isEqualTo("layout/test")
        Truth.assertThat(deserializeConfigFromPb(header.config, null).toString())
            .isEqualTo("DEFAULT")
        Truth.assertThat(header.exportedSymbolCount).isEqualTo(0)
        Truth.assertThat(header.type).isEqualTo(Resources.FileReference.Type.UNKNOWN)

        val dataString = String(data)
        Truth.assertThat(dataString).isEqualTo(input)
    }

    @Test
    fun testCompileFileWithCustomSourcePath() {
        val input = // language=XML
                """<?xml version="1.0" encoding="utf-8"?>
        <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android">
            <TextBox android:id="@+id/text_one" android:background="@color/color_one"/>
        </LinearLayout>
    """.trimIndent()

        val path = FileUtils.join("main", "res", "values", "test.xml")
        val options = ResourceCompilerOptions(sourcePath = path)
        val result = ContainerReader(
                testXmlFile(type = AaptResourceType.LAYOUT, input = input, options = options))
        Truth.assertThat(result.numEntries).isEqualTo(1)
        val entries = result.entries as List<FileEntry>
        val sourcePath: String = entries[0].header.sourcePath
        Truth.assertThat(sourcePath).isEqualTo(path)
    }

    @Test
    fun testMacros() {
        val input = """
      <?xml version="1.0" encoding="utf-8"?>
      <resources>
        <macro name="m1">Hello world</macro>
        <macro name="m2">@macro/m1</macro>
      </resources>
    """.trimIndent()

        val result = ContainerReader(testValuesFile(input))
        Truth.assertThat(result.entries).hasSize(1)

        val entry = result.entries[0] as? TableEntry
        Truth.assertThat(entry).isNotNull()

        val newTable = ResourceTable()
        Truth.assertThat(deserializeTableFromPb(entry!!.table, newTable, null)).isTrue()

        val m1 = getValue(newTable, "macro/m1") as? Macro
        Truth.assertThat(m1).isNotNull()
        Truth.assertThat(m1!!.rawValue).isEqualTo("Hello world")

        val m2 = getValue(newTable, "macro/m2") as? Macro
        Truth.assertThat(m2).isNotNull()
        Truth.assertThat(m2!!.rawValue).isEqualTo("@macro/m1")
    }
}
