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
 * Copyright 2000-2002,2004 The Apache Software Foundation.
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

package jaxp.sun.org.apache.xerces.internal.xni;

import java.util.Enumeration;

/**
 * The Augmentations interface defines a table of additional data that may
 * be passed along the document pipeline. The information can contain extra
 * arguments or infoset augmentations, for example PSVI. This additional
 * information is identified by a String key.
 * <p>
 * <strong>Note:</strong>
 * Methods that receive Augmentations are required to copy the information
 * if it is to be saved for use beyond the scope of the method.
 * The Augmentations content is volatile, and maybe modified by any method in
 * any component in the pipeline. Therefore, methods passed this structure
 * should not save any reference to the structure.
 *
 * @author Elena Litani, IBM
 */

public interface Augmentations {


    /**
     * Add additional information identified by a key to the Augmentations structure.
     *
     * @param key    Identifier, can't be <code>null</code>
     * @param item   Additional information
     *
     * @return the previous value of the specified key in the Augmentations structure,
     *         or <code>null</code> if it did not have one.
     */
    public Object putItem (String key, Object item);


    /**
     * Get information identified by a key from the Augmentations structure
     *
     * @param key    Identifier, can't be <code>null</code>
     *
     * @return the value to which the key is mapped in the Augmentations structure;
     *         <code>null</code> if the key is not mapped to any value.
     */
    public Object getItem(String key);


    /**
     * Remove additional info from the Augmentations structure
     *
     * @param key    Identifier, can't be <code>null</code>
     * @return the previous value of the specified key in the Augmentations structure,
     *         or <code>null</code> if it did not have one.
     */
    public Object removeItem (String key);


    /**
     * Returns an enumeration of the keys in the Augmentations structure
     *
     */
    public Enumeration keys ();


    /**
     * Remove all objects from the Augmentations structure.
     */
    public void removeAllItems ();

}
