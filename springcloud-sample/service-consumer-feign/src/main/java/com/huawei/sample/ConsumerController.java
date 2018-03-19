package com.huawei.sample;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

	private static final Logger LOGGER = Logger.getLogger(ConsumerController.class);

	@Autowired
	private ConsumerFeignClient consumerFeignClient;

        @RequestMapping("/hello/{name}")
	public String hello(@PathVariable String name) {
		return consumerFeignClient.hello(name);
	}
		
	@RequestMapping("/consumer/services")
	public String services() {
		return consumerFeignClient.services();
	}

	@RequestMapping(value = "/consumer/instances")
	public String instances(@RequestParam(value = "serviceId", required = false, defaultValue = "") String serviceId) {

		LOGGER.info("serviceId: " + serviceId);

		return consumerFeignClient.instances(serviceId);
	}

	@RequestMapping(value = "/consumer/hello")
	public String hello() {
		return consumerFeignClient.hello();
	}
	
}
