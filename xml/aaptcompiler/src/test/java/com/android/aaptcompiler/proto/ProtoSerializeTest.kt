package com.android.aaptcompiler.proto

import com.android.aapt.Resources
import com.android.aapt.Resources.Attribute.FormatFlags
import com.android.aaptcompiler.ArrayResource
import com.android.aaptcompiler.AttributeResource
import com.android.aaptcompiler.BasicString
import com.android.aaptcompiler.BinaryPrimitive
import com.android.aaptcompiler.ConfigDescription
import com.android.aaptcompiler.FileReference
import com.android.aaptcompiler.Id
import com.android.aaptcompiler.Overlayable
import com.android.aaptcompiler.OverlayableItem
import com.android.aaptcompiler.Plural
import com.android.aaptcompiler.RawString
import com.android.aaptcompiler.Reference
import com.android.aaptcompiler.ResourceFile
import com.android.aaptcompiler.ResourceTable
import com.android.aaptcompiler.Source
import com.android.aaptcompiler.Span
import com.android.aaptcompiler.StringPool
import com.android.aaptcompiler.Style
import com.android.aaptcompiler.StyleString
import com.android.aaptcompiler.Styleable
import com.android.aaptcompiler.StyledString
import com.android.aaptcompiler.Visibility
import com.android.aaptcompiler.android.ResStringPool
import com.android.aaptcompiler.android.ResValue
import com.android.aaptcompiler.android.ResValue.DataType.INT_DEC
import com.android.aaptcompiler.android.ResValue.DataType.INT_HEX
import com.android.aaptcompiler.android.ResValue.DataType.NULL
import com.android.aaptcompiler.android.ResValue.NullFormat
import com.android.aaptcompiler.buffer.BigBuffer
import com.android.aaptcompiler.getValue
import com.android.aaptcompiler.makeEmpty
import com.android.aaptcompiler.parse
import com.android.aaptcompiler.parseResourceName
import com.android.aaptcompiler.testutils.ResourceTableBuilder
import com.android.aaptcompiler.testutils.parseNameOrFail
import com.android.aaptcompiler.tryParseBool
import com.android.aaptcompiler.tryParseColor
import com.android.aaptcompiler.tryParseFloat
import com.android.aaptcompiler.tryParseInt
import com.android.aaptcompiler.tryParseNullOrEmpty
import com.itsaky.androidide.layoutlib.resources.ResourceVisibility
import com.google.common.truth.Truth
import org.junit.Test
import java.nio.ByteBuffer

class ProtoSerializeTest {

  private fun testSerializeDeserialize(primitive: BinaryPrimitive) {
    val pbPrimitive = serializeBinPrimitiveToPb(primitive, null)
    val deserialized = deserializeBinPrimitiveFromPb(pbPrimitive, null)

    Truth.assertThat(deserialized).isEqualTo(primitive)
  }

  private fun testSerializeDeserialize(configDescription: ConfigDescription) {
    val pbConfig = serializeConfigToPb(configDescription, null, null)
    val deserialized = deserializeConfigFromPb(pbConfig, null)

    Truth.assertThat(deserialized).isEqualTo(configDescription)
  }

  private fun testSerializeDeserialize(string: BasicString, givenPool: StringPool) {
    val pbString = serializeStringToPb(string)
    val deserialized = deserializeStringFromPb(pbString, givenPool)

    Truth.assertThat(deserialized).isEqualTo(string)
  }

  private fun testSerializeDeserialize(string: RawString, givenPool: StringPool) {
    val pbString = serializeRawToPb(string)
    val deserialized = deserializeRawFromPb(pbString, givenPool)

    Truth.assertThat(deserialized).isEqualTo(string)
  }

  private fun testSerializeDeserialize(string: StyledString, givenPool: StringPool) {
    val pbString = serializeStyledStrToPb(string)
    val deserialized = deserializeStyledStrFromPb(pbString, givenPool)

    Truth.assertThat(deserialized).isEqualTo(string)
  }

  private fun testSerializeDeserialize(
    file: FileReference, givenPool: StringPool, config: ConfigDescription) {

    val pbFileRef = serializeFileRefToPb(file)
    val deserialized = deserializeFileRefFromPb(pbFileRef, givenPool, config)

    Truth.assertThat(deserialized).isEqualTo(file)
  }

  private fun testSerializeDeserialize(overlayableItem: OverlayableItem) {
    val sourcePool = StringPool()
    val overlayable = overlayableItem.overlayable
    val pbOverlayableItem = serializeOverlayableToPb(
      overlayableItem, mutableListOf(), Resources.ResourceTable.newBuilder(), sourcePool)

    val buffer = BigBuffer(1028)
    sourcePool.flattenUtf16(buffer, null)
    val extractedSources = ResStringPool.get(ByteBuffer.wrap(buffer.toBytes()), buffer.size)
    val deserialized = deserializeOverlayableFromPb(
      pbOverlayableItem, overlayable, extractedSources, null)

    Truth.assertThat(deserialized).isNotNull()
    Truth.assertThat(deserialized!!).isEqualTo(overlayableItem)
    Truth.assertThat(deserialized.source).isEqualTo(overlayableItem.source)
  }

  private fun testSerializeDeserialize(reference: Reference) {
    val pbReference = serializeReferenceToPb(reference)
    val deserialized = deserializeReferenceFromPb(pbReference, null)

    Truth.assertThat(deserialized).isNotNull()
    Truth.assertThat(deserialized!!).isEqualTo(reference)
    Truth.assertThat(deserialized.isPrivate).isEqualTo(reference.isPrivate)
    Truth.assertThat(deserialized.id).isEqualTo(reference.id)
  }

  private fun testSerializeDeserialize(attr: AttributeResource) {
    val sourcePool = StringPool()
    val pbAttr = serializeAttrToPb(attr, sourcePool)

    val buffer = BigBuffer(1028)
    sourcePool.flattenUtf16(buffer, null)
    val extractedSources = ResStringPool.get(ByteBuffer.wrap(buffer.toBytes()), buffer.size)
    val deserialized = deserializeAttrFromPb(pbAttr, extractedSources, null)

    Truth.assertThat(deserialized).isNotNull()
    Truth.assertThat(deserialized!!).isEqualTo(attr)

    for (index in attr.symbols.indices) {
      val original = attr.symbols[index]
      val translated = deserialized.symbols[index]

      Truth.assertThat(original.symbol.source).isEqualTo(translated.symbol.source)
      Truth.assertThat(original.symbol.comment).isEqualTo(translated.symbol.comment)
    }
  }

  private fun testSerializeDeserialize(style: Style, givenPool: StringPool) {
    val sourcePool = StringPool()
    val pbStyle = serializeStyleToPb(style, sourcePool, null)

    val buffer = BigBuffer(1028)
    sourcePool.flattenUtf16(buffer, null)
    val extractedSources = ResStringPool.get(ByteBuffer.wrap(buffer.toBytes()), buffer.size)
    val deserialized =
      deserializeStyleFromPb(pbStyle, givenPool, ConfigDescription(), extractedSources, null)

    Truth.assertThat(deserialized).isNotNull()
    Truth.assertThat(deserialized!!).isEqualTo(style)
    if (style.parent != null) {
      Truth.assertThat(deserialized.parent).isNotNull()
      Truth.assertThat(deserialized.parent!!.source).isEqualTo(style.parent!!.source)
    }

    for (index in style.entries.indices) {
      val original = style.entries[index]
      val translated = deserialized.entries[index]

      Truth.assertThat(translated.key.source).isEqualTo(original.key.source)
      Truth.assertThat(translated.key.comment).isEqualTo(original.key.comment)
      // When deserialized, the key's metadata is reflected in the value as well.
      Truth.assertThat(translated.value!!.source).isEqualTo(translated.key.source)
      Truth.assertThat(translated.value!!.comment).isEqualTo(translated.key.comment)
    }
  }

  private fun testSerializeDeserialize(array: ArrayResource, givenPool: StringPool) {
    val sourcePool = StringPool()
    val pbArray = serializeArrayToPb(array, sourcePool, null)

    val buffer = BigBuffer(1028)
    sourcePool.flattenUtf16(buffer, null)
    val extractedSources = ResStringPool.get(ByteBuffer.wrap(buffer.toBytes()), buffer.size)
    val deserialized = deserializeArrayFromPb(
        pbArray, givenPool, ConfigDescription(), extractedSources, null)

    Truth.assertThat(deserialized).isNotNull()
    Truth.assertThat(deserialized!!).isEqualTo(array)

    for (index in array.elements.indices) {
      val original = array.elements[index]
      val translated = deserialized.elements[index]

      Truth.assertThat(original.source).isEqualTo(translated.source)
      Truth.assertThat(original.comment).isEqualTo(translated.comment)
    }
  }

  private fun testSerializeDeserialize(styleable: Styleable) {
    val sourcePool = StringPool()
    val pbStyleable = serializeStyleableToPb(styleable, sourcePool)

    val buffer = BigBuffer(1028)
    sourcePool.flattenUtf16(buffer, null)
    val extractedSources = ResStringPool.get(ByteBuffer.wrap(buffer.toBytes()), buffer.size)
    val deserialized = deserializeStyleableFromPb(pbStyleable, extractedSources, null)

    Truth.assertThat(deserialized).isNotNull()
    Truth.assertThat(deserialized!!).isEqualTo(styleable)
    for (index in styleable.entries.indices) {
      val original = styleable.entries[index]
      val translated = styleable.entries[index]

      Truth.assertThat(original.source).isEqualTo(translated.source)
      Truth.assertThat(original.comment).isEqualTo(translated.comment)
    }
  }

  private fun testSerializeDeserialize(plural: Plural, givenPool: StringPool) {
    val sourcePool = StringPool()
    val pbPlural = serializePluralToPb(plural, sourcePool, null)

    val buffer = BigBuffer(1028)
    sourcePool.flattenUtf16(buffer, null)
    val extractedSources = ResStringPool.get(ByteBuffer.wrap(buffer.toBytes()), buffer.size)
    val deserialized =
      deserializePluralFromPb(pbPlural, givenPool, ConfigDescription(), extractedSources, null)

    Truth.assertThat(deserialized).isNotNull()
    Truth.assertThat(deserialized!!).isEqualTo(plural)

    for (index in plural.values.indices) {
      val original = plural.values[index]
      val translated = deserialized.values[index]

      if (original != null) {
        Truth.assertThat(original.source).isEqualTo(translated!!.source)
        Truth.assertThat(original.comment).isEqualTo(translated.comment)
      }
    }
  }

  @Test
  fun testSerializeBinaryPrimitive() {
    // Boolean type
    testSerializeDeserialize(tryParseBool("true")!!)
    // Null type
    testSerializeDeserialize(
      BinaryPrimitive(ResValue(NULL, NullFormat.UNDEFINED))
    )
    // Empty type
    testSerializeDeserialize(tryParseNullOrEmpty("@empty") as BinaryPrimitive)
    // Int types
    testSerializeDeserialize(tryParseInt("0xabcd")!!)
    testSerializeDeserialize(tryParseInt("-1024")!!)
    // Color types
    testSerializeDeserialize(tryParseColor("#ff0000ff")!!)
    testSerializeDeserialize(tryParseColor("#00ffff")!!)
    testSerializeDeserialize(tryParseColor("#f000")!!)
    testSerializeDeserialize(tryParseColor("#840")!!)
    // Float types
    testSerializeDeserialize(tryParseFloat("3.14")!!)
    testSerializeDeserialize(tryParseFloat("0x1.ff")!!)
    // Dimension types
    testSerializeDeserialize(tryParseFloat("20dp")!!)
    testSerializeDeserialize(tryParseFloat("0x19p0dp")!!)
    // Fraction types
    testSerializeDeserialize(tryParseFloat("20%")!!)
    testSerializeDeserialize(tryParseFloat("80%p")!!)
  }

  @Test
  fun testSerializeConfig() {
    // Empty config.
    testSerializeDeserialize(parse(""))
    testSerializeDeserialize(parse("mcc123"))

    testSerializeDeserialize(parse("mnc02"))
    testSerializeDeserialize(parse("mnc002"))
    testSerializeDeserialize(parse("mnc123"))

    testSerializeDeserialize(parse("en"))
    testSerializeDeserialize(parse("en-rGB"))
    testSerializeDeserialize(parse("b+en+GB"))

    testSerializeDeserialize(parse("ldltr"))
    testSerializeDeserialize(parse("ldrtl"))

    testSerializeDeserialize(parse("sw3600dp"))

    testSerializeDeserialize(parse("w300dp"))

    testSerializeDeserialize(parse("h400dp"))

    testSerializeDeserialize(parse("small"))
    testSerializeDeserialize(parse("normal"))
    testSerializeDeserialize(parse("large"))
    testSerializeDeserialize(parse("xlarge"))

    testSerializeDeserialize(parse("long"))
    testSerializeDeserialize(parse("notlong"))

    testSerializeDeserialize(parse("round"))
    testSerializeDeserialize(parse("notround"))

    testSerializeDeserialize(parse("widecg"))
    testSerializeDeserialize(parse("nowidecg"))

    testSerializeDeserialize(parse("highdr"))
    testSerializeDeserialize(parse("lowdr"))

    testSerializeDeserialize(parse("port"))
    testSerializeDeserialize(parse("land"))
    testSerializeDeserialize(parse("square"))

    testSerializeDeserialize(parse("desk"))
    testSerializeDeserialize(parse("car"))
    testSerializeDeserialize(parse("television"))
    testSerializeDeserialize(parse("appliance"))
    testSerializeDeserialize(parse("watch"))
    testSerializeDeserialize(parse("vrheadset"))

    testSerializeDeserialize(parse("night"))
    testSerializeDeserialize(parse("notnight"))

    testSerializeDeserialize(parse("300dpi"))
    testSerializeDeserialize(parse("hdpi"))

    testSerializeDeserialize(parse("notouch"))
    testSerializeDeserialize(parse("stylus"))
    testSerializeDeserialize(parse("finger"))

    testSerializeDeserialize(parse("keysexposed"))
    testSerializeDeserialize(parse("keyshidden"))
    testSerializeDeserialize(parse("keyssoft"))

    testSerializeDeserialize(parse("nokeys"))
    testSerializeDeserialize(parse("qwerty"))
    testSerializeDeserialize(parse("12key"))

    testSerializeDeserialize(parse("navhidden"))
    testSerializeDeserialize(parse("navexposed"))

    testSerializeDeserialize(parse("nonav"))
    testSerializeDeserialize(parse("dpad"))
    testSerializeDeserialize(parse("trackball"))
    testSerializeDeserialize(parse("wheel"))

    testSerializeDeserialize(parse("300x200"))

    testSerializeDeserialize(parse("v8"))

    testSerializeDeserialize(
      parse("mcc310-pl-sw720dp-normal-long-port-night-xhdpi-keyssoft-qwerty-navexposed-nonav"))
    testSerializeDeserialize(
      parse("mcc123-mnc456-b+en+GB-ldltr-sw300dp-w300dp-h400dp-large-long-round-widecg-highdr-" +
        "land-car-night-xhdpi-stylus-keysexposed-qwerty-navhidden-dpad-300x200-v23"))

    testSerializeDeserialize(parse("neuter"))
    testSerializeDeserialize(parse("feminine"))
    testSerializeDeserialize(parse("masculine"))
  }

  @Test
  fun testSerializeStrings() {
    val originalPool = StringPool()
    val newPool = StringPool()

    testSerializeDeserialize(BasicString(originalPool.makeRef("one")), newPool)
    testSerializeDeserialize(BasicString(originalPool.makeRef("two")), newPool)
    testSerializeDeserialize(BasicString(originalPool.makeRef("three")), newPool)
    testSerializeDeserialize(BasicString(originalPool.makeRef("three")), newPool)
    testSerializeDeserialize(BasicString(originalPool.makeRef("four")), newPool)
    testSerializeDeserialize(BasicString(originalPool.makeRef("four")), newPool)
    testSerializeDeserialize(BasicString(originalPool.makeRef("four")), newPool)
    testSerializeDeserialize(BasicString(originalPool.makeRef("four")), newPool)

    Truth.assertThat(newPool.size()).isEqualTo(4)

    testSerializeDeserialize(
      StyledString(
        originalPool.makeRef(
          StyleString(
            "Hello World!",
            listOf(Span("b", 0, 4), Span("i", 6, 10)))
        ),
        listOf()),
      newPool)

    Truth.assertThat(newPool.size()).isEqualTo(7)

    val fileRef = FileReference(originalPool.makeRef("values/strings.xml"))
    fileRef.type = ResourceFile.Type.ProtoXml
    testSerializeDeserialize(fileRef, newPool, ConfigDescription())

    Truth.assertThat(newPool.size()).isEqualTo(8)

    testSerializeDeserialize(RawString(originalPool.makeRef("raw")), newPool)

    Truth.assertThat(newPool.size()).isEqualTo(9)
  }

  @Test
  fun testOverlayableSerialization() {
    val overlayable = Overlayable("name", "actor", Source("res/values/overlayable.xml", 17))
    val overlayableItem = OverlayableItem(
      overlayable,
      OverlayableItem.Policy.SYSTEM or OverlayableItem.Policy.PRODUCT,
      "comment",
      Source("res/values/overlayable.xml", 19)
    )

    val overlayableItem2 = OverlayableItem(overlayable)

    testSerializeDeserialize(overlayableItem)
    testSerializeDeserialize(overlayableItem2)
  }

  @Test
  fun testReferenceSerialization() {
    val reference = Reference(parseResourceName("android:string/foo")!!.resourceName)

    testSerializeDeserialize(reference)

    val reference2 = Reference(parseResourceName("android:string/bar")!!.resourceName)
    reference2.isPrivate = true
    reference2.id = 0x01010000

    testSerializeDeserialize(reference2)
  }

  @Test
  fun testAttrSerialization() {
    val emptyAttribute = AttributeResource(FormatFlags.ENUM_VALUE)
    testSerializeDeserialize(emptyAttribute)

    val attributeExample = AttributeResource(FormatFlags.FLAGS_VALUE)
    attributeExample.minInt = 0
    attributeExample.maxInt = 1 shl 16

    attributeExample.symbols.add(
      AttributeResource.Symbol(
        Reference(parseNameOrFail("android:attr/bar")),
        1,
        ResValue.DataType.INT_DEC.byteValue))
    attributeExample.symbols.add(
      AttributeResource.Symbol(
        Reference(parseNameOrFail("android:attr/baz")),
        2,
        ResValue.DataType.INT_DEC.byteValue))
    attributeExample.symbols.add(
      AttributeResource.Symbol(
        Reference(parseNameOrFail("android:attr/bat")),
        4,
        ResValue.DataType.INT_DEC.byteValue))
    testSerializeDeserialize(attributeExample)
  }

  @Test
  fun testStyleSerialization() {
    val emptyStyle = Style()
    val oldPool = StringPool()
    val newPool = StringPool()

    testSerializeDeserialize(emptyStyle, newPool)
    Truth.assertThat(newPool.size()).isEqualTo(0)

    val styleExampleNoParent = Style()
    styleExampleNoParent.entries.add(
      Style.Entry(
        Reference(parseResourceName("android:string/foo")!!.resourceName),
        BasicString(oldPool.makeRef("Hello there!"))
      ))
    val keyReference = Reference(parseResourceName("android:bool/bar")!!.resourceName)
    keyReference.source = Source("res/values/style.xml", 30)
    keyReference.comment = "Whether or not the bar state can be active."
    styleExampleNoParent.entries.add(
      Style.Entry(
        keyReference,
        tryParseBool("true")
      ))

    testSerializeDeserialize(styleExampleNoParent, newPool)
    Truth.assertThat(newPool.size()).isEqualTo(1)
  }

  @Test
  fun testStyleableSerialization() {
    val emptyStyleable = Styleable()

    testSerializeDeserialize(emptyStyleable)

    val styleableExample = Styleable()
    val reference = Reference(parseResourceName("android:attr/myAttribute")!!.resourceName)
    reference.source = Source("res/values/styleable.xml", 86)
    styleableExample.entries.add(reference)
    testSerializeDeserialize(styleableExample)
  }

  @Test
  fun testArraySerialization() {
    val emptyArray = ArrayResource()
    val oldPool = StringPool()
    val newPool = StringPool()

    testSerializeDeserialize(emptyArray, newPool)
    Truth.assertThat(newPool.size()).isEqualTo(0)

    val arrayExample = ArrayResource()
    val basicString = BasicString(oldPool.makeRef("Here we go."))
    basicString.source = Source("res/values/hello.xml", 29)
    basicString.comment = "I don't know what's happening."
    arrayExample.elements.add(basicString)
    arrayExample.elements.add(Reference(parseResourceName("android:attr/hi")!!.resourceName))
    arrayExample.elements.add(Id())

    testSerializeDeserialize(arrayExample, newPool)
    Truth.assertThat(newPool.size()).isEqualTo(1)
  }

  @Test
  fun testPluralSerialization() {
    val emptyPlural = Plural()
    val oldPool = StringPool()
    val newPool = StringPool()

    testSerializeDeserialize(emptyPlural, newPool)
    Truth.assertThat(newPool.size()).isEqualTo(0)

    val pluralExample = Plural()
    val oneApple = BasicString(oldPool.makeRef("apple"))
    oneApple.source = Source("res/values/strings.xml", 29)
    pluralExample.setValue(Plural.Type.ONE, oneApple)
    val otherApples = BasicString(oldPool.makeRef("apples"))
    otherApples.source = Source("res/values/strings.xml", 30)
    otherApples.comment = "Hello world."
    pluralExample.setValue(Plural.Type.OTHER, otherApples)
    val manyApples = BasicString(oldPool.makeRef("all the apples"))
    // No source on purpose.
    pluralExample.setValue(Plural.Type.MANY, manyApples)

    testSerializeDeserialize(pluralExample, newPool)
    Truth.assertThat(newPool.size()).isEqualTo(3)
  }

  @Test
  fun testSerializeSinglePackage() {
    val table = ResourceTableBuilder()
      .setPackageId("com.app.a", 0x7f)
      .addFileReference("com.app.a:layout/main", "res/layout/main.xml", 0x7f020000)
      .addReference("com.app.a:layout/other", "com.app.a:layout/main", 0x7f020001)
      .addString("com.app.a:string/text", "hi")
      .addValue("com.app.a:id/foo", Id())
      .setSymbolState("com.app.a:bool/foo", ResourceVisibility.UNDEFINED, 0, true)
      .build()

    val publicSymbol = Visibility(level = ResourceVisibility.PUBLIC)
    Truth.assertThat(
      table.setVisibilityWithId(
        parseNameOrFail("com.app.a:layout/main"),
        publicSymbol,
        0x7f020000)).isTrue()

    val id = getValue(table, "com.app.a:id/foo") as? Id
    Truth.assertThat(id).isNotNull()

    // Make a plural.
    val plural = Plural()
    plural.setValue(Plural.Type.ONE, BasicString(table.stringPool.makeRef("one")))
    Truth.assertThat(
      table.addResource(
        parseNameOrFail("com.app.a:plurals/hey"),
        ConfigDescription(),
        "",
        plural)).isTrue()

    // Make a styled string.
    val styleString = StyleString("hello", listOf(Span("b", 0, 4)))
    Truth.assertThat(
      table.addResource(
        parseNameOrFail("com.app.a:string/styled"),
        ConfigDescription(),
        "",
        StyledString(table.stringPool.makeRef(styleString), listOf())
      )).isTrue()

    // Make a resource with different products.
    Truth.assertThat(
      table.addResource(
        parseNameOrFail("com.app.a:integer/one"),
        parse("land"),
        "",
        BinaryPrimitive(ResValue(INT_DEC, 123))
      )).isTrue()
    Truth.assertThat(
      table.addResource(
        parseNameOrFail("com.app.a:integer/one"),
        parse("land"),
        "tablet",
        BinaryPrimitive(ResValue(INT_HEX, 321))
      )).isTrue()

    // Make a reference with both resource name and resource ID.
    // The reference should point to a resource outside of this table to test that both name and id
    // get serialized.
    val expectedRef = Reference(parseNameOrFail("android:layout/main"))
    expectedRef.id = 0x01020000
    Truth.assertThat(
      table.addResource(
        parseNameOrFail("com.app.a:layout/abc"), ConfigDescription(), "", expectedRef)).isTrue()

    // Make an overlayable resource.
    val overlayableItem = OverlayableItem(
      Overlayable("OverlayableName", "overlay://theme", Source("res/values/overlayable.xml", 40)),
      source = Source("res/values/overlayable.xml", 42)
    )
    Truth.assertThat(
        table.setOverlayable(
          parseNameOrFail("com.app.a:integer/overlayable"), overlayableItem)).isTrue()

    val pbTable = serializeTableToPb(table, null)

    val newTable = ResourceTable()
    Truth.assertThat(deserializeTableFromPb(pbTable, newTable, null)).isTrue()

    val newId = getValue(newTable, "com.app.a:id/foo") as? Id
    Truth.assertThat(newId).isNotNull()
    Truth.assertThat(newId!!.weak).isEqualTo(id!!.weak)

    val newLayout = newTable.findResource(parseNameOrFail("com.app.a:layout/main"))
    Truth.assertThat(newLayout).isNotNull()
    Truth.assertThat(newLayout!!.group.visibility).isEqualTo(ResourceVisibility.PUBLIC)
    Truth.assertThat(newLayout.entry.visibility.level).isEqualTo(ResourceVisibility.PUBLIC)

    val newBool = newTable.findResource(parseNameOrFail("com.app.a:bool/foo"))
    Truth.assertThat(newBool).isNotNull()
    Truth.assertThat(newBool!!.entry.visibility.level).isEqualTo(ResourceVisibility.UNDEFINED)
    Truth.assertThat(newBool.entry.allowNew).isNotNull()

    // Find the product dependent values.
    val newPrimitive =
      getValue(newTable, "com.app.a:integer/one", parse("land"), "tablet") as? BinaryPrimitive
    Truth.assertThat(newPrimitive).isNotNull()
    Truth.assertThat(newPrimitive!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_HEX)
    Truth.assertThat(newPrimitive.resValue.data).isEqualTo(321)

    val newPrimitive2 =
      getValue(newTable, "com.app.a:integer/one", parse("land")) as? BinaryPrimitive
    Truth.assertThat(newPrimitive2).isNotNull()
    Truth.assertThat(newPrimitive2!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_DEC)
    Truth.assertThat(newPrimitive2.resValue.data).isEqualTo(123)

    val newActualRef = getValue(newTable, "com.app.a:layout/abc") as? Reference
    Truth.assertThat(newActualRef).isNotNull()
    Truth.assertThat(newActualRef!!.name).isNotNull()
    Truth.assertThat(newActualRef.id).isNotNull()
    Truth.assertThat(newActualRef).isEqualTo(expectedRef)

    val newActualFileRef = getValue(newTable, "com.app.a:layout/main") as? FileReference
    Truth.assertThat(newActualFileRef).isNotNull()

    val newStyledString = getValue(newTable, "com.app.a:string/styled") as? StyledString
    Truth.assertThat(newStyledString).isNotNull()
    Truth.assertThat(newStyledString!!.ref.value()).isEqualTo("hello")
    Truth.assertThat(newStyledString.ref.spans()).hasSize(1)
    val newSpan = newStyledString.ref.spans()[0]
    Truth.assertThat(newSpan.name.value()).isEqualTo("b")
    Truth.assertThat(newSpan.firstChar).isEqualTo(0)
    Truth.assertThat(newSpan.lastChar).isEqualTo(4)

    val newOverlayable = newTable.findResource(parseNameOrFail("com.app.a:integer/overlayable"))
    Truth.assertThat(newOverlayable).isNotNull()
    val actualOverlayableItem = newOverlayable!!.entry.overlayable
    Truth.assertThat(actualOverlayableItem).isNotNull()
    Truth.assertThat(actualOverlayableItem!!.overlayable.name).isEqualTo("OverlayableName")
    Truth.assertThat(actualOverlayableItem.overlayable.actor).isEqualTo("overlay://theme")
    Truth.assertThat(actualOverlayableItem.overlayable.source)
      .isEqualTo(Source("res/values/overlayable.xml", 40))
    Truth.assertThat(actualOverlayableItem.policies).isEqualTo(OverlayableItem.Policy.NONE)
    Truth.assertThat(actualOverlayableItem.source)
      .isEqualTo(Source("res/values/overlayable.xml", 42))
  }

  @Test
  fun serializeAndDeserializeTableOfPrimitives() {
    val table = ResourceTableBuilder()
      .addValue("android:bool/boolean_true", tryParseBool("True")!!)
      .addValue("android:bool/boolean_false", tryParseBool("False")!!)
      .addValue("android:color/color_rgb8", tryParseColor("#AABBCC")!!)
      .addValue("android:color/color_argb8", tryParseColor("#11223344")!!)
      .addValue("android:color/color_rgb4", tryParseColor("#DEF")!!)
      .addValue("android:color/color_argb4", tryParseColor("#5678")!!)
      .addValue("android:integer/integer_444", tryParseInt("444")!!)
      .addValue("android:integer/integer_neg333", tryParseInt("-333")!!)
      .addValue("android:integer/hex_int_abcd", tryParseInt("0xABCD")!!)
      .addValue("android:dimen/dimen_1_39mm", tryParseFloat("1.39mm")!!)
      .addValue("android:fraction/fraction_27", tryParseFloat("27%")!!)
      .addValue("android:integer/null", makeEmpty())
      .build()

    val pbTable = serializeTableToPb(table, null)

    val newTable = ResourceTable()
    Truth.assertThat(deserializeTableFromPb(pbTable, newTable, null)).isTrue()

    var binPrimitive = getValue(newTable, "android:bool/boolean_true") as? BinaryPrimitive
    Truth.assertThat(binPrimitive).isNotNull()
    Truth.assertThat(binPrimitive!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_BOOLEAN)
    Truth.assertThat(binPrimitive.resValue.data).isNotEqualTo(0)

    binPrimitive = getValue(newTable, "android:bool/boolean_false") as? BinaryPrimitive
    Truth.assertThat(binPrimitive).isNotNull()
    Truth.assertThat(binPrimitive!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_BOOLEAN)
    Truth.assertThat(binPrimitive.resValue.data).isEqualTo(0)

    binPrimitive = getValue(newTable, "android:color/color_rgb8") as? BinaryPrimitive
    Truth.assertThat(binPrimitive).isNotNull()
    Truth.assertThat(binPrimitive!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_COLOR_RGB8)
    Truth.assertThat(binPrimitive.resValue.data).isEqualTo(0xffaabbcc.toInt())

    binPrimitive = getValue(newTable, "android:color/color_argb8") as? BinaryPrimitive
    Truth.assertThat(binPrimitive).isNotNull()
    Truth.assertThat(binPrimitive!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_COLOR_ARGB8)
    Truth.assertThat(binPrimitive.resValue.data).isEqualTo(0x11223344)

    binPrimitive = getValue(newTable, "android:color/color_rgb4") as? BinaryPrimitive
    Truth.assertThat(binPrimitive).isNotNull()
    Truth.assertThat(binPrimitive!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_COLOR_RGB4)
    Truth.assertThat(binPrimitive.resValue.data).isEqualTo(0xffddeeff.toInt())

    binPrimitive = getValue(newTable, "android:color/color_argb4") as? BinaryPrimitive
    Truth.assertThat(binPrimitive).isNotNull()
    Truth.assertThat(binPrimitive!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_COLOR_ARGB4)
    Truth.assertThat(binPrimitive.resValue.data).isEqualTo(0x55667788)

    binPrimitive = getValue(newTable, "android:integer/integer_444") as? BinaryPrimitive
    Truth.assertThat(binPrimitive).isNotNull()
    Truth.assertThat(binPrimitive!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_DEC)
    Truth.assertThat(binPrimitive.resValue.data).isEqualTo(444)

    binPrimitive = getValue(newTable, "android:integer/integer_neg333") as? BinaryPrimitive
    Truth.assertThat(binPrimitive).isNotNull()
    Truth.assertThat(binPrimitive!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_DEC)
    Truth.assertThat(binPrimitive.resValue.data).isEqualTo(-333)

    binPrimitive = getValue(newTable, "android:integer/hex_int_abcd") as? BinaryPrimitive
    Truth.assertThat(binPrimitive).isNotNull()
    Truth.assertThat(binPrimitive!!.resValue.dataType).isEqualTo(ResValue.DataType.INT_HEX)
    Truth.assertThat(binPrimitive.resValue.data).isEqualTo(0xabcd)

    binPrimitive = getValue(newTable, "android:dimen/dimen_1_39mm") as? BinaryPrimitive
    Truth.assertThat(binPrimitive).isNotNull()
    Truth.assertThat(binPrimitive!!.resValue.dataType).isEqualTo(ResValue.DataType.DIMENSION)
    Truth.assertThat(binPrimitive.resValue.data).isEqualTo(tryParseFloat("1.39mm")!!.resValue.data)

    binPrimitive = getValue(newTable, "android:fraction/fraction_27") as? BinaryPrimitive
    Truth.assertThat(binPrimitive).isNotNull()
    Truth.assertThat(binPrimitive!!.resValue.dataType).isEqualTo(ResValue.DataType.FRACTION)
    Truth.assertThat(binPrimitive.resValue.data).isEqualTo(tryParseFloat("27%")!!.resValue.data)

    binPrimitive = getValue(newTable, "android:integer/null") as? BinaryPrimitive
    Truth.assertThat(binPrimitive!!.resValue.dataType).isEqualTo(ResValue.DataType.NULL)
    Truth.assertThat(binPrimitive.resValue.data).isEqualTo(ResValue.NullFormat.EMPTY)
  }

  @Test
  fun serializeAndDeserializeOverlayable() {
    val foo = OverlayableItem(
      Overlayable("CustomizableResources", "overlay://customization", Source.EMPTY),
      OverlayableItem.Policy.SYSTEM or OverlayableItem.Policy.PRODUCT)
    val bar = OverlayableItem(
      Overlayable("TaskBar", "overlay://theme", Source.EMPTY),
      OverlayableItem.Policy.PUBLIC or OverlayableItem.Policy.VENDOR)
    val baz = OverlayableItem(
      Overlayable("FrontPack", "overlay://theme", Source.EMPTY),
      OverlayableItem.Policy.PUBLIC)
    val boz = OverlayableItem(
      Overlayable("IconPack", "overlay://theme", Source.EMPTY),
      OverlayableItem.Policy.SIGNATURE or OverlayableItem.Policy.ODM or OverlayableItem.Policy.OEM)
    val biz = OverlayableItem(
      Overlayable("Other", "overlay://customization", Source.EMPTY),
      comment = "comment")
    val table = ResourceTableBuilder()
      .setOverlayable("com.app.a:bool/foo", foo)
      .setOverlayable("com.app.a:bool/bar", bar)
      .setOverlayable("com.app.a:bool/baz", baz)
      .setOverlayable("com.app.a:bool/boz", boz)
      .setOverlayable("com.app.a:bool/biz", biz)
      .addValue("com.app.a:bool/fiz", tryParseBool("true")!!)
      .build()

    val pbTable = serializeTableToPb(table, null)

    val newTable = ResourceTable()
    Truth.assertThat(deserializeTableFromPb(pbTable, newTable, null)).isTrue()

    var searchResult = newTable.findResource(parseNameOrFail("com.app.a:bool/foo"))
    Truth.assertThat(searchResult).isNotNull()
    Truth.assertThat(searchResult!!.entry.overlayable).isNotNull()
    var item = searchResult.entry.overlayable!!
    Truth.assertThat(item.overlayable.name).isEqualTo("CustomizableResources")
    Truth.assertThat(item.overlayable.actor).isEqualTo("overlay://customization")
    Truth.assertThat(item.policies)
      .isEqualTo(OverlayableItem.Policy.SYSTEM or OverlayableItem.Policy.PRODUCT)

    searchResult = newTable.findResource(parseNameOrFail("com.app.a:bool/bar"))
    Truth.assertThat(searchResult).isNotNull()
    Truth.assertThat(searchResult!!.entry.overlayable).isNotNull()
    item = searchResult.entry.overlayable!!
    Truth.assertThat(item.overlayable.name).isEqualTo("TaskBar")
    Truth.assertThat(item.overlayable.actor).isEqualTo("overlay://theme")
    Truth.assertThat(item.policies)
      .isEqualTo(OverlayableItem.Policy.PUBLIC or OverlayableItem.Policy.VENDOR)

    searchResult = newTable.findResource(parseNameOrFail("com.app.a:bool/baz"))
    Truth.assertThat(searchResult).isNotNull()
    Truth.assertThat(searchResult!!.entry.overlayable).isNotNull()
    item = searchResult.entry.overlayable!!
    Truth.assertThat(item.overlayable.name).isEqualTo("FrontPack")
    Truth.assertThat(item.overlayable.actor).isEqualTo("overlay://theme")
    Truth.assertThat(item.policies).isEqualTo(OverlayableItem.Policy.PUBLIC)

    searchResult = newTable.findResource(parseNameOrFail("com.app.a:bool/boz"))
    Truth.assertThat(searchResult).isNotNull()
    Truth.assertThat(searchResult!!.entry.overlayable).isNotNull()
    item = searchResult.entry.overlayable!!
    Truth.assertThat(item.overlayable.name).isEqualTo("IconPack")
    Truth.assertThat(item.overlayable.actor).isEqualTo("overlay://theme")
    Truth.assertThat(item.policies)
      .isEqualTo(
        OverlayableItem.Policy.SIGNATURE or
          OverlayableItem.Policy.ODM or
          OverlayableItem.Policy.OEM)

    searchResult = newTable.findResource(parseNameOrFail("com.app.a:bool/biz"))
    Truth.assertThat(searchResult).isNotNull()
    Truth.assertThat(searchResult!!.entry.overlayable).isNotNull()
    item = searchResult.entry.overlayable!!
    Truth.assertThat(item.overlayable.name).isEqualTo("Other")
    Truth.assertThat(item.overlayable.actor).isEqualTo("overlay://customization")
    Truth.assertThat(item.policies).isEqualTo(OverlayableItem.Policy.NONE)
    Truth.assertThat(item.comment).isEqualTo("comment")

    searchResult = newTable.findResource(parseNameOrFail("com.app.a:bool/fiz"))
    Truth.assertThat(searchResult).isNotNull()
    Truth.assertThat(searchResult!!.entry.overlayable).isNull()
  }
}
