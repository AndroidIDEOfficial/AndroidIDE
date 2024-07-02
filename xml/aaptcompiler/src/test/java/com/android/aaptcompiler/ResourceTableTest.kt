package com.android.aaptcompiler

import com.android.aapt.Resources.Attribute.FormatFlags
import com.android.aaptcompiler.AaptResourceType.ATTR
import com.android.aaptcompiler.AaptResourceType.ID
import com.android.aaptcompiler.AaptResourceType.STYLEABLE
import com.android.aaptcompiler.android.ResTableConfig
import com.android.aaptcompiler.android.ResValue
import com.android.aaptcompiler.android.ResValue.DataType.INT_BOOLEAN
import com.itsaky.androidide.layoutlib.resources.ResourceVisibility
import com.google.common.truth.Truth
import org.junit.Test

internal fun getValue(
  table: ResourceTable,
  resName: String,
  config: ConfigDescription = ConfigDescription(),
  productName: String = ""): Value? {

  val name = parseResourceName(resName)!!.resourceName

  val result = table.findResource(name)
  result ?: return null

  val configValue = result.entry.findValue(config, productName)
  configValue ?: return null

  return configValue.value
}

class ResourceTableTest {

  @Test
  fun failToAddResourceWithBadName() {
    val table = ResourceTable()

    val id = Id()
    id.source = Source("test.xml", 21)

    Truth.assertThat(
      table.addResource(
        ResourceName("android", ID, "hey,there"),
        ConfigDescription(),
        "",
        id)).isFalse()

    Truth.assertThat(
      table.addResource(
        ResourceName("android", ID, "hey:there"),
        ConfigDescription(),
        "",
        id)).isFalse()
  }

  @Test
  fun testAddResourceWithMangledNameHandlesWeirdNames() {
    val table = ResourceTable()
    val id = Id()
    id.source = Source ("test.xml", 23)

    Truth.assertThat(
      table.addResourceMangled(
        parseResourceName("android:id/heythere       ")!!.resourceName,
        ConfigDescription(),
        "",
        id)).isTrue()
  }

  @Test
  fun testAddResource() {
    val table = ResourceTable()

    val id = Id()
    id.source = Source("test.xml", 25)

    Truth.assertThat(
      table.addResource(
        parseResourceName("android:attr/id")!!.resourceName,
        ConfigDescription(),
        "",
        id)).isTrue()

    Truth.assertThat(getValue(table, "android:attr/id")).isNotNull()
  }

  @Test
  fun testStyleables() {
    // Styleable children are nested deep under ResourceEntry, so the ResourceGroup.getStyleable()
    // method was introduce. This test checks that it works correctly.
    val table = ResourceTable()

    val styleableOne = Styleable()
    styleableOne.entries.add(0, Reference(ResourceName("", ATTR, "child_one")))
    styleableOne.entries.add(1, Reference(ResourceName("android", ATTR, "child_two")))

    Truth.assertThat(
            table.addResource(
                    ResourceName("", STYLEABLE, "styleable_parent_one"),
                    ConfigDescription(),
                    "",
                    styleableOne)).isTrue()

    val styleableTwo = Styleable()
    styleableTwo.entries.add(0, Reference(ResourceName("", ATTR, "child_one")))
    styleableTwo.entries.add(1, Reference(ResourceName("android", ATTR, "child_three")))

    Truth.assertThat(
            table.addResource(
                    ResourceName("", STYLEABLE, "styleable_parent_two"),
                    ConfigDescription(),
                    "",
                    styleableTwo)).isTrue()

    Truth.assertThat(getValue(table, "styleable/styleable_parent_one")).isNotNull()
    Truth.assertThat(getValue(table, "styleable/styleable_parent_two")).isNotNull()

    Truth.assertThat(table.packages).hasSize(1)

    Truth.assertThat(table.packages[0].groups).hasSize(1)
    val styleableGroup = table.packages[0].groups[0]

    Truth.assertThat(styleableGroup.entries).hasSize(2)

    val styleableEntryOne = styleableGroup.entries.entries.toList()[0]
    Truth.assertThat(styleableEntryOne).isNotNull()
    Truth.assertThat(styleableEntryOne.value.values.first().name).isEqualTo("styleable_parent_one")
    val children = table.packages[0].groups[0].getStyleable(styleableEntryOne).entries
    Truth.assertThat(children).hasSize(2)
    Truth.assertThat(children[0].name.toString()).isEqualTo("attr/child_one")
    Truth.assertThat(children[1].name.toString()).isEqualTo("android:attr/child_two")

    val styleableEntryTwo = styleableGroup.entries.entries.toList()[1]
    Truth.assertThat(styleableEntryTwo).isNotNull()
    Truth.assertThat(styleableEntryTwo.value.values.first().name).isEqualTo("styleable_parent_two")
    val children_two = table.packages[0].groups[0].getStyleable(styleableEntryTwo).entries
    Truth.assertThat(children_two).hasSize(2)
    Truth.assertThat(children_two[0].name.toString()).isEqualTo("attr/child_one")
    Truth.assertThat(children_two[1].name.toString()).isEqualTo("android:attr/child_three")
  }

  @Test
  fun testAddMultipleResource() {
    val table = ResourceTable()
    val config = ConfigDescription()
    val languageConfig =
      ConfigDescription(ResTableConfig(language = byteArrayOf('p'.code.toByte(), 'l'.code.toByte())))

    val id1 = Id()
    id1.source = Source("test/path/file.xml", 10)
    val id2 = Id()
    id2.source = Source("test/path/file.xml", 12)
    val id3 = Id()
    id3.source = Source("test/path/file.xml", 14)
    val id4 = Id()
    id4.source = Source ("test/path/file.xml", 20)

    Truth.assertThat(
      table.addResource(
        parseResourceName("android:attr/layout_width")!!.resourceName,
        config,
        "",
        id1)).isTrue()

    Truth.assertThat(
      table.addResource(
        parseResourceName("android:attr/id")!!.resourceName,
          config,
          "",
          id2)).isTrue()

    Truth.assertThat(
      table.addResource(
        parseResourceName("android:string/ok")!!.resourceName,
        config,
        "",
        id3)).isTrue()

    Truth.assertThat(
      table.addResource(
        parseResourceName("android:string/ok")!!.resourceName,
        languageConfig,
        "",
        id4)).isTrue()

    Truth.assertThat(getValue(table, "android:attr/layout_width")).isNotNull()
    Truth.assertThat(getValue(table, "android:attr/id")).isNotNull()
    Truth.assertThat(getValue(table, "android:string/ok")).isNotNull()
    Truth.assertThat(getValue(table, "android:string/ok", languageConfig)).isNotNull()
  }

  @Test
  fun testOverrideWeakResource() {
    val table = ResourceTable()
    val config = ConfigDescription()

    val weakAttr = AttributeResource()
    weakAttr.weak = true
    val strongAttr = AttributeResource()
    strongAttr.weak = false

    Truth.assertThat(
      table.addResource(
        parseResourceName("android:attr/foo")!!.resourceName,
        config,
        "",
        weakAttr)).isTrue()

    var attr = getValue(table, "android:attr/foo") as? AttributeResource
    Truth.assertThat(attr).isNotNull()
    Truth.assertThat(attr!!.weak).isTrue()

    Truth.assertThat(
      table.addResource(
        parseResourceName("android:attr/foo")!!.resourceName,
        config,
        "",
        strongAttr)).isTrue()

    attr = getValue(table, "android:attr/foo") as? AttributeResource
    Truth.assertThat(attr).isNotNull()
    Truth.assertThat(attr!!.weak).isFalse()
  }

  @Test
  fun testCompatibleDuplicateAttributesAreAllowed() {
    val table = ResourceTable()
    val config = ConfigDescription()

    val name = parseResourceName("android:attr/foo")!!.resourceName
    val weak1 = AttributeResource(FormatFlags.STRING_VALUE)
    weak1.weak = true
    val weak2 = AttributeResource(
      FormatFlags.STRING_VALUE or
        FormatFlags.REFERENCE_VALUE)
    weak2.weak = true

    Truth.assertThat(table.addResource(name, config, "", weak1)).isTrue()
    Truth.assertThat(table.addResource(name, config, "", weak2)).isTrue()
  }

  @Test
  fun testProductVaryingValues() {
    val table = ResourceTable()
    val config = parse("land")

    val name = parseResourceName("android:string/foo")!!.resourceName

    Truth.assertThat(table.addResource(name, config, "tablet", Id())).isTrue()
    Truth.assertThat(table.addResource(name, config, "phone", Id())).isTrue()

    Truth.assertThat(getValue(table, "android:string/foo", parse("land"), "tablet")).isNotNull()
    Truth.assertThat(getValue(table, "android:string/foo", parse("land"), "phone")).isNotNull()

    val searchResult = table.findResource(parseResourceName("android:string/foo")!!.resourceName)
    Truth.assertThat(searchResult).isNotNull()

    val values = searchResult!!.entry.values
    values.sortBy { it.product }
    Truth.assertThat(values).hasSize(2)
    Truth.assertThat(values[0].product).isEqualTo("phone")
    Truth.assertThat(values[1].product).isEqualTo("tablet")
  }

  private fun visibilityOfResourceTest(
    table: ResourceTable,
    name: ResourceName,
    expected: ResourceVisibility,
    expectedComment: String) {

    val result = table.findResource(name)
    Truth.assertThat(result).isNotNull()

    val visibility = result!!.entry.visibility
    Truth.assertThat(visibility.level).isEqualTo(expected)
    Truth.assertThat(visibility.comment).isEqualTo(expectedComment)
  }

  @Test
  fun testSetVisibility() {
    val table = ResourceTable()
    val name = parseResourceName("android:string/foo")!!.resourceName

    var visibility = Visibility(comment = "private", level = ResourceVisibility.PRIVATE)
    Truth.assertThat(table.setVisibility(name, visibility)).isTrue()
    visibilityOfResourceTest(table, name, ResourceVisibility.PRIVATE, "private")

    // Undefined visibility will not override defined visibility.
    visibility = Visibility(comment = "undefined", level = ResourceVisibility.UNDEFINED)
    Truth.assertThat(table.setVisibility(name, visibility)).isTrue()
    visibilityOfResourceTest(table, name, ResourceVisibility.PRIVATE, "private")

    // Public visibility clashes with previously defined private visibility.
    visibility = Visibility(comment = "public", level = ResourceVisibility.PUBLIC)
    Truth.assertThat(table.setVisibility(name, visibility)).isFalse()

  }

  @Test
  fun testSetVisibilityWithId() {
    val table = ResourceTable()

    var name = parseResourceName("android:string/foo")!!.resourceName
    val visibility1 = Visibility(comment = "default", level = ResourceVisibility.UNDEFINED)
    Truth.assertThat(table.setVisibilityWithId(name, visibility1, 0)).isTrue()
    visibilityOfResourceTest(table, name, ResourceVisibility.UNDEFINED, "default")

    val visibility2 = Visibility(comment = "public", level = ResourceVisibility.PUBLIC)
    Truth.assertThat(table.setVisibilityWithId(name, visibility2, 0x01020000)).isTrue()
    visibilityOfResourceTest(table, name, ResourceVisibility.PUBLIC, "public")

    name = parseResourceName("android:string/foobar")!!.resourceName
    val visibility3 = Visibility(comment = "private2", level = ResourceVisibility.PRIVATE)
    Truth.assertThat(table.setVisibilityWithId(name, visibility3, 0)).isTrue()
    visibilityOfResourceTest(table, name, ResourceVisibility.PRIVATE, "private2")
  }

  @Test
  fun testSetAllowNew() {
    val table = ResourceTable()
    val name = parseResourceName("android:string/foo")!!.resourceName

    var allowNew = AllowNew(Source(""), "first")
    Truth.assertThat(table.setAllowNew(name, allowNew)).isTrue()
    var result = table.findResource(name)
    Truth.assertThat(result).isNotNull()
    Truth.assertThat(result!!.entry.allowNew).isNotNull()
    Truth.assertThat(result.entry.allowNew!!.comment).isEqualTo("first")

    allowNew = AllowNew(Source(""), "second")
    Truth.assertThat(table.setAllowNew(name, allowNew)).isTrue()
    result = table.findResource(name)
    Truth.assertThat(result).isNotNull()
    Truth.assertThat(result!!.entry.allowNew).isNotNull()
    Truth.assertThat(result.entry.allowNew!!.comment).isEqualTo("second")
  }

  @Test
  fun testSetOverlayable() {
    val table = ResourceTable()
    val overlayable =
      Overlayable("Name", "overlay://theme", Source("res/values/overlayable.xml", 40))

    val overlayableItem = OverlayableItem(
      overlayable,
      OverlayableItem.Policy.PRODUCT or OverlayableItem.Policy.VENDOR,
      "comment",
      Source("res/values/overlayable.xml", 42)
    )

    val name = parseResourceName("android:string/foo")!!.resourceName
    table.setOverlayable(name, overlayableItem)

    val result = table.findResource(name)
    Truth.assertThat(result).isNotNull()
    Truth.assertThat(result!!.entry.overlayable).isNotNull()

    val entryOverlayable = result.entry.overlayable!!
    Truth.assertThat(entryOverlayable.overlayable.name).isEqualTo("Name")
    Truth.assertThat(entryOverlayable.overlayable.actor).isEqualTo("overlay://theme")
    Truth.assertThat(entryOverlayable.overlayable.source.path)
      .isEqualTo("res/values/overlayable.xml")
    Truth.assertThat(entryOverlayable.overlayable.source.line).isEqualTo(40)
    Truth.assertThat(entryOverlayable.policies)
      .isEqualTo(OverlayableItem.Policy.PRODUCT or OverlayableItem.Policy.VENDOR)
    Truth.assertThat(entryOverlayable.comment).isEqualTo("comment")
    Truth.assertThat(entryOverlayable.source.path).isEqualTo("res/values/overlayable.xml")
    Truth.assertThat(entryOverlayable.source.line).isEqualTo(42)
  }

  @Test
  fun testSetMultipleOverlayableResources() {
    val table = ResourceTable()

    val foo = parseResourceName("android:string/foo")!!.resourceName
    val overlayable = Overlayable("Name", "overlay://theme", Source(""))
    val overlayableItem = OverlayableItem(overlayable, OverlayableItem.Policy.PRODUCT)
    Truth.assertThat(table.setOverlayable(foo, overlayableItem)).isTrue()

    val bar = parseResourceName("android:string/bar")!!.resourceName
    val overlayableItem2 = OverlayableItem(overlayable, OverlayableItem.Policy.PRODUCT)
    Truth.assertThat(table.setOverlayable(bar, overlayableItem2)).isTrue()

    val baz = parseResourceName("android:string/baz")!!.resourceName
    val overlayableItem3 = OverlayableItem(overlayable, OverlayableItem.Policy.VENDOR)
    Truth.assertThat(table.setOverlayable(baz, overlayableItem3)).isTrue()
  }

  @Test
  fun testSetSameOverlayableDifferentNames() {
    val table = ResourceTable()

    val foo = parseResourceName("android:string/foo")!!.resourceName
    val overlayable = Overlayable("Name", "overlay://theme", Source(""))
    val overlayableItem = OverlayableItem(overlayable, OverlayableItem.Policy.PRODUCT)
    Truth.assertThat(table.setOverlayable(foo, overlayableItem)).isTrue()

    val bar = parseResourceName("android:string/bar")!!.resourceName
    val overlayable2 = Overlayable("Name2", "overlay://theme", Source(""))
    val overlayableItem2 = OverlayableItem(overlayable2, OverlayableItem.Policy.PRODUCT)
    Truth.assertThat(table.setOverlayable(bar, overlayableItem2)).isTrue()
  }

  @Test
  fun failToSetOverlayablesSameResource() {
    val table = ResourceTable()

    val name = parseResourceName("android:string/foo")!!.resourceName

    val overlayable = Overlayable("Name", "overlay://theme", Source(""))
    val overlayableItem = OverlayableItem(overlayable)
    Truth.assertThat(table.setOverlayable(name, overlayableItem)).isTrue()

    val overlayableItem2 = OverlayableItem(overlayable)
    Truth.assertThat(table.setOverlayable(name, overlayableItem2)).isFalse()
  }

  @Test
  fun failToSetDifferentOverlayablesSameResource() {
    val table = ResourceTable()

    val name = parseResourceName("android:string/foo")!!.resourceName

    val overlayable = Overlayable("Name", "overlay://theme", Source(""))
    val overlayableItem = OverlayableItem(overlayable)
    Truth.assertThat(table.setOverlayable(name, overlayableItem)).isTrue()

    val overlayable2 = Overlayable("Other", "overlay://theme", Source(""))
    val overlayableItem2 = OverlayableItem(overlayable2)
    Truth.assertThat(table.setOverlayable(name, overlayableItem2)).isFalse()
  }

  @Test
  fun testAllowDuplicateResourceNames() {
    val table = ResourceTable(false)

    val name = parseResourceName("android:bool/foo")!!.resourceName

    Truth.assertThat(
      table.addResourceWithId(
        name,
        0x7f0100ff,
        ConfigDescription(),
        "",
        BinaryPrimitive(ResValue(INT_BOOLEAN, 0))
      )).isTrue()
    Truth.assertThat(
      table.addResourceWithId(
        name,
        0x7f010100,
        ConfigDescription(),
        "",
        BinaryPrimitive(ResValue(INT_BOOLEAN, 1))
      )).isTrue()

    Truth.assertThat(
      table.setVisibilityWithId(
        name, Visibility(level = ResourceVisibility.PUBLIC), 0x7f0100ff)).isTrue()

    Truth.assertThat(
      table.setVisibilityWithId(
        name, Visibility(level = ResourceVisibility.PRIVATE), 0x7f010100)).isTrue()

    val tablePackage = table.findPackageById(0x7f)
    Truth.assertThat(tablePackage).isNotNull()
    val tableGroup = tablePackage!!.findGroup(AaptResourceType.BOOL)
    Truth.assertThat(tableGroup).isNotNull()

    val entry1 = tableGroup!!.findEntry("foo", 0x00ff)
    Truth.assertThat(entry1).isNotNull()
    Truth.assertThat(entry1!!.id).isEqualTo(0x00ff)
    Truth.assertThat(entry1.values).hasSize(1)
    val value1 = entry1.values[0].value as? BinaryPrimitive
    Truth.assertThat(value1).isNotNull()
    Truth.assertThat(value1!!.resValue.data).isEqualTo(0)
    Truth.assertThat(entry1.visibility.level).isEqualTo(ResourceVisibility.PUBLIC)

    val entry2 = tableGroup.findEntry("foo", 0x0100)
    Truth.assertThat(entry2).isNotNull()
    Truth.assertThat(entry2!!.id).isEqualTo(0x0100)
    Truth.assertThat(entry2.values).hasSize(1)
    val value2 = entry2.values[0].value as? BinaryPrimitive
    Truth.assertThat(value2).isNotNull()
    Truth.assertThat(value2!!.resValue.data).isEqualTo(1)
    Truth.assertThat(entry2.visibility.level).isEqualTo(ResourceVisibility.PRIVATE)
  }
}
