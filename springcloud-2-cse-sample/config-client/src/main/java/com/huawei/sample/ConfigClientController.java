package com.huawei.sample;

import com.netflix.config.DynamicPropertyFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ConfigClientController {
	@Value("${profile:null}")
	private String profile;

	@GetMapping("/profile")
	public String hello() {
		return this.profile;
	}

	@GetMapping("/profile2")
	public String hello2() {
		return DynamicPropertyFactory.getInstance().getStringProperty("profile", null).getValue();
	}
}