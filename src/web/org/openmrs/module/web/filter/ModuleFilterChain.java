/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.web.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * This class is an implementation of FilterChain for use in using Filters defined within Modules.
 * It enables the Module system to iterate through all of the defined Filters before continuing down
 * the initial filter chain.
 */
public class ModuleFilterChain implements FilterChain {
	
	// Properties
	private Iterator<Filter> filterIterator;
	
	private FilterChain initialFilterChain;
	
	/**
	 * Private constructor which sets all required properties
	 * 
	 * @param filters: The Collection of {@link Filter}s that this FilterChain will iterate over
	 *            before returning control back the the <code>initialFilterChain</code>
	 * @param initialFilterChain: The {@link FilterChain} to return control to once all of the
	 *            {@link Filter}s have been executed
	 */
	private ModuleFilterChain(Collection<Filter> filters, FilterChain initialFilterChain) {
		this.filterIterator = filters.iterator();
		this.initialFilterChain = initialFilterChain;
	}
	
	/**
	 * Factory method to construct and return a ModuleFilterChain
	 * 
	 * @param filters: The Collection of {@link Filter}s that this FilterChain will iterate over
	 *            before returning control back the the <code>initialFilterChain</code>
	 * @param initialFilterChain: The {@link FilterChain} to return control to once all of the
	 *            {@link Filter}s have been executed
	 * @return The ModuleFilterChain that is fully initialized with the passed parameters
	 */
	public static ModuleFilterChain getInstance(Collection<Filter> filters, FilterChain initialFilterChain) {
		return new ModuleFilterChain(filters, initialFilterChain);
	}
	
	/**
	 * This Iterates across all of the Filters defined by modules before handing control back over
	 * to the initial filter chain to continue on.
	 * 
	 * @see javax.servlet.FilterChain#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse)
	 */
	public void doFilter(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		if (filterIterator.hasNext()) {
			Filter f = filterIterator.next();
			f.doFilter(request, response, this);
		} else {
			initialFilterChain.doFilter(request, response);
		}
	}
}
