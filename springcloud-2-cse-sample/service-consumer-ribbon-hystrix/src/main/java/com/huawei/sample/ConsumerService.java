package com.huawei.sample;

import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class ConsumerService {
	private static final Logger LOGGER = Logger.getLogger(ConsumerService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AsyncRestTemplate asnycRestTemplate;

	@HystrixCommand(commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000") }, fallbackMethod = "fallback")
	public String syncHello(String name) {
		String url = "http://service-provider/hello/" + name;

		LOGGER.info("url: " + url);

		return restTemplate.getForObject(url, String.class);
	}

	@HystrixCommand(fallbackMethod = "fallback")
	public String asyncHello(String name) throws InterruptedException, ExecutionException {
		String url = "http://service-provider/hello/" + name;

		LOGGER.info("url: " + url);

		ListenableFuture<ResponseEntity<String>> future = asnycRestTemplate.getForEntity(url, String.class);
		return future.get().getBody();
	}

	public String fallback(String name) {
		return "fallback " + name;
	}

}
