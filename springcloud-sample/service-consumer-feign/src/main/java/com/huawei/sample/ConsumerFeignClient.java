package com.huawei.sample;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-provider")
public interface ConsumerFeignClient {

        @GetMapping("/hello/{name}")
	public String hello(@PathVariable String name);
	
	@GetMapping("/services")
	String services();

	@GetMapping("/instances")
	String instances(@RequestParam(value = "serviceId", required = false, defaultValue = "") String serviceId);

	@GetMapping("/hello")
	String hello();

}
