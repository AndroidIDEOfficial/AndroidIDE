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
 * $Id: XResourceBundleBase.java,v 1.2.4.1 2005/09/15 08:16:05 suresh_emailid Exp $
 */
package jaxp.sun.org.apache.xml.internal.utils.res;

import java.util.ListResourceBundle;

/**
 * This is an interface for error messages.  This class is misnamed,
 * and should be called XalanResourceBundle, or some such.
 * @xsl.usage internal
 */
abstract public class XResourceBundleBase extends ListResourceBundle
{

  /**
   * Get the error string associated with the error code
   *
   * @param errorCode Error code
   *
   * @return error string associated with the given error code
   */
  abstract public String getMessageKey(int errorCode);

  /**
   * Get the warning string associated with the error code
   *
   * @param errorCode Error code
   *
   * @return warning string associated with the given error code
   */
  abstract public String getWarningKey(int errorCode);
}
