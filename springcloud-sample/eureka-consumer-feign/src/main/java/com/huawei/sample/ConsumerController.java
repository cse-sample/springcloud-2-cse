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

	@GetMapping("/consumer/services")
	public String services() {
		return consumerFeignClient.services();
	}

	@GetMapping(value = "/consumer/instances")
	public String instances(@RequestParam(value = "serviceId", required = false, defaultValue = "") String serviceId) {

		LOGGER.info("serviceId: " + serviceId);

		return consumerFeignClient.instances(serviceId);
	}

	@GetMapping(value = "/consumer/hello")
	public String hello() {
		return consumerFeignClient.hello();
	}
	
}
