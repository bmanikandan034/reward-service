package com.rewards.service.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/***
 * @author Manikandan B
 */
@Slf4j
@Component
public class RequestHandleInterceptor implements HandlerInterceptor {

	private static final String START_TIME = "startTime";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		long start = System.currentTimeMillis();
		request.setAttribute(START_TIME, start);

		String method = request.getMethod();
		String uri = request.getRequestURI();
		String query = request.getQueryString();

		log.info("Incoming → {} {}{}", method, uri, query != null ? "?" + query : "");

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {

		long start = (long) request.getAttribute(START_TIME);
		long duration = System.currentTimeMillis() - start;

		log.info("Completed → {} {} → status={} → time={}ms", request.getMethod(), request.getRequestURI(),
				response.getStatus(), duration);

		if (ex != null) {
			log.error("Request failed with exception", ex);
		}
	}
}
