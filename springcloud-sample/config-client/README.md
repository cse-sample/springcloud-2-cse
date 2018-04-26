## 创建服务配置客户端

这里我们基于Config Client实现从服务配置中心读取配置

### 1.创建服务配置Client工程

最简单的方式是访问http://start.spring.io/ 进行项目的初始化，Switch to the full version，选择创建Config Client工程，工程名称为config-client。

![](https://github.com/cse-sample/springcloud-2-cse/blob/master/springcloud-sample/images/Initializr_config_client.png)

工程生成后在本地解压，导入到Eclipse中，可以看到工程pom.xml关键依赖已配置：

```xml
<name>config-client</name>
<description>Spring Cloud Config Client</description>

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
		<artifactId>spring-cloud-starter-config</artifactId>
	</dependency>

	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-actuator</artifactId>
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

### 3.修改启动类

修改ConfigClientApplication.java：

增加@EnableDiscoveryClient注解，从注册中心获取配置中心地址

```Java
@SpringBootApplication
@EnableDiscoveryClient
public class ConfigClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigClientApplication.class, args);
	}
}
```

### 4.编写Controler
添加一个接口读取配置项内容：
```Java
@RestController
@RefreshScope
public class ConfigClientController {
        # 
	@Value("${profile}")
	private String profile;

	@GetMapping("/profile")
	public String hello() {
		return this.profile;
	}
}
```

### 5.修改应用配置
修改 application.properties或application.yaml

```
spring.application.name=config-client
server.port=7062

添加bootstrap.properties，配置对接的配置中心地址和环境

eureka.client.serviceUrl.defaultZone=http://localhost:7071/eureka/

# 环境指定为dev
spring.cloud.config.profile=dev
# 分支指定为git仓库的master
spring.cloud.config.label=master
# 从注册中心获取配置服务地址
spring.cloud.config.discovery.enabled=true
spring.cloud.config.discovery.serviceId=config-server
# 直接指定配置服务地址
# spring.cloud.config.url=http://127.0.0.1:7061

# 开放自动刷新权限
management.security.enabled=false
```

### 6.启动和测试
直接运行ConfigClientApplication的main函数，启动Config Client。

访问[http://localhost:7071/](http://localhost:7071/)，可以看到Eureka Server管理界面上新增加一条CONGIG-CLIENT服务实例记录

访问[http://localhost:7062/profile](http://127.0.0.1:7062/profile)获取指定应用的配置项：

default
