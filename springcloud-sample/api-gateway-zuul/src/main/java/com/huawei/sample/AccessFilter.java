package com.huawei.sample;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@RestController
public class AccessFilter extends ZuulFilter {

	private static final Logger LOGGER = Logger.getLogger(AccessFilter.class);

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		return false;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		LOGGER.info(String.format("send {} request to {}", request.getMethod(), request.getRequestURL().toString()));

		Object accessToken = request.getParameter("accessToken");
		if (accessToken == null) {
			LOGGER.warn("access token is empty");
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			return null;
		}
		LOGGER.info("access token ok");
		return null;
	}

}
