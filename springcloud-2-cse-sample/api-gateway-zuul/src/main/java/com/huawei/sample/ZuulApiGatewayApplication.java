package com.huawei.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@EnableZuulProxy
@ImportResource(locations = "classpath*:META-INF/spring/*.bean.xml")
public class ZuulApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulApiGatewayApplication.class, args);
	}
}