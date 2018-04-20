package com.huawei.sample;

import org.springframework.stereotype.Component;

@Component
public class ConsumerClientFallback implements ConsumerFeignClient {

	@Override
	public String hello(String name) {
		return "hello fallback";
	}

	@Override
	public String services() {
		return "services fallback";
	}

	@Override
	public String instances(String serviceId) {
		return "instances fallback";
	}

}
