## 创建服务消费者--使用Feign实现客户端负载均衡

Feign是一个声明式的Java Http客户端，它使得编写Java HTTP客户端变得更加简单。
我们只需要使用Feign来创建一个接口并用注解来配置它既可完成，它具备可插拔的注解支持，包括Feign注解和JAX-RS注解。

Spring Cloud为Feign增加了对Spring MVC注解的支持，整合了Ribbon和Eureka，默认实现了负载均衡的效果。

这里我们使用Feign来消费服务提供者的接口。

### 1.创建工程添加依赖

依然可访问http://start.spring.io/ 进行项目的初始化，Switch to the full version，选择包含“Eureka Discovery”，“Feign”组件，工程名称为service-consumer-feign。

另一种简单的做法是在service-consumer工程基础上改造：copy后修改工程名称，然后在pom.xml追加feign依赖

```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-feign</artifactId>
</dependency>
```

### 2.添加@EnableFeignClients注解开启Feign的功能

在程序的启动类ServiceFeignApplication ，加上@EnableFeignClients注解开启Feign的功能添加@LoadBalanced来开启负载均衡能力。

```Java
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaConsumerApplication.class, args);
	}
}
```

定义一个Feign接口，通过@FeignClient("服务名")，来指定调用哪个服务。
比如在代码中调用了service-provider服务的“/hello”接口，代码如下：

```Java
@FeignClient("service-provider")
public interface ConsumerFeignClient {

        @RequestMapping("/hello/{name}")
	public String hello(@PathVariable String name);
}
```
* 使用@FeignClient("service-service")注解来绑定该接口对应名称为service-provider的服务
* 通过Spring MVC的注解来配置service-provider服务下的具体实现

为服务增加一个简单的接口, 通过上面定义的Feign客户端ConsumerFeignClient来消费服务提供者接口, 代码如下：
```Java
@RestController
public class ConsumerController {

	@Autowired
	private ConsumerFeignClient consumerFeignClient;

        @RequestMapping("/hello/{name}")
	public String hello(@PathVariable String name) {
		return consumerFeignClient.hello(name);
	}
}
```

### 3.修改应用配置
修改 application.propertie或application.yaml，增加如下配置：

```
spring.application.name=service-consumer-feign

server.port=7093

eureka.client.serviceUrl.defaultZone=http://localhost:7071/eureka/
```
其中：

* server.port: 指明了应用启动的端口号
* eureka.client.serviceUrl.defaultZone: 指明了注册服务中心的URL

### 4.启动应用
直接运行ConsumerApplication的main函数

访问[http://localhost:7071/](http://localhost:7071/)，可以看到Eureka Server自带的UI管理界面上新增一条SERVICE-CONSUMER-FEIGN服务实例记录

访问[http://localhost:7093/hello/springcloud](http://localhost:7093/hello/springcloud)，调用服务/hello接口
