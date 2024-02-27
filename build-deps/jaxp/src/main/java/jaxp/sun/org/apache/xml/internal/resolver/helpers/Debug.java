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
// Debug.java - Print debug messages

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

package jaxp.sun.org.apache.xml.internal.resolver.helpers;

/**
 * Static debugging/messaging class for Catalogs.
 *
 * <p>This class defines a set of static methods that can be called
 * to produce debugging messages. Messages have an associated "debug
 * level" and messages below the current setting are not displayed.</p>
 *
 * @author Norman Walsh
 * <a href="mailto:Norman.Walsh@Sun.COM">Norman.Walsh@Sun.COM</a>
 *
 */
public class Debug {
  /** The internal debug level. */
  protected int debug = 0;

  /** Constructor */
  public Debug() {
    // nop
  }

  /** Set the debug level for future messages. */
  public void setDebug(int newDebug) {
    debug = newDebug;
  }

  /** Get the current debug level. */
  public int getDebug() {
    return debug;
  }

  /**
   * Print debug message (if the debug level is high enough).
   *
   * <p>Prints "the message"</p>
   *
   * @param level The debug level of this message. This message
   * will only be
   * displayed if the current debug level is at least equal to this
   * value.
   * @param message The text of the message.
   */
  public void message(int level, String message) {
    if (debug >= level) {
      System.out.println(message);
    }
  }

  /**
   * Print debug message (if the debug level is high enough).
   *
   * <p>Prints "the message: spec"</p>
   *
   * @param level The debug level of this message. This message
   * will only be
   * displayed if the current debug level is at least equal to this
   * value.
   * @param message The text of the message.
   * @param spec An argument to the message.
   */
  public void message(int level, String message, String spec) {
    if (debug >= level) {
      System.out.println(message + ": " + spec);
    }
  }

  /**
   * Print debug message (if the debug level is high enough).
   *
   * <p>Prints "the message: spec1" and "spec2" indented on the next line.</p>
   *
   * @param level The debug level of this message. This message
   * will only be
   * displayed if the current debug level is at least equal to this
   * value.
   * @param message The text of the message.
   * @param spec1 An argument to the message.
   * @param spec2 Another argument to the message.
   */
  public void message(int level, String message,
                             String spec1, String spec2) {
    if (debug >= level) {
      System.out.println(message + ": " + spec1);
      System.out.println("\t" + spec2);
    }
  }
}
