package com.buildbrighton.badge.web;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;

public class BaseController implements ServletContextAware{

	protected String contextPath;

	public void setServletContext(ServletContext servletContext) {
		contextPath = servletContext.getContextPath();
    }

}
