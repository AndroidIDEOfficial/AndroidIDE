package com.android.aaptcompiler.proto

import android.aapt.pb.internal.ResourcesInternal
import com.android.aapt.ConfigurationOuterClass
import com.android.aapt.Resources
import com.android.aaptcompiler.ArrayResource
import com.android.aaptcompiler.AttributeResource
import com.android.aaptcompiler.BasicString
import com.android.aaptcompiler.BinaryPrimitive
import com.android.aaptcompiler.ConfigDescription
import com.android.aaptcompiler.FileReference
import com.android.aaptcompiler.Id
import com.android.aaptcompiler.Item
import com.android.aaptcompiler.Macro
import com.android.aaptcompiler.Overlayable
import com.android.aaptcompiler.OverlayableItem
import com.android.aaptcompiler.Plural
import com.android.aaptcompiler.RawString
import com.android.aaptcompiler.Reference
import com.android.aaptcompiler.ResourceCompilationException
import com.android.aaptcompiler.ResourceFile
import com.android.aaptcompiler.ResourceName
import com.android.aaptcompiler.ResourceTable
import com.android.aaptcompiler.Source
import com.android.aaptcompiler.StringPool
import com.android.aaptcompiler.Style
import com.android.aaptcompiler.Styleable
import com.android.aaptcompiler.StyledString
import com.android.aaptcompiler.ToolFingerprint
import com.android.aaptcompiler.Value
import com.android.aaptcompiler.android.ResTableConfig
import com.android.aaptcompiler.android.ResValue
import com.android.aaptcompiler.blameSource
import com.android.aaptcompiler.buffer.BigBuffer
import com.android.utils.ILogger
import com.google.protobuf.ByteString
import com.itsaky.androidide.layoutlib.resources.ResourceVisibility

internal fun serializePoolToPb(pool: StringPool, logger: ILogger?): Resources.StringPool {
  val buffer = BigBuffer(1024)
  try {
    pool.flattenUtf8(buffer, logger)
  } catch (e: Exception) {
    throw ResourceCompilationException("Failed to flatten utf-8 string.", e)
  }

  val builder = Resources.StringPool.newBuilder()
  builder.setData(ByteString.copyFrom(buffer.toBytes()))

  return builder.build()
}

internal fun serializeSourceToPb(source: Source, sourcePool: StringPool): Resources.Source {

  val ref = sourcePool.makeRef(source.path)
  val sourceBuilder = Resources.Source.newBuilder()

  sourceBuilder.setPathIdx(ref.index())
  if (source.line != null) {
    sourceBuilder.setPosition(
      Resources.SourcePosition.newBuilder().setLineNumber(source.line!!).build()
    )
  }
  return sourceBuilder.build()
}

fun serializeTableToPb(table: ResourceTable, logger: ILogger? = null): Resources.ResourceTable {
  val tableBuilder = Resources.ResourceTable.newBuilder()
  val sourcePool = StringPool()


  tableBuilder.addToolFingerprint(
    Resources.ToolFingerprint.newBuilder()
      .setTool(ToolFingerprint.TOOL_NAME)
      .setVersion(ToolFingerprint.FINGERPRINT)
      .build()
  )
  val overlayables = mutableListOf<Overlayable>()
  for (resourcePackage in table.packages) {
    val packageBuilder = Resources.Package.newBuilder()

    val packageId = resourcePackage.id
    if (packageId != null) {
      packageBuilder.setPackageId(
        Resources.PackageId.newBuilder()
          .setId(packageId.toInt())
          .build()
      )
    }
    packageBuilder.setPackageName(resourcePackage.name)

    for (resourceGroup in resourcePackage.groups) {
      val groupBuilder = Resources.Type.newBuilder()

      val groupId = resourceGroup.id
      if (groupId != null) {
        groupBuilder.setTypeId(
          Resources.TypeId.newBuilder()
            .setId(groupId.toInt())
            .build()
        )
      }

      groupBuilder.setName(resourceGroup.type.tagName)

      for (idToEntries in resourceGroup.entries.values) {
        for (entry in idToEntries.values) {
          val entryBuilder = Resources.Entry.newBuilder()

          val entryId = entry.id
          if (entryId != null) {
            entryBuilder.setEntryId(
              Resources.EntryId.newBuilder()
                .setId(entryId.toInt())
                .build()
            )
          }
          entryBuilder.setName(entry.name)

          val visibilityBuilder = Resources.Visibility.newBuilder()
          if (entry.visibility.source.isNotEmpty()) {
            visibilityBuilder.setSource(serializeSourceToPb(entry.visibility.source, sourcePool))
          }
          entryBuilder.setVisibility(
            visibilityBuilder
              .setLevel(serializeVisibilityToPb(entry.visibility.level))
              .setComment(entry.visibility.comment)
              .build()
          )

          val entryAllowNew = entry.allowNew
          if (entryAllowNew != null) {
            val allowNewBuilder = Resources.AllowNew.newBuilder()
            if (entryAllowNew.source.isNotEmpty()) {
              allowNewBuilder.setSource(serializeSourceToPb(entryAllowNew.source, sourcePool))
            }
            entryBuilder.setAllowNew(
              allowNewBuilder
                .setComment(entryAllowNew.comment)
                .build()
            )
          }

          val entryOverlayableItem = entry.overlayable
          if (entryOverlayableItem != null) {

            entryBuilder.setOverlayableItem(
              serializeOverlayableToPb(
                entryOverlayableItem,
                overlayables,
                tableBuilder,
                sourcePool
              )
            )
          }

          for (configValue in entry.values) {
            entryBuilder.addConfigValue(
              Resources.ConfigValue.newBuilder()
                .setConfig(serializeConfigToPb(configValue.config, configValue.product, logger))
                .setValue(serializeValueToPb(configValue.value!!, sourcePool, logger))
                .build()
            )
          }
          groupBuilder.addEntry(entryBuilder.build())
        }
      }
      packageBuilder.addType(groupBuilder.build())
    }
    tableBuilder.addPackage(packageBuilder.build())
  }
  return tableBuilder.setSourcePool(serializePoolToPb(sourcePool, logger))
    .build()
}

fun serializeVisibilityToPb(resourceVisibility: ResourceVisibility) =
  when (resourceVisibility) {
    ResourceVisibility.PRIVATE,
    ResourceVisibility.PRIVATE_XML_ONLY -> Resources.Visibility.Level.PRIVATE

    ResourceVisibility.PUBLIC -> Resources.Visibility.Level.PUBLIC
    else -> Resources.Visibility.Level.UNKNOWN
  }

fun serializeCompiledFileToPb(file: ResourceFile): ResourcesInternal.CompiledFile {
  val compiledFile = ResourcesInternal.CompiledFile.newBuilder()
    .setResourceName(file.name.toString())
    .setSourcePath(file.source.path)
    .setType(serializeFileTypeToPb(file.type))
    .setConfig(serializeConfigToPb(file.configuration, null, null))

  for (exportedResourceName in file.exportedSymbols) {
    compiledFile.addExportedSymbol(
      ResourcesInternal.CompiledFile.Symbol.newBuilder()
        .setResourceName(exportedResourceName.name.toString())
        .setSource(
          Resources.SourcePosition.newBuilder()
            .setLineNumber(exportedResourceName.line)
        )
    )
  }
  return compiledFile.build()
}

fun serializeConfigToPb(
  config: ConfigDescription,
  product: String?,
  logger: ILogger?
): ConfigurationOuterClass.Configuration {

  val configBuilder = ConfigurationOuterClass.Configuration.newBuilder()
  configBuilder.setMcc(config.mcc.toInt() and 0xffff)
  configBuilder.setMnc(config.mnc.toInt() and 0xffff)
  configBuilder.setLocale(config.getBcp47Locale())

  configBuilder.setLayoutDirection(
    when ((config.screenLayout.toInt() and ResTableConfig.SCREEN_LAYOUT.DIR_MASK).toByte()) {
      ResTableConfig.SCREEN_LAYOUT.DIR_LTR ->
        ConfigurationOuterClass.Configuration.LayoutDirection.LAYOUT_DIRECTION_LTR

      ResTableConfig.SCREEN_LAYOUT.DIR_RTL ->
        ConfigurationOuterClass.Configuration.LayoutDirection.LAYOUT_DIRECTION_RTL

      else -> ConfigurationOuterClass.Configuration.LayoutDirection.LAYOUT_DIRECTION_UNSET
    }
  )

  configBuilder.setScreenWidth(config.screenWidth)
  configBuilder.setScreenHeight(config.screenHeight)
  configBuilder.setScreenWidthDp(config.screenWidthDp)
  configBuilder.setScreenHeightDp(config.screenHeightDp)
  configBuilder.setSmallestScreenWidthDp(config.smallestScreenWidthDp)

  configBuilder.setScreenLayoutSize(
    when ((config.screenLayout.toInt() and ResTableConfig.SCREEN_LAYOUT.SIZE_MASK).toByte()) {
      ResTableConfig.SCREEN_LAYOUT.SIZE_SMALL ->
        ConfigurationOuterClass.Configuration.ScreenLayoutSize.SCREEN_LAYOUT_SIZE_SMALL

      ResTableConfig.SCREEN_LAYOUT.SIZE_NORMAL ->
        ConfigurationOuterClass.Configuration.ScreenLayoutSize.SCREEN_LAYOUT_SIZE_NORMAL

      ResTableConfig.SCREEN_LAYOUT.SIZE_LARGE ->
        ConfigurationOuterClass.Configuration.ScreenLayoutSize.SCREEN_LAYOUT_SIZE_LARGE

      ResTableConfig.SCREEN_LAYOUT.SIZE_XLARGE ->
        ConfigurationOuterClass.Configuration.ScreenLayoutSize.SCREEN_LAYOUT_SIZE_XLARGE

      else -> ConfigurationOuterClass.Configuration.ScreenLayoutSize.SCREEN_LAYOUT_SIZE_UNSET
    }
  )

  configBuilder.setScreenLayoutLong(
    when (
      (config.screenLayout.toInt() and ResTableConfig.SCREEN_LAYOUT.SCREENLONG_MASK).toByte()) {

      ResTableConfig.SCREEN_LAYOUT.SCREENLONG_YES ->
        ConfigurationOuterClass.Configuration.ScreenLayoutLong.SCREEN_LAYOUT_LONG_LONG

      ResTableConfig.SCREEN_LAYOUT.SCREENLONG_NO ->
        ConfigurationOuterClass.Configuration.ScreenLayoutLong.SCREEN_LAYOUT_LONG_NOTLONG

      else -> ConfigurationOuterClass.Configuration.ScreenLayoutLong.SCREEN_LAYOUT_LONG_UNSET
    }
  )

  configBuilder.setScreenRound(
    when (
      (config.screenLayout2.toInt() and ResTableConfig.SCREEN_LAYOUT2.SCREENROUND_MASK).toByte()) {

      ResTableConfig.SCREEN_LAYOUT2.SCREENROUND_YES ->
        ConfigurationOuterClass.Configuration.ScreenRound.SCREEN_ROUND_ROUND

      ResTableConfig.SCREEN_LAYOUT2.SCREENROUND_NO ->
        ConfigurationOuterClass.Configuration.ScreenRound.SCREEN_ROUND_NOTROUND

      else -> ConfigurationOuterClass.Configuration.ScreenRound.SCREEN_ROUND_UNSET
    }
  )

  configBuilder.setWideColorGamut(
    when ((config.colorMode.toInt() and ResTableConfig.COLOR_MODE.WIDE_GAMUT_MASK).toByte()) {
      ResTableConfig.COLOR_MODE.WIDE_GAMUT_YES ->
        ConfigurationOuterClass.Configuration.WideColorGamut.WIDE_COLOR_GAMUT_WIDECG

      ResTableConfig.COLOR_MODE.WIDE_GAMUT_NO ->
        ConfigurationOuterClass.Configuration.WideColorGamut.WIDE_COLOR_GAMUT_NOWIDECG

      else -> ConfigurationOuterClass.Configuration.WideColorGamut.WIDE_COLOR_GAMUT_UNSET
    }
  )

  configBuilder.setHdr(
    when ((config.colorMode.toInt() and ResTableConfig.COLOR_MODE.HDR_MASK).toByte()) {
      ResTableConfig.COLOR_MODE.HDR_YES -> ConfigurationOuterClass.Configuration.Hdr.HDR_HIGHDR
      ResTableConfig.COLOR_MODE.HDR_NO -> ConfigurationOuterClass.Configuration.Hdr.HDR_LOWDR
      else -> ConfigurationOuterClass.Configuration.Hdr.HDR_UNSET
    }
  )

  configBuilder.setOrientation(
    when (config.orientation) {
      ResTableConfig.ORIENTATION.PORT ->
        ConfigurationOuterClass.Configuration.Orientation.ORIENTATION_PORT

      ResTableConfig.ORIENTATION.LAND ->
        ConfigurationOuterClass.Configuration.Orientation.ORIENTATION_LAND

      ResTableConfig.ORIENTATION.SQUARE ->
        ConfigurationOuterClass.Configuration.Orientation.ORIENTATION_SQUARE

      else -> ConfigurationOuterClass.Configuration.Orientation.ORIENTATION_UNSET
    }
  )

  configBuilder.setUiModeType(
    when ((config.uiMode.toInt() and ResTableConfig.UI_MODE.TYPE_MASK).toByte()) {
      ResTableConfig.UI_MODE.TYPE_NORMAL ->
        ConfigurationOuterClass.Configuration.UiModeType.UI_MODE_TYPE_NORMAL

      ResTableConfig.UI_MODE.TYPE_DESK ->
        ConfigurationOuterClass.Configuration.UiModeType.UI_MODE_TYPE_DESK

      ResTableConfig.UI_MODE.TYPE_CAR ->
        ConfigurationOuterClass.Configuration.UiModeType.UI_MODE_TYPE_CAR

      ResTableConfig.UI_MODE.TYPE_TELEVISION ->
        ConfigurationOuterClass.Configuration.UiModeType.UI_MODE_TYPE_TELEVISION

      ResTableConfig.UI_MODE.TYPE_APPLIANCE ->
        ConfigurationOuterClass.Configuration.UiModeType.UI_MODE_TYPE_APPLIANCE

      ResTableConfig.UI_MODE.TYPE_WATCH ->
        ConfigurationOuterClass.Configuration.UiModeType.UI_MODE_TYPE_WATCH

      ResTableConfig.UI_MODE.TYPE_VR_HEADSET ->
        ConfigurationOuterClass.Configuration.UiModeType.UI_MODE_TYPE_VRHEADSET

      else -> ConfigurationOuterClass.Configuration.UiModeType.UI_MODE_TYPE_UNSET
    }
  )

  configBuilder.setUiModeNight(
    when ((config.uiMode.toInt() and ResTableConfig.UI_MODE.NIGHT_MASK).toByte()) {
      ResTableConfig.UI_MODE.NIGHT_YES ->
        ConfigurationOuterClass.Configuration.UiModeNight.UI_MODE_NIGHT_NIGHT

      ResTableConfig.UI_MODE.NIGHT_NO ->
        ConfigurationOuterClass.Configuration.UiModeNight.UI_MODE_NIGHT_NOTNIGHT

      else -> ConfigurationOuterClass.Configuration.UiModeNight.UI_MODE_NIGHT_UNSET
    }
  )

  configBuilder.setDensity(config.density)

  configBuilder.setTouchscreen(
    when (config.touchscreen) {
      ResTableConfig.TOUCHSCREEN.NOTOUCH ->
        ConfigurationOuterClass.Configuration.Touchscreen.TOUCHSCREEN_NOTOUCH

      ResTableConfig.TOUCHSCREEN.STYLUS ->
        ConfigurationOuterClass.Configuration.Touchscreen.TOUCHSCREEN_STYLUS

      ResTableConfig.TOUCHSCREEN.FINGER ->
        ConfigurationOuterClass.Configuration.Touchscreen.TOUCHSCREEN_FINGER

      else -> ConfigurationOuterClass.Configuration.Touchscreen.TOUCHSCREEN_UNSET
    }
  )

  configBuilder.setKeysHidden(
    when ((config.inputFlags.toInt() and ResTableConfig.INPUT_FLAGS.KEYSHIDDEN_MASK).toByte()) {
      ResTableConfig.INPUT_FLAGS.KEYSHIDDEN_NO ->
        ConfigurationOuterClass.Configuration.KeysHidden.KEYS_HIDDEN_KEYSEXPOSED

      ResTableConfig.INPUT_FLAGS.KEYSHIDDEN_YES ->
        ConfigurationOuterClass.Configuration.KeysHidden.KEYS_HIDDEN_KEYSHIDDEN

      ResTableConfig.INPUT_FLAGS.KEYSHIDDEN_SOFT ->
        ConfigurationOuterClass.Configuration.KeysHidden.KEYS_HIDDEN_KEYSSOFT

      else -> ConfigurationOuterClass.Configuration.KeysHidden.KEYS_HIDDEN_UNSET
    }
  )

  configBuilder.setKeyboard(
    when (config.keyboard) {
      ResTableConfig.KEYBOARD.NOKEYS ->
        ConfigurationOuterClass.Configuration.Keyboard.KEYBOARD_NOKEYS

      ResTableConfig.KEYBOARD.QWERTY ->
        ConfigurationOuterClass.Configuration.Keyboard.KEYBOARD_QWERTY

      ResTableConfig.KEYBOARD.TWELVEKEY ->
        ConfigurationOuterClass.Configuration.Keyboard.KEYBOARD_TWELVEKEY

      else -> ConfigurationOuterClass.Configuration.Keyboard.KEYBOARD_UNSET
    }
  )

  configBuilder.setNavHidden(
    when ((config.inputFlags.toInt() and ResTableConfig.INPUT_FLAGS.NAVHIDDEN_MASK).toByte()) {
      ResTableConfig.INPUT_FLAGS.NAVHIDDEN_YES ->
        ConfigurationOuterClass.Configuration.NavHidden.NAV_HIDDEN_NAVHIDDEN

      ResTableConfig.INPUT_FLAGS.NAVHIDDEN_NO ->
        ConfigurationOuterClass.Configuration.NavHidden.NAV_HIDDEN_NAVEXPOSED

      else -> ConfigurationOuterClass.Configuration.NavHidden.NAV_HIDDEN_UNSET
    }
  )

  configBuilder.setNavigation(
    when (config.navigation) {
      ResTableConfig.NAVIGATION.NONAV ->
        ConfigurationOuterClass.Configuration.Navigation.NAVIGATION_NONAV

      ResTableConfig.NAVIGATION.DPAD ->
        ConfigurationOuterClass.Configuration.Navigation.NAVIGATION_DPAD

      ResTableConfig.NAVIGATION.TRACKBALL ->
        ConfigurationOuterClass.Configuration.Navigation.NAVIGATION_TRACKBALL

      ResTableConfig.NAVIGATION.WHEEL ->
        ConfigurationOuterClass.Configuration.Navigation.NAVIGATION_WHEEL

      else -> ConfigurationOuterClass.Configuration.Navigation.NAVIGATION_UNSET
    }
  )

  configBuilder.setGrammaticalGender(
    when (config.grammaticalInflection) {
      ResTableConfig.GRAMMATICAL_GENDER.NEUTER ->
        ConfigurationOuterClass.Configuration.GrammaticalGender.GRAM_GENDER_NEUTER

      ResTableConfig.GRAMMATICAL_GENDER.FEMININE ->
        ConfigurationOuterClass.Configuration.GrammaticalGender.GRAM_GENDER_FEMININE

      ResTableConfig.GRAMMATICAL_GENDER.MASCULINE ->
        ConfigurationOuterClass.Configuration.GrammaticalGender.GRAM_GENDER_MASCULINE

      else -> ConfigurationOuterClass.Configuration.GrammaticalGender.GRAM_GENDER_USET
    }
  )

  configBuilder.setSdkVersion(config.sdkVersion.toInt() and 0xffff)

  if (product != null) {
    configBuilder.setProduct(product)
  }

  return configBuilder.build()
}

fun serializeOverlayableToPb(
  item: OverlayableItem,
  overlayables: MutableList<Overlayable>,
  table: Resources.ResourceTable.Builder,
  sourcePool: StringPool
): Resources.OverlayableItem {

  // Retrieve the index of the overlayable in the list of groups that have already been serialized.
  val foundIndex = overlayables.indexOf(item.overlayable)

  // Serialize the overlayable if it has not been serialized already.
  if (foundIndex == -1) {
    overlayables.add(item.overlayable)
    val overlayableBuilder = Resources.Overlayable.newBuilder()
      .setName(item.overlayable.name)
      .setActor(item.overlayable.actor)
    if (item.overlayable.source.isNotEmpty()) {
      overlayableBuilder.setSource(serializeSourceToPb(item.overlayable.source, sourcePool))
    }
    table.addOverlayable(overlayableBuilder.build())
  }

  val overlayableIndex = if (foundIndex == -1) overlayables.lastIndex else foundIndex

  val itemBuilder = Resources.OverlayableItem.newBuilder()
  itemBuilder.setOverlayableIdx(overlayableIndex)

  if ((item.policies and OverlayableItem.Policy.PUBLIC) != 0) {
    itemBuilder.addPolicy(Resources.OverlayableItem.Policy.PUBLIC)
  }
  if ((item.policies and OverlayableItem.Policy.PRODUCT) != 0) {
    itemBuilder.addPolicy(Resources.OverlayableItem.Policy.PRODUCT)
  }
  if ((item.policies and OverlayableItem.Policy.SYSTEM) != 0) {
    itemBuilder.addPolicy(Resources.OverlayableItem.Policy.SYSTEM)
  }
  if ((item.policies and OverlayableItem.Policy.VENDOR) != 0) {
    itemBuilder.addPolicy(Resources.OverlayableItem.Policy.VENDOR)
  }
  if ((item.policies and OverlayableItem.Policy.SIGNATURE) != 0) {
    itemBuilder.addPolicy(Resources.OverlayableItem.Policy.SIGNATURE)
  }
  if ((item.policies and OverlayableItem.Policy.SYSTEM) != 0) {
    itemBuilder.addPolicy(Resources.OverlayableItem.Policy.SYSTEM)
  }
  if ((item.policies and OverlayableItem.Policy.ODM) != 0) {
    itemBuilder.addPolicy(Resources.OverlayableItem.Policy.ODM)
  }
  if ((item.policies and OverlayableItem.Policy.OEM) != 0) {
    itemBuilder.addPolicy(Resources.OverlayableItem.Policy.OEM)
  }

  if (item.source.isNotEmpty()) {
    itemBuilder.setSource(serializeSourceToPb(item.source, sourcePool))
  }
  itemBuilder.setComment(item.comment)

  return itemBuilder.build()
}

fun serializeReferenceTypeToPb(type: Reference.Type) = when (type) {
  Reference.Type.RESOURCE ->
    Resources.Reference.Type.REFERENCE

  Reference.Type.ATTRIBUTE ->
    Resources.Reference.Type.ATTRIBUTE
}

fun serializeReferenceToPb(ref: Reference): Resources.Reference {
  val refBuilder = Resources.Reference.newBuilder()
  refBuilder.setId(ref.id ?: 0)

  if (ref.name != ResourceName.EMPTY) {
    refBuilder.setName(ref.name.toString())
  }

  refBuilder.setPrivate(ref.isPrivate)
  refBuilder.setType(serializeReferenceTypeToPb(ref.referenceType))
  refBuilder.setAllowRaw(ref.allowRaw)
  refBuilder.setTypeFlags(ref.typeFlags!!)

  return refBuilder.build()
}

fun serializePluralTypeToPb(type: Plural.Type) = when (type) {
  Plural.Type.ZERO -> Resources.Plural.Arity.ZERO
  Plural.Type.ONE -> Resources.Plural.Arity.ONE
  Plural.Type.TWO -> Resources.Plural.Arity.TWO
  Plural.Type.FEW -> Resources.Plural.Arity.FEW
  Plural.Type.MANY -> Resources.Plural.Arity.MANY
  else -> Resources.Plural.Arity.OTHER
}

fun serializeFileTypeToPb(type: ResourceFile.Type) = when (type) {
  ResourceFile.Type.BinaryXml -> Resources.FileReference.Type.BINARY_XML
  ResourceFile.Type.ProtoXml -> Resources.FileReference.Type.PROTO_XML
  ResourceFile.Type.Png -> Resources.FileReference.Type.PNG
  else -> Resources.FileReference.Type.UNKNOWN
}

fun serializeStringToPb(string: BasicString) =
  Resources.String.newBuilder().setValue(string.ref.value()).build()

fun serializeRawToPb(string: RawString) =
  Resources.RawString.newBuilder().setValue(string.value.value()).build()

fun serializeStyledStrToPb(string: StyledString): Resources.StyledString {
  val styleBuilder = Resources.StyledString.newBuilder()
  styleBuilder.setValue(string.ref.value())
  for (span in string.ref.spans()) {
    val spanBuilder = Resources.StyledString.Span.newBuilder()
    spanBuilder.setTag(span.name.value())
      .setFirstChar(span.firstChar)
      .setLastChar(span.lastChar)
    styleBuilder.addSpan(spanBuilder.build())
  }
  return styleBuilder.build()
}

fun serializeFileRefToPb(file: FileReference) =
  Resources.FileReference.newBuilder()
    .setPath(file.path.value())
    .setType(serializeFileTypeToPb(file.type))
    .build()

fun serializeBinPrimitiveToPb(primitive: BinaryPrimitive, logger: ILogger?): Resources.Primitive {
  val resVal = primitive.flatten()

  val primitiveBuilder = Resources.Primitive.newBuilder()

  if (resVal == null) {
    logger?.error(
      null, "%s, Failed to serialize primitive %s.", blameSource(primitive.source), primitive
    )
    return primitiveBuilder.build()
  }

  when (resVal.dataType) {
    ResValue.DataType.NULL -> {
      when (resVal.data) {
        ResValue.NullFormat.UNDEFINED ->
          primitiveBuilder.setNullValue(Resources.Primitive.NullType.newBuilder().build())

        ResValue.NullFormat.EMPTY ->
          primitiveBuilder.setEmptyValue(Resources.Primitive.EmptyType.newBuilder().build())

        else -> {
          val errorMsg = "%s, Invalid null format value '%s' for primitive %s."
          logger?.error(null, errorMsg, blameSource(primitive.source), resVal.data, primitive)
        }
      }
    }

    ResValue.DataType.FLOAT -> primitiveBuilder.setFloatValue(Float.fromBits(resVal.data))
    ResValue.DataType.DIMENSION -> primitiveBuilder.setDimensionValue(resVal.data)
    ResValue.DataType.FRACTION -> primitiveBuilder.setFractionValue(resVal.data)
    ResValue.DataType.INT_DEC -> primitiveBuilder.setIntDecimalValue(resVal.data)
    ResValue.DataType.INT_HEX -> primitiveBuilder.setIntHexadecimalValue(resVal.data)
    ResValue.DataType.INT_BOOLEAN -> primitiveBuilder.setBooleanValue(resVal.data != 0)
    ResValue.DataType.INT_COLOR_ARGB8 -> primitiveBuilder.setColorArgb8Value(resVal.data)
    ResValue.DataType.INT_COLOR_RGB8 -> primitiveBuilder.setColorRgb8Value(resVal.data)
    ResValue.DataType.INT_COLOR_ARGB4 -> primitiveBuilder.setColorArgb4Value(resVal.data)
    ResValue.DataType.INT_COLOR_RGB4 -> primitiveBuilder.setColorRgb4Value(resVal.data)
    else -> {
      val errorMsg = "%s, Invalid data type '%s' for primitive %s."
      logger?.error(null, errorMsg, blameSource(primitive.source), resVal.dataType, primitive)
    }
  }

  return primitiveBuilder.build()
}

fun serializeAttrToPb(attribute: AttributeResource, sourcePool: StringPool): Resources.Attribute {
  val attrBuilder = Resources.Attribute.newBuilder()
    .setFormatFlags(attribute.typeMask)
    .setMinInt(attribute.minInt)
    .setMaxInt(attribute.maxInt)

  for (symbol in attribute.symbols) {
    val symbolBuilder = Resources.Attribute.Symbol.newBuilder()
    if (symbol.symbol.source.isNotEmpty()) {
      symbolBuilder.setSource(serializeSourceToPb(symbol.symbol.source, sourcePool))
    }
    attrBuilder.addSymbol(
      symbolBuilder
        .setComment(symbol.symbol.comment)
        .setName(serializeReferenceToPb(symbol.symbol))
        .setValue(symbol.value)
        .setType(symbol.type.toInt())
        .build()
    )
  }

  return attrBuilder.build()
}

fun serializeStyleToPb(style: Style, sourcePool: StringPool, logger: ILogger?): Resources.Style {
  val styleBuilder = Resources.Style.newBuilder()

  val parent = style.parent
  if (parent != null) {
    styleBuilder.setParent(serializeReferenceToPb(parent))
    if (parent.source.isNotEmpty()) {
      styleBuilder.setParentSource(serializeSourceToPb(parent.source, sourcePool))
    }
  }

  for (entry in style.entries) {
    val entryBuilder = Resources.Style.Entry.newBuilder()
    if (entry.key.source.isNotEmpty()) {
      entryBuilder.setSource(serializeSourceToPb(entry.key.source, sourcePool))
    }
    styleBuilder.addEntry(
      entryBuilder
        .setKey(serializeReferenceToPb(entry.key))
        .setComment(entry.key.comment)
        .setItem(serializeItemToPb(entry.value!!, logger))
        .build()
    )
  }
  return styleBuilder.build()
}

fun serializeStyleableToPb(styleable: Styleable, sourcePool: StringPool): Resources.Styleable {

  val styleableBuilder = Resources.Styleable.newBuilder()
  for (entry in styleable.entries) {
    val entryBuilder = Resources.Styleable.Entry.newBuilder()
    if (entry.source.isNotEmpty()) {
      entryBuilder.setSource(serializeSourceToPb(entry.source, sourcePool))
    }
    styleableBuilder.addEntry(
      entryBuilder
        .setComment(entry.comment)
        .setAttr(serializeReferenceToPb(entry))
        .build()
    )
  }
  return styleableBuilder.build()
}

fun serializeArrayToPb(
  array: ArrayResource, sourcePool: StringPool, logger: ILogger?
): Resources.Array {

  val arrayBuilder = Resources.Array.newBuilder()
  for (element in array.elements) {
    val entryBuilder = Resources.Array.Element.newBuilder()
    if (element.source.isNotEmpty()) {
      entryBuilder.setSource(serializeSourceToPb(element.source, sourcePool))
    }
    arrayBuilder.addElement(
      entryBuilder
        .setComment(element.comment)
        .setItem(serializeItemToPb(element, logger))
        .build()
    )
  }
  return arrayBuilder.build()
}

fun serializePluralToPb(
  plural: Plural, sourcePool: StringPool, logger: ILogger?
): Resources.Plural {

  val pluralBuilder = Resources.Plural.newBuilder()
  for (type in Plural.Type.TYPES) {
    val entry = plural.values[type.ordinal]
    entry ?: continue
    val entryBuilder = Resources.Plural.Entry.newBuilder()
    if (entry.source.isNotEmpty()) {
      entryBuilder.setSource(serializeSourceToPb(entry.source, sourcePool))
    }
    pluralBuilder.addEntry(
      entryBuilder
        .setComment(entry.comment)
        .setArity(serializePluralTypeToPb(type))
        .setItem(serializeItemToPb(entry, logger))
        .build()
    )
  }
  return pluralBuilder.build()
}

fun serializeMacroToPb(macro: Macro): Resources.MacroBody {
  val macroBuilder = Resources.MacroBody.newBuilder()
  macroBuilder.rawString = macro.rawValue!!

  val styleString = Resources.StyleString.newBuilder()
  styleString.str = macro.styleString!!.str
  for (span in macro.styleString!!.spans) {
    val spansBuilder = Resources.StyleString.Span.newBuilder()
    spansBuilder.name = span.name
    spansBuilder.startIndex = span.firstChar
    spansBuilder.endIndex = span.lastChar
    styleString.addSpans(spansBuilder.build())
  }
  macroBuilder.styleString = styleString.build()

  for (untranslatables in macro.untranslatables) {
    val section = Resources.UntranslatableSection.newBuilder()
    section.startIndex = untranslatables.startIndex.toLong()
    section.endIndex = untranslatables.endIndex.toLong()
    macroBuilder.addUntranslatableSections(section.build())
  }

  for (aliasNamespace in macro.aliasNamespaces) {
    val namespace = Resources.NamespaceAlias.newBuilder()
    namespace.prefix = aliasNamespace.alias
    namespace.packageName = aliasNamespace.packageName
    namespace.isPrivate = aliasNamespace.isPrivate
    macroBuilder.addNamespaceStack(namespace.build())
  }

  return macroBuilder.build()
}

fun serializeItemToPb(item: Item, logger: ILogger?): Resources.Item {
  val itemBuilder = Resources.Item.newBuilder()
  when (item) {
    is Reference -> itemBuilder.setRef(serializeReferenceToPb(item))
    is BasicString -> itemBuilder.setStr(serializeStringToPb(item))
    is RawString -> itemBuilder.setRawStr(serializeRawToPb(item))
    is StyledString -> itemBuilder.setStyledStr(serializeStyledStrToPb(item))
    is FileReference -> itemBuilder.setFile(serializeFileRefToPb(item))
    is Id -> itemBuilder.setId(Resources.Id.newBuilder().build())
    is BinaryPrimitive -> itemBuilder.setPrim(serializeBinPrimitiveToPb(item, logger))
    else -> {
      val errorMsg = "%s, Unrecognized item type %s, for value %s."
      logger?.error(null, errorMsg, blameSource(item.source), item.javaClass, item)
    }
  }
  return itemBuilder.build()
}

fun serializeValueToPb(value: Value, sourcePool: StringPool, logger: ILogger?): Resources.Value {
  val valueBuilder = Resources.Value.newBuilder()

  if (value is Item) {
    valueBuilder.setItem(serializeItemToPb(value, logger))
  } else {
    val compoundBuilder = Resources.CompoundValue.newBuilder()
    when (value) {
      is AttributeResource -> compoundBuilder.setAttr(serializeAttrToPb(value, sourcePool))
      is Style -> compoundBuilder.setStyle(serializeStyleToPb(value, sourcePool, logger))
      is Styleable -> compoundBuilder.setStyleable(serializeStyleableToPb(value, sourcePool))
      is ArrayResource -> compoundBuilder.setArray(serializeArrayToPb(value, sourcePool, logger))
      is Plural -> compoundBuilder.setPlural(serializePluralToPb(value, sourcePool, logger))
      is Macro -> compoundBuilder.setMacro(serializeMacroToPb(value))
      else -> {
        val errorMsg = "%s, Unrecognized value type %s, for value %s."
        logger?.error(null, errorMsg, blameSource(value.source), value.javaClass, value)
      }
    }
    valueBuilder.setCompoundValue(compoundBuilder.build())
  }
  if (value.source.isNotEmpty()) {
    valueBuilder.setSource(serializeSourceToPb(value.source, sourcePool))
  }
  return valueBuilder
    .setComment(value.comment)
    .setWeak(value.weak)
    .build()
}
