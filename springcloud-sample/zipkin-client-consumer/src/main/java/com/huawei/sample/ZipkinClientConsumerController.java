package com.huawei.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ZipkinClientConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @RequestMapping("/hello/{name}")
    public String hello(@PathVariable("name") String name) {
        ServiceInstance serviceInst = loadBalancerClient.choose("zipkin-client-provider");

        String url = serviceInst.getUri() + "/hello/" + name;

        return restTemplate.getForObject(url, String.class);
    }
}