package com.huawei.sample;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController {

	private static final Logger LOGGER = Logger.getLogger(ProviderController.class);

	@Autowired
	private Registration registration;

	@Autowired
	private DiscoveryClient discoveryClient;

	@RequestMapping("/hello/{name}")
	public String hello(@PathVariable String name) {
		return "hello " + name;
	}

	@RequestMapping("/services")
	public String services() {
		List<String> services = discoveryClient.getServices();

		LOGGER.info("services: " + services);

		return "services: " + services;
	}

	@RequestMapping("/instances")
	public String instance(@RequestParam(value = "serviceId", required = false) String serviceId) {
		if (serviceId == null)
			serviceId = registration.getServiceId();
		List<ServiceInstance> insts = discoveryClient.getInstances(serviceId);

		if (insts == null || insts.isEmpty()) {
			LOGGER.warn(serviceId + " has no service instance.");
		}

		insts.forEach(serviceInst -> {
			LOGGER.info("service instance, host " + serviceInst.getUri());
		});

		return serviceId + "  instance : " + insts.stream().map(inst -> inst.getUri()).collect(Collectors.toList());
	}

}
