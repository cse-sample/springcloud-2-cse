package com.huawei.sample;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "service-provider", fallback = ConsumerClientFallback.class)
public interface ConsumerFeignClient {

	@RequestMapping(value = "/hello/{name}")
	public String hello(@PathVariable("name") String name);

	@RequestMapping(value = "/services")
	String services();

	@RequestMapping(value = "/instances")
	String instances(@RequestParam(value = "serviceId", required = false, defaultValue = "") String serviceId);
}
