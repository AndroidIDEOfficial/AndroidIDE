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
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * $Id: MsgKey.java,v 1.1.4.1 2005/09/08 11:03:11 suresh_emailid Exp $
 */
package jaxp.sun.org.apache.xml.internal.serializer.utils;

/**
 * This class is not a public API,
 * It is used internally by serializer and is public,
 * in the Java sense, only because its use crosses
 * package boundaries.
 * <p>
 * This class holds only the message keys used
 * when generating messages.
 */
public class MsgKey {

    /** An internal error with the messages,
     * this is the message to use if the message key can't be found
     */
    public static final String BAD_MSGKEY = "BAD_MSGKEY";

    /**
     * An internal error with the messages,
     * this is the message to use if the message format operation failed.
     */
    public static final String BAD_MSGFORMAT = "BAD_MSGFORMAT";

    public static final String ER_RESOURCE_COULD_NOT_FIND =
        "ER_RESOURCE_COULD_NOT_FIND";
    public static final String ER_RESOURCE_COULD_NOT_LOAD =
        "ER_RESOURCE_COULD_NOT_LOAD";
    public static final String ER_BUFFER_SIZE_LESSTHAN_ZERO =
        "ER_BUFFER_SIZE_LESSTHAN_ZERO";
    public static final String ER_INVALID_UTF16_SURROGATE =
        "ER_INVALID_UTF16_SURROGATE";
    public static final String ER_OIERROR = "ER_OIERROR";
    public static final String ER_NAMESPACE_PREFIX = "ER_NAMESPACE_PREFIX";
    public static final String ER_STRAY_ATTRIBUTE = "ER_STRAY_ATTRIBUTE";
    public static final String ER_STRAY_NAMESPACE = "ER_STRAY_NAMESPACE";
    public static final String ER_COULD_NOT_LOAD_RESOURCE =
        "ER_COULD_NOT_LOAD_RESOURCE";
    public static final String ER_COULD_NOT_LOAD_METHOD_PROPERTY =
        "ER_COULD_NOT_LOAD_METHOD_PROPERTY";
    public static final String ER_SERIALIZER_NOT_CONTENTHANDLER =
        "ER_SERIALIZER_NOT_CONTENTHANDLER";
    public static final String ER_ILLEGAL_ATTRIBUTE_POSITION =
        "ER_ILLEGAL_ATTRIBUTE_POSITION";
    public static final String ER_ILLEGAL_CHARACTER = "ER_ILLEGAL_CHARACTER";

    public static final String ER_INVALID_PORT = "ER_INVALID_PORT";
    public static final String ER_PORT_WHEN_HOST_NULL =
        "ER_PORT_WHEN_HOST_NULL";
    public static final String ER_HOST_ADDRESS_NOT_WELLFORMED =
        "ER_HOST_ADDRESS_NOT_WELLFORMED";
    public static final String ER_SCHEME_NOT_CONFORMANT =
        "ER_SCHEME_NOT_CONFORMANT";
    public static final String ER_SCHEME_FROM_NULL_STRING =
        "ER_SCHEME_FROM_NULL_STRING";
    public static final String ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE =
        "ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE";
    public static final String ER_PATH_INVALID_CHAR = "ER_PATH_INVALID_CHAR";
    public static final String ER_NO_SCHEME_INURI = "ER_NO_SCHEME_INURI";
    public static final String ER_FRAG_INVALID_CHAR = "ER_FRAG_INVALID_CHAR";
    public static final String ER_FRAG_WHEN_PATH_NULL =
        "ER_FRAG_WHEN_PATH_NULL";
    public static final String ER_FRAG_FOR_GENERIC_URI =
        "ER_FRAG_FOR_GENERIC_URI";
    public static final String ER_NO_SCHEME_IN_URI = "ER_NO_SCHEME_IN_URI";
    public static final String ER_CANNOT_INIT_URI_EMPTY_PARMS =
        "ER_CANNOT_INIT_URI_EMPTY_PARMS";
    public static final String ER_NO_FRAGMENT_STRING_IN_PATH =
        "ER_NO_FRAGMENT_STRING_IN_PATH";
    public static final String ER_NO_QUERY_STRING_IN_PATH =
        "ER_NO_QUERY_STRING_IN_PATH";
    public static final String ER_NO_PORT_IF_NO_HOST = "ER_NO_PORT_IF_NO_HOST";
    public static final String ER_NO_USERINFO_IF_NO_HOST =
        "ER_NO_USERINFO_IF_NO_HOST";
    public static final String ER_SCHEME_REQUIRED = "ER_SCHEME_REQUIRED";
    public static final String ER_XML_VERSION_NOT_SUPPORTED = "ER_XML_VERSION_NOT_SUPPORTED";
    public static final String ER_FACTORY_PROPERTY_MISSING = "ER_FACTORY_PROPERTY_MISSING";
    public static final String ER_ENCODING_NOT_SUPPORTED = "ER_ENCODING_NOT_SUPPORTED";

}
