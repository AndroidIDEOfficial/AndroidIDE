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
 * Copyright 2001-2004 The Apache Software Foundation.
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
 * $Id: ProcessorVersion.java,v 1.2.4.1 2005/08/31 10:30:36 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc;


/**
 * Admin class that assigns a version number to the XSLTC software.
 * The version number is made up from three fields as in:
 * MAJOR.MINOR[.DELTA]. Fields are incremented based on the following:
 * DELTA field: changes for each bug fix, developer fixing the bug should
 *              increment this field.
 * MINOR field: API changes or a milestone culminating from several
 *              bug fixes. DELTA field goes to zero and MINOR is
 *              incremented such as: {1.0,1.0.1,1.0.2,1.0.3,...1.0.18,1.1}
 * MAJOR field: milestone culminating in fundamental API changes or
 *              architectural changes.  MINOR field goes to zero
 *              and MAJOR is incremented such as: {...,1.1.14,1.2,2.0}
 * Stability of a release follows: X.0 > X.X > X.X.X
 * @author G. Todd Miller
 */
public class ProcessorVersion {
    private static int MAJOR = 1;
    private static int MINOR = 0;
    private static int DELTA = 0;

    public static void main(String[] args) {
        System.out.println("XSLTC version " + MAJOR + "." + MINOR +
            ((DELTA > 0) ? ("."+DELTA) : ("")));
    }
}
