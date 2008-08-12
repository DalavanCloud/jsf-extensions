/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.mock;

import java.io.IOException;

import javax.faces.application.ResourceHandler;
import javax.faces.application.Resource;
import javax.faces.context.FacesContext;

/**
 * Created by IntelliJ IDEA. User: rlubke Date: Jun 15, 2008 Time: 3:14:15 PM To
 * change this template use File | Settings | File Templates.
 */
public class MockResourceHandler extends ResourceHandler {

    public Resource createResource(String resourceName) {
        throw new UnsupportedOperationException();
    }

    public Resource createResource(String resourceName, String libraryName) {
        throw new UnsupportedOperationException();
    }

    public Resource createResource(String resourceName,
                                   String libraryName,
                                   String contentType) {
        throw new UnsupportedOperationException();
    }

    public void handleResourceRequest(FacesContext context) throws IOException {
        throw new UnsupportedOperationException();
    }

    public boolean isResourceRequest(FacesContext context) {
        throw new UnsupportedOperationException();
    }
    
    public boolean libraryExists(String libraryName) {
        throw new UnsupportedOperationException();
    }

    public String getRendererTypeForResourceName(String resourceName) {
        if (resourceName.endsWith(".js")) {
            return "javax.faces.resource.Script";
        } else if (resourceName.endsWith(".css")) {
            return "javax.faces.resource.Stylesheet";
        } else {
            return null;  
        }
    }

}
