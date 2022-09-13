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
 * $Id: StringToIntTable.java,v 1.1.4.1 2005/09/08 11:03:19 suresh_emailid Exp $
 */
package jaxp.sun.org.apache.xml.internal.serializer.utils;

/**
 * A very simple lookup table that stores a list of strings, the even
 * number strings being keys, and the odd number strings being values.
 *
 * This class is a copy of the one in com.sun.org.apache.xml.internal.utils.
 * It exists to cut the serializers dependancy on that package.
 *
 * This class is not a public API, it is only public so it can be used
 * in com.sun.org.apache.xml.internal.serializer.
 *
 * @xsl.usage internal
 */
public final class StringToIntTable
{

  public static final int INVALID_KEY = -10000;

  /** Block size to allocate          */
  private int m_blocksize;

  /** Array of strings this table points to. Associated with ints
   * in m_values         */
  private String m_map[];

  /** Array of ints this table points. Associated with strings from
   * m_map.         */
  private int m_values[];

  /** Number of ints in the table          */
  private int m_firstFree = 0;

  /** Size of this table         */
  private int m_mapSize;

  /**
   * Default constructor.  Note that the default
   * block size is very small, for small lists.
   */
  public StringToIntTable()
  {

    m_blocksize = 8;
    m_mapSize = m_blocksize;
    m_map = new String[m_blocksize];
    m_values = new int[m_blocksize];
  }

  /**
   * Construct a StringToIntTable, using the given block size.
   *
   * @param blocksize Size of block to allocate
   */
  public StringToIntTable(int blocksize)
  {

    m_blocksize = blocksize;
    m_mapSize = blocksize;
    m_map = new String[blocksize];
    m_values = new int[m_blocksize];
  }

  /**
   * Get the length of the list.
   *
   * @return the length of the list
   */
  public final int getLength()
  {
    return m_firstFree;
  }

  /**
   * Append a string onto the vector.
   *
   * @param key String to append
   * @param value The int value of the string
   */
  public final void put(String key, int value)
  {

    if ((m_firstFree + 1) >= m_mapSize)
    {
      m_mapSize += m_blocksize;

      String newMap[] = new String[m_mapSize];

      System.arraycopy(m_map, 0, newMap, 0, m_firstFree + 1);

      m_map = newMap;

      int newValues[] = new int[m_mapSize];

      System.arraycopy(m_values, 0, newValues, 0, m_firstFree + 1);

      m_values = newValues;
    }

    m_map[m_firstFree] = key;
    m_values[m_firstFree] = value;

    m_firstFree++;
  }

  /**
   * Tell if the table contains the given string.
   *
   * @param key String to look for
   *
   * @return The String's int value
   *
   */
  public final int get(String key)
  {

    for (int i = 0; i < m_firstFree; i++)
    {
      if (m_map[i].equals(key))
        return m_values[i];
    }

    return INVALID_KEY;
  }

  /**
   * Tell if the table contains the given string. Ignore case.
   *
   * @param key String to look for
   *
   * @return The string's int value
   */
  public final int getIgnoreCase(String key)
  {

    if (null == key)
        return INVALID_KEY;

    for (int i = 0; i < m_firstFree; i++)
    {
      if (m_map[i].equalsIgnoreCase(key))
        return m_values[i];
    }

    return INVALID_KEY;
  }

  /**
   * Tell if the table contains the given string.
   *
   * @param key String to look for
   *
   * @return True if the string is in the table
   */
  public final boolean contains(String key)
  {

    for (int i = 0; i < m_firstFree; i++)
    {
      if (m_map[i].equals(key))
        return true;
    }

    return false;
  }

  /**
   * Return array of keys in the table.
   *
   * @return Array of strings
   */
  public final String[] keys()
  {
    String [] keysArr = new String[m_firstFree];

    for (int i = 0; i < m_firstFree; i++)
    {
      keysArr[i] = m_map[i];
    }

    return keysArr;
  }
}
