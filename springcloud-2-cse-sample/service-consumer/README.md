## 创建服务消费者

这里我们基于Eureka Client创建一个服务消费者服务。

### 1.从 Spring Initializr 进行项目的初始化

最简单的方式是访问http://start.spring.io/ 进行项目的初始化，Switch to the full version，选择包含“Eureka Discover”组件，工程名称为service-consumer。

![](https://github.com/cse-sample/springcloud-2-cse/blob/master/springcloud-sample/images/Initializr_eureka_discovery.png)

工程生成后在本地解压，导入到Eclipse中，可以看到工程pom.xml关键依赖已配置：

```xml
<parent>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-parent</artifactId>
	<version>1.5.10.RELEASE</version>
	<relativePath/> <!-- lookup parent from repository -->
</parent>

<properties>
	<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
	<java.version>1.8</java.version>
	<spring-cloud.version>Edgware.SR2</spring-cloud.version>
</properties>

<dependencies>
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-eureka</artifactId>
	</dependency>

	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-test</artifactId>
		<scope>test</scope>
	</dependency>
</dependencies>

<dependencyManagement>
	<dependencies>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-dependencies</artifactId>
			<version>${spring-cloud.version}</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>
	</dependencies>
</dependencyManagement>
```

### 2.启用服务注册发现

在 EurekaProviderApplication.java 上

增加@EnableDiscoveryClient注解表明应用开启服务注册与发现功能，

初始化RestTemplate 与 AsyncRestTemplate这两个客户端进行服务调用。

```Java
@SpringBootApplication
@EnableDiscoveryClient
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
		SpringApplication.run(EurekaConsumerApplication.class, args);
	}
}
```

为服务增加一个简单的接口,  使用RestTemplate 与 AsyncRestTemplate这两个客户端调用服务提供者接口：

```Java
@RestController
@RequestMapping("/consumer")
public class ConsumerController {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired 
	private AsyncRestTemplate asyncRestTemplate;

	@Autowired
	private LoadBalancerClient loadBalancerClient;

	@RequestMapping("/hello-sync/{name}")
	public String syncHello(@PathVariable String name) {
		ServiceInstance serviceInst = loadBalancerClient.choose("eureka-provider");

		String url = serviceInst.getUri() + "/hello/" + name;

		return restTemplate.getForObject(url, String.class);
	}
	
	@RequestMapping("/hello-async/{name}")
	public String asyncHello(@PathVariable String name) throws InterruptedException, ExecutionException {
		ServiceInstance serviceInst = loadBalancerClient.choose("eureka-provider");

		String url = serviceInst.getUri() + "/hello/" + name;

		ListenableFuture<ResponseEntity<String>> future = asyncRestTemplate.getForEntity(url, String.class);
		return future.get().getBody();
	}
}	
```

### 3.修改应用配置
修改 application.propertie或application.yaml，增加如下配置：

```
spring.application.name=service-consumer

server.port=7091

eureka.client.serviceUrl.defaultZone=http://localhost:7071/eureka/
```
其中：

* server.port: 指明了应用启动的端口号
* eureka.client.serviceUrl.defaultZone: 指明了注册服务中心的URL

### 4.启动应用
直接运行ConsumerApplication的main函数

访问[http://localhost:7071/](http://localhost:7071/)，可以看到Eureka Server自带的UI管理界面上新增一条SERVICE-CONSUMER服务实例记录

访问[http://localhost:7091/consumer/hello-sync/springcloud](http://localhost:7091/consumer/hello-sync/springcloud)，同步方式调用服务/hello接口

访问[http://localhost:7091/consumer/hello-async/springcloud](http://localhost:7091/consumer/hello-async/springcloud)，异步方式调用服务/hello接口

