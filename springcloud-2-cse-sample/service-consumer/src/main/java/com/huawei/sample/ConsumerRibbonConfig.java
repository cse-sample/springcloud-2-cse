package com.huawei.sample;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import org.apache.servicecomb.springboot.starter.discovery.ServiceCombServerList;
import org.springframework.context.annotation.Bean;

public class ConsumerRibbonConfig {
    @Bean
    ServerList<Server> ribbonServerList(IClientConfig config) {
        ServiceCombServerList serverList = new ServiceCombServerList();
        serverList.initWithNiwsConfig(config);
        return serverList;
    }
}
