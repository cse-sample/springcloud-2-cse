package com.huawei.sample;

import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/ribbon")
public class ConsumerController {

	private static final Logger LOGGER = Logger.getLogger(ConsumerController.class);

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private AsyncRestTemplate asnycRestTemplate;

	@RequestMapping("/hello/{name}")
	public String syncHello(@PathVariable("name") String name) {
		String url = "http://service-provider/hello/" + name;
		
		LOGGER.info("url: " + url);

		return restTemplate.getForObject(url, String.class);
	}
	
	@RequestMapping("/hello-async/{name}")
	public String asyncHello(@PathVariable String name) throws InterruptedException, ExecutionException {
		String url = "http://service-provider/hello/" + name;
		
		LOGGER.info("url: " + url);

		ListenableFuture<ResponseEntity<String>> future = asnycRestTemplate.getForEntity(url, String.class);
		return future.get().getBody();
	}
}
