package com.huawei.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@EnableDiscoveryClient
@ImportResource(locations = "classpath*:META-INF/spring/*.bean.xml")
public class ConfigClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigClientApplication.class, args);
	}
}
