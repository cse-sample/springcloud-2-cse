package com.huawei.sample;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("cse-provider")
public interface ConsumerFeignClient {

	@GetMapping("/services")
	String services();

	@GetMapping("/instances")
	String instances(@RequestParam(value = "serviceId", required = false, defaultValue = "") String serviceId);

	@GetMapping("/hello")
	String hello();

}
