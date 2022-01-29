/*
 * reserved comment block
 * DO NOT REMOVE OR ALTER!
 */
/*
 * Copyright 2005 The Apache Software Foundation.
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

package com.itsaky.org.apache.xerces.internal.xs.datatypes;

import com.itsaky.org.apache.xerces.internal.xni.QName;

/**
 * Interface to expose QName actual values
 *
 * @author Ankit Pasricha, IBM
 */
public interface XSQName {

    /**
     * @return com.itsaky.org.apache.xerces.internal.xni.QName class instance
     */
    public QName getXNIQName();

    /**
     * @return jaxp.xml.namespace.QName class instance
     */
    public jaxp.xml.namespace.QName getJAXPQName();
}
