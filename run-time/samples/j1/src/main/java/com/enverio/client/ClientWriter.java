/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

package com.enverio.client;

import com.sun.faces.extensions.avatar.lifecycle.AsyncResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;

public class ClientWriter extends ResponseWriterWrapper {
    
    
    private final static ThreadLocal<ClientWriter> Instance = new ThreadLocal<ClientWriter>();
    
    public static boolean exists() {
        return Instance.get() != null;
    }
    
    public static ClientWriter getInstance(boolean create) {
        ClientWriter cw = Instance.get();
        if (cw == null && create) {
            try {
                cw = new ClientWriter(AsyncResponse.getInstance().getResponseWriter());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            Instance.set(cw);
        }
        return cw;
    }
    
    public static ClientWriter getInstance() {
        return getInstance(true);
    }
    
    public static void clearInstance() {
        ClientWriter instance = getInstance(false);
        Instance.remove();
    }
    

    private final ResponseWriter writer;

    private final StringBuffer sb = new StringBuffer(16);

    private ClientWriter(ResponseWriter writer) {
        this.writer = writer;
    }

    protected ResponseWriter getWrapped() {
        return this.writer;
    }

    private StringBuffer getBuffer() {
        this.sb.setLength(0);
        return sb;
    }
    
    public boolean scriptResource(FacesContext faces, String resource) throws IOException {
        if (resource.charAt(0) != '/') {
            throw new IOException("Resource must begin with '/'");
        }
        Set<String> rsc = getResourcesWritten(faces);
        if (rsc.contains(resource)) return false;
        StringBuffer sb = new StringBuffer(resource.length() + 11);
        sb.append("resource://");
        sb.append(resource.substring(1));
        this.writer.startElement("script", null);
        this.writer.writeAttribute("type","text/javascript", null);
        this.writer.writeURIAttribute("src", faces.getApplication().getViewHandler().getResourceURL(faces, sb.toString()), null);
        this.writer.endElement("script");
        rsc.add(resource);
        return true;
    }
    
    protected Set<String> getResourcesWritten(FacesContext faces) {
        Map<String, Object> attr = faces.getExternalContext().getRequestMap();
        Set<String> s = (Set<String>) attr.get("facelets.ResourcesWritten");
        if (s == null) {
            s = new HashSet<String>();
            attr.put("facelets.ResourcesWritten", s);
        }
        return s;
    }
    
    public ClientWriter startScript() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        this.startScript(context.getViewRoot());
        return this;
    }
    
    public ClientWriter startScript(UIComponent c) throws IOException {
        this.writer.startElement("script", c);
        this.writer.writeAttribute("type","text/javascript", null);
        this.writer.writeText("", null); // close start element
        return this;
    }
    
    public ClientWriter endScript() throws IOException {
        this.writer.endElement("script");
        this.writer.flush();
        return this;
    }

    public ClientWriter id(String id, Script... s) throws IOException {
        if (s == null)
            return this;
        for (Script i : s) {
            i.write(this.getBuffer().append("$('").append(id).append("')")
                    .toString(), this);
        }
        return this;
    }

    public ClientWriter select(UIComponent c, Script... s) throws IOException {
        if (c == null || s == null) return this;
        FacesContext faces = FacesContext.getCurrentInstance();
        return this.id(c.getClientId(faces), s);
    }

    public ClientWriter select(String selector, Script... s) throws IOException {
        if (s == null || selector == null)
            return this;
        this.writer.write(this.getBuffer().append("$$('").append(selector)
                .append("').each(function(e){").toString());
        for (Script i : s) {
            i.write("e", this);
        }
        this.writer.write("});");
        return this;
    }

    public ResponseWriter cloneWithWriter(Writer writer) {
        return new ClientWriter(super.cloneWithWriter(writer));
    }
}
