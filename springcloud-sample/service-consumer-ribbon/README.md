## 创建服务消费者--使用Ribbon

Ribbon是一个客户端负载均衡的组件
 * 和Eureka完美整合
 * 支持多种协议-HTTP,TCP,UDP
 * caching/batching
 * built in failure resiliency
 
 这里我们使用Ribbon with Eureka来消费服务提供者的接口

### 1.创建工程添加依赖

依然可访问http://start.spring.io/ 进行项目的初始化，Switch to the full version，选择创建Ribbon类型的工程，工程名称为service-consumer-ribbon。

简单的做法是在service-consumer工程基础上改造，首先在pom.xml追加ribbon依赖

```xml
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-ribbon</artifactId>
	</dependency>
```

### 2.添加@LoadBalanced来开启负载均衡能力

初始化RestTemplate 与 AsyncRestTemplate这两个客户端时添加@LoadBalanced来开启负载均衡能力。

```Java
@SpringBootApplication
@EnableDiscoveryClient
public class ConsumerApplication {

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	@LoadBalanced
	public AsyncRestTemplate asyncRestTemplate() {
		return new AsyncRestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}
}
```

为服务增加一个简单的接口,  使用RestTemplate 与 AsyncRestTemplate这两个客户端调用服务提供者接口：

```Java
@RestController
public class ConsumerController {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired 
	private AsyncRestTemplate asyncRestTemplate;

	@RequestMapping("/hello-sync/{name}")
	public String syncHello(@PathVariable String name) {
		String url = "http://service-provider/hello/" + name;

		return restTemplate.getForObject(url, String.class);
	}
	
	@RequestMapping("/hello-async/{name}")
	public String asyncHello(@PathVariable String name) throws InterruptedException, ExecutionException {
		String url = "http://service-provider/hello/" + name;
		
		ListenableFuture<ResponseEntity<String>> future = asnycRestTemplate.getForEntity(url, String.class);
		return future.get().getBody();
	}
}	
```

### 3.修改应用配置
修改 application.propertie或application.yaml，增加如下配置：

```
spring.application.name=service-consumer-ribbon

server.port=7092

eureka.client.serviceUrl.defaultZone=http://localhost:7071/eureka/
```
其中：

* server.port: 指明了应用启动的端口号
* eureka.client.serviceUrl.defaultZone: 指明了注册服务中心的URL

### 4.启动应用
直接运行ConsumerApplication的main函数

访问[http://localhost:7071/](http://localhost:7071/)，可以看到Eureka Server自带的UI管理界面上新增一条SERVICE-CONSUMER-RIBBON服务实例记录

访问[http://localhost:7092/hello-sync/springcloud](http://localhost:7092/hello-sync/springcloud)，同步方式调用服务/hello接口

访问[http://localhost:7092/hello-async/springcloud](http://localhost:7092/hello-async/springcloud)，异步方式调用服务/hello接口

