package com.smartool.service.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartool.common.dto.User;
import com.smartool.service.CommonUtils;
import com.smartool.service.Constants;
import com.smartool.service.Encrypter;
import com.smartool.service.ErrorMessages;
import com.smartool.service.SmartoolException;
import com.smartool.service.UserSession;
import com.smartool.service.UserSessionManager;
import com.smartool.service.dao.UserDao;

public class AuthenticationInterceptor implements HandlerInterceptor {
	private static Logger logger = Logger.getLogger(AuthenticationInterceptor.class);
	private static Set<String> safeMethod = new HashSet<String>();

	static {
		safeMethod.add(Constants.GET_SECURITY_CODE_PATH);
		safeMethod.add(Constants.USER_LOGIN_PATH);
		safeMethod.add(Constants.USER_REGISTER_PATH);
	}

	private static final String DEFAULT_ERROR_MESSAGE = "Unknown";
	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private UserDao userDao;

	@Autowired
	private Encrypter encrypter;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		UserSessionManager.setSessionUser(null);
		logger.debug("AuthenticationInterceptor Pre-handle: " + request.getContextPath() + ", handler: " + handler);
		if (CommonUtils.isSaveMethod(handler)) {
			return true;
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (Constants.KEY_FOR_USER_TOKEN.equals(cookie.getName())) {
					String value = cookie.getValue();
					User user = cookieToUser(value);
					if (user != null) {
						UserSessionManager.setSessionUser(user);
						// Refresh token
						addCookieIntoResponse(response, user);
						return true;
					}
				}
			}
		}
		// Shall do redirect?
		throw new SmartoolException(HttpStatus.UNAUTHORIZED.value(), ErrorMessages.PLEASE_LOGIN_FIRST_ERROR_MESSAGE);
	}

	public void addCookieIntoResponse(HttpServletResponse response, User user) {
		String cookieValue = userToCookie(user);
		Cookie newCookie = new Cookie(Constants.KEY_FOR_USER_TOKEN, cookieValue);
		newCookie.setMaxAge(Constants.COOKIE_AGE);
		newCookie.setPath(Constants.COOKIE_PATH);
		response.addCookie(newCookie);
	}

	private User cookieToUser(String cookieValue) {
		try {
			UserSession userSession = objectMapper.readValue(encrypter.decrypt(cookieValue), UserSession.class);
			if (userSession.getUserId() != null) {
				User user = userDao.getUserById(userSession.getUserId());
				return user;
			}
		} catch (Exception e) {
			logger.warn("Fail to parse cookie: ", e);
		}
		return null;
	}

	private String userToCookie(User user) {
		UserSession userSession = new UserSession(user.getId(), System.currentTimeMillis());
		try {
			return encrypter.encrypt(objectMapper.writeValueAsBytes(userSession));
		} catch (JsonProcessingException e) {
			logger.warn("Fail to generate cookie: ", e);
			return "";
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		UserSessionManager.clearSessionUser();
		logger.debug("AuthenticationInterceptor Post-handle: " + handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		logger.debug("AuthenticationInterceptor After-completion: " + handler);
		if (ex != null) {
			Map<String, String> map = new HashMap<String, String>();
			if (ex instanceof SmartoolException) {
				SmartoolException smartoolException = (SmartoolException) ex;
				response.setStatus(smartoolException.getErrorCode());
				map.put("message", ex.getMessage());
			} else {
				response.setStatus(500);
				map.put("message", DEFAULT_ERROR_MESSAGE);
			}
			response.getOutputStream().write(objectMapper.writeValueAsBytes(map));
		}
	}

}
