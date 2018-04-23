package com.huawei.sample;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZipkinClientProviderController {

    @RequestMapping("/hello/{name}")
    public String hello(@PathVariable("name") String name) {
        return "hello " + name;
    }
}