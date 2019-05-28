package com.cbt.controller;

import com.cbt.util.SpringUtil;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;
  
  
public class LanguageFilter implements Filter {  
  
    @Override  
    public void init(FilterConfig filterConfig) throws ServletException {  
  
    }  
  
    @Override  
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {  
        request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE, SpringUtil.getBean(org.springframework.web.servlet.i18n.CookieLocaleResolver.class));  
        Locale locale = RequestContextUtils.getLocale((HttpServletRequest) request);  
        request.setAttribute("easeyeI18nLang", locale.getLanguage());  
        chain.doFilter(request, response);  
    }  
  
    @Override  
    public void destroy() {  
    }  
  
}  