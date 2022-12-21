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
 * $Id: LocaleUtility.java,v 1.2.4.1 2005/09/15 08:15:47 suresh_emailid Exp $
 */

package jaxp.sun.org.apache.xml.internal.utils;

import java.util.Locale;

/**
 * @author Igor Hersht, igorh@ca.ibm.com
 */
public class LocaleUtility {
    /**
     * IETF RFC 1766 tag separator
     */
    public final static char IETF_SEPARATOR = '-';
    public final static String EMPTY_STRING = "";


 public static Locale langToLocale(String lang) {
       if((lang == null) || lang.equals(EMPTY_STRING)){ // not specified => getDefault
            return Locale.getDefault();
       }
        String language = EMPTY_STRING;
        String country =  EMPTY_STRING;
        String variant =  EMPTY_STRING;

        int i1 = lang.indexOf(IETF_SEPARATOR);
        if (i1 < 0) {
            language = lang;
        } else {
            language = lang.substring(0, i1);
            ++i1;
            int i2 = lang.indexOf(IETF_SEPARATOR, i1);
            if (i2 < 0) {
                country = lang.substring(i1);
            } else {
                country = lang.substring(i1, i2);
                variant = lang.substring(i2+1);
            }
        }

        if(language.length() == 2){
           language = language.toLowerCase();
        }else {
          language = EMPTY_STRING;
        }

        if(country.length() == 2){
           country = country.toUpperCase();
        }else {
          country = EMPTY_STRING;
        }

        if((variant.length() > 0) &&
        ((language.length() == 2) ||(country.length() == 2))){
           variant = variant.toUpperCase();
        }else{
            variant = EMPTY_STRING;
        }

        return new Locale(language, country, variant );
    }



 }
