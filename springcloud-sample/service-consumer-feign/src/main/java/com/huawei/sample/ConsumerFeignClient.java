package com.huawei.sample;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-provider")
public interface ConsumerFeignClient {

    @RequestMapping(value = "/hello/{name}")
	public String hello(@PathVariable String name);
	
	@RequestMapping(value = "/services")
	String services();

	@RequestMapping(value = "/instances")
	String instances(@RequestParam(value = "serviceId", required = false, defaultValue = "") String serviceId);

	@RequestMapping(value = "/hello")
	String hello();

}
