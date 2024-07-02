package com.android.aaptcompiler

import com.android.aaptcompiler.ResourceFile.Type.ProtoXml
import java.io.InputStream
import java.lang.RuntimeException
import jaxp.xml.namespace.QName
import jaxp.xml.stream.XMLEventReader
import jaxp.xml.stream.XMLStreamConstants
import jaxp.xml.stream.XMLStreamException
import jaxp.xml.stream.events.Attribute
import jaxp.xml.stream.events.Characters
import jaxp.xml.stream.events.EndElement
import jaxp.xml.stream.events.Namespace
import jaxp.xml.stream.events.StartElement
import jaxp.xml.stream.events.XMLEvent

private const val AAPT_ATTR_TAG = "attr"

/** Mangles outlined resources so they are only accessible from this file */
private fun mangleEntry(pck: String, entry: String) = "$pck${'$'}$entry"

/**
 * Processes a XML file as a android resource.
 *
 * This consists of three primary processes:
 * 1. Collect created ID resources.
 * Gather all ids specified in xml attributes with the create marker. i.e.
 *
 *     <TextView
 *       android:id="@+id/player_name"
 *       android:text="@+string/player_name"/>
 * In this XML, the resource id/player_name would be exported by this xml file, since it has the "+"
 * resource creation marker. Even though string/player_name is marked with the creation marker, it
 * is not an id resource and is ignored.
 *
 * 2. Outline inlined XML resources.
 * In android XML files (drawables, layouts, etc.) it is possible to write an XML resource inside of
 * another XML resource.
 *
 * Consider drawable/player_background:
 *     <shape
 *       xmlns:android="http://schemas.android.com/apk/res/android"
 *       android:shape="rectangle">
 *       <gradient
 *         android:startColor="#FF000000"
 *         android:endColor="#FFAA0000"
 *         android:angle="35" />
 *       <corners
 *         android:radius="10dp" />
 *     </shape>
 * And consider:
 *     <TextView
 *       android:id="@+id/player_name"
 *       android:text="@string/player_name"
 *       android:background=@"drawable/player_background"/>
 *
 * If this drawable is only used here we can simplify this to one file:
 *     <TextView
 *       android:id="@+id/player_name"
 *       android:text="@string/player_name">
 *       <aapt:attr name="android:background">
 *         <shape
 *           xmlns:android="http://schemas.android.com/apk/res/android"
 *           android:shape="rectangle">
 *           <gradient
 *             android:startColor="#FF000000"
 *             android:endColor="#FFAA0000"
 *             android:angle="35" />
 *           <corners
 *             android:radius="10dp" />
 *         </shape>
 *       </aapt:attr>
 *     </TextView>
 *
 * This is possible by outlining the aapt_attr element into a separate XML file. Effectively undoing
 * the inlining by the developer. So this class outlines the inline XML and sets the parent elements
 * attribute to reference the outlned attr.
 *
 * 3. Flatten XMLs to proto.
 * Flatten all XML files (including outlined XMLs in (2.)) to proto to be written as output.
 *
 * The XMLProcessor handles all of these processes simultaneously to require only a single pass over
 * the XML file.
 *
 * @property source: The source that this processor is going to process.
 */
class XmlProcessor(val source: Source, val logger: BlameLogger?) {

    lateinit var primaryFile: ResourceFile
        private set

    var xmlResources = listOf<XmlResource>()
        private set

    /**
     * Processes the XML resource.
     *
     * Outlines all inline XML aapt:attr resources, collects all created id resources, and flattens
     * the resulting XMLs to proto.
     */
    fun process(file: ResourceFile, inputFile: InputStream) {
        primaryFile = file

        var eventReader: XMLEventReader? = null
        try {
            eventReader = xmlInputFactory.createXMLEventReader(inputFile)
            val collectedIds = mutableMapOf<ResourceName, SourcedResourceName>()
            val protoBuilders = mutableMapOf<ResourceFile, XmlResourceBuilder>()

            val documentStart = eventReader.nextEvent()
            if (!documentStart.isStartDocument) {
                error("Failed to find start of XML from ${blameSource(source)}")
            }

            var rootStart: XMLEvent? = null
            while (eventReader.hasNext()) {
                rootStart = eventReader.nextEvent()
                // ignore comments and text before the root tag
                if (rootStart.isStartElement) {
                    break
                }
            }
            rootStart ?: return

            val builder = XmlResourceBuilder(primaryFile)
            protoBuilders[file] = builder

            processElement(
                rootStart.asStartElement(),
                eventReader,
                collectedIds,
                protoBuilders,
                builder
            )

            primaryFile.exportedSymbols.addAll(collectedIds.values.toList().sortedBy { it.name })

            xmlResources = protoBuilders.values.map { it.build() }
                .sortedWith { left, right ->
                    when {
                        // The primary file should come first in the result.
                        left === right -> 0
                        left.file.name === primaryFile.name -> -1
                        right.file.name === primaryFile.name -> 1
                        else -> left.file.name.compareTo(right.file.name)
                    }
                }
        } catch (xmlException: XMLStreamException) {
            val message = xmlException.message ?: ""
            if (!message.contains("Premature end of file", true)) {
                // Having no root is not an error, but any other xml format exception is.
                throw xmlException
            }
        } finally {
            eventReader?.close()
        }
    }

    /**
     * Flattens the XML element, as well as all text, comments, and child elements underneath it.
     *
     * This method flattens the XML tree whose root is [startElement], aggregates all created ids,
     * and outlines any given aapt:attr xml subtrees that may exist in the given XML.
     *
     * @param startElement the start element that represents the root of the XML tree to be
     * flattened. The start element should not be pointing to a <aapt:attr> element, as those are
     * processed in a different method.
     * @param eventReader the stream from which the XML file is read. The event reader should be in
     * a state where [startElement] was the last event read. When this method finishes,
     * [eventReader] will be positioned immediately after the [EndElement] that aligns with the
     * supplied [StartElement].
     * @param collectedIds The map of all created ID resources in the XML document. Any created ids
     * found in the XML tree represented by [startElement] will be added to this map.
     * @param resourceBuilders The map of all XML resource builders created for this XML document.
     * Any new builders created because of <aapt:attr> subelements of [startElement] will be added
     * to this map.
     * @param currentBuilder The current xml resource builder that this [startElement] is to be
     * flattened to. When this function returns, current builder will have the same active element
     * as it did when the method started (or will be finished if this element was the top element of
     * the builder). The subtree represented by [startElement] will be added as a child element to
     * the current element.
     * @param inheritedNamespaceContext When creating the top element of an extracted <aapt:attr>
     * element, this variable will contain the context for all the xml namespaces that are active in
     * [StartElement] that will need to be flattened to proto.
     */
    private fun processElement(
        startElement: StartElement,
        eventReader: XMLEventReader,
        collectedIds: MutableMap<ResourceName, SourcedResourceName>,
        resourceBuilders: MutableMap<ResourceFile, XmlResourceBuilder>,
        currentBuilder: XmlResourceBuilder,
        inheritedNamespaceContext: NamespaceContext? = null
    ) {
        // If the current start element is an aapt:attr, then either it is the root of the xml or
        // there exist consecutively nested aapt:attr tags. I.e.
        // <ListView>
        //   <aapt:attr ...>
        //     <aapt:attr ...>
        //       ...
        //     </aapt:attr>
        //   </aapt:attr>
        // </ListView>
        // Both of these are invalid.
        if (isAaptAttribute(startElement.name)) {
            val exception = RuntimeException(
                "${blameSource(source, startElement.location)}: " +
                        "<aapt:attr> blocks are not allowed as the root of documents, or " +
                        "as a child element under another <aapt:attr>."
            )
            walkToEndOfElement(startElement, eventReader)
            throw exception
        }

        // First, gather any new ids in the attributes of the element.
        gatherIds(startElement, collectedIds)

        // Add the element to the proto builder.
        val elementName = startElement.name
        val elementLocation = startElement.location
        currentBuilder.startElement(
            elementName.localPart,
            elementName.namespaceURI ?: "",
            elementLocation.lineNumber,
            elementLocation.columnNumber
        )

        // Gather all the namespace declarations.
        val namespaceDeclarations = startElement.namespaces
        val namespaces = mutableListOf<Namespace>()
        while (namespaceDeclarations.hasNext()) {
            namespaces.add(namespaceDeclarations.next() as Namespace)
        }

        // Add all inherited namespaces that are not defined by the current element (as they would
        // be overwritten).
        inheritedNamespaceContext?.namespacePrefixes()?.forEach { prefix ->
            if (namespaces.firstOrNull { ns -> ns.prefix == prefix } == null) {
                currentBuilder.addNamespaceDeclaration(
                    inheritedNamespaceContext.uriForPrefix(prefix)!!, prefix
                )
            }
        }

        // Add namespaces to proto.
        for (namespace in namespaces) {
            currentBuilder.addNamespaceDeclaration(
                namespace.namespaceURI,
                namespace.prefix,
                elementLocation.lineNumber,
                elementLocation.columnNumber
            )
        }

        // Add attributes to proto.
        val attributes = startElement.attributes
        while (attributes.hasNext()) {
            val attribute = attributes.next() as Attribute
            val attrName = attribute.name
            currentBuilder.addAttribute(
                attrName.localPart,
                attrName.namespaceURI,
                attribute.value,
                elementLocation.lineNumber,
                elementLocation.columnNumber
            )
        }

        // Add information from children.
        while (eventReader.hasNext()) {
            val nextEvent = eventReader.nextEvent()

            if (nextEvent.isEndElement) {
                // We're done with the current element.
                break
            }

            if (nextEvent.isStartElement) {
                // If the element is a aapt:attr, we need to extract that attr.
                val elName = nextEvent.asStartElement().name
                if (isAaptAttribute(elName)) {
                    outlineAttribute(
                      currentBuilder,
                      nextEvent.asStartElement(),
                      eventReader,
                      collectedIds,
                      resourceBuilders
                    )
                } else {
                    // We're going down a level, so process that element.
                    processElement(
                      nextEvent.asStartElement(),
                      eventReader,
                      collectedIds,
                      resourceBuilders,
                      currentBuilder
                    )
                }
                continue
            }

            if (nextEvent.isCharacters) {
                processText(nextEvent.asCharacters(), currentBuilder)
            }

            if (nextEvent.eventType == XMLStreamConstants.COMMENT) {
                processComment(currentBuilder)
            }
        }

        // Finally finish element
        currentBuilder.endElement()
    }

    /**
     * Outlines the aapt:attr and assigns the correct attibute in the parent XML to the outlined
     * value.
     *
     * @param parentBuilder the xml proto builder that is currently set to the parent element of the
     * aapt:attr. This is so we can set the attribute on the parent element to a reference of the
     * outlined xml resource.
     * @param attrElement the startElement corresponding to the aapt:attr to be outlined.
     * @param eventReader the stream from which the XML file is read. The event reader should be in
     * a state where [attrElement] was the last event read. When this method finishes, [eventReader]
     * will be positioned immediately after the [EndElement] that aligns with the supplied
     * [attrElement].
     * @param collectedIds The map of all created ID resources in the XML document. Any created ids
     * found in the XML tree represented by [attrElement] will be added to this map.
     * @param resourceBuilders The map of all XML resource builders created for this XML document.
     * At least one new builder will be added from the outlined aapt:attr (if successful), or more
     * if there are nested aapt:attr definitions.
     * @return true if and only if the element was successfully outlined.
     */
    private fun outlineAttribute(
        parentBuilder: XmlResourceBuilder,
        attrElement: StartElement,
        eventReader: XMLEventReader,
        collectedIds: MutableMap<ResourceName, SourcedResourceName>,
        resourceBuilders: MutableMap<ResourceFile, XmlResourceBuilder>
    ) {

        val nameAttribute = attrElement.getAttributeByName(QName("name"))
        if (nameAttribute == null) {
            val exception = RuntimeException(
                "${blameSource(source, attrElement.location)}:" +
                        "<${attrElement.name}> tag requires the 'name' attribute."
            )
            walkToEndOfElement(attrElement, eventReader)
            throw exception
        }

        val reference = parseXmlAttributeName(nameAttribute.value)
        val nameValue = reference.name

        val extractedPackage = transformPackageAlias(attrElement, nameValue.pck!!)
        if (extractedPackage == null) {
            val exception = RuntimeException(
                blameSource(source, attrElement.location).toString() +
                        "Invalid namespace prefix '${nameValue.pck}' " +
                        "for value of 'name' attribute '$nameValue'."
            )
            walkToEndOfElement(attrElement, eventReader)
            throw exception
        }

        // Check whether the namespace is or resource is private in order to properly set the
        // extracted attr package.
        val isPrivateNs = extractedPackage.isPrivate || reference.isPrivate

        // We need to differentiate between no-namespace defined, or the alias resolves to an empty
        // package. If it is the former, we need to use the res-auto schema.
        val attrUri = when {
            nameValue.pck.isNullOrEmpty() -> ""
            extractedPackage.packageName.isEmpty() -> SCHEMA_AUTO
            else -> constructPackageUri(extractedPackage.packageName, isPrivateNs)
        }

        // Now to process the inline xml itself.
        val resource = processAaptAttr(
            attrElement,
            eventReader,
            collectedIds,
            resourceBuilders,
            parentBuilder.namespaceContext
        )

        // Check to see if the attribute would overwrite a previously defined one.
        if (parentBuilder.findAttribute(nameValue.entry!!, attrUri) != null) {
            error("Cannot find attribute ${nameValue.entry}")
        }

        // Now to add the new attribute to the parent xml element.
        parentBuilder.addAttribute(
            nameValue.entry!!,
            attrUri,
            "@${resource.name}",
            attrElement.location.lineNumber,
            attrElement.location.columnNumber
        )
    }

    private fun isAaptAttribute(elName: QName): Boolean =
        elName.namespaceURI == AAPT_ATTR_URI && elName.localPart == AAPT_ATTR_TAG

    /**
     * Processes the element that is the immediate child of an aapt:attr element. This is the root
     * element of the outlined XML resource.
     *
     * @param startElement the start element that represents the root of the XML file to be
     * extracted.
     * @param eventReader the stream from which the XML file is read. The event reader should be in
     * a state where [startElement] was the last event read. When this method finishes,
     * [eventReader] will be positioned immediately after the [EndElement] that aligns with the
     * supplied [StartElement].
     * @param collectedIds The map of all created ID resources in the XML document. Any created ids
     * found in the XML tree represented by [startElement] will be added to this map.
     * @param resourceBuilders The map of all XML resource builders created for this XML document.
     * Any new builders created because of <aapt:attr> subelements of [startElement] will be added
     * to this map.
     * @param namespaceContext All inherited xmlns definitions so far in the XML document before the
     * root of this XML subtree.
     * @return the ResourceFile corresponding to the outlined element. This value will be null if
     * the outlining is unsuccessful.
     */
    private fun processAaptAttr(
        startElement: StartElement,
        eventReader: XMLEventReader,
        collectedIds: MutableMap<ResourceName, SourcedResourceName>,
        resourceBuilders: MutableMap<ResourceFile, XmlResourceBuilder>,
        namespaceContext: NamespaceContext
    ): ResourceFile {

        val outputFile = getNextAttrResourceFile(resourceBuilders.size - 1, startElement)
        val outputBuilder = XmlResourceBuilder(outputFile)

        resourceBuilders[outputFile] = outputBuilder

        val errors = mutableListOf<String>()
        var foundChild = false

        while (eventReader.hasNext()) {
            val event = eventReader.nextEvent()

            if (event.isEndElement) {
                // we're done with the aapt:attr
                break
            }

            if (event.isCharacters) {
                // Non-whitespace text is not allowed as children of a aapt:attr
                if (!event.asCharacters().isWhiteSpace) {
                    errors += "${blameSource(source, event.location)}: " +
                            "Can't extract text into its own resource."
                }
                continue
            }

            if (event.isStartElement) {
                // An aapt:attr element cannot have more than one element child.
                if (foundChild) {
                    errors += "${blameSource(source, event.location)}: " +
                            "Inline XML resources must have a single root."
                    walkToEndOfElement(event.asStartElement(), eventReader)
                    continue
                }
                foundChild = true
                processElement(
                  event.asStartElement(),
                  eventReader,
                  collectedIds,
                  resourceBuilders,
                  outputBuilder,
                  namespaceContext
                )
            }

            // Comments are skipped.
        }

        if (!foundChild) {
            error(
                "${blameSource(source, startElement.location)}:" +
                        " No resource to outline. <${startElement.name}> block is empty.")
        }

        if (errors.any()) {
            error(errors.joinToString(separator = ","))
        }

        return outputFile
    }

    private fun processText(characters: Characters, builder: XmlResourceBuilder) =
        builder.addText(characters.data)

    private fun processComment(builder: XmlResourceBuilder) = builder.addComment()

    private fun gatherIds(
        startElement: StartElement,
        collectedIds: MutableMap<ResourceName, SourcedResourceName>
    ) {
        val iterator = startElement.attributes
        while (iterator.hasNext()) {
            val attribute = iterator.next() as Attribute

            val parsedRef = parseReference(attribute.value)
            parsedRef ?: continue

            val resourceName = parsedRef.reference.name
            if (parsedRef.createNew && resourceName.type == AaptResourceType.ID) {
                if (!isValidResourceEntryName(resourceName.entry!!)) {
                    error("${blameSource(source, startElement.location)} " +
                            "Id '$resourceName' has an invalid entry name '${resourceName.entry}'.")
                } else {
                    collectedIds.putIfAbsent(
                        resourceName,
                        SourcedResourceName(resourceName, startElement.location.lineNumber)
                    )
                }
            }
        }
    }

    private fun getNextAttrResourceFile(
        suffix: Int, element: StartElement
    ): ResourceFile {

        val newEntryName = mangleEntry("", "${primaryFile.name.entry}__$suffix")

        return ResourceFile(
            primaryFile.name.copy(entry = newEntryName),
            primaryFile.configuration,
            primaryFile.source.withLine(element.location.lineNumber),
            ProtoXml
        )
    }
}
