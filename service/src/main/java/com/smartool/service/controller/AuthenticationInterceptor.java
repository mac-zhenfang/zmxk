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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartool.common.dto.User;
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
		// TODO clear thread local
		System.out.println("Pre-handle: " + request.getContextPath() + ", handler: " + handler);
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			if (isSaveMethod(handlerMethod)) {
				return true;
			}
		}
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (Constants.KEY_FOR_USER_TOKEN.equals(cookie.getName())) {
				String value = cookie.getValue();
				User user = cookieToUser(value);
				if (user != null) {
					UserSessionManager.setSessionUser(user);
					return true;
				}
			}
		}
		// Shall do redirect?
		throw new SmartoolException(401, ErrorMessages.PLEASE_LOGIN_FIRST_ERROR_MESSAGE);
	}

	private User cookieToUser(String cookieValue) {
		try {
			UserSession userSession = objectMapper.readValue(cookieValue, UserSession.class);
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

	private static boolean isSaveMethod(HandlerMethod handlerMethod) {
		if (handlerMethod.getBean() instanceof UserController) {
			RequestMapping requestMapping = handlerMethod.getMethod().getAnnotation(RequestMapping.class);
			String[] values = requestMapping.value();
			if (values != null && values.length >= 1) {
				String value = values[0];
				if (safeMethod.contains(value)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		User user = UserSessionManager.getSessionUser();
		if (user != null) {
			String cookieValue = userToCookie(user);
			response.setHeader("Set-Cookie", Constants.KEY_FOR_USER_TOKEN + "=" + cookieValue + ";Max-Age=2592000;");
		}
		System.out.println("Post-handle: " + handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("After-completion: " + handler);
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
