## 创建服务提供者

Eureka提供云端服务发现，以实现云端中间层服务自动发现和故障转移。
Spring Cloud 集成了 Eureka，并提供了开箱即用的支持。Eureka可细分为 Eureka Server, Eureka Client。

这里我们基于Eureka Client创建一个服务提供者服务。

### 1.从 Spring Initializr 进行项目的初始化

最简单的方式是访问http://start.spring.io/ 进行项目的初始化，Switch to the full version，选择创建Eureka Discovery工程，工程名称为service-provider。

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

### 2.启用服务注册和发现

在 ProviderApplication.java 上增加<html>@EnableDiscoveryClient</html>注解表明应用开启服务注册与发现功能。


```Java
@SpringBootApplication
@EnableDiscoveryClient
public class ProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
	}
}
```

为服务增加一个简单的接口：

```Java
@RestController
public class ProviderController {

	@RequestMapping("/hello/{name}")
	public String hello(@PathVariable String name) {
		return "hello " + name;
	}
}
```

### 3.修改应用配置
修改 application.propertie或application.yaml，增加如下配置：

```
spring.application.name=service-provider

server.port=7081

eureka.client.serviceUrl.defaultZone=http://localhost:7071/eureka/
```
其中：

* server.port: 指明了应用启动的端口号
* eureka.client.serviceUrl.defaultZone: 指明了注册服务中心的URL

### 4.启动服务
直接运行ProviderApplication的main函数

访问[http://localhost:7071/](http://localhost:7071/)，可以看到Eureka Server管理界面上新增加一条SERVICE-PROVIDER服务实例记录

访问[http://localhost:7081/hello/springcloud](http://localhost:7081/hello/springcloud)，调用service-provider服务/hello接口
