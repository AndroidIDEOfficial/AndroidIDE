/**
 * Copyright (c) 2020 Red Hat, Inc. and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * <p>SPDX-License-Identifier: EPL-2.0
 *
 * <p>Contributors: Red Hat Inc. - initial API and implementation
 */
package org.eclipse.lemminx.dom;

import com.itsaky.androidide.models.Range;

/** Target range API. */
public interface TargetRange {

  /**
   * Returns the target range.
   *
   * @return the target range.
   */
  Range getTargetRange();

  /**
   * Returns the target URI.
   *
   * @return the target URI.
   */
  String getTargetURI();
}
