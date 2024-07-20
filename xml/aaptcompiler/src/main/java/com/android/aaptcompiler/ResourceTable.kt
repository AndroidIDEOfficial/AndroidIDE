package com.android.aaptcompiler

import androidx.collection.mutableIntObjectMapOf
import com.android.aapt.Resources
import com.itsaky.androidide.layoutlib.resources.ResourceVisibility
import com.itsaky.androidide.xml.res.IResourceEntry
import com.itsaky.androidide.xml.res.IResourceGroup
import com.itsaky.androidide.xml.res.IResourceTable
import com.itsaky.androidide.xml.res.IResourceTablePackage
import com.itsaky.androidide.xml.res.ISearchResult
import java.io.File
import java.util.SortedMap
import java.util.TreeMap

/**
 * The container and index for all resources defined for a given app.
 *
 * <p> The Resource Table is an organizer of all resources. It sorts the resources by package, then
 * by type, then by name, by config, and finally by product, if applicable. [addResource],
 * [addFileReference] are used to add resources declared in the current app and have their resource
 * names validated. While [addResourceMangled] and [addFileReferenceMangled] are used to add
 * resources from libraries and are not validated.
 */
class ResourceTable(val validateResources: Boolean = false, val logger: BlameLogger? = null) :
  IResourceTable {
  /**
   * The string pool used by this resource table. Values that reference strings must use
   * this pool to create their strings.
   */
  val stringPool = StringPool()

  /** The list of packages in this table. */
  override val packages = mutableListOf<ResourceTablePackage>()

  /**
   * Set of dynamic packages that this table may reference. Their package names get encoded into the
   * resources.arsc along with their compile-time assigned IDs.
   */
  private val includedPackages = mutableIntObjectMapOf<String>()

  enum class CollisionResult {
    KEEP_ORIGINAL,
    CONFLICT,
    TAKE_NEW
  }

  data class SearchResult(
    override val tablePackage: ResourceTablePackage,
    override val group: ResourceGroup,
    override val entry: ResourceEntry
  ) : ISearchResult

  /**
   * Adds a resource to the table with the given value.
   *
   * @param name The full name of this resource. This includes package name, resource type, and the
   *   entry name of the resource.
   * @param config The configuration this value for the given resource apply.
   * @param product The product for which this value applies.
   * @param value The value associated with the given resource.
   * @return Returns false if and only if the resource was not able to be added. This is caused if
   *  the resource name failed to be validated or there exists a conflict with a resource already in
   *  the table.
   */
  fun addResource(
    name: ResourceName, config: ConfigDescription, product: String, value: Value
  ): Boolean =
    addResourceImpl(
      name, 0, config, product, value, ::resourceNameValidator, ::resolveValueCollision
    )


  /**
   * Adds a resource to the table with the given id and value.
   *
   * @param name The full name of this resource. This includes package name, resource type, and the
   *   entry name of the resource.
   * @param id the id of the given resource.
   * @param config The configuration this value for the given resource apply.
   * @param product The product for which this value applies.
   * @param value The value associated with the given resource.
   * @return Returns false if and only if the resource was not able to be added. This is caused if
   *  the resource name failed to be validated, there exists a conflict with a resource already in
   *  the table, or the id is a dynamic id but does not match with the package, type, or entry.
   */
  fun addResourceWithId(
    name: ResourceName,
    id: Int,
    config: ConfigDescription,
    product: String,
    value: Value
  ): Boolean =
    addResourceImpl(
      name, id, config, product, value, ::resourceNameValidator, ::resolveValueCollision
    )

  /**
   * Adds a file reference resource to the table with the given file path.
   *
   * @param name The full name of this resource. This includes package name, resource type and the
   *   entry name of the resource.
   * @param config The configuration this value for the given file reference applies.
   * @param source The place in source where the reference exists.
   * @param path The path name to the file being referenced.
   * @return Returns false if and only if the resource was not able to be added. This is caused if
   *  the resource name failed to be validated or there exists a conflict with a resource already in
   *  the table.
   */
  fun addFileReference(
    name: ResourceName, config: ConfigDescription, source: Source, path: String
  ): Boolean =
    addFileReferenceImpl(
      name, config, source, path, null, ::resourceNameValidator
    )

  /**
   * Same as [addFileReference], but doesn't verify the name of the file reference resource. This is
   * used when loading resources from an existing binary resource table that may have mangled
   * resources.
   *
   * @param name The full name of this resource. This includes package name, resource type and the
   *   entry name of the resource.
   * @param config The configuration this value for the given file reference applies.
   * @param source The place in source where the reference exists.
   * @param path The path name to the file being referenced.
   * @param file The file object referenced by this resource.
   * @return Returns false if and only if the resource was not able to be added. This is caused if
   *  the resource name failed to be validated or there exists a conflict with a resource already in
   *  the table.
   */
  fun addFileReferenceMangled(
    name: ResourceName,
    config: ConfigDescription,
    source: Source,
    path: String,
    file: File
  ): Boolean =
    addFileReferenceImpl(name, config, source, path, file, ::skipNameValidator)

  /**
   * Same as [addResource], but doesn't verify the name of the resource. This is used when loading
   * resources from an existing binary resource table that may have mangled names.
   *
   * @param name The full name of this resource. This includes package name, resource type, and the
   *   entry name of the resource.
   * @param config The configuration this value for the given resource apply.
   * @param product The product for which this value applies.
   * @param value The value associated with the given resource.
   * @return Returns false if and only if the resource was not able to be added. This is caused if
   *  there exists a conflict with a resource already in the table.
   */
  fun addResourceMangled(
    name: ResourceName, config: ConfigDescription, product: String, value: Value
  ): Boolean =
    addResourceImpl(
      name, 0, config, product, value, ::skipNameValidator, ::resolveValueCollision
    )

  /**
   * Same as [addResourceWithId], but doesn't verify the name of the resource. This is used when
   * loading resources from an existing binary resource table that may have mangled names.
   *
   * @param name The full name of this resource. This includes package name, resource type, and the
   *   entry name of the resource.
   * @param id the id of the given resource.
   * @param config The configuration this value for the given resource apply.
   * @param product The product for which this value applies.
   * @param value The value associated with the given resource.
   * @return Returns false if and only if the resource was not able to be added. This is caused if
   *  there exists a conflict with a resource already in the table, or the id is a dynamic id but
   *  does not match with the package, type, or entry.
   */
  fun addResourceWithIdMangled(
    name: ResourceName,
    id: Int,
    config: ConfigDescription,
    product: String,
    value: Value
  ) =
    addResourceImpl(
      name, id, config, product, value, ::skipNameValidator, ::resolveValueCollision
    )

  /**
   * Sets the resource with the given name to the set visibility. If this resource has no value in
   * the table, the resource will be created with no value set to the given visibility.
   *
   * @param name The full name of the resource, whose visibility will be modified.
   * @param visibility the new Visibility of the resource.
   * @return true, if and only if the visibility was able to be updated. This may not happen if the
   * visibility has already been set to at least as visible as the new visibility.
   */
  fun setVisibility(name: ResourceName, visibility: Visibility) =
    setVisibilityImpl(name, visibility, 0, ::resourceNameValidator)

  fun setVisibilityWithId(name: ResourceName, visibility: Visibility, id: Int) =
    setVisibilityImpl(name, visibility, id, ::resourceNameValidator)

  fun setVisibilityMangled(name: ResourceName, visibility: Visibility) =
    setVisibilityImpl(name, visibility, 0, ::skipNameValidator)

  fun setVisibilityWithIdMangled(name: ResourceName, visibility: Visibility, id: Int) =
    setVisibilityImpl(name, visibility, id, ::skipNameValidator)

  fun setAllowNew(name: ResourceName, allowNew: AllowNew) =
    setAllowNewImpl(name, allowNew, ::resourceNameValidator)

  fun setAllowNewMangled(name: ResourceName, allowNew: AllowNew) =
    setAllowNewImpl(name, allowNew, ::skipNameValidator)

  fun setOverlayable(name: ResourceName, overlayable: OverlayableItem) =
    setOverlayableImpl(name, overlayable, ::resourceNameValidator)

  fun setOverlayableMangled(name: ResourceName, overlayable: OverlayableItem) =
    setOverlayableImpl(name, overlayable, ::skipNameValidator)

  override fun findResource(name: ResourceName): SearchResult? {
    val tablePackage = findPackage(name.pck!!)
    tablePackage ?: return null

    val group = tablePackage.findGroup(name.type)
    group ?: return null

    val entry = group.findEntry(name.entry!!)
    entry ?: return null

    return SearchResult(tablePackage, group, entry)
  }

  /**
   * Returns the package struct with the given name, or null if such a package does not
   * exist. The empty string is a valid package and typically is used to represent the
   * 'current' package before it is known to the ResourceTable.
   *
   * @param name the name of the package.
   * @return the [ResourceTablePackage] with the requested name or {@code null} if that package
   *   does not exist in the table.
   */
  override fun findPackage(name: String): ResourceTablePackage? {
    return packages.find { it.name == name }
  }

  fun findPackageById(id: Byte): ResourceTablePackage? {
    return packages.find { it.id == id }
  }

  fun createPackage(name: String, id: Byte = 0): ResourceTablePackage? {
    val tablePackage = findOrCreatePackage(name)

    if (id != 0.toByte()) {
      when {
        tablePackage.id == null -> {
          tablePackage.id = id
          return tablePackage
        }

        tablePackage.id != id -> return null
      }
    }

    return tablePackage
  }

  /**
   * Attempts to find a package having the specified name and ID. If not found, a new package
   * of the specified parameters is created and returned.
   */
  fun createPackageAllowingDuplicateNames(name: String, id: Byte): ResourceTablePackage {
    val match = packages.find { it.name == name && it.id == id }
    if (match != null) {
      return match
    }
    val newPackage = ResourceTablePackage(name, id)
    packages.add(newPackage)
    return newPackage
  }

  private fun findOrCreatePackage(name: String): ResourceTablePackage {
    val tablePackage = findPackage(name)
    return when (tablePackage) {
      null -> {
        val newPackage = ResourceTablePackage()
        newPackage.name = name
        packages.add(newPackage)
        newPackage
      }

      else -> tablePackage
    }
  }

  fun sort() {
    packages.sortWith(compareBy({ it.name }, { it.id }))
    for (pkg in packages) {
      pkg.groups.sortWith(compareBy({ it.type.ordinal }, { it.id }))
      for (group in pkg.groups) {
        for (entryByName in group.entries.values) {
          for (entry in entryByName.values) {
            entry.values.sortWith(compareBy({ it.config }, { it.product }))
          }
        }
      }
    }
  }

  private fun logError(source: BlameLogger.Source?, message: String) {
    logger?.error(message, source)
  }

  private fun validateName(
    nameValidator: (String) -> String, name: ResourceName, source: Source
  ): Boolean {
    val badCodePoint = nameValidator.invoke(name.entry!!)
    if (badCodePoint.isNotEmpty()) {
      logError(
        blameSource(source),
        "Resource '$name' has invalid entry name '${name.entry}'. " +
          "Invalid character '$badCodePoint'."
      )
      return false
    }
    return true
  }

  private fun addResourceImpl(
    name: ResourceName,
    id: Int,
    config: ConfigDescription,
    product: String,
    value: Value,
    nameValidator: (String) -> String,
    conflictResolver: (Value, Value) -> CollisionResult
  ): Boolean {

    val source = value.source

    if (!validateName(nameValidator, name, source)) {
      return false
    }

    val tablePackage = findOrCreatePackage(name.pck!!)
    val packageId = tablePackage.id
    if (id.isValidDynamicId() && packageId != null && id.getPackageId() != packageId) {
      logError(
        blameSource(value.source),
        "Failed to add resource '$name' with ID ${id.toString(16)} because " +
          "package '${tablePackage.name}' already has ID ${packageId.toString(16)}."
      )
      return false
    }

    val checkId = validateResources && id.isValidDynamicId()
    val useId = !validateResources && id.isValidDynamicId()

    val resourceGroup =
      tablePackage.findOrCreateGroup(name.type, if (useId) id.getTypeId() else null)
    val groupId = resourceGroup.id
    if (checkId && groupId != null && groupId != id.getTypeId()) {
      logError(
        blameSource(value.source),
        "Failed to add resource '$name' with ID ${id.toString(16)} because type " +
          "'${resourceGroup.type.tagName}' already has ID ${groupId.toString(16)}."
      )
      return false
    }

    val resourceEntry =
      resourceGroup.findOrCreateEntry(name.entry!!, if (useId) id.getEntryId() else null)
    val entryId = resourceEntry.id
    if (checkId && entryId != null && id.getEntryId() != entryId) {
      logError(
        blameSource(value.source),
        "Failed to add resource '$name' with ID ${id.toString(16)}, because resource already" +
          " has ID ${resourceIdFromParts(packageId!!, groupId!!, entryId).toString(16)}."
      )
      return false
    }

    val configValue = resourceEntry.findOrCreateValue(config, product)
    val oldValue = configValue.value
    if (oldValue == null) {
      // Resource does not exist, add it now.
      configValue.value = value
    } else {
      when (conflictResolver.invoke(oldValue, value)) {
        CollisionResult.TAKE_NEW -> configValue.value = value
        CollisionResult.CONFLICT -> {
          val previousSource =
            logger?.getOriginalSource(blameSource(oldValue.source)) ?: blameSource(oldValue.source)
          logError(
            blameSource(value.source),
            "Duplicate value for resource '$name' with config '$config' and product '$product'. " +
              "Resource was previously defined here: $previousSource."
          )
          return false
        }

        CollisionResult.KEEP_ORIGINAL -> {}
      }
    }

    if (id.isValidDynamicId()) {
      tablePackage.id = id.getPackageId()
      resourceGroup.id = id.getTypeId()
      resourceEntry.id = id.getEntryId()
    }

    return true
  }

  private fun addFileReferenceImpl(
    name: ResourceName,
    config: ConfigDescription,
    source: Source,
    path: String,
    file: File?,
    nameValidator: (String) -> String
  ): Boolean {
    val fileReference = FileReference(stringPool.makeRef(path))
    fileReference.source = source
    fileReference.file = file
    return addResourceImpl(
      name, 0, config, "", fileReference, nameValidator, ::resolveValueCollision
    )
  }

  private fun setVisibilityImpl(
    name: ResourceName,
    visibility: Visibility,
    id: Int,
    nameValidator: (String) -> String
  ): Boolean {
    val source = visibility.source
    if (!validateName(nameValidator, name, source)) {
      return false
    }

    val tablePackage = findOrCreatePackage(name.pck!!)
    val packageId = tablePackage.id
    if (id.isValidDynamicId() && packageId != null && id.getPackageId() != packageId) {
      logError(
        blameSource(source),
        "Failed to add resource '$name' with ID ${id.toString(16)} because package " +
          "'${tablePackage.name}' already has ID ${packageId.toString(16)}."
      )
      return false
    }

    val checkId = validateResources && id.isValidDynamicId()
    val useId = !validateResources && id.isValidDynamicId()

    val resourceGroup =
      tablePackage.findOrCreateGroup(name.type, if (useId) id.getTypeId() else null)
    val groupId = resourceGroup.id
    if (checkId && groupId != null && groupId != id.getTypeId()) {
      logError(
        blameSource(source),
        "Failed to add resource '$name' with ID ${id.toString(16)} because type " +
          "'${resourceGroup.type.tagName}' already has ID ${groupId.toString(16)}."
      )
      return false
    }

    val resourceEntry =
      resourceGroup.findOrCreateEntry(name.entry!!, if (useId) id.getEntryId() else null)
    val entryId = resourceEntry.id
    if (checkId && entryId != null && id.getEntryId() != entryId) {
      logError(
        blameSource(source),
        "Failed to add resource '$name' with ID ${id.toString(16)}, because resource already " +
          "has ID ${resourceIdFromParts(packageId!!, groupId!!, entryId).toString(16)}."
      )
      return false
    }

    if (id.isValidDynamicId()) {
      tablePackage.id = id.getPackageId()
      resourceGroup.id = id.getTypeId()
      resourceEntry.id = id.getEntryId()
    }

    if (visibility.level == ResourceVisibility.PUBLIC) {
      // TODO: verify what resourceGroup's visibility is used for.
      resourceGroup.visibility = ResourceVisibility.PUBLIC
    }

    when {
      visibility.level == ResourceVisibility.UNDEFINED &&
        resourceEntry.visibility.level != ResourceVisibility.UNDEFINED -> {
        // We can't undefine a symbol. Ignore
      }

      visibility.level == ResourceVisibility.PRIVATE &&
        resourceEntry.visibility.level == ResourceVisibility.PUBLIC -> {
        logError(
          blameSource(source),
          "Failed to add resource '$name' as private (java-symbol) because it was " +
            "previously defined as public."
        )
        return false
      }

      visibility.level == ResourceVisibility.PUBLIC &&
        resourceEntry.visibility.level == ResourceVisibility.PRIVATE -> {
        logError(
          blameSource(source),
          "Failed to add resource '$name' as public because it was previously defined as " +
            "private (java-symbol)."
        )
        return false
      }

      else -> {
        // This symbol definition takes precedence.
        resourceEntry.visibility = visibility
      }
    }

    return true
  }

  private fun setAllowNewImpl(
    name: ResourceName, allowNew: AllowNew, nameValidator: (String) -> String
  ): Boolean {
    if (!validateName(nameValidator, name, allowNew.source)) {
      return false
    }

    val tablePackage = findOrCreatePackage(name.pck!!)
    val group = tablePackage.findOrCreateGroup(name.type)
    val entry = group.findOrCreateEntry(name.entry!!)
    entry.allowNew = allowNew
    return true
  }

  private fun setOverlayableImpl(
    name: ResourceName, overlayable: OverlayableItem, nameValidator: (String) -> String
  ): Boolean {
    if (!validateName(nameValidator, name, overlayable.source)) {
      return false
    }

    val tablePackage = findOrCreatePackage(name.pck!!)
    val group = tablePackage.findOrCreateGroup(name.type)
    val entry = group.findOrCreateEntry(name.entry!!)

    val oldEntry = entry.overlayable
    if (oldEntry != null) {
      val previousSource =
        logger?.getOriginalSource(blameSource(oldEntry.source)) ?: blameSource(oldEntry.source)
      logError(
        blameSource(overlayable.source),
        "Failed to add overlayable declaration for resource '$name', because resource already " +
          "has an overlayable defined here: $previousSource."
      )
      return false
    }
    entry.overlayable = overlayable
    return true
  }

  companion object {
    fun resolveValueCollision(existing: Value, incoming: Value): CollisionResult {
      val existingAttr = existing as? AttributeResource
      val incomingAttr = incoming as? AttributeResource

      incomingAttr ?: return when {
        incoming.weak -> CollisionResult.KEEP_ORIGINAL
        existing.weak -> CollisionResult.TAKE_NEW
        else -> CollisionResult.CONFLICT
      }

      existingAttr ?: return when {
        existing.weak -> CollisionResult.TAKE_NEW
        else -> CollisionResult.CONFLICT
      }

      // Attribute specific handling. Since declarations and definitions of attributes can happen
      // almost anywhere, we need special handling to see which definition sticks.
      if (existingAttr.isCompatibleWith(incomingAttr)) {
        // The two attributes are both DECLs, but they are plain attributes with compatible formats.
        // keep the strongest.
        return if (existingAttr.weak) CollisionResult.TAKE_NEW else CollisionResult.KEEP_ORIGINAL
      }

      if (existingAttr.weak && existingAttr.typeMask == Resources.Attribute.FormatFlags.ANY_VALUE) {
        // Any incoming attribute is better than this.
        return CollisionResult.TAKE_NEW
      }

      if (incomingAttr.weak && incomingAttr.typeMask == Resources.Attribute.FormatFlags.ANY_VALUE) {
        // The incoming attribute may be a USE instead of a DECL. Keep the existing attribute.
        return CollisionResult.KEEP_ORIGINAL
      }

      return CollisionResult.CONFLICT
    }

    fun resourceNameValidator(name: String): String =
      if (isValidResourceEntryName(name)) "" else name

    fun skipNameValidator(name: String): String {
      return ""
    }
  }
}

/** the public status of a resource */
class Visibility(
  val source: Source = Source(""),
  val comment: String = "",
  val level: ResourceVisibility = ResourceVisibility.UNDEFINED
)

/** Represents <add-resource> in an overlay */
class AllowNew(
  val source: Source,
  val comment: String
)

class Overlayable(
  val name: String,
  val actor: String,
  val source: Source
) {

  constructor() : this("", "", Source(""))

  companion object {
    const val ACTOR_SCHEME = "overlay"
    const val ACTOR_SCHEME_URI = "$ACTOR_SCHEME://"
  }

  override fun equals(other: Any?): Boolean {
    if (other is Overlayable) {
      return name == other.name && actor == other.actor
    }
    return false
  }
}

class OverlayableItem(
  val overlayable: Overlayable,
  val policies: Int = Policy.NONE,
  val comment: String = "",
  val source: Source = Source("")
) {

  override fun equals(other: Any?): Boolean {
    if (other is OverlayableItem) {
      return overlayable == other.overlayable && policies == other.policies
    }
    return false
  }

  /** Represents the types of overlays that are allowed to overlay the resource. */
  object Policy {
    const val NONE = 0

    /** The resource can be overlaid by any overlay. */
    const val PUBLIC = 1 shl 0

    /** The resource can be overlaid by any overlay on the system partition. */
    const val SYSTEM = 1 shl 1

    /** The resource can be overlaid by any overlay on the vendor partition. */
    const val VENDOR = 1 shl 2

    /** The resource can be overlaid by any overlay on the product partition. */
    const val PRODUCT = 1 shl 3

    /** The resource can be overlaid by any overlay signed with the same signature as its actor. */
    const val SIGNATURE = 1 shl 4

    /** The resource can be overlaid by any overlay on the odm partition. */
    const val ODM = 1 shl 5

    /** The resource can be overlaid by any overlay on the oem partition. */
    const val OEM = 1 shl 6
  }
}

/** Represents all groups by type under a single package. */
class ResourceTablePackage(override var name: String = "", var id: Byte? = null) :
  IResourceTablePackage {

  internal val groups = mutableListOf<ResourceGroup>()

  override fun findGroup(type: AaptResourceType, groupId: Byte?) =
    if (groupId != null) {
      groups.find { type == it.type && groupId == it.id }
        ?: groups.find { type == it.type && it.id == null }
    } else {
      groups.find { type == it.type }
    }

  fun findOrCreateGroup(type: AaptResourceType, groupId: Byte? = null): ResourceGroup {
    val group = findGroup(type, groupId)
    return when (group) {
      null -> {
        val newGroup = ResourceGroup(type)
        newGroup.id = groupId
        groups.add(newGroup)
        newGroup
      }

      else -> group
    }
  }
}

/**
 * Represents all resource entries grouped under a resource type (eg. string, drawable, layout,
 * etc.).
 */
class ResourceGroup(val type: AaptResourceType) : IResourceGroup {
  var id: Byte? = null
  var visibility = ResourceVisibility.UNDEFINED

  internal val entries = sortedMapOf<String, SortedMap<Short?, ResourceEntry>>()

  // To get Styleable's children we need to reach the ResourceEntry's value first
  internal fun getStyleable(entry: Map.Entry<String, SortedMap<Short?, ResourceEntry>>): Styleable {
    // To get the actual value we need to find the correct Item that's nested deep in the map
    // TODO: Keep children at map value level.
    val mapEntry = entry.value
    val styleableContainer = mapEntry.values.first().values
    // Only one Item should be held in the container
    if (styleableContainer.size != 1)
      error("Too many resources in one entry: ${styleableContainer.size}")
    // The children will be present under the Styleable Item.
    return styleableContainer[0].value!! as Styleable
  }

  override fun findEntry(name: String, entryId: Short?): ResourceEntry? {
    val nameGroup = entries[name] ?: return null
    return if (entryId != null) {
      nameGroup[entryId] ?: nameGroup[null]
    } else {
      nameGroup[nameGroup.firstKey()]
    }
  }

  override fun findEntries(entryId: Short?, predicate: (String) -> Boolean): List<IResourceEntry> {
    return entries.mapNotNull { (key, value) ->
      if (!predicate(key)) return@mapNotNull null
      if (entryId != null) {
        value[entryId] ?: value[null]
      } else {
        value[value.firstKey()]
      }
    }
  }

  fun findOrCreateEntry(name: String, entryId: Short? = null): ResourceEntry {
    val entry = findEntry(name, entryId)
    return when (entry) {
      null -> {
        val newEntry = ResourceEntry(name)
        newEntry.id = entryId
        entries.getOrPut(name) { TreeMap(nullsFirst()) }[entryId] = newEntry
        newEntry
      }

      else -> entry
    }
  }
}

/** Represents a resource entry, which may have varying values for each defined configuration. */
class ResourceEntry(override val name: String) : IResourceEntry {
  var id: Short? = null
  var visibility = Visibility(Source(""), "", ResourceVisibility.UNDEFINED)

  var allowNew: AllowNew? = null
  var overlayable: OverlayableItem? = null

  override val values = mutableListOf<ResourceConfigValue>()

  override fun findValue(config: ConfigDescription, product: String): ResourceConfigValue? {
    return values.find { it.config == config && it.product == product }
  }

  fun findOrCreateValue(config: ConfigDescription, product: String = ""): ResourceConfigValue {
    val configValue = findValue(config, product)
    return when (configValue) {
      null -> {
        val newConfigValue = ResourceConfigValue(config, product)
        values.add(newConfigValue)
        newConfigValue
      }

      else -> configValue
    }
  }
}
