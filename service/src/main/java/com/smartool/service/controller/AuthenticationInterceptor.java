package com.smartool.service.controller;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.smartool.service.Constants;

public class AuthenticationInterceptor implements HandlerInterceptor {
	private static Set<String> safeMethod = new HashSet<String>();

	static {
		safeMethod.add(Constants.GET_SECURITY_CODE_PATH);
		safeMethod.add(Constants.USER_LOGIN_PATH);
		safeMethod.add(Constants.USER_REGISTER_PATH);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Pre-handle: " + request.getContextPath() + ", handler: " + handler);
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			if (isSaveMethod(handlerMethod)) {
				return true;
			}
		}
		return true;
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
		// TODO Auto-generated method stub
		// response.setHeader("Set-Cookie", Constants.KEY_FOR_USER_ID + "=" +
		// createdUser.getId() + ";Max-Age=2592000;");
		System.out.println("Post-handle: " + handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("After-completion: " + handler);
	}

}
