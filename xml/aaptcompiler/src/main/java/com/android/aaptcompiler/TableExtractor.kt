package com.android.aaptcompiler

import com.android.aapt.Resources
import com.android.aapt.Resources.Attribute.FormatFlags
import com.android.aaptcompiler.AaptResourceType.ID
import com.android.aaptcompiler.AaptResourceType.STYLE
import com.android.aaptcompiler.StringPool.Context
import com.android.aaptcompiler.StringPool.Context.Priority.NORMAL
import com.android.aaptcompiler.android.stringToInt
import com.itsaky.androidide.layoutlib.resources.ResourceVisibility
import java.io.InputStream
import jaxp.xml.XMLConstants
import jaxp.xml.namespace.QName
import jaxp.xml.stream.XMLEventReader
import jaxp.xml.stream.XMLStreamConstants
import jaxp.xml.stream.XMLStreamException
import jaxp.xml.stream.events.Attribute
import jaxp.xml.stream.events.Comment
import jaxp.xml.stream.events.StartElement
import jaxp.xml.stream.events.XMLEvent

/**
 * Namespace uri for the xliff:g tag in XML.
 *
 * This is used to identify the xliff:g spans in processed strings, in order to mark the
 * untranslatable sections of string resources.
 */
private const val XLIFF_NS_URI = "urn:oasis:names:tc:xliff:document:1.2"

/**
 * Resource parsed from the XML, with all relevant metadata.
 *
 * @property config The config description of the resource. This should be the same as the config
 *   of the source.
 * @property source The start location in the xml from which this resource was extracted.
 * @property comment The comment describing the resource that appeared before it in the xml. This
 *   will be an empty string if no comment was supplied.
 */
private class ParsedResource(
  var config: ConfigDescription, val source: Source, val comment: String) {

  constructor() : this(ConfigDescription(), Source(""), "")

  /** The name of the resource extraccted from the xml. */
  var name: ResourceName = ResourceName.EMPTY
  /** The product name for the given resource value. */
  var productString: String = ""
  /** The id of the resource. A value of 0 means the id was not supplied. */
  var resourceId = 0
  /** The visibility of the extracted resource. */
  var visibility = ResourceVisibility.UNDEFINED
  /** Whether the resource has <add-resource> in an overlay. */
  var allowNew = false
  /**
   * The overlayable representation of this resource. This value is {@code null} if it is not
   * overlayable.
   */
  var overlayableItem: OverlayableItem? = null
  /**
   * The value of the resource, this might be null if this is a use of a resource. (i.e. an
   * <attr> within a <declare-styleable>
   */
  var value : Value? = null
  /**
   * The child resources of the given resource. These resources will be added to the table when
   * {@code this} is added. The connection of the resources to this resource should be reflected in
   * the [value] of the parsed resource.
   */
  val children = mutableListOf<ParsedResource>()
}

/**
 * All options for the Table Extractor.
 *
 * @property translatable Whether the default setting for this parser is to allow translation.
 *
 * @property errorOnPositionalArgs Whether positional arguments in formatted strings are treated as
 *   errors or warnings.
 *
 * @property visibility the default visibility of resources extracted. If non-null, all new
 *   resources are set with this visibility, and will error if trying to parse the <public>,
 *   <public-group>, <java-symbol> or <symbol> tags.
 */
data class TableExtractorOptions(
  val translatable: Boolean = true,
  val errorOnPositionalArgs: Boolean = true,
  val visibility: ResourceVisibility? = null)

/** Returns true if the element is <skip> or <eat-comment> and can be safely ignored */
fun shouldIgnoreElement(elementName: QName): Boolean {
  return elementName.namespaceURI.isEmpty() &&
    (elementName.localPart == "skip" || elementName.localPart == "eat-comment")
}

fun parseFormatNoEnumsOrFlags(name: String): Int =
  when (name) {
    "reference" -> Resources.Attribute.FormatFlags.REFERENCE_VALUE
    "string" -> Resources.Attribute.FormatFlags.STRING_VALUE
    "integer" -> Resources.Attribute.FormatFlags.INTEGER_VALUE
    "boolean" -> Resources.Attribute.FormatFlags.BOOLEAN_VALUE
    "color" -> Resources.Attribute.FormatFlags.COLOR_VALUE
    "float" -> Resources.Attribute.FormatFlags.FLOAT_VALUE
    "dimension" -> Resources.Attribute.FormatFlags.DIMENSION_VALUE
    "fraction" -> Resources.Attribute.FormatFlags.FRACTION_VALUE
    else -> 0
  }

fun parseFormatType(name: String): Int =
  when (name) {
    "enum" -> Resources.Attribute.FormatFlags.ENUM_VALUE
    "flags" -> Resources.Attribute.FormatFlags.FLAGS_VALUE
    else -> parseFormatNoEnumsOrFlags(name)
  }

fun parseFormatAttribute(value: String): Int {
  var mask = 0
  for (part in value.split('|')) {
    val type = parseFormatType(part.trim())
    if (type == 0) {
      return 0
    }
    mask = mask or type
  }
  return mask
}

/**
 * Class that parses an XML file for resources and adds them to a ResourceTable.
 *
 * <p> Primarily, as each resource needs to be parsed, the name, package, and type of resource is
 * extracted.
 *
 * <p> Then, if an item, a call to [parseItem] is invoked with a type mask of the valid types
 * the xml element can be parsed as. This will result in a more specialized call to [parseXml]
 * which will proceed to flatten the xml subtree of the item (which may include span tags and
 * untranslatable section tags) and then attempt to process the flattened xml string (in accordance
 * with the valid types specified by the type mask). Finally, the parsed resource value will be
 * added to [ResourceTable], if successful.
 *
 * <p> Bag types (resources whose xml element's children also represent resources), i.e. array, are
 * handled a little differently. After the resource name is extracted, the appropriate bag-parsing
 * method is invoked, which will in turn call [parseItem] with a type mask of the valid types that
 * the bag allows. After all child elements have been parsed successfully, the bag resource and all
 * child resources are added to the [ResourceTable]
 *
 * @property table The resource table for the extracted resources to be added.
 * @property source The source of the extracted resources.
 * @property config The config description of the source.
 * @property options The options with how the resources should be extracted.
 */
class TableExtractor(
  val table: ResourceTable,
  val source: Source,
  val config: ConfigDescription,
  val options: TableExtractorOptions,
  val logger: BlameLogger
) {

  fun extract(inputFile: InputStream) {
    var eventReader : XMLEventReader? = null
    try {
      eventReader = xmlInputFactory.createXMLEventReader(inputFile)

      val documentStart = eventReader.nextEvent()
      if (!documentStart.isStartDocument) {
        val userReadableSource = logger.getOriginalSource(blameSource(source)).toString().trim()
        error("Failed to find start of XML $userReadableSource")
      }

      var rootStart: XMLEvent? = null
      while(eventReader.hasNext()) {
        rootStart = eventReader.nextEvent()
        // ignore comments and text before the root tag
        if (rootStart.isStartElement) {
          break
        }
      }
      rootStart ?: return

      val rootName = rootStart.asStartElement().name
      if (rootName.namespaceURI != null && rootName.localPart != "resources") {
        val userReadableSource = logger.getOriginalSource(blameSource(source)).toString().trim()
        error(
            "Root xml element of resource table not labeled 'resources' ($userReadableSource)."
        )
      }
      extractResourceValues(eventReader)
    } catch (xmlException: XMLStreamException) {
      if (xmlException.message?.contains("Premature end of file.", true) != true) {
          // Having no root is not an error, but any other xml format exception is.
          throw xmlException
      }
    } finally {
      eventReader?.close()
    }
  }

  private fun logError(source: BlameLogger.Source, message: String) {
      logger.error(message, source)
  }

  /**
   * Extracts all the resources from the given eventReader.
   *
   * <p> The eventReader is assumed have just read the root "resources" start element. All resource
   * values extracted are added to the [table] property.
   *
   * @param eventReader: The source of the resources to extract. This is expected to be directly
   * after the root xml element when this method is invoked. The eventReader will be after the
   * corresponding end element when this method returns.
   */
  private fun extractResourceValues(eventReader: XMLEventReader) {

    val errors = mutableListOf<String>()
    var comment = ""

    while (eventReader.hasNext()) {
      val event = eventReader.nextEvent()

      if (event.eventType == XMLStreamConstants.COMMENT) {
        comment = (event as Comment).text.trim()
        continue
      }

      if (event.isCharacters) {
        if (!event.asCharacters().isWhiteSpace) {
          // non-whitespace characters are not allowed here
          errors += "Plain text is not allowed at ${blameSource(source, event.location)}."
        }
        continue
      }

      if (event.isEndElement) {
        // we've exhausted all resources
        break
      }

      if (!event.isStartElement) {
        errors +=
            "Unexpected element type: ${event.eventType}, ${blameSource(source, event.location)}."
      }

      val element = event.asStartElement()
      val elementName = element.name
      if (elementName.namespaceURI.isNotEmpty()) {
        // skip unrecognized namespaces
        walkToEndOfElement(element.asStartElement(), eventReader)
        continue
      }

      if (elementName.localPart == "skip" || elementName.localPart == "eat-comment") {
        comment = ""
        walkToEndOfElement(element.asStartElement(), eventReader)
        continue
      }

      val parsedResource =
        ParsedResource(config, source.withLine(element.location.lineNumber), comment)
      comment = ""

      // extract the product name if possible
      val productName = element.getAttributeByName(QName("product"))
      if (productName != null) {
        parsedResource.productString = productName.value
      }

      if (!extractResource(element, eventReader, parsedResource)) {
        errors += "Can not extract resource from $parsedResource."
      }

      if (!addResourceToTable(parsedResource)) {
        errors += "Can not add resource ($parsedResource) to table."
      }
    }
    if (errors.any()) {
        error(errors.joinToString(separator = ","))
    }
  }

  /**
   * Extracts the [Value] of a resource from the given element. This can be either an [Item] or a
   *   nested value type.
   *
   * @param element The start of the element to be translated as a [Value].
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param parsedResource The [ParsedResource] to hold the extracted value upon success.
   * @return Whether or not the parsing was a success.
   */
  private fun extractResource(
    element : StartElement,
    eventReader : XMLEventReader,
    parsedResource : ParsedResource): Boolean {

    var resourceTypeName = element.name.localPart

    // the format of the value of this resource.
    var resourceFormat = 0

    var canBeItem = true
    var canBeBag = true

    if (resourceTypeName == "item") {
      canBeBag = false

      // the default format for <item> is any. This can be overridden by the format attribute
      resourceFormat = Resources.Attribute.FormatFlags.ANY_VALUE

      val formatAttribute = element.getAttributeByName(QName("format"))
      if (formatAttribute != null) {
        resourceFormat = parseFormatNoEnumsOrFlags(formatAttribute.value)
        if (resourceFormat == 0) {
          logError(
            blameSource(parsedResource.source),
            "Resource has an invalid format of ${formatAttribute.value}.")
          walkToEndOfElement(element, eventReader)
          return false
        }
      }

      // Items have their type encoded in the type attribute.
      val typeAttribute = element.getAttributeByName(QName("type"))
      if (typeAttribute == null) {
        logError(blameSource(parsedResource.source), "<item> must have a 'type' attribute")
        walkToEndOfElement(element, eventReader)
        return false
      }
      resourceTypeName = typeAttribute.value

    } else if (resourceTypeName == "bag") {
      canBeItem = false

      // Bags have their type encoded in the type attribute.
      val typeAttribute = element.getAttributeByName(QName("type"))
      if (typeAttribute == null) {
        logError(blameSource(parsedResource.source), "<bag> must have a 'type' attribute")
        walkToEndOfElement(element, eventReader)
        return false
      }
    }

    // get name of the resource. This will be checked later, because not all xml elements require
    // a name.
    val nameAttribute = element.getAttributeByName(QName("name"))

    if (resourceTypeName == "id") {
      if (nameAttribute == null) {
        logError(
          blameSource(parsedResource.source), "<${element.name}> is missing the 'name' attribute.")
        walkToEndOfElement(element, eventReader)
        return false
      }

      // Grab the name of the resource. This will be validated later, as not all XML resources
      // require a name.
      parsedResource.name =
        parsedResource.name.copy(type = ID, entry = nameAttribute.value)
      parseItem(element, eventReader, parsedResource, resourceFormat)

      val item = parsedResource.value
      when {
        item is BasicString && item.ref.value().isEmpty() ->
          // If no inner element exists, represent a unique identifier
          parsedResource.value = Id()
        item is Reference && item.id == null ->
          // A null reference also means there is no inner element when ids are in the form:
          //    <id name="name"/>
          parsedResource.value = Id()
        (item is Reference && item.name.type != ID) || item !is Reference -> {
          // if an inner element exists, the inner element must be a reference to another id
          logError(
            blameSource(parsedResource.source),
            "<${element.name}> inner element must either be a resource reference or empty.")
          return false
        }
      }
      return true
    }

    if (canBeItem) {
      val (type, typeMask) = when (resourceTypeName) {
        "bool" -> Pair(AaptResourceType.BOOL, Resources.Attribute.FormatFlags.BOOLEAN_VALUE)
        "color" -> Pair(AaptResourceType.COLOR, Resources.Attribute.FormatFlags.COLOR_VALUE)
        "configVarying" ->
          Pair(AaptResourceType.CONFIG_VARYING, Resources.Attribute.FormatFlags.ANY_VALUE)
        "dimen" ->
          Pair(
            AaptResourceType.DIMEN,
            Resources.Attribute.FormatFlags.FLOAT_VALUE or
              Resources.Attribute.FormatFlags.FRACTION_VALUE or
              Resources.Attribute.FormatFlags.DIMENSION_VALUE)
        "drawable" -> Pair(AaptResourceType.DRAWABLE, Resources.Attribute.FormatFlags.COLOR_VALUE)
        "fraction" ->
          Pair(
            AaptResourceType.FRACTION,
            Resources.Attribute.FormatFlags.FLOAT_VALUE or
              Resources.Attribute.FormatFlags.FRACTION_VALUE or
              Resources.Attribute.FormatFlags.DIMENSION_VALUE)
        "integer" -> Pair(AaptResourceType.INTEGER, Resources.Attribute.FormatFlags.INTEGER_VALUE)
        "string" -> Pair(AaptResourceType.STRING, Resources.Attribute.FormatFlags.STRING_VALUE)
        else -> Pair(null, Resources.Attribute.FormatFlags.ANY_VALUE)
      }
      if (type != null) {
        // this is an item record its type and format and start parsing.
        if (nameAttribute == null) {
          logError(
            blameSource(parsedResource.source),
            "<${element.name}> is missing the 'name' attribute.")
          walkToEndOfElement(element, eventReader)
          return false
        }

        parsedResource.name = ResourceName( "", type, nameAttribute.value)

        // Only use the implied format of the type when there is no explicit format.
        if (resourceFormat == 0) {
          resourceFormat = typeMask
        }
        return parseItem(element, eventReader, parsedResource, resourceFormat)
      }
    }

    if (canBeBag) {
      val parseBagMethod = when(resourceTypeName) {
        "add-resource" -> ::parseAddResource
        "array" -> ::parseArray
        "attr" -> ::parseAttr
        "configVarying" -> ::parseConfigVarying
        "declare-styleable" -> ::parseDeclareStyleable
        "integer-array" -> ::parseIntegerArray
        "java-symbol" -> ::parseSymbol
        "macro" -> ::parseMacro
        "overlayable" -> ::parseOverlayable
        "plurals" -> ::parsePlural
        "public" -> ::parsePublic
        "public-group" -> ::parsePublicGroup
        "string-array" -> ::parseStringArray
        "style" -> ::parseStyle
        "symbol" -> ::parseSymbol
        else -> null
      }

      if (parseBagMethod != null) {
        // ensure we have a name (unless this is a <public-group> or <overlayable>).
        if (resourceTypeName != "public-group" && resourceTypeName != "overlayable") {
          if (nameAttribute == null) {
            walkToEndOfElement(element, eventReader)
            logError(
              blameSource(parsedResource.source),
              "<${element.name}> is missing the 'name' attribute.")
            return false
          }

          parsedResource.name = parsedResource.name.copy(entry=nameAttribute.value)
        }

        // Call the associated parse method. The type will be filled in by the parse function
        return parseBagMethod(element, eventReader, parsedResource)
      }

    }

    if (canBeItem) {
      val parsedType = resourceTypeFromTag(resourceTypeName)
      if (parsedType != null) {
        if (nameAttribute == null) {
          logError(
            blameSource(parsedResource.source),
            "<${element.name}> is missing the 'name' attribute.")
          walkToEndOfElement(element, eventReader)
          return false
        }

        parsedResource.name = ResourceName("", parsedType, nameAttribute.value)
        parsedResource.value =
          parseXml(element, eventReader, Resources.Attribute.FormatFlags.REFERENCE_VALUE, false)

        if (parsedResource.value == null) {
          logError(
            blameSource(parsedResource.source),
            "Invalid value for type '${parsedType.tagName}'. Expected a reference.")
          return false
        }

        return true
      }
    }

    logError(blameSource(parsedResource.source), "Unknown resource type '$resourceTypeName'")
    walkToEndOfElement(element, eventReader)
    return false
  }

  /**
   * Parses the XML subtree and returns an Item.
   *
   * @param element The start of the element to be translated as an item type.
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param resourceFormat A type mask that specifies which formats are valid for the xml to be
   *   interpreted as.
   * @param allowRawString If true, a [RawString] representing the xml is returned if it could not
   *   be parsed as any valid resource [Item]. If false, {@code null} will be returned instead on
   *   failure.
   *
   * @return The [Item] that represents the xml subtree. This will be {@code null} if the xml failed
   *   to be interpreted as a valid resource.
   */
  private fun parseXml(
    element: StartElement,
    eventReader: XMLEventReader,
    resourceFormat: Int,
    allowRawString : Boolean) : Item? {

    val flattenedXml = flattenXmlSubTree(element, eventReader)
    if (!flattenedXml.success) {
      return null
    }

    if (flattenedXml.styleString.spans.isNotEmpty()) {
      // can only be a StyledString
      return StyledString(
        table.stringPool.makeRef(
          flattenedXml.styleString,
          Context(NORMAL.priority, config)
        ),
        flattenedXml.untranslatableSections)
    }

    // Process the raw value
    val processedItem =
      tryParseItemForAttribute(flattenedXml.rawString, resourceFormat) {
        val id = Id()
        id.source = source
        table.addResource(it, ConfigDescription(), "", id)
      }

    if (processedItem != null) {
      // Fix up the reference.
      if (processedItem is Reference) {
        resolvePackage(element, processedItem)
      }
      return processedItem
    }

    // Try making a regular string.
    if (resourceFormat and Resources.Attribute.FormatFlags.STRING_VALUE != 0) {
      // use trimmed escaped string.
      return BasicString(
        table.stringPool.makeRef(
          flattenedXml.styleString.str, Context(config = config)
        ),
        flattenedXml.untranslatableSections)
    }

    // if the text is empty, and the value is not allowed to be a string, encode it as a @null.
    if (flattenedXml.rawString.trim().isEmpty()) {
      return makeNull()
    }

    if (allowRawString) {
        val raw = flattenedXml.rawString.let { raw ->
            // Remove space, newline character wrapping (typically due to IDE formatting)
            // and user added quotations for styleable children due to lack of type.
            val isNotWrappingChar: (Char) -> Boolean = { it !in setOf(' ', '\n') }
            val firstNonWrappingIndex = raw.indexOfFirst(isNotWrappingChar)
            val lastNonWrappingIndex = raw.indexOfLast(isNotWrappingChar)
            raw.substring(
                if (firstNonWrappingIndex == - 1) 0 else firstNonWrappingIndex,
                if (lastNonWrappingIndex == - 1) 0 else lastNonWrappingIndex + 1
            )
                .removeSurrounding("\"")
        }
      return RawString(
        table.stringPool.makeRef(raw, Context(config=config)))
    }

    return null
  }

  /**
   * Attempts to parse the xml subtree as an item resource.
   *
   * @param element The start of the element to be translated as an item type.
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param parsedResource The resource to put the parsed [Item] into, if successful.
   * @param resourceFormat A type mask that specifies which formats are valid for the xml to be
   *   interpreted as.
   * @return Whether or not the xml could be parsed.
   */
  private fun parseItem(
    element: StartElement,
    eventReader: XMLEventReader,
    parsedResource: ParsedResource,
    resourceFormat: Int) : Boolean {

    if (resourceFormat == Resources.Attribute.FormatFlags.STRING_VALUE) {
      return parseString(element, eventReader, parsedResource)
    }

    parsedResource.value = parseXml(element, eventReader, resourceFormat, false)
    if (parsedResource.value == null) {
      logError(
        blameSource(parsedResource.source),
        "Invalid <${parsedResource.name.type.tagName}> for given resource value.")
      return false
    }
    return true
  }

  /**
   * Attempts to parse the xml element as a String, including whether the string is formatted or
   * translatable.
   *
   * @param element The start of the element to be translated at a string.
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param parsedResource the resource to put the parsed String into. The [ParsedResource.value]
   *   will be set to either a [BasicString] or [StyleString] resource, if successful.
   * @return Whether or not the element could be parsed as a String resource.
   */
  private fun parseString(
    element: StartElement, eventReader: XMLEventReader, parsedResource: ParsedResource): Boolean {
    var formatted = true
    val formattedAttribute = element.getAttributeByName(QName("formatted"))
    if (formattedAttribute != null) {
      val maybeFormatted = parseAsBool(formattedAttribute.value)
      if (maybeFormatted == null) {
        logError(
          blameSource(parsedResource.source),
          "Invalid value for the 'formatted' attribute. " +
                  "Was '${formattedAttribute.value}', must be a boolean.")
        walkToEndOfElement(element, eventReader)
        return false
      }
      formatted = maybeFormatted
    }

    var translatable = options.translatable
    val translatableAttribute = element.getAttributeByName(QName("translatable"))
    if (translatableAttribute != null) {
      val maybeTranslatable = parseAsBool(translatableAttribute.value)
      if (maybeTranslatable == null) {
        logError(
          blameSource(parsedResource.source),
          "Invalid value for 'translatable' attribute. " +
                  "Was '${translatableAttribute.value}', must be a boolean.")
        walkToEndOfElement(element, eventReader)
        return false
      }
      translatable = maybeTranslatable
    }

    val value =
      parseXml(element, eventReader, Resources.Attribute.FormatFlags.STRING_VALUE, false)
    if (value == null) {
      logError( blameSource(parsedResource.source), "${parsedResource.name} does not contain a valid string resource.")
      return false
    }

    if (value is BasicString) {
      value.translatable = translatable

      if (formatted && translatable) {
        if (!verifyJavaStringFormat(value.toString())) {
          val errorMsg = "Multiple substitutions specified in non-positional format of string " +
                  "resource ${parsedResource.name}. Did you mean to add the formatted=\"false\" attribute?"
          if (options.errorOnPositionalArgs) {
            logError(blameSource(parsedResource.source), errorMsg)
            return false
          }
          logger.warning(errorMsg, blameSource(parsedResource.source))
        }
      }
    } else if (value is StyledString) {
      value.translatable = translatable
    }
    parsedResource.value = value
    return true
  }

  /**
   * Parses the xml element specified by {@code element} as a Enum or Flag value of an Attribute.
   *
   * @param element The start element that represents the symbol to be parsed.
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param tag The name of the flag or enum item.
   * @return The child resource if the parsing was successful, or {@code null} if the parsing
   *   failed.
   */
  private fun parseEnumOrFlagItem(
    element: StartElement, eventReader: XMLEventReader, tag: String): AttributeResource.Symbol? {
    val elementSource = source.withLine(element.location.lineNumber)

    walkToEndOfElement(element, eventReader)

    val nameAttribute = element.getAttributeByName(QName("name"))
    if (nameAttribute == null) {
      logError(blameSource(elementSource), "No attribute 'name' found for tag <$tag>.")
      return null
    }

    val valueAttribute = element.getAttributeByName(QName("value"))
    if (valueAttribute == null) {
      logError(blameSource(elementSource), "No attribute 'value' found for tag <$tag>.")
      return null
    }

    val resValue = stringToInt(valueAttribute.value)
    if (resValue == null) {
      logError(
        blameSource(elementSource), "Invalid value 'null' for <$tag>. Must be an integer.")
      return null
    }

    val reference = Reference()
    reference.name = ResourceName("", ID, nameAttribute.value)
    return AttributeResource.Symbol(reference, resValue.data, resValue.dataType.byteValue)
  }

  /**
   * Parses the xml element specified by {@code element} as an [Item] under the style.
   *
   * @param element The start element that represents the symbol to be parsed.
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param style The parent style of which this resource is a part.
   * @return The child resource if the parsing was successful, or {@code null} if the parsing
   *   failed.
   */
  private fun parseStyleItem(
    element: StartElement, eventReader: XMLEventReader, style: Style
  ): Boolean {
    val itemSource = source.withLine(element.location.lineNumber)

    val nameAttribute = element.getAttributeByName(QName("name"))
    if (nameAttribute == null) {
      logError(blameSource(source, element.location), "<item> must have a 'name' attribute.")
      walkToEndOfElement(element, eventReader)
      return false
    }

    val key = parseXmlAttributeName(nameAttribute.value)

    resolvePackage(element, key)
    key.source = itemSource

    val xmlItem = parseXml(element, eventReader, 0, true)
    if (xmlItem == null) {
      logError(
        blameSource(source, element.location),
        "Could not parse style item with name '${nameAttribute.value}'.")
      return false
    }

    style.entries.add(Style.Entry(key, xmlItem))
    return true
  }

  /**
   * Parses the XML subtree as a StyleString (flattened XML representation for strings with
   * formatting).
   *
   * @param eventReader the xml to flattened. The reader should have just read the start of the
   *   element that is needed to be flattened. After this method is invoked, the event reader will
   *   be after the end of the element that the flattened xml is to represent.
   * @return
   *   <p> If Parsing fails, the [FlattenedXml.success] fill be set to false and the rest of the
   *   flattened xml will be left in a unspecified state.
   *   <p> Otherwise:
   *   [FlattenedXml.styleString] contains the escaped and whitespace trimmed text with included
   *     spans.
   *   [FlattenedXml.rawString] contains the unescaped text.
   *   [FlattenedXml.untranslatableSections] contains the sections of the string that should not be
   *     translated.
   */
  private fun flattenXmlSubTree(
    startElement: StartElement, eventReader: XMLEventReader) : FlattenedXml {

    var depth = 1

    val builder = XmlStringBuilder()

    while (depth > 0) {
      val event = eventReader.nextEvent()

      when {
        event.isCharacters -> builder.append(event.asCharacters().data)

        event.isStartElement -> {

          val element = event.asStartElement()
          val elementName = element.name

          when (elementName.namespaceURI) {
            XMLConstants.NULL_NS_URI -> {
              // This is an HTML tag which we encode as a span.
              val spanName = StringBuilder(elementName.localPart)
              val attributes = element.attributes
              while (attributes.hasNext()) {
                val attribute = attributes.next() as Attribute
                spanName.append(";${attribute.name.localPart}=${attribute.value}")
              }
              builder.startSpan(spanName.toString())
            }
            XLIFF_NS_URI -> {
              // This is an XLIFF tag which is not encoded as a span.
              if (elementName.localPart == "g") {
                // start untranslatable 'g' tag. Unknown XLIFF tags are ignored.
                builder.startUntranslatable()
              }
            }
            else -> {
              // besides XLIFF, any other namespaced tags are unsupported and ignored.
              logger.warning(
                "Ignoring element '$elementName' with unknown namespace '${elementName.namespaceURI}'.",
                blameSource(source.withLine(element.location.lineNumber))
              )
            }

          }
          ++depth
        }
        event.isEndElement -> {

          val element = event.asEndElement()
          val elementName = element.name

          --depth
          when (elementName.namespaceURI) {
            XMLConstants.NULL_NS_URI -> {
              if (depth != 0) {
                builder.endSpan()
              }
            }
            XLIFF_NS_URI -> {
              if (elementName.localPart == "g") {
                builder.endUntranslatable()
              }
            }
          }
        }
      }
    }

    val flattenedXml = builder.getFlattenedXml()
    if (builder.error.isNotEmpty()) {
      val resourceName = startElement.getAttributeByName(QName("name")).value
      logError(
        blameSource(source, startElement.location),
        "Failed to flatten XML for resource '$resourceName' with error: ${builder.error}")
    }
    return flattenedXml
  }

  /**
   * Parses the xml with a "symbol" tag
   *
   * @param element The start element that represents the symbol to be parsed.
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param parsedResource where the parsed symbol will be stored, if the parsing was successful.
   * @return Whether of not the parsing was successful.
   */
  private fun parseSymbol(
    element: StartElement, eventReader: XMLEventReader, parsedResource: ParsedResource): Boolean {
    var error = false
    if (options.visibility != null) {
      logError(
        blameSource(source, element.location),
        "<java-symbol> and <symbol> tags are not supported with resource visibility.")
      error = true
    }

    // Symbols should have the default config
    if (parsedResource.config != ConfigDescription()) {
      logger.warning(
        "Ignoring configuration '${parsedResource.config}' for <${element.name}> tag.",
        blameSource(source, element.location)
      )
    }

    if (!parseSymbolImpl(element, eventReader, parsedResource)) {
      return false
    }

    parsedResource.visibility = ResourceVisibility.PRIVATE
    return !error
  }

  private fun parseMacro(
    element: StartElement, eventReader: XMLEventReader, parsedResource: ParsedResource): Boolean {
    parsedResource.name = parsedResource.name.copy(type = AaptResourceType.MACRO)

    // Macros can only be defined in the default config
    val defaultConfig = ConfigDescription()
    if (parsedResource.config != defaultConfig) {
      logError(
        blameSource(source, element.location),
        "<macro> tags cannot be declared in configurations other than the default configuration")
      return false
    }

    val flattenedXml = flattenXmlSubTree(element, eventReader)
    if (!flattenedXml.success) {
        return false
    }

    // TODO(198264572): extract namespaces
    parsedResource.value = Macro(flattenedXml.rawString, flattenedXml.styleString, flattenedXml.untranslatableSections)

    return true
  }

  /**
   * Parses the xml with an "add-resource" tag.
   *
   * @param element The start element that represents the symbol to be parsed.
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param parsedResource where the parsed symbol will be stored, if the parsing was successful.
   * @return Whether of not the parsing was successful.
   */
  private fun parseAddResource(
    element: StartElement, eventReader: XMLEventReader, parsedResource: ParsedResource): Boolean {
    if (parseSymbolImpl(element, eventReader, parsedResource)) {
      parsedResource.visibility = ResourceVisibility.UNDEFINED
      parsedResource.allowNew = true
      return true
    }
    return false
  }

  /**
   * Parses the xml as a Symbol represented by the specified start element. Then, stores the
   * value in the parsed resource.
   *
   * @param element The start element that represents the symbol to be parsed.
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param parsedResource where the parsed symbol will be stored, if the parsing was successful.
   * @return Whether of not the parsing was successful.
   */
  private fun parseSymbolImpl(
    element: StartElement, eventReader: XMLEventReader, parsedResource: ParsedResource): Boolean {
    val typeAttribute = element.getAttributeByName(QName("type"))
    if (typeAttribute == null) {
      walkToEndOfElement(element, eventReader)
      logError(
        blameSource(source, element.location),
        "<${element.name}> must have a 'type' attribute.")
      return false
    }

    val parsedType = resourceTypeFromTag(typeAttribute.value)
    if (parsedType == null) {
      walkToEndOfElement(element, eventReader)
      logError(
        blameSource(source, element.location),
        "Invalid resource type '${typeAttribute.value}' in <${element.name}> resource.")
      return false
    }

    parsedResource.name = parsedResource.name.copy(type = parsedType)
    walkToEndOfElement(element, eventReader)
    return true
  }

  /**
   * Parses the xml represented by the "attr" tag.
   *
   * @param element The start element that represents the [AttributeResource] to be parsed.
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param parsedResource where the [AttributeResource] will be stored, if successful.
   * @return Whether of not the parsing was successful.
   */
  private fun parseAttr(
    element: StartElement, eventReader: XMLEventReader, parsedResource: ParsedResource): Boolean =
    parseAttrImpl(element, eventReader, parsedResource, false)

  /**
   * parses the xml as a [AttributeResource] represented by the specified start element. Then stores
   * the value in the parsed resource.
   *
   * @param element The start element that represents the symbol to be parsed.
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param parsedResource where the parsed symbol will be stored, if the parsing was successful.
   * @param isWeak whether or not the resource should be parsed as a weak attr (declaration).
   * @return Whether of not the parsing was successful.
   */
  private fun parseAttrImpl(
    element: StartElement,
    eventReader: XMLEventReader,
    parsedResource: ParsedResource,
    isWeak: Boolean): Boolean {
    parsedResource.name = parsedResource.name.copy(type = AaptResourceType.ATTR)

    // Attributes only end up in default configuration
    val defaultConfig = ConfigDescription()
    if (parsedResource.config != defaultConfig) {
      logger.warning(
        "Ignoring configuration '${parsedResource.config}' for <${element.name}> tag.",
        blameSource(source, element.location)
      )
      parsedResource.config = defaultConfig
    }

    var typeMask = 0

    val formatAttribute = element.getAttributeByName(QName("format"))
    if (formatAttribute != null) {
      typeMask = parseFormatAttribute(formatAttribute.value)
      if (typeMask == 0) {
        logError(
          blameSource(source, element.location),
          "Invalid attribute format '${formatAttribute.value}'")
        walkToEndOfElement(element, eventReader)
        return false
      }
    }

    var min: Int? = null
    var max: Int? = null

    val minAttribute = element.getAttributeByName(QName("min"))
    val maxAttribute = element.getAttributeByName(QName("max"))

    if (minAttribute != null) {
      val minString = minAttribute.value.trim()
      if (minString.isNotEmpty()) {
        val minRes = stringToInt(minString)
        if (minRes != null) {
          min = minRes.data
        }
      }

      if (min == null) {
        logError(
          blameSource(source, element.location),
          "Invalid 'min' value '$minString'. Integer value required.")
        walkToEndOfElement(element, eventReader)
        return false
      }
    }

    if (maxAttribute != null) {
      val maxString = maxAttribute.value.trim()
      if (maxString.isNotEmpty()) {
        val maxRes = stringToInt(maxString)
        if (maxRes != null) {
          max = maxRes.data
        }
      }

      if (max == null) {
        logError(
          blameSource(source, element.location),
          "Invalid 'max' value '$maxString'. Integer value required.")
        walkToEndOfElement(element, eventReader)
        return false
      }
    }

    if ((min != null || max != null) &&
      (typeMask and Resources.Attribute.FormatFlags.INTEGER_VALUE) == 0) {
      logError(
        blameSource(source, element.location),
        "'min' and 'max' attributes can only be used with format='integer' on <${element.name}> resource")
      walkToEndOfElement(element, eventReader)
      return false
    }

    val symbolMap = mutableMapOf<String, AttributeResource.Symbol>()

    var comment = ""
    var error = false

    while (eventReader.hasNext()) {
      val event = eventReader.nextEvent()
      if (event.eventType == XMLStreamConstants.COMMENT) {
        comment = (event as Comment).text.trim()
        continue
      }

      if (event.isEndElement) {
        break
      }

      if (!event.isStartElement) {
        // skip text
        continue
      }

      val childElement = event.asStartElement()

      val childSource = source.withLine(event.location.lineNumber)
      val childName = childElement.name
      if (childName.namespaceURI.isEmpty() &&
        (childName.localPart == "flag" || childName.localPart == "enum")) {
        var itemError = false
        when (childName.localPart) {
          "enum" -> {
            if ((typeMask and Resources.Attribute.FormatFlags.FLAGS_VALUE) != 0) {
              logError(
                blameSource(source, childElement.location),
                "Cannot define both <enum> and <flag> under the same <${element.name}> resource.")
              error = true
              itemError = true
            }
            typeMask = typeMask or Resources.Attribute.FormatFlags.ENUM_VALUE
          }
          "flag" -> {
            if ((typeMask and Resources.Attribute.FormatFlags.ENUM_VALUE) != 0) {
              logError(
                blameSource(source, childElement.location),
                "Cannot define both <enum> and <flag> under the same <${element.name}> resource.")
              error = true
              itemError = true
            }
            typeMask = typeMask or Resources.Attribute.FormatFlags.FLAGS_VALUE
          }
        }

        if (itemError) {
          continue
        }

        val symbol = parseEnumOrFlagItem(childElement, eventReader, childName.localPart)
        if (symbol != null) {
          val childResource = ParsedResource(defaultConfig, childSource, "")
          childResource.name = symbol.symbol.name
          childResource.value = Id()

          parsedResource.children.add(childResource)

          symbol.symbol.comment = comment
          symbol.symbol.source = childSource

          val symbolName = symbol.symbol.name.toString()
          if (symbolMap.contains(symbolName)) {
            val newSource =
              logger.getOriginalSource(blameSource(symbol.symbol.source))
            val previousSource =
              logger.getOriginalSource(blameSource(symbolMap[symbolName]!!.symbol.source))
            val errorMsg =
              "Duplicate symbol '$symbolName' defined here: $newSource" +
                      " and here: $previousSource"
            logError(blameSource(symbol.symbol.source), errorMsg)
            error = true
          }
          symbolMap[symbolName] = symbol
        } else {
          error = true
        }
      } else{
        if (!shouldIgnoreElement(childName)) {
          logError(
            blameSource(childSource),
            "Unrecognized tag <$childName> of child element of <${element.name}>.")
          error = true
        }
        walkToEndOfElement(childElement, eventReader)
      }
      comment = ""
    }

    if (error) {
      return false
    }

    val resource = AttributeResource(
      if (typeMask == 0) FormatFlags.ANY_VALUE else typeMask)
    resource.weak = isWeak
    resource.symbols.addAll(symbolMap.values)
    resource.minInt = min ?: Int.MIN_VALUE
    resource.maxInt = max ?: Int.MAX_VALUE
    parsedResource.value = resource
    return true
  }

  /**
   * Parse the xml that is contained by the "array" tag. The valid format of the child items will be
   * parsed from the format attribute of {@code element}
   *
   * @param element The start element of the [ArrayResource] to be parsed.
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param parsedResource where the read resource will be placed.
   */
  private fun parseArray(
    element: StartElement, eventReader: XMLEventReader, parsedResource: ParsedResource): Boolean {
    var resourceFormat = Resources.Attribute.FormatFlags.ANY_VALUE
    val formatAttribute = element.getAttributeByName(QName("format"))
    if (formatAttribute != null) {
      resourceFormat = parseFormatNoEnumsOrFlags(formatAttribute.value)
      if (resourceFormat == 0) {
        logError(
          blameSource(source, element.location),
          "Invalid format value: '${formatAttribute.value}'.")
        walkToEndOfElement(element, eventReader)
        return false
      }
    }
    return parseArrayImpl(element, eventReader, parsedResource, resourceFormat)
  }

  /**
   * Parse the xml that is contained by the "integer-array" tag.
   *
   * @param element The start element of the [ArrayResource] to be parsed.
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param parsedResource where the read resource will be placed.
   */
  private fun parseIntegerArray(
    element: StartElement, eventReader: XMLEventReader, parsedResource: ParsedResource) =
    parseArrayImpl(
      element, eventReader, parsedResource, Resources.Attribute.FormatFlags.INTEGER_VALUE)

  /**
   * Parse the xml that is contained by the "string-array" tag.
   *
   * @param element The start element of the [ArrayResource] to be parsed.
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param parsedResource where the read resource will be placed.
   */
  private fun parseStringArray(
    element: StartElement, eventReader: XMLEventReader, parsedResource: ParsedResource) =
    parseArrayImpl(
      element, eventReader, parsedResource, Resources.Attribute.FormatFlags.STRING_VALUE)

  /**
   * Parse the xml as an [ArrayResource].
   *
   * @param element The start element of the [ArrayResource] to be parsed.
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param parsedResource where the read resource will be placed.
   * @param resourceFormat A type mask that specifies which formats are valid for the child elements
   *   of the array to be interpreted as.
   * @return Whether or not the parsing was successful.
   */
  private fun parseArrayImpl(
    element: StartElement,
    eventReader: XMLEventReader,
    parsedResource: ParsedResource,
    resourceFormat: Int): Boolean {

    parsedResource.name = parsedResource.name.copy(type = AaptResourceType.ARRAY)

    val array = ArrayResource()
    var translatable = options.translatable

    val translatableAttribute = element.getAttributeByName(QName("translatable"))
    if (translatableAttribute != null) {
      val translatableValue = parseAsBool(translatableAttribute.value)
      if (translatableValue == null) {
        logError(
          blameSource(parsedResource.source),
          "Invalid value for 'translatable' attribute. Must be a boolean.")
        walkToEndOfElement(element, eventReader)
        return false
      }
      translatable = translatableValue
    }
    array.translatable = translatable

    var error = false
    while (eventReader.hasNext()) {
      val event = eventReader.nextEvent()
      if (event.isEndElement) {
        break
      }

      if (!event.isStartElement) {
        // Skip text and comments
        continue
      }

      val childElement = event.asStartElement()
      val childSource = source.withLine(childElement.location.lineNumber)
      val childName = childElement.name
      when {
        childName.namespaceURI.isEmpty() && childName.localPart == "item" -> {
          val childItem = parseXml(childElement, eventReader, resourceFormat, false)
          if (childItem != null) {
            childItem.source = childSource
            array.elements.add(childItem)
          } else {
            logError(blameSource(source, element.location), "Could not parse array item.")
            error = true
          }
        }
        !shouldIgnoreElement(childName) -> {
          logError(
            blameSource(childSource), "Unknown tag <$childName> in <${element.name}> resource.")
          error = true
          walkToEndOfElement(childElement, eventReader)
        }
        else -> {
          walkToEndOfElement(childElement, eventReader)
        }
      }
    }

    if (error) {
      return false
    }

    parsedResource.value = array
    return true
  }

  /**
   * Parses the xml contained in a "configVarying" tag.
   *
   * @param element The start element of the [Style] to be parsed.
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param parsedResource Where the parsed resource will be placed.
   * @return returns whether or not the parsing was a success.
   */
  private fun parseConfigVarying(
    element: StartElement, eventReader: XMLEventReader, parsedResource: ParsedResource) =
    parseStyleImpl(element, eventReader, parsedResource, AaptResourceType.CONFIG_VARYING)

  /**
   * Parses the xml contained in a "style" tag.
   *
   * @param element The start element of the [Style] to be parsed.
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param parsedResource Where the parsed resource will be placed.
   * @return returns whether or not the parsing was a success.
   */
  private fun parseStyle(
    element: StartElement, eventReader: XMLEventReader, parsedResource: ParsedResource) =
    parseStyleImpl(element, eventReader, parsedResource, AaptResourceType.STYLE)

  /**
   * Parses the xml element as a [Style].
   *
   * @param element The start element of the [Style] to be parsed.
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param parsedResource Where the parsed resource will be placed.
   * @param type The actual type of the [Style] being parsed, which is reflected in the
   *   [ResourceName] of the parsed resource.
   * @return returns whether or not the parsing was a success.
   */
  private fun parseStyleImpl(
    element: StartElement,
    eventReader: XMLEventReader,
    parsedResource: ParsedResource,
    type: AaptResourceType
  ): Boolean {

    parsedResource.name = parsedResource.name.copy(type = type)

    val style = Style()

    val parentAttribute = element.getAttributeByName(QName("parent"))
    if (parentAttribute != null) {
      // If the parent is empty, we don't have a parent but we don't attempt to infer one either.
      if (parentAttribute.value.isNotEmpty()) {
        val parseResult = parseStyleParentReference(parentAttribute.value)
        if (parseResult.parent == null) {
          logError(blameSource(source, element.location), parseResult.errorString)
          walkToEndOfElement(element, eventReader)
          return false
        }
        style.parent = parseResult.parent

        // Transform the namespace prefix to the actual package name, and mark the reference as
        // private if appropriate.
        resolvePackage(element, style.parent!!)
      }
    } else {
      // No parent was specified, so try inferring it from the style name.
      val styleName = parsedResource.name.entry!!
      val marker = styleName.lastIndexOf('.')
      if (marker != -1) {
        style.parentInferred = true
        style.parent =
          Reference(ResourceName("", STYLE, styleName.substring(0, marker)))
      }
    }

    var error = false

    while (eventReader.hasNext()) {
      val event = eventReader.nextEvent()

      if (event.isEndElement) {
        break
      }

      if (!event.isStartElement) {
        // skip text and comments
        continue
      }

      val childElement = event.asStartElement()
      val childName = childElement.name

      if (childName.namespaceURI.isEmpty() && childName.localPart == "item") {
        if (!parseStyleItem(childElement, eventReader, style)) {
          error = true
        }
      } else {
        if (!shouldIgnoreElement(childName)) {
          logError(
            blameSource(source, childElement.location),
            "Unrecognized child element <$childName> of <${element.name}> resource.")
          error = true
        }
        walkToEndOfElement(childElement, eventReader)
      }
    }

    if (error) {
      return false
    }

    parsedResource.value = style
    return true
  }

  /**
   * Parses the xml element contained by a "declare-styleable" tag as a [Styleable] resource.
   *
   * @param element The start element of the [Styleable] to be parsed.
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param parsedResource Where the parsed resource will be placed.
   * @return returns whether or not the parsing was a success.
   */
  private fun parseDeclareStyleable(
    element: StartElement, eventReader: XMLEventReader, parsedResource: ParsedResource): Boolean {
    parsedResource.name = parsedResource.name.copy(type = AaptResourceType.STYLEABLE)

    // TODO(b/153454907): add option for preservation of stylable visibility to match aapt2
    parsedResource.visibility = ResourceVisibility.PUBLIC

    // Declare-stylable only ends up in the default config
    val defaultConfig = ConfigDescription()
    if (parsedResource.config != defaultConfig) {
      logger.warning(
        "Ignoring configuration '${parsedResource.config}' for <${element.name}> tag.",
        blameSource(source, element.location)
      )
      parsedResource.config = defaultConfig
    }

    val styleable = Styleable()

    var comment = ""
    var error = false

    while (eventReader.hasNext()) {
      val event = eventReader.nextEvent()

      if (event.eventType == XMLStreamConstants.COMMENT) {
        comment = (event as Comment).text.trim()
        continue
      }

      if (event.isEndElement) {
        // We're done here
        break
      }

      if (!event.isStartElement) {
        continue
      }

      val childElement = event.asStartElement()
      val childName = childElement.name
      val itemSource = source.withLine(childElement.location.lineNumber)

      if (childName.namespaceURI.isEmpty() && childName.localPart == "attr") {
        val nameAttribute = childElement.getAttributeByName(QName("name"))
        if (nameAttribute == null) {
          logError(blameSource(itemSource), "<attr> tag must have a 'name' attribute.")
          error = true
          walkToEndOfElement(childElement, eventReader)
          continue
        }

        // If this is a declaration, the package name may be in the name. Separate these out.
        // Eg. <attr name="android:text" />
        val nameReference = parseXmlAttributeName(nameAttribute.value)
        resolvePackage(childElement, nameReference)

        // Create the ParsedResource that will add the attribute to the table.
        val childResource = ParsedResource(defaultConfig, itemSource, comment)
        childResource.name = nameReference.name

        if (!parseAttrImpl(childElement, eventReader, childResource, true)) {
          error = true
          continue
        }

        nameReference.comment = childResource.comment
        nameReference.source = itemSource
        styleable.entries.add(nameReference)

        parsedResource.children.add(childResource)

      } else {
        if (!shouldIgnoreElement(childName)) {
          logError(
            blameSource(itemSource), "Unknown tag of <$childName> in <${element.name}> resource.")
          error = true
        }
        walkToEndOfElement(childElement, eventReader)
      }

      comment = ""
    }

    if (error) {
      return false
    }

    parsedResource.value = styleable
    return true
  }

  /**
   * Parses the xml element surrounded by the "overlayable" tag as an [Overlayable] resource.
   *
   * @param element The start element of the [Overlayable] to be parsed.
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param parsedResource Where the parsed resource will be placed.
   * @return returns whether or not the parsing was a success.
   */
  private fun parseOverlayable(
    element: StartElement, eventReader: XMLEventReader, parsedResource: ParsedResource): Boolean {

    val defaultConfig = ConfigDescription()
    if (parsedResource.config != defaultConfig) {
      logger.warning(
        "Ignoring configuration '${parsedResource.config}' for <${element.name}> tag.",
        blameSource(source, element.location)
      )
    }

    val nameAttribute = element.getAttributeByName(QName(null, "name"))
    if (nameAttribute == null) {
      logError(
          blameSource(source, element.location), "<overlayable> tag must have a 'name' attribute")
      return false
    }

    val actorAttribute = element.getAttributeByName(QName(null, "actor"))
    if (actorAttribute != null && !actorAttribute.value.startsWith(Overlayable.ACTOR_SCHEME_URI)) {
      logError(
        blameSource(source, element.location),
          "<overlayable> tag has a 'actor' attribute: '${actorAttribute.value}'. " +
                  "Value must use the schema: ${Overlayable.ACTOR_SCHEME_URI}.")
      return false
    }

    val overlayable = Overlayable(nameAttribute.value, actorAttribute?.value ?: "", source)

    var error = false
    var comment = ""
    var currentPolicies: Int = OverlayableItem.Policy.NONE
    var depth = 1
    while (eventReader.hasNext()) {
      val event = eventReader.nextEvent()

      if (event.isEndElement) {
        --depth
        if (depth == 0) {
          // Break the loop, exiting <overlayable>
          break
        }
        // Clear the current policies when exiting the <policy> tags.
        currentPolicies = OverlayableItem.Policy.NONE
        continue
      }

      if (event.eventType == XMLStreamConstants.COMMENT) {
        comment = (event as Comment).text.trim()
        continue
      }

      if (!event.isStartElement) {
        // Skip whitespace and text
        continue
      }

      val childElement = event.asStartElement()
      val childName = childElement.name
      when {
        childName.namespaceURI == XMLConstants.NULL_NS_URI && childName.localPart == "item" -> {
          val childResource =
            parseOverlayableItem(childElement, eventReader, currentPolicies, overlayable, comment)
          comment = ""
          if (childResource == null) {
            error = true
          } else {
            parsedResource.children.add(childResource)
          }
        }
        childName.namespaceURI == XMLConstants.NULL_NS_URI &&
          childName.localPart == "policy" -> {

          ++depth
          val newPolicy = parsePoliciesFromElement(childElement, currentPolicies)

          if (newPolicy == null) {
            error = true
            currentPolicies = OverlayableItem.Policy.NONE
          } else {
            currentPolicies = newPolicy
          }
          comment = ""
        }
        !shouldIgnoreElement(childName) -> {
          logError(
              blameSource(source, childElement.location),
              "Unrecognized tag '$childName' within an <overlayable> resource.")
          error = true
        }
        else -> comment = ""
      }
    }

    return !error
  }

  /**
   * Parses the xml element as a [OverlayableItem] within an [Overlayable] resource.
   *
   * @param element The start element of the [OverlayableItem] to be parsed.
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param policies The policies of the current policy block that this [OverlayableItem] is a part
   *   of. The value of policies should be non-zero.
   * @param overlayable The overlayable which the parsed resource will be a part of.
   * @param comment The comment that applies to this element.
   * @return The parsedResource representing the parsed Overlayable Item. If their is an issue
   *   parsing, then {@code null} is returned.
   */
  private fun parseOverlayableItem(
    element: StartElement,
    eventReader: XMLEventReader,
    policies: Int,
    overlayable: Overlayable,
    comment: String): ParsedResource? {

    if (policies == OverlayableItem.Policy.NONE) {
      logError(
          blameSource(source, element.location),
          "<item> within an <overlayable> must be inside a <policy> block.")
      walkToEndOfElement(element, eventReader)
      return null
    }

    // Items specify the name and type of resource that should be overlayable.
    val nameAttribute = element.getAttributeByName(QName(null, "name"))
    if (nameAttribute == null || nameAttribute.value.isNullOrEmpty()) {
      logError(
          blameSource(source, element.location),
          "<item> within an <overlayable> must have a 'name' attribute.")
      walkToEndOfElement(element, eventReader)
      return null
    }

    val typeAttribute = element.getAttributeByName(QName(null, "type"))
    if (typeAttribute == null || typeAttribute.value.isNullOrEmpty()) {
      logError(
          blameSource(source, element.location),
          "<item> within an <overlayable> must have a 'type' attribute.")
      walkToEndOfElement(element, eventReader)
      return null
    }

    val type = resourceTypeFromTag(typeAttribute.value)
    if (type == null) {
      logError(
          blameSource(source, element.location),
          "Invalid resource type '${typeAttribute.value}' in <item> in <overlayable> resource.")
      walkToEndOfElement(element, eventReader)
      return null
    }

    val overlayableItem =
      OverlayableItem(overlayable, policies, comment, source.withLine(element.location.lineNumber))
    val childResource = ParsedResource()
    childResource.name = childResource.name.copy(type = type, entry = nameAttribute.value)
    childResource.overlayableItem = overlayableItem

    walkToEndOfElement(element, eventReader)

    return childResource
  }

  /**
   * Parses the "type" attribute of the <policy> block for policies. This does not move the
   * xml parser, as this does not read
   *
   * @param element The start element of the <policy> block.
   * @param oldPolicies The current policies to be overwritten. As nested policy blocks are not
   *   allowed, the oldPolicies is expected to be [OverlayableItem.Policy.NONE].
   * @return The new policy values or null if an error occurred.
   */
  private fun parsePoliciesFromElement(
    element: StartElement, oldPolicies: Int): Int? {

    if (oldPolicies != OverlayableItem.Policy.NONE) {
      // If the policy list is not empty, then we are currently inside a policy element.
      logError(
          blameSource(source, element.location), "Policy blocks should not be nested recursively.")
      return null
    }

    val typeAttribute = element.getAttributeByName(QName(null, "type"))
    if (typeAttribute == null || typeAttribute.value.isNullOrEmpty()) {
      logError(blameSource(source, element.location), "<policy> must have a 'type' attribute.")
      return null
    }

    var newPolicy = OverlayableItem.Policy.NONE
    // Parse the polices separated by vertical bar characters to allow for specifying multiple
    // policies. Items within the policy tag will have the specified policy.
    for (string in typeAttribute.value.split('|')) {
      newPolicy = newPolicy or when(string.trim()) {
        "odm" -> OverlayableItem.Policy.ODM
        "oem" -> OverlayableItem.Policy.OEM
        "product" -> OverlayableItem.Policy.PRODUCT
        "public" -> OverlayableItem.Policy.PUBLIC
        "signature" -> OverlayableItem.Policy.SIGNATURE
        "system" -> OverlayableItem.Policy.SYSTEM
        "vendor" -> OverlayableItem.Policy.VENDOR
        else -> {
          logError(
              blameSource(source, element.location),
              "<policy> has unsupported type '${string.trim()}'.")
          return null
        }
      }
    }

    return newPolicy
  }

  /**
   * Parses the {@code element} as a [Plural] resource.
   *
   * @param element The start element of the [Plural] to be parsed.
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param parsedResource Where the parsed resource will be placed.
   * @return returns whether or not the parsing was a success.
   */
  private fun parsePlural(
    element: StartElement, eventReader: XMLEventReader, parsedResource: ParsedResource): Boolean {
    parsedResource.name = parsedResource.name.copy(type= AaptResourceType.PLURALS)

    val plural = Plural()

    var error = false
    while (eventReader.hasNext()) {
      val event = eventReader.nextEvent()

      if (event.isEndElement) {
        // We're done with the plural
        break
      }

      if (!event.isStartElement) {
        // Skip text and comments.
        continue
      }

      val childElement = event.asStartElement()
      val childName = childElement.name
      if (childName.namespaceURI.isEmpty() && childName.localPart == "item") {
        val quantityAttribute = childElement.getAttributeByName(QName("quantity"))
        if (quantityAttribute == null) {
          logError(
              blameSource(source, childElement.location),
              "<item> in <plurals> are required to have the 'quantity' attribute.")
          walkToEndOfElement(childElement, eventReader)
          error = true
          continue
        }

        val trimmedQuantity = quantityAttribute.value.trim()
        val pluralType = when (trimmedQuantity) {
          "zero" -> Plural.Type.ZERO
          "one" -> Plural.Type.ONE
          "two" -> Plural.Type.TWO
          "few" -> Plural.Type.FEW
          "many" -> Plural.Type.MANY
          "other" -> Plural.Type.OTHER
          else -> null
        }

        if (pluralType == null) {
          logError(
              blameSource(source, childElement.location),
              "Unrecognized quantity value '$trimmedQuantity' specified in <item> " +
                      "in <plurals> resource.")
          walkToEndOfElement(childElement, eventReader)
          error = true
          continue
        }

        val pluralIndex = pluralType.ordinal
        if (plural.values[pluralIndex] != null) {
          logError(
            blameSource(source, childElement.location),
              "<item> has quantity '$trimmedQuantity' which has already been specified " +
                      "in <plurals> resource '${element.name}'")
          error = true
          walkToEndOfElement(childElement, eventReader)
          continue
        }

        plural.values[pluralIndex] =
          parseXml(childElement, eventReader, Resources.Attribute.FormatFlags.STRING_VALUE, false)
        if (plural.values[pluralIndex] == null) {
          error = true
        }
      } else {
        if (!shouldIgnoreElement(childName)) {
          logError(
              blameSource(source, childElement.location),
              "Unrecognized tag '$childName' within an <plurals> resource.")
          error = true
        }
        walkToEndOfElement(childElement, eventReader)
      }
    }

    if (error) {
      return false
    }

    parsedResource.value = plural
    return true
  }

  /**
   * Parses the {@code element} as a public resource.
   *
   * @param element The start element of the resource to be parsed.
   * @param eventReader The xml to be read. The event reader should have just pulled the
   *   {@code StartElement} element. After this method is invoked the eventReader will be placed
   *   after the corresponding end tag for element.
   * @param parsedResource Where the parsed resource will be placed.
   * @return returns whether or not the parsing was a success.
   */
  private fun parsePublic(
    element: StartElement, eventReader: XMLEventReader, parsedResource: ParsedResource): Boolean {

    if (options.visibility != null ) {
      logError(
          blameSource(source, element.location),
          "<public> tag not allowed with --visibility flag.")
      walkToEndOfElement(element, eventReader)
      return false
    }

    if (parsedResource.config != ConfigDescription()) {
      logger.warning(
        "Ignoring configuration '${parsedResource.config}' for <${element.name}> tag.",
        blameSource(source, element.location)
      )
    }

    val typeAttribute = element.getAttributeByName(QName("type"))
    if (typeAttribute == null) {
      logError(blameSource(source, element.location), "<public> must have a 'type' attribute.")
      walkToEndOfElement(element, eventReader)
      return false
    }

    val parsedType = resourceTypeFromTag(typeAttribute.value)
    if (parsedType == null) {
      logError(
          blameSource(source, element.location),
          "Invalid resource type '${typeAttribute.value}' in <public> resource.")
      walkToEndOfElement(element, eventReader)
      return false
    }

    parsedResource.name = parsedResource.name.copy(type = parsedType)

    val idAttribute = element.getAttributeByName(QName("id"))
    if (idAttribute != null) {
      val id = parseResourceId(idAttribute.value)
      if (id == null) {
        logError(
            blameSource(source, element.location),
            "Invalid resource Id '${idAttribute.value}' in <public> resource.")
        walkToEndOfElement(element, eventReader)
        return false
      }
      parsedResource.resourceId = id
    }

    if (parsedType == ID) {
      // An ID marked as public is also the definition of an ID.
      parsedResource.value = Id()
    }

    parsedResource.visibility = ResourceVisibility.PUBLIC
    walkToEndOfElement(element, eventReader)
    return true
  }

  /**
   * parses the {@code element} as a PublicGroup resource. The {@code eventReader} will be after the
   * corresponding end of {@code element}
   */
  private fun parsePublicGroup(
    element: StartElement, eventReader: XMLEventReader, parsedResource: ParsedResource): Boolean {
    if (options.visibility != null) {
      logError(
          blameSource(source, element.location),
          "<public-group> tag not allowed with --visibility flag.")
      walkToEndOfElement(element, eventReader)
      return false
    }

    if (parsedResource.config != ConfigDescription()) {
      logger.warning(
        "Ignoring configuration '${parsedResource.config}' for <${element.name}> tag.",
        blameSource(source, element.location)
      )
    }

    val typeAttribute = element.getAttributeByName(QName("type"))
    if (typeAttribute == null) {
      logError(
          blameSource(source, element.location),
          "<public-group> must have a 'type' attribute.")
      walkToEndOfElement(element, eventReader)
      return false
    }

    val parsedType = resourceTypeFromTag(typeAttribute.value)
    if (parsedType == null) {
      logError(
          blameSource(source, element.location),
          "Invalid resource type '${typeAttribute.value}' in <public-group>.")
      walkToEndOfElement(element, eventReader)
      return false
    }

    val idAttribute = element.getAttributeByName(QName("first-id"))
    if (idAttribute == null) {
      logError(
          blameSource(source, element.location),
          "<public-group> must have a 'first-id' attribute.")
      walkToEndOfElement(element, eventReader)
      return false
    }

    val idVal = parseResourceId(idAttribute.value)
    if (idVal == null) {
      logError(
          blameSource(source, element.location),
          "Invalid resource ID '${idAttribute.value}' in <public-group>. Integer expected.")
      walkToEndOfElement(element, eventReader)
      return false
    }

    var childId = idVal

    var comment = ""
    var error = false
    while (eventReader.hasNext()) {
      val event = eventReader.nextEvent()

      if (event.isEndElement) {
        // we're done with the public group.
        break
      }

      if (event.eventType == XMLStreamConstants.COMMENT) {
        comment = (event as Comment).text
        continue
      }

      if (!event.isStartElement) {
        // Skip text.
        continue
      }

      val childElement = event.asStartElement()
      val childName = childElement.name
      val itemSource = source.withLine(childElement.location.lineNumber)
      if (childName.namespaceURI.isEmpty() && childName.localPart == "public") {
        val nameAttribute = childElement.getAttributeByName(QName("name"))
        if (nameAttribute ==  null) {
          logError(
              blameSource(source, childElement.location), "<public> must have a 'name' attribute.")
          walkToEndOfElement(childElement, eventReader)
          error = true
          continue
        }

        val childIdAttribute = childElement.getAttributeByName(QName("id"))
        if (childIdAttribute != null) {
          logError(
              blameSource(source, childElement.location),
              "'id' attribute is not allowed on <public> tags within a <public-group>.")
          walkToEndOfElement(childElement, eventReader)
          error = true
          continue
        }

        val childTypeAttribute = childElement.getAttributeByName(QName("type"))
        if (childTypeAttribute != null) {
          logError(
              blameSource(source, childElement.location),
              "'type' attribute is not allowed on <public> tags within a <public-group>.")
          walkToEndOfElement(childElement, eventReader)
          error = true
          continue
        }

        val childResource = ParsedResource(ConfigDescription(), itemSource, comment)
        childResource.name = ResourceName("", parsedType, nameAttribute.value)
        childResource.resourceId = childId
        childResource.visibility = ResourceVisibility.PUBLIC
        parsedResource.children.add(childResource)

        ++childId
        walkToEndOfElement(childElement, eventReader)
      } else {
        if (!shouldIgnoreElement(childName)) {
          logError(
              blameSource(source, childElement.location),
              "Unrecognized tag '$childName' within an <public-group> resource.")
          error = true
        }
        walkToEndOfElement(childElement, eventReader)
      }
    }
    return !error
  }

  /**
   * Adds the given parsed resource to the [table] property.
   *
   * @param parsedResource the resource parsed from xml.
   */
  private fun addResourceToTable(parsedResource: ParsedResource): Boolean {
    if (parsedResource.visibility != ResourceVisibility.UNDEFINED) {
      val visibility =
        Visibility(parsedResource.source, parsedResource.comment, parsedResource.visibility)
      if (!table.setVisibilityWithId(parsedResource.name, visibility, parsedResource.resourceId)) {
        return false
      }
    }

    if (parsedResource.allowNew) {
      val allowNew = AllowNew(parsedResource.source, parsedResource.comment)
      if (!table.setAllowNew(parsedResource.name, allowNew)) {
        return false
      }
    }

    val overlayableItem = parsedResource.overlayableItem
    if (overlayableItem != null) {
      if (!table.setOverlayable(parsedResource.name, overlayableItem)) {
        return false
      }
    }

    val resource = parsedResource.value
    if (resource != null) {
      // Attach the comment, source and config to the resource.
      resource.comment = parsedResource.comment
      resource.source = parsedResource.source

      if (!table.addResourceWithId(
          parsedResource.name,
          parsedResource.resourceId,
          parsedResource.config,
          parsedResource.productString,
          resource)) {
        return false
      }
    }

    var error = false

    for (child in parsedResource.children) {
      error = error || !addResourceToTable(child)
    }
    return !error
  }
}
