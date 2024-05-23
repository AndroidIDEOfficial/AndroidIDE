package com.android.aaptcompiler.testutils

import com.android.aaptcompiler.AllowNew
import com.android.aaptcompiler.BasicString
import com.android.aaptcompiler.ConfigDescription
import com.android.aaptcompiler.FileReference
import com.android.aaptcompiler.Id
import com.android.aaptcompiler.OverlayableItem
import com.android.aaptcompiler.Reference
import com.android.aaptcompiler.ResourceName
import com.android.aaptcompiler.ResourceTable
import com.android.aaptcompiler.Source
import com.android.aaptcompiler.Value
import com.android.aaptcompiler.Visibility
import com.android.aaptcompiler.parseResourceName
import com.itsaky.androidide.layoutlib.resources.ResourceVisibility
import com.google.common.truth.Truth
import java.io.File

fun parseNameOrFail(name: String): ResourceName {
  val resNameInfo = parseResourceName(name)
  Truth.assertThat(resNameInfo).isNotNull()
  return resNameInfo!!.resourceName
}

class ResourceTableBuilder {
  private var table = ResourceTable()

  fun build(): ResourceTable {
    val result = table
    table = ResourceTable()
    return result
  }

  fun setPackageId(packageName: String, id: Byte): ResourceTableBuilder {
    val resourcePackage = table.createPackage(packageName, id)
    Truth.assertThat(resourcePackage).isNotNull()
    return this
  }

  fun addSimple(name: String, id: Int, config: ConfigDescription = ConfigDescription()) =
    addValue(name, Id(), id, config)

  fun addReference(name: String, ref: String, id: Int = 0) =
    addValue(name, Reference(parseNameOrFail(ref)), id)

  fun addString(
    name: String, value: String, id: Int = 0, config: ConfigDescription = ConfigDescription()) =
    addValue(name, BasicString(table.stringPool.makeRef(value)), id, config)

  fun addFileReference(
    name: String,
    path: String,
    id: Int = 0,
    config: ConfigDescription = ConfigDescription(),
    file: File? = null): ResourceTableBuilder {

    val fileRef = FileReference(table.stringPool.makeRef(path))
    fileRef.file = file
    return addValue(name, fileRef, id, config)
  }

  fun addValue(
    name: String,
    value: Value,
    id: Int = 0,
    config: ConfigDescription = ConfigDescription()): ResourceTableBuilder {

    Truth.assertThat(
      table.addResourceWithIdMangled(
        parseNameOrFail(name),
        id,
        config,
        "",
        value)).isTrue()
    return this
  }

  fun setSymbolState(
    name: String,
    level: ResourceVisibility,
    id: Int,
    allowNew: Boolean): ResourceTableBuilder {

    val resName = parseNameOrFail(name)
    val visibility = Visibility(level = level)
    Truth.assertThat(
      table.setVisibilityWithIdMangled(
        resName,
        visibility,
        id)).isTrue()
    if (allowNew) {
      Truth.assertThat(
        table.setAllowNewMangled(
          resName,
          AllowNew(Source(""), ""))).isTrue()
    }
    return this
  }

  fun setOverlayable(name: String, overlayable: OverlayableItem): ResourceTableBuilder {
    Truth.assertThat(
      table.setOverlayable(
        parseNameOrFail(name),
        overlayable)).isTrue()
    return this
  }
}