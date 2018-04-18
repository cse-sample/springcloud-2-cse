package com.huawei.sample;

import org.apache.log4j.Logger;
import org.apache.servicecomb.serviceregistry.RegistryUtils;
import org.apache.servicecomb.serviceregistry.api.registry.Microservice;
import org.apache.servicecomb.serviceregistry.api.registry.MicroserviceInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProviderController {

    private static final Logger LOGGER = Logger.getLogger(ProviderController.class);

    //@Autowired
    private Registration registration;

    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping("/hello/{name}")
    public String hello(@PathVariable("name") String name) {
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

        List<MicroserviceInstance> insts;
        Microservice microservice = RegistryUtils.getMicroservice();
        String versionRule = "0.0+";
        if (serviceId == null) {
            serviceId = microservice.getServiceName();
            versionRule = microservice.getVersion();
        }
        insts = RegistryUtils.findServiceInstance(microservice.getAppId(), serviceId, versionRule);


        if (insts == null || insts.isEmpty()) {
            LOGGER.warn(serviceId + " has no service instance.");
        }

        insts.forEach(serviceInst -> {
            LOGGER.info("service instance, host " + serviceInst.getEndpoints());
        });

        return serviceId + "  instance : " + insts.stream().map(inst -> inst.getEndpoints().toString()).collect(Collectors.toList());
    }
}
