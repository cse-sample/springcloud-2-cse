package com.huawei.sample;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumerController {

	private static final Logger LOGGER = Logger.getLogger(ConsumerController.class);

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/consumer/services")
	public String services() {

		String url = "http://eureka-provider/services";

		LOGGER.info("url: " + url);

		return restTemplate.getForObject(url, String.class);
	}

	@GetMapping(value = "/consumer/instances")
	public String instances(@RequestParam(value = "serviceId", required = false, defaultValue = "") String serviceId) {
		String url = "http://eureka-provider/instances";

		if (serviceId != null && !serviceId.isEmpty()) {
			url = url + "?serviceId=" + serviceId;
		}

		LOGGER.info("url: " + url);

		return restTemplate.getForObject(url, String.class);
	}

}
