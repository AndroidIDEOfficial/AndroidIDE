/**
 * Copyright (c) 2018 Angelo ZERR All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v20.html
 *
 * <p>SPDX-License-Identifier: EPL-2.0
 *
 * <p>Contributors: Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.lemminx.uriresolver;

import java.io.IOException;

import jaxp.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import jaxp.sun.org.apache.xerces.internal.xni.XNIException;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;

/** URI resolver API */
public interface URIResolverExtension extends XMLEntityResolver {

  public static final String DEFAULT = "default";

  default String getName() {
    return getClass().getSimpleName();
  }

  /**
   * @param baseLocation - the location of the resource that contains the uri
   * @param publicId - an optional public identifier (i.e. namespace name), or null if none
   * @param systemId - an absolute or relative URI, or null if none
   * @return an absolute URI representation of the 'logical' location of the resource
   */
  public String resolve(String baseLocation, String publicId, String systemId);

  @Override
  default XMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier)
      throws XNIException, IOException {
    return null;
  }
}
