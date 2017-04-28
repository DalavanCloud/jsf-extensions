/*
 * ScriptRenderer.java
 */

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


package com.sun.faces.extensions.compres.render;

import java.io.IOException;
import java.util.Map;
import javax.faces.application.CompResApplication;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

/**
 *
 * @author edburns
 */
public class ScriptRenderer extends Renderer {
    public void encodeBegin(FacesContext facesContext, UIComponent comp) throws IOException {
        Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
        Resource resource = ResourceRendererHelper.getResource(facesContext,
                comp);
        ResponseWriter writer;
        assert(null != resource);
        String scriptURI = resource.getURI();
        // If this script has not yet been written to the request
        if (null != scriptURI && !requestMap.containsKey(scriptURI)) {
            writer = facesContext.getResponseWriter();
            writer.startElement("script", comp);
            writer.writeAttribute("type", resource.getContentType(), "type");
            writer.writeURIAttribute("src", scriptURI, "src");
            writer.endElement("script");
            requestMap.put(scriptURI, scriptURI);
        }
    }

    public boolean getRendersChildren() {
        boolean retValue;
        
        retValue = true;
        return retValue;
    }
    
    
    

    
}