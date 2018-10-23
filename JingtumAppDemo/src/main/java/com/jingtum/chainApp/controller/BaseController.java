package com.jingtum.chainApp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jingtum.chainApp.util.HttpUtil;

public class BaseController {
	/**
	 * 得到request对象
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		return request;
	}

	/**
	 * 从HttpServletRequest(request)中获取对应的参数,如果参数值为null则返回null
	 * 
	 * @param <T>
	 * @param name
	 * @param clazz
	 * @return
	 */
	public <T> T getParameter(String name, Class<T> clazz) {
		return HttpUtil.getValue(getRequest(), name, clazz);
	}
}
