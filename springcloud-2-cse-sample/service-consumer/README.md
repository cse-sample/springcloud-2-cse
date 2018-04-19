## 服务消费者接入CSE

这里我们基于Eureka Client创建一个服务消费者服务，演示如何接入CSE
详细文档可参考[Spring Cloud应用接入CSE](https://support.huaweicloud.com/devg-cse/cse_03_0096.html)

### 1.修改pom文件




- 删除spring-cloud-starter-eureka-server依赖，并增加spring-boot-starter-web运行依赖

```xml
<!--<dependency>-->
	<!--<groupId>org.springframework.cloud</groupId>-->
	<!--<artifactId>spring-cloud-starter-eureka</artifactId>-->
<!--</dependency>-->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
```
- dependencyManagement中增加cse-dependency，方便管理三方件。可根据需求选择不同CSE版本，当前最新为2.3.12。

```xml
<dependencyManagement>
	<dependencies>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-dependencies</artifactId>
			<version>${spring-cloud.version}</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>
		<dependency>
			<groupId>com.huawei.paas.cse</groupId>
			<artifactId>cse-dependency</artifactId>
			<version>2.3.12</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>
	</dependencies>
</dependencyManagement>
```
- 增加CSE服务注册和发现依赖

```xml
<dependency>
	<groupId>org.apache.servicecomb</groupId>
	<artifactId>spring-boot-starter-registry</artifactId>
</dependency>
<dependency>
	<groupId>org.apache.servicecomb</groupId>
	<artifactId>spring-boot-starter-configuration</artifactId>
</dependency>
<dependency>
	<groupId>org.apache.servicecomb</groupId>
	<artifactId>spring-boot-starter-discovery</artifactId>
</dependency>
<dependency>
	<groupId>com.huawei.paas.cse</groupId>
	<artifactId>foundation-auth</artifactId>
	<exclusions>
		<exclusion>
			<groupId>org.slf4j</groupId>
			<artifactId>slf4j-log4j12</artifactId>
		</exclusion>
	</exclusions>
</dependency>
```

### 2.重载Spring Cloud服务列表实现
实现 `com.netflix.loadbalancer.ServerList` 接口，从CSE服务中心获取微服务列表。新增 `ConsumerRibbonConfig.java`，内容如下：
```Java
public class ConsumerRibbonConfig {
    @Bean
    ServerList<Server> ribbonServerList(IClientConfig config) {
        ServiceCombServerList serverList = new ServiceCombServerList();
        serverList.initWithNiwsConfig(config);
        return serverList;
    }
}
```

### 3.启用服务注册和发现

在原 ProviderApplication.java 中增加<html>@ImportResource</html>，自动注入CSE依赖Bean。增加<html>@RibbonClient</html>，显式获取服务提供者的实例信息。


```Java
package com.huawei.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@ImportResource(locations = "classpath*:META-INF/spring/*.bean.xml")
@RibbonClient(name = "service-provider", configuration = ConsumerRibbonConfig.class)
public class ConsumerApplication {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public AsyncRestTemplate asyncRestTemplate() {
        return new AsyncRestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
```
其中：

* @RibbonClient(name = "<font color=red>service-provider</font>", configuration = ConsumerRibbonConfig.class): service-provider为Provider微服务名称

### 4.修改应用配置
src/main/resources下增加微服务描述文件microservice.yaml，如下配置：

```yaml
cse-config-order: 100
APPLICATION_ID: springcloud-2-cse-sample
service_description:
  name: service-consumer
  version: 0.0.1
cse:
  service:
    registry:
      address: https://cse.cn-north-1.myhwclouds.com:443
      instance:
        watch: false
  config:
    client:
      serverUri: https://cse.cn-north-1.myhwclouds.com:443
      refreshMode: 1
      refresh_interval: 15000
  rest:
    address: 0.0.0.0:7091 # 7091端口与src/main/resources/application.yml中server.port保持一致
  credentials:
    accessKey: your access key in CSE
    secretKey: your secret key in CSE
    akskCustomCipher: default
```
其中：

* cse.credentials.accessKey: 用户华为云账户AK
* cse.credentials.secretKey: 用户华为云账户SK
* cse.rest.address: 注册到服务中心的EndPoint，需要与src/main/resources/application.yml中server.port保持一致

### 5.启动服务
直接运行ConsumerApplication的main函数

访问[http://localhost:7091/consumer/hello-sync/springcloud](http://localhost:7091/consumer/hello-sync/springcloud)，同步方式调用服务/hello接口

访问[http://localhost:7091/consumer/hello-async/springcloud](http://localhost:7091/consumer/hello-async/springcloud)，异步方式调用服务/hello接口
