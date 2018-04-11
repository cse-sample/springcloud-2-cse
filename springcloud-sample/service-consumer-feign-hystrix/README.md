## 创建服务消费者--Feign和Hystrix的结合

Hystrix是一个帮助解决分布式系统交互时超时处理和容错的类库。

基于微服务构造的分布式系统，被分层解耦为诸多存在调用依赖关系的微服务，一个请求需要调用多个微服务是非常常见。较底层的服务如果出现故障，会连锁传导到依赖他的上层服务，并逐渐扩散到影响整个系统，发生“雪崩”效应。为了解决这个问题，业界提出了断路器模型，当对特定的服务的调用的不可用达（错误或超时）到一个阀值（比如5秒20次）断路器将会被打开，避免连锁故障，fallback方法降级直接返回一个固定值。

Hystrix就是用来解决这类问题的。

### 1.开启Hystrix

在service-consumer-feign工程基础上改造，Feign是自带断路器的，需要在配置文件中配置打开它，在配置文件加以下代码：

```
    feign.hystrix.enabled=true
```

### 2.添加@EnableHystrix注解开启Hystrix

```Java
@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
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
### 3.服务方法上添加@HystrixCommand注解

在syncHello, asyncHello方法上加上@HystrixCommand注解，对添加了注解的方法插入熔断器的功能，并指定了fallbackMethod熔断方法。

```Java
@Service
public class ConsumerService {
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AsyncRestTemplate asnycRestTemplate;

	@HystrixCommand(fallbackMethod = "fallback")
	public String syncHello(String name) {
		String url = "http://service-provider/hello/" + name;

		return restTemplate.getForObject(url, String.class);
	}

	@HystrixCommand(fallbackMethod = "fallback")
	public String asyncHello(String name) throws InterruptedException, ExecutionException {
		String url = "http://service-provider/hello/" + name;

		ListenableFuture<ResponseEntity<String>> future = asnycRestTemplate.getForEntity(url, String.class);
		return future.get().getBody();
	}

	public String fallback(String name) {
		return "fallback " + name;
	}

}	
```

### 4.修改应用配置
修改 application.propertie或application.yaml，增加如下配置：

```
spring.application.name=service-consumer-feign-hystrix

server.port=7095

eureka.client.serviceUrl.defaultZone=http://localhost:7071/eureka/
```
其中：

* server.port: 指明了应用启动的端口号
* eureka.client.serviceUrl.defaultZone: 指明了注册服务中心的URL

### 5.启动应用
直接运行ConsumerApplication的main函数

访问[http://localhost:7071/](http://localhost:7071/)，可以看到Eureka Server自带的UI管理界面上新增一条SERVICE-CONSUMER-FEIGN-HYSTRIX服务实例记录

访问[http://localhost:7095/feign-hystrix/hello-sync/springcloud](http://localhost:7095/ribbon-hystrix/hello-sync/springcloud)，返回：

hello springcloud

停掉service-provider服务，再次调用上面的接口，返回：

fallback springcloud
