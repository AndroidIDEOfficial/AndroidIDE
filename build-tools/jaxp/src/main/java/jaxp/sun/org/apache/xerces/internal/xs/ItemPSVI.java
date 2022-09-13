/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * reserved comment block
 * DO NOT REMOVE OR ALTER!
 */
/*
 * Copyright 2003,2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jaxp.sun.org.apache.xerces.internal.xs;

/**
 *  Represents an abstract PSVI item for an element or an attribute
 * information item.
 */
public interface ItemPSVI {
    /**
     * Validity value indicating that validation has either not been performed
     * or that a strict assessment of validity could not be performed.
     */
    public static final short VALIDITY_NOTKNOWN         = 0;
    /**
     *  Validity value indicating that validation has been strictly assessed
     * and the item in question is invalid according to the rules of schema
     * validation.
     */
    public static final short VALIDITY_INVALID          = 1;
    /**
     *  Validation status indicating that schema validation has been performed
     * and the item in question is valid according to the rules of schema
     * validation.
     */
    public static final short VALIDITY_VALID            = 2;
    /**
     *  Validation status indicating that schema validation has been performed
     * and the item in question has specifically been skipped.
     */
    public static final short VALIDATION_NONE           = 0;
    /**
     * Validation status indicating that schema validation has been performed
     * on the item in question under the rules of lax validation.
     */
    public static final short VALIDATION_PARTIAL        = 1;
    /**
     *  Validation status indicating that full schema validation has been
     * performed on the item.
     */
    public static final short VALIDATION_FULL           = 2;
    /**
     *  The nearest ancestor element information item with a
     * <code>[schema information]</code> property (or this element item
     * itself if it has such a property). For more information refer to
     * element validation context and attribute validation context .
     */
    public String getValidationContext();

    /**
     *  <code>[validity]</code>: determines the validity of the schema item
     * with respect to the validation being attempted. The value will be one
     * of the constants: <code>VALIDITY_NOTKNOWN</code>,
     * <code>VALIDITY_INVALID</code> or <code>VALIDITY_VALID</code>.
     */
    public short getValidity();

    /**
     *  <code>[validation attempted]</code>: determines the extent to which
     * the schema item has been validated. The value will be one of the
     * constants: <code>VALIDATION_NONE</code>,
     * <code>VALIDATION_PARTIAL</code> or <code>VALIDATION_FULL</code>.
     */
    public short getValidationAttempted();

    /**
     *  <code>[schema error code]</code>: a list of error codes generated from
     * the validation attempt or an empty <code>StringList</code> if no
     * errors occurred during the validation attempt.
     */
    public StringList getErrorCodes();

    /**
     * <code>[schema normalized value]</code>: the normalized value of this
     * item after validation.
     */
    public String getSchemaNormalizedValue();

    /**
     * <code>[schema normalized value]</code>: Binding specific actual value
     * or <code>null</code> if the value is in error.
     * @exception XSException
     *   NOT_SUPPORTED_ERR: Raised if the implementation does not support this
     *   method.
     */
    public Object getActualNormalizedValue()
                                   throws XSException;

    /**
     * The actual value built-in datatype, e.g.
     * <code>STRING_DT, SHORT_DT</code>. If the type definition of this
     * value is a list type definition, this method returns
     * <code>LIST_DT</code>. If the type definition of this value is a list
     * type definition whose item type is a union type definition, this
     * method returns <code>LISTOFUNION_DT</code>. To query the actual value
     * of the list or list of union type definitions use
     * <code>itemValueTypes</code>. If the <code>actualNormalizedValue</code>
     *  is <code>null</code>, this method returns <code>UNAVAILABLE_DT</code>
     * .
     * @exception XSException
     *   NOT_SUPPORTED_ERR: Raised if the implementation does not support this
     *   method.
     */
    public short getActualNormalizedValueType()
                                   throws XSException;

    /**
     * In the case the actual value represents a list, i.e. the
     * <code>actualNormalizedValueType</code> is <code>LIST_DT</code>, the
     * returned array consists of one type kind which represents the itemType
     * . For example:
     * <pre> &lt;simpleType name="listtype"&gt; &lt;list
     * itemType="positiveInteger"/&gt; &lt;/simpleType&gt; &lt;element
     * name="list" type="listtype"/&gt; ... &lt;list&gt;1 2 3&lt;/list&gt; </pre>
     *
     * The <code>schemaNormalizedValue</code> value is "1 2 3", the
     * <code>actualNormalizedValueType</code> value is <code>LIST_DT</code>,
     * and the <code>itemValueTypes</code> is an array of size 1 with the
     * value <code>POSITIVEINTEGER_DT</code>.
     * <br> If the actual value represents a list type definition whose item
     * type is a union type definition, i.e. <code>LISTOFUNION_DT</code>,
     * for each actual value in the list the array contains the
     * corresponding memberType kind. For example:
     * <pre> &lt;simpleType
     * name='union_type' memberTypes="integer string"/&gt; &lt;simpleType
     * name='listOfUnion'&gt; &lt;list itemType='union_type'/&gt;
     * &lt;/simpleType&gt; &lt;element name="list" type="listOfUnion"/&gt;
     * ... &lt;list&gt;1 2 foo&lt;/list&gt; </pre>
     *  The
     * <code>schemaNormalizedValue</code> value is "1 2 foo", the
     * <code>actualNormalizedValueType</code> is <code>LISTOFUNION_DT</code>
     * , and the <code>itemValueTypes</code> is an array of size 3 with the
     * following values: <code>INTEGER_DT, INTEGER_DT, STRING_DT</code>.
     * @exception XSException
     *   NOT_SUPPORTED_ERR: Raised if the implementation does not support this
     *   method.
     */
    public ShortList getItemValueTypes()
                                   throws XSException;

    /**
     *  <code>[type definition]</code>: an item isomorphic to the type
     * definition used to validate the schema item.
     */
    public XSTypeDefinition getTypeDefinition();

    /**
     * <code>[member type definition]</code>: if and only if that type
     * definition is a simple type definition with {variety} union, or a
     * complex type definition whose {content type} is a simple type
     * definition with {variety} union, then an item isomorphic to that
     * member of the union's {member type definitions} which actually
     * validated the schema item's normalized value.
     */
    public XSSimpleTypeDefinition getMemberTypeDefinition();

    /**
     * <code>[schema default]</code>: the canonical lexical representation of
     * the declaration's {value constraint} value. For more information
     * refer to element schema default and attribute schema default.
     */
    public String getSchemaDefault();

    /**
     * <code>[schema specified]</code>: if true, the value was specified in
     * the schema. If false, the value comes from the infoset. For more
     * information refer to element specified and attribute specified.
     */
    public boolean getIsSchemaSpecified();

}
