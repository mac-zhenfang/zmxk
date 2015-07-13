package com.smartool.service.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.smartool.common.dto.User;
import com.smartool.service.CommonUtils;
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
			String requestedUserId = getRequestedUserId(handlerMethod);
			boolean selfOnly = apiScope.selfOnly();
			String userRoleId = sessionUser.getRoleId();
			String userId = sessionUser.getId();
			if (requestedScope.getValue().compareTo(userRoleId) > 0) {
				throw new SmartoolException(HttpStatus.FORBIDDEN.value(), ErrorMessages.FORBIDEN_ERROR_MESSAGE);
			} else if (requestedScope.getValue().compareTo(userRoleId) < 0) {
				return true;
			} else {
				if (UserRole.NORMAL_USER.equals(requestedScope) && selfOnly) {
					if (request.getPathInfo().contains(userId)) {
						return true;
					} else {
						throw new SmartoolException(HttpStatus.FORBIDDEN.value(), ErrorMessages.FORBIDEN_ERROR_MESSAGE);
					}
					// return requestedUserId == null || userId != null &&
					// userId.equals(requestedUserId);
				}
				return true;
			}
		}
		// What to return here?
		return false;
	}

	private String getRequestedUserId(HandlerMethod handlerMethod) {
		// TODO
		MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
		if (methodParameters == null) {
			return null;
		}
		for (MethodParameter methodParameter : methodParameters) {
			methodParameter.getParameterAnnotation(PathVariable.class);
		}
		return null;
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
