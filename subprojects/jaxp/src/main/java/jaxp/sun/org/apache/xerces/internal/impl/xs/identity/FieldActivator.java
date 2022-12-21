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
 * Copyright 2001,2002,2004 The Apache Software Foundation.
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

package jaxp.sun.org.apache.xerces.internal.impl.xs.identity;


/**
 * Interface for a field activator. The field activator is responsible
 * for activating fields within a specific scope; the caller merely
 * requests the fields to be activated.
 *
 * @xerces.internal
 *
 * @author Andy Clark, IBM
 *
 */
public interface FieldActivator {

    //
    // FieldActivator methods
    //

    /**
     * Start the value scope for the specified identity constraint. This
     * method is called when the selector matches in order to initialize
     * the value store.
     *
     * @param identityConstraint The identity constraint.
     * @param initialDepth  the depth at which the selector began matching
     */
    public void startValueScopeFor(IdentityConstraint identityConstraint,
            int initialDepth);

    /**
     * Request to activate the specified field. This method returns the
     * matcher for the field.
     * It's also important for the implementor to ensure that it marks whether a Field
     * is permitted to match a value--that is, to call the setMayMatch(Field, Boolean) method.
     *
     * @param field The field to activate.
     * @param initialDepth the 0-indexed depth in the instance document at which the Selector began to match.
     */
    public XPathMatcher activateField(Field field, int initialDepth);

    /**
     * Sets whether the given field is permitted to match a value.
     * This should be used to catch instance documents that try
     * and match a field several times in the same scope.
     *
     * @param field The field that may be permitted to be matched.
     * @param state Boolean indiciating whether the field may be matched.
     */
    public void setMayMatch(Field field, Boolean state);

    /**
     * Returns whether the given field is permitted to match a value.
     *
     * @param field The field that may be permitted to be matched.
     * @return Boolean indicating whether the field may be matched.
     */
    public Boolean mayMatch(Field field);

    /**
     * Ends the value scope for the specified identity constraint.
     *
     * @param identityConstraint The identity constraint.
     * @param initialDepth  the 0-indexed depth where the Selector began to match.
     */
    public void endValueScopeFor(IdentityConstraint identityConstraint, int initialDepth);

} // interface FieldActivator
