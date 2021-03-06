package com.smartool.service.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.smartool.common.dto.User;
import com.smartool.service.ErrorMessages;
import com.smartool.service.SmartoolException;
import com.smartool.service.UserRole;
import com.smartool.service.UserSessionManager;
import com.smartool.service.controller.annotation.ApiScope;

public class AuthorizationInterceptor implements HandlerInterceptor {
	private static Logger logger = Logger.getLogger(AuthorizationInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.debug("AuthorizationInterceptor Pre-handle: " + handler);
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			ApiScope apiScope = handlerMethod.getMethod().getAnnotation(ApiScope.class);
			if (apiScope == null) {
				// Allow all operations for FreeScope API
				return true;
			}
			User sessionUser = UserSessionManager.getSessionUser();
			if (sessionUser == null) {
				throw new SmartoolException(HttpStatus.UNAUTHORIZED.value(),
						ErrorMessages.PLEASE_LOGIN_FIRST_ERROR_MESSAGE);
			}
			UserRole requestedScope = apiScope.userScope();
			// String requestedUserId = getRequestedUserId(handlerMethod);
			String userRoleId = sessionUser.getRoleId();
			if (requestedScope.getValue().compareTo(userRoleId) > 0) {
				throw new SmartoolException(HttpStatus.FORBIDDEN.value(), ErrorMessages.FORBIDEN_ERROR_MESSAGE);
			} else {
				return true;
			}
		}
		// What to return here?
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.debug("AuthorizationInterceptor Post-handle: " + handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		logger.debug("AuthorizationInterceptor After-completion: " + handler);
	}

}
