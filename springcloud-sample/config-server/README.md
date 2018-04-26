## 创建服务配置中心

SpringCloud体系中，SpringCloud Config承担微服务注册中心，集中化管理微服务的配置，目前支持本地存储、Git以及Subversion。

Spring Cloud Config提供服务器端和客户端的支持：

Config Server: 集中式的配置服务器，它用于集中管理应用各个环境下配置，默认使用Git存储配置内容。

Config Client: 配置客户端，用于操作存储在Config Server上的配置属性，ConfigClient随微服务启动时会请求Config Server获取所需要的配置属性，然后缓存这些属性以提高性能。

这里我们基于Config Server实现一个服务配置中心。

### 1.创建配置中心仓库

这里以Git仓库作为配置项存放地址，仓库地址：
https://github.com/cse-sample/springcloud-2-cse/springcloud-sample/config-repo

添加配置文件config-client.properties：
```
profile=default
```

### 2.创建配置服务

最简单的方式是访问http://start.spring.io/ 进行项目的初始化，Switch to the full version，选择创建Config Server工程，工程名称为config-service。

![](https://github.com/cse-sample/springcloud-2-cse/blob/master/springcloud-sample/images/Initializr_config_server.png)

工程生成后在本地解压，导入到Eclipse中，可以看到工程pom.xml关键依赖已配置：

```xml
<name>config-server</name>
<description>Spring Cloud Config Server</description>

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
		<artifactId>spring-cloud-config-server</artifactId>
	</dependency>

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

### 3.启用Config Server

修改ConfigServerApplication.java：

增加@EnableConfigServer注解，启用配置服务

增加@EnableDiscoveryClient注解，服务本身注册到Eureka服务注册中心

```Java
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}
}
```
### 3.修改应用配置
修改 application.propertie或application.yaml，设置服务器端口号和配置服务的对接的Git仓库

```
spring.application.name=config-server
server.port=7061

eureka.client.serviceUrl.defaultZone: http://localhost:7071/eureka/

# 配置服务的对接的Git仓库
spring.cloud.config.server.git.uri: https://github.com/cse-sample/springcloud-2-cse
spring.cloud.config.server.git.search-paths: springcloud-sample/config-repo
spring.cloud.config.server.git.username: your username
spring.cloud.config.server.git.password: your paasword
```

### 4.启动服务配置中心
直接运行ConfigServerApplication的main函数，启动Config Server。

访问[http://localhost:7071/](http://localhost:7071/)，可以看到Eureka Server管理界面上新增加一条CONGIG-SERVER服务实例记录

访问[http://localhost:7061/config-client/default](http://127.0.0.1:7061/config-client/default)获取指定应用的配置项：

{"name":"config-client","profiles":["default"],"label":null,"version":"600b66934a9d8c7d7290de16f759a97900d803b0","state":null,"propertySources":[{"name":"https://github.com/cse-sample/springcloud-2-cse/springcloud-sample/config-repo/config-client.properties","source":{"profile":"default"}}]}

