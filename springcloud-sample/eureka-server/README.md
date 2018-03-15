## 创建服务注册中心

Eureka提供云端服务发现，以实现云端中间层服务自动发现和故障转移。
Spring Cloud 集成了 Eureka，并提供了开箱即用的支持。Eureka可细分为 Eureka Server, Eureka Client。

这里我们基于Eureka Server实现一个服务注册中心。

### 从 Spring Initializr 进行项目的初始化

最简单的方式是访问http://start.spring.io/ 进行项目的初始化，Switch to the full version，选择创建Eureka Server工程，工程名称为eureka-service。创建完成后导入eclipse

![](https://github.com/cse-sample/springcloud-2-cse/blob/master/springcloud-sample/images/Initializr_eureka_server.png)

### 启用 Eureka Server

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
### 修改应用配置
修改 application.properties，增加如下配置：

```
spring.application.name=eureka-server

server.port=7071
eureka.instance.hostname=localhost

eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
eureka.client.serviceUrl.defaultZone=http://0.0.0.0:${server.port}/eureka/
```
其中：

server.port: 指明了应用启动的端口号
eureka.instance.hostname: 应用的主机名称
eureka.client.registerWithEureka: 值为false自身仅作为服务器，不作为客户端
eureka.client.fetchRegistry: 值为false无需注册自身
eureka.client.serviceUrl.defaultZone: 指明了应用的URL

### 启动Eureka Service
