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
// TR9401CatalogReader.java - Read OASIS Catalog files

/*
 * Copyright 2001-2004 The Apache Software Foundation or its licensors,
 * as applicable.
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

package jaxp.sun.org.apache.xml.internal.resolver.readers;

import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;
import jaxp.sun.org.apache.xml.internal.resolver.Catalog;
import jaxp.sun.org.apache.xml.internal.resolver.CatalogEntry;
import jaxp.sun.org.apache.xml.internal.resolver.CatalogException;

/**
 * Parses OASIS Open Catalog files.
 *
 * <p>This class reads OASIS Open Catalog files, returning a stream
 * of tokens.</p>
 *
 * <p>This code interrogates the following non-standard system properties:</p>
 *
 * <dl>
 * <dt><b>xml.catalog.debug</b></dt>
 * <dd><p>Sets the debug level. A value of 0 is assumed if the
 * property is not set or is not a number.</p></dd>
 * </dl>
 *
 * @see Catalog
 *
 * @author Norman Walsh
 * <a href="mailto:Norman.Walsh@Sun.COM">Norman.Walsh@Sun.COM</a>
 *
 */
public class TR9401CatalogReader extends TextCatalogReader {

  /**
   * Start parsing an OASIS TR9401 Open Catalog file. The file is
   * actually read and parsed
   * as needed by <code>nextEntry</code>.
   *
   * <p>In a TR9401 Catalog the 'DELEGATE' entry delegates public
   * identifiers. There is no delegate entry for system identifiers
   * or URIs.</p>
   *
   * @param catalog The Catalog to populate
   * @param is The input stream from which to read the TR9401 Catalog
   *
   * @throws MalformedURLException Improper fileUrl
   * @throws IOException Error reading catalog file
   */
  public void readCatalog(Catalog catalog, InputStream is)
    throws MalformedURLException, IOException {

    catfile = is;

    if (catfile == null) {
      return;
    }

    Vector unknownEntry = null;

    try {
      while (true) {
        String token = nextToken();

        if (token == null) {
          if (unknownEntry != null) {
            catalog.unknownEntry(unknownEntry);
            unknownEntry = null;
          }
          catfile.close();
          catfile = null;
          return;
        }

        String entryToken = null;
        if (caseSensitive) {
          entryToken = token;
        } else {
          entryToken = token.toUpperCase();
        }

        if (entryToken.equals("DELEGATE")) {
          entryToken = "DELEGATE_PUBLIC";
        }

        try {
          int type = CatalogEntry.getEntryType(entryToken);
          int numArgs = CatalogEntry.getEntryArgCount(type);
          Vector args = new Vector();

          if (unknownEntry != null) {
            catalog.unknownEntry(unknownEntry);
            unknownEntry = null;
          }

          for (int count = 0; count < numArgs; count++) {
            args.addElement(nextToken());
          }

          catalog.addEntry(new CatalogEntry(entryToken, args));
        } catch (CatalogException cex) {
          if (cex.getExceptionType() == CatalogException.INVALID_ENTRY_TYPE) {
            if (unknownEntry == null) {
              unknownEntry = new Vector();
            }
            unknownEntry.addElement(token);
          } else if (cex.getExceptionType() == CatalogException.INVALID_ENTRY) {
            catalog.getCatalogManager().debug.message(1, "Invalid catalog entry", token);
            unknownEntry = null;
          } else if (cex.getExceptionType() == CatalogException.UNENDED_COMMENT) {
            catalog.getCatalogManager().debug.message(1, cex.getMessage());
          }
        }
      }
    } catch (CatalogException cex2) {
      if (cex2.getExceptionType() == CatalogException.UNENDED_COMMENT) {
        catalog.getCatalogManager().debug.message(1, cex2.getMessage());
      }
    }

  }
}
