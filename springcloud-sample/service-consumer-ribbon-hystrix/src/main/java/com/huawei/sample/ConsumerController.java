package com.huawei.sample;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

	@Autowired
	private ConsumerService consumerService;

	@RequestMapping("/hello-sync/{name}")
	public String syncHello(@PathVariable("name") String name) {
		return consumerService.syncHello(name);
	}

	@RequestMapping("/hello-async/{name}")
	public String asyncHello(@PathVariable String name) throws InterruptedException, ExecutionException {
		return consumerService.asyncHello(name);
	}
}
