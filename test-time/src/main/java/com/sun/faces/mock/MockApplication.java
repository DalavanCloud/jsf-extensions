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

/*
 * $Id: MockApplication.java,v 1.1 2005/10/18 17:47:51 edburns Exp $
 */

package com.sun.faces.mock;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.convert.Converter;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.event.ActionEvent;
import javax.faces.validator.Validator;
import javax.servlet.ServletContext;

import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.el.ELException;
import javax.el.ELContextListener;

import com.sun.el.ExpressionFactoryImpl;


public class MockApplication extends Application {


    public MockApplication() {
        addComponent("TestNamingContainer",
                     "javax.faces.webapp.TestNamingContainer");
        addComponent("TestComponent", "javax.faces.webapp.TestComponent");
        addComponent("TestInput", "javax.faces.component.UIInput");
        addComponent("TestOutput", "javax.faces.component.UIOutput");
        addConverter("Integer", "javax.faces.convert.IntegerConverter");
        addConverter("javax.faces.Number", 
		     "javax.faces.convert.NumberConverter");
        addConverter("javax.faces.Long", 
		     "javax.faces.convert.LongConverter");
        addValidator("Length", "javax.faces.validator.LengthValidator");
	servletContext = new MockServletContext();
    }

    private ServletContext servletContext = null;

    private ActionListener actionListener = null;
    private static boolean processActionCalled = false;
    public ActionListener getActionListener() {
	if (null == actionListener) {
	    actionListener = new ActionListener() {
		    public void processAction(ActionEvent e) {
			processActionCalled = true;
		    }
		    // see if the other object is the same as our
		    // anonymous inner class implementation.
		    public boolean equals(Object otherObj) {
			if (!(otherObj instanceof ActionListener)) {
			    return false;
			}
			ActionListener other = (ActionListener) otherObj;

			processActionCalled = false;
			other.processAction(null);
			boolean result = processActionCalled;
			processActionCalled = false;
			return result;
		    }
		};
	}
	
        return (this.actionListener);
    }
    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }


    private NavigationHandler navigationHandler = null;
    public NavigationHandler getNavigationHandler() {
        return (this.navigationHandler);
    }
    public void setNavigationHandler(NavigationHandler navigationHandler) {
        this.navigationHandler = navigationHandler;
    }


    private PropertyResolver propertyResolver = null;
    public PropertyResolver getPropertyResolver() {
        if (propertyResolver == null) {
            propertyResolver = new MockPropertyResolver();
        }
        return (this.propertyResolver);
    }
    public void setPropertyResolver(PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
    }


    public MethodBinding createMethodBinding(String ref, Class params[]) {
        if (ref == null) {
            throw new NullPointerException();
        } else {
            return (new MockMethodBinding(this, ref, params));
        }
    }


    public ValueBinding createValueBinding(String ref) {
        if (ref == null) {
            throw new NullPointerException();
        } else {
            return (new MockValueBinding(this, ref));
        }
    }

    // PENDING(edburns): implement

    public void addELResolver(ELResolver resolver) {
    }

    // PENDING(edburns): implement

    public ELResolver getELResolver() {
	return null;
    }

    private ExpressionFactory expressionFactory = null;

    public ExpressionFactory getExpressionFactory() {
	if (null == expressionFactory) {
	    expressionFactory = new ExpressionFactoryImpl();
	}
	return expressionFactory;
    }
    
    public Object evaluateExpressionGet(FacesContext context,
					String expression, 
					Class expectedType) throws ELException{
	ValueExpression ve = getExpressionFactory().createValueExpression(context.getELContext(),expression, expectedType);
	return ve.getValue(context.getELContext());
    }
    
    

    private VariableResolver variableResolver = null;
    public VariableResolver getVariableResolver() {
        if (variableResolver == null) {
            variableResolver = new MockVariableResolver();
        }
        return (this.variableResolver);
    }
    public void setVariableResolver(VariableResolver variableResolver) {
        this.variableResolver = variableResolver;
    }

    private ViewHandler viewHandler = null;
    public ViewHandler getViewHandler() {
	if (null == viewHandler) {
	    viewHandler = new MockViewHandler();
	}
        return (this.viewHandler);
    }
    public void setViewHandler(ViewHandler viewHandler) {
        this.viewHandler = viewHandler;
    }


    private StateManager stateManager = null;
    public StateManager getStateManager() {
	if (null == stateManager) {
	    stateManager = new MockStateManager();
	}
        return (this.stateManager);
    }
    public void setStateManager(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    private Map components = new HashMap();
    public void addComponent(String componentType, String componentClass) {
        components.put(componentType, componentClass);
    }
    public UIComponent createComponent(String componentType) {
        String componentClass = (String) components.get(componentType);
        try {
            Class clazz = Class.forName(componentClass);
            return ((UIComponent) clazz.newInstance());
        } catch (Exception e) {
            throw new FacesException(e);
        }
    }
    public UIComponent createComponent(ValueBinding componentBinding,
                                       FacesContext context,
                                       String componentType)
        throws FacesException {
	throw new FacesException(new UnsupportedOperationException());
    }
    public UIComponent createComponent(ValueExpression componentExpression,
                                                FacesContext context,
                                                String componentType) 
	throws FacesException {
	throw new FacesException(new UnsupportedOperationException());
    }
    
    public Iterator getComponentTypes() {
        return (components.keySet().iterator());
    }


    private Map converters = new HashMap();
    public void addConverter(String converterId, String converterClass) {
        converters.put(converterId, converterClass);
    }
    public void addConverter(Class targetClass, String converterClass) {
        throw new UnsupportedOperationException();
    }
    public Converter createConverter(String converterId) {
        String converterClass = (String) converters.get(converterId);
        try {
            Class clazz = Class.forName(converterClass);
            return ((Converter) clazz.newInstance());
        } catch (Exception e) {
            throw new FacesException(e);
        }
    }
    public Converter createConverter(Class targetClass) {
        throw new UnsupportedOperationException();
    }
    public Iterator getConverterIds() {
        return (converters.keySet().iterator());
    }
    public Iterator getConverterTypes() {
        throw new UnsupportedOperationException();
    }
    
    private String messageBundle = null;
    public void setMessageBundle(String messageBundle) {
	this.messageBundle = messageBundle;
    }

    public String getMessageBundle() {
	return messageBundle;
    }

    private Map validators = new HashMap();
    public void addValidator(String validatorId, String validatorClass) {
        validators.put(validatorId, validatorClass);
    }
    public Validator createValidator(String validatorId) {
        String validatorClass = (String) validators.get(validatorId);
        try {
            Class clazz = Class.forName(validatorClass);
            return ((Validator) clazz.newInstance());
        } catch (Exception e) {
            throw new FacesException(e);
        }
    }
    public Iterator getValidatorIds() {
        return (validators.keySet().iterator());
    }

    public Iterator getSupportedLocales() {
	return Collections.EMPTY_LIST.iterator();
    }

    public void setSupportedLocales(Collection newLocales) {
    }

    public void addELContextListener(ELContextListener listener) {
	// PENDING(edburns): maybe implement
    }

    public void removeELContextListener(ELContextListener listener) {
	// PENDING(edburns): maybe implement
    }

    public ELContextListener [] getELContextListeners() {
	// PENDING(edburns): maybe implement
	return (ELContextListener []) java.lang.reflect.Array.newInstance(ELContextListener.class,
						   0);
    }

    public Locale getDefaultLocale(){
	return Locale.getDefault();
    }

    public void setDefaultLocale(Locale newLocale) {
    }

    public String getDefaultRenderKitId() { 
	return null;
    }

    public void setDefaultRenderKitId(String renderKitId) {
    }
    
    public ResourceBundle getResourceBundle(FacesContext ctx, String name) {
        return null;
    }

}
