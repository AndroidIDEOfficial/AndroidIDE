package org.w3c.dom.events;

import org.w3c.dom.Node;
/**
 * The <code>MutationEvent</code> interface provides specific contextual
 * information associated with Mutation events.
 * <p>See also the <a href='http://www.w3.org/TR/2000/REC-DOM-Level-2-Events-20001113'>Document Object Model (DOM) Level 2 Events Specification</a>.
 * @since DOM Level 2
 */
public interface MutationEvent extends Event {
    // attrChangeType
    /**
     * The <code>Attr</code> was modified in place.
     */
    public static final short MODIFICATION              = 1;
    /**
     * The <code>Attr</code> was just added.
     */
    public static final short ADDITION                  = 2;
    /**
     * The <code>Attr</code> was just removed.
     */
    public static final short REMOVAL                   = 3;
    /**
     *  <code>relatedNode</code> is used to identify a secondary node related
     * to a mutation event. For example, if a mutation event is dispatched
     * to a node indicating that its parent has changed, the
     * <code>relatedNode</code> is the changed parent. If an event is
     * instead dispatched to a subtree indicating a node was changed within
     * it, the <code>relatedNode</code> is the changed node. In the case of
     * the DOMAttrModified event it indicates the <code>Attr</code> node
     * which was modified, added, or removed.
     */
    public Node getRelatedNode();
    /**
     *  <code>prevValue</code> indicates the previous value of the
     * <code>Attr</code> node in DOMAttrModified events, and of the
     * <code>CharacterData</code> node in DOMCharacterDataModified events.
     */
    public String getPrevValue();
    /**
     *  <code>newValue</code> indicates the new value of the <code>Attr</code>
     * node in DOMAttrModified events, and of the <code>CharacterData</code>
     * node in DOMCharacterDataModified events.
     */
    public String getNewValue();
    /**
     *  <code>attrName</code> indicates the name of the changed
     * <code>Attr</code> node in a DOMAttrModified event.
     */
    public String getAttrName();
    /**
     *  <code>attrChange</code> indicates the type of change which triggered
     * the DOMAttrModified event. The values can be <code>MODIFICATION</code>
     * , <code>ADDITION</code>, or <code>REMOVAL</code>.
     */
    public short getAttrChange();
    /**
     * The <code>initMutationEvent</code> method is used to initialize the
     * value of a <code>MutationEvent</code> created through the
     * <code>DocumentEvent</code> interface. This method may only be called
     * before the <code>MutationEvent</code> has been dispatched via the
     * <code>dispatchEvent</code> method, though it may be called multiple
     * times during that phase if necessary. If called multiple times, the
     * final invocation takes precedence.
     * @param typeArg Specifies the event type.
     * @param canBubbleArg Specifies whether or not the event can bubble.
     * @param cancelableArg Specifies whether or not the event's default
     *   action can be prevented.
     * @param relatedNodeArg Specifies the <code>Event</code>'s related Node.
     * @param prevValueArg Specifies the <code>Event</code>'s
     *   <code>prevValue</code> attribute. This value may be null.
     * @param newValueArg Specifies the <code>Event</code>'s
     *   <code>newValue</code> attribute. This value may be null.
     * @param attrNameArg Specifies the <code>Event</code>'s
     *   <code>attrName</code> attribute. This value may be null.
     * @param attrChangeArg Specifies the <code>Event</code>'s
     *   <code>attrChange</code> attribute
     */
    public void initMutationEvent(String typeArg,
                                  boolean canBubbleArg,
                                  boolean cancelableArg,
                                  Node relatedNodeArg,
                                  String prevValueArg,
                                  String newValueArg,
                                  String attrNameArg,
                                  short attrChangeArg);
}