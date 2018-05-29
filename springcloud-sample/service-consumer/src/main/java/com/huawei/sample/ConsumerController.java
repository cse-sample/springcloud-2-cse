package com.huawei.sample;

import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {

	private static final Logger LOGGER = Logger.getLogger(ConsumerController.class);

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired 
	private AsyncRestTemplate asyncRestTemplate;

	@Autowired
	private LoadBalancerClient loadBalancerClient;

	@RequestMapping("/hello/{name}")
	public String syncHello(@PathVariable("name") String name) {
		
		ServiceInstance serviceInst = loadBalancerClient.choose("service-provider");

		String url = serviceInst.getUri() + "/hello/" + name;

		LOGGER.info("url: " + url);

		return restTemplate.getForObject(url, String.class);
	}
	
	@RequestMapping("/hello-async/{name}")
	public String asyncHello(@PathVariable("name") String name) throws InterruptedException, ExecutionException {
		ServiceInstance serviceInst = loadBalancerClient.choose("service-provider");

		String url = serviceInst.getUri() + "/hello/" + name;

		LOGGER.info("url: " + url);

		ListenableFuture<ResponseEntity<String>> future = asyncRestTemplate.getForEntity(url, String.class);
		return future.get().getBody();
	}
	
	@RequestMapping("/services")
	public String services() {
		ServiceInstance serviceInst = loadBalancerClient.choose("service-provider");

		String url = serviceInst.getUri() + "/services";

		LOGGER.info("url: " + url);

		return restTemplate.getForObject(url, String.class);
	}
	
	@RequestMapping(value = "/instances")
	public String instances(@RequestParam(value = "serviceId", required = false, defaultValue = "") String serviceId) {
		ServiceInstance serviceInst = loadBalancerClient.choose("service-provider");

		String url = serviceInst.getUri() + "/instances";

		if (serviceId != null && !serviceId.isEmpty()) {
			url = url + "?serviceId=" + serviceId;
		}

		LOGGER.info("url: " + url);

		return restTemplate.getForObject(url, String.class);
	}
	
	
}
