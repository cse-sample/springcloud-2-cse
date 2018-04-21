## 创建服务配置中心

Eureka提供云端服务发现，以实现云端中间层服务自动发现和故障转移。
Spring Cloud 集成了 Eureka，并提供了开箱即用的支持。Eureka可细分为 Eureka Server, Eureka Client。

这里我们基于Eureka Server实现一个服务注册中心。

### 1.从 Spring Initializr 进行项目的初始化

最简单的方式是访问http://start.spring.io/ 进行项目的初始化，Switch to the full version，选择创建Eureka Server工程，工程名称为eureka-service。

![](https://github.com/cse-sample/springcloud-2-cse/blob/master/springcloud-sample/images/Initializr_eureka_server.png)

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
		<artifactId>spring-cloud-starter-eureka-server</artifactId>
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

### 2.启用Eureka Server

在 EurekaSererApplication.java 上增加<html>@EnableEurekaServer</html>注解

```Java
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
	}
}
```
### 3.修改应用配置
修改 application.propertie或application.yaml，增加如下配置：

```
spring.application.name=eureka-server

server.port=7071
eureka.instance.hostname=localhost

eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
eureka.client.serviceUrl.defaultZone=http://0.0.0.0:${server.port}/eureka/
```
其中：

* server.port: 指明了应用启动的端口号
* eureka.instance.hostname: 应用的主机名称
* eureka.client.registerWithEureka: 值为false自身仅作为服务器，不作为客户端
* eureka.client.fetchRegistry: 值为false无需注册自身
* eureka.client.serviceUrl.defaultZone: 指明了应用的URL

### 4.启动服务注册中心
直接运行EurekaServerApplication的main函数，启动Eureka Server。
访问[http://localhost:7071/](http://localhost:7071/)，可以看到Eureka Server自带的UI 管理界面
