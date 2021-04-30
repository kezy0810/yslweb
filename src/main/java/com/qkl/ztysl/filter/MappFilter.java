package com.qkl.ztysl.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.qkl.ztysl.api.common.Constant;


public class MappFilter implements Filter{
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession();
		String uri = request.getRequestURI().replaceAll("/$", "");
		/*if (uri.equals(request.getContextPath() + "/service/user/login")|| uri.equals(request.getContextPath() + "/service/user/logout")) {
			filterChain.doFilter(request, response);
			return;
		}
		if (null == session.getAttribute(Constant.LOGIN_USER)) {
			response.sendRedirect(request.getContextPath() + "/service/user/login");
			return;
		}*/
		filterChain.doFilter(request, response);
		return;
	}

	@Override
	public void destroy() {
		
	}

}
