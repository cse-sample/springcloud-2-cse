## 创建服务配置客户端接入CSE

这里我们基于SpringCloud Config Client创建一个服务配置客户端，演示如何接入CSE配置中心。详细文档可参考[Spring Cloud应用接入CSE](https://support.huaweicloud.com/devg-cse/cse_03_0096.html)。

### 1.修改pom文件

- 删除spring-cloud-starter-eureka依赖，并增加spring-boot-starter-web运行依赖

```xml
<!--<dependency>-->
	<!--<groupId>org.springframework.cloud</groupId>-->
	<!--<artifactId>spring-cloud-starter-eureka</artifactId>-->
<!--</dependency>-->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

- 增加CSE服务注册和发现依赖。可根据需求选择不同CSE版本，当前最新为2.3.20

```xml
<dependency>
	<groupId>com.huawei.paas.cse</groupId>
	<artifactId>cse-solution-spring-cloud</artifactId>
	<version>2.3.20</version>
</dependency>
```

### 2.获取动态配置
访问[ServiceStage](https://console.huaweicloud.com/servicestage/?region=cn-north-1#/cse/services/tab/services)或[CSE](https://console.huaweicloud.com/cse/?region=cn-north-1#/cse/services/tab/services), 将配置手工添加到配置中心（在服务实例注册到配置中心后添加）

![](https://github.com/cse-sample/springcloud-2-cse/blob/master/springcloud-2-cse-sample/images/service_config.png)

CSE不仅支撑@Value和@RefreshScope方式获取动态配置信息；也支持DynamicPropertyFactory获取最新配置信息。如新增一个接口，可以实时获取CSE配置中心的最新profile配置：

```Java
@RestController
@RefreshScope
public class ConfigClientController {
	@Value("${profile}")
	private String profile;

	@GetMapping("/profile")
	public String profile() {
		return this.profile;
	}

	@GetMapping("/profile2")
	public String profile2() {
		return DynamicPropertyFactory.getInstance().getStringProperty("profile", null).getValue();
	}
}
```

### 3.修改应用配置
修改 bootstrap.properties，从而接入CSE服务中心，增加如下配置：

```yaml
cse.credentials.accessKey=your access key
cse.credentials.secretKey=your secret key
cse.credentials.akskCustomCipher=default
cse.credentials.project=cn-north-1
cse.service.registry.address=https://cse.cn-north-1.myhuaweicloud.com
cse.config.client.serverUri=https://cse.cn-north-1.myhuaweicloud.com
```
其中：

* cse.credentials.accessKey: 用户华为云账户AK
* cse.credentials.secretKey: 用户华为云账户SK
* cse.credentials.akskCustomCipher: 加密方式，默认不加密
* cse.credentials.project: 可选华北-北京（cn-north-1）、华南-广州（cn-south-1）、华东-上海二（cn-east-2），默认cn-north-1
* cse.service.registry.address: CSE注册中心地址，默认连接华北-北京一
* cse.config.client.serverUri: CSE配置中心地址，默认连接华北-北京一

**附区域、注册与配置中心地址：**

| 区域(Region)   |   cse.credentials.project   |    cse.service.registry.address / cse.config.client.serverUri |   
| -------------- | --------------------------- | ---------------------------------------  | 
|华北-北京一  | cn-north-1      | https://cse.cn-north-1.myhuaweicloud.com |    
|华南-广州    | c-south-1      | https://cse.cn-south-1.myhuaweicloud.com |
|华东-上海二  | cn-east-2     | https://cse.cn-east-2.myhuaweicloud.com |

### 4.启动服务配置中心

直接运行ConfigClientApplication的main函数，启动Config Client。

访问[ServiceStage](https://console.huaweicloud.com/servicestage/?region=cn-north-1#/cse/services/tab/services)或[CSE](https://console.huaweicloud.com/cse/?region=cn-north-1#/cse/services/tab/services)，切换到服务目录，查看服务实例是否注册成功

访问[http://localhost:7062/profile](http://localhost:7061/profile)，

访问[http://localhost:7062/profile2](http://localhost:7061/profile2)
