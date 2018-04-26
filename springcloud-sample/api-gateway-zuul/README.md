## 创建服务网关API Gateway

Zuul是Netflix基于JVM的路由器和服务器端负载均衡器，主要功能包括：反向代理，智能路由，权限校验等。Zuul默认集成了Ribbon来定位一个通过发现转发的实例

Spring Cloud微服务架构，客户端请求一般Ngnix --> Zuul -->微服务。这里我们基于Netflix Zuul创建一个服务网关，将服务能力以Rest接口方式开发出去。

### 1.从 Spring Initializr 进行项目的初始化

最简单的方式是访问http://start.spring.io/ 进行项目的初始化，Switch to the full version，选择包含“Eureka Discover”，“Zuul”组件，工程名称为api-gateway-zuul。

![](https://github.com/cse-sample/springcloud-2-cse/blob/master/springcloud-sample/images/Initializr_zuul_apigate.png)

工程生成后在本地解压，导入到Eclipse中，可以看到工程pom.xml关键依赖已配置：

```xml
<name>api-gateway-zuul</name>
<description>Spring Cloud Zuul API Gateway</description>

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
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-zuul</artifactId>
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

### 2.启用服务网关功能

修改ZuulApiGatewayApplication.java：

增加@EnableZuulProxy注解，开启服务网关功能

```Java
@SpringBootApplication
@EnableZuulProxy
public class ZuulApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulApiGatewayApplication.class, args);
	}
}
```

### 3.修改应用配置
修改 application.propertie或application.yaml，增加如下配置：

```
spring.application.name=api-gateway-zuul

server.port=8080

eureka.client.serviceUrl.defaultZone=http://localhost:7071/eureka/
```
其中：

* server.port: 指明了应用启动的端口号
* eureka.client.serviceUrl.defaultZone: 指明了注册服务中心的URL

配置路由，开放service-provider服务的/hello接口：
```
zuul.routes.service-provider.path=/hello/**

zuul.routes.service-provider.stripPrefix=false
```

### 4.启动应用
直接运行ZuulApiGatewayApplication的main函数

访问[http://localhost:7071/](http://localhost:7071/)，可以看到Eureka Server自带的UI管理界面上新增一条ZUUL-API-GATEWAY服务实例记录

访问[http://localhost:8080/hello/springcloud](http://localhost:8080/hello/springcloud)，调用API Gateway接口：

hello springcloud
