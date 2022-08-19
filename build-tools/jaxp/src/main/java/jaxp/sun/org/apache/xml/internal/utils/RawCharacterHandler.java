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
 * Copyright 1999-2004 The Apache Software Foundation.
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
 * $Id: RawCharacterHandler.java,v 1.2.4.1 2005/09/15 08:15:52 suresh_emailid Exp $
 */
package jaxp.sun.org.apache.xml.internal.utils;

import jaxp.xml.transform.TransformerException;

/**
 * An interface that a Serializer/ContentHandler/ContentHandler must
 * implement in order for disable-output-escaping to work.
 * @xsl.usage advanced
 */
public interface RawCharacterHandler
{

  /**
   * Serialize the characters without escaping.
   *
   * @param ch Array of characters
   * @param start Start index of characters in the array
   * @param length Number of characters in the array
   *
   * @throws TransformerException
   */
  public void charactersRaw(char ch[], int start, int length)
    throws TransformerException;
}
