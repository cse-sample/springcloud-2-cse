## 创建服务配置客户端接入CSE

Eureka提供云端服务发现，以实现云端中间层服务自动发现和故障转移。
Spring Cloud 集成了 Eureka，并提供了开箱即用的支持。Eureka可细分为 Eureka Server, Eureka Client。

这里我们基于Eureka Client创建一个服务配置客户端，演示如何接入CSE。详细文档可参考[Spring Cloud应用接入CSE](https://support.huaweicloud.com/devg-cse/cse_03_0096.html)。

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

- 增加CSE服务注册和发现依赖。可根据需求选择不同CSE版本，当前最新为2.3.19

```xml
<dependency>
	<groupId>com.huawei.paas.cse</groupId>
	<artifactId>cse-solution-spring-cloud</artifactId>
	<version>2.3.19</version>
</dependency>
```

### 2.获取动态配置
CSE不仅支撑@Value和@RefreshScope方式获取动态配置信息；也支持DynamicPropertyFactory获取最新配置信息。如新增一个接口，可以实时获取CSE配置中心的最新profile配置：
```Java
@GetMapping("/profile2")
public String hello2() {
	return DynamicPropertyFactory.getInstance().getStringProperty("profile", null).getValue();
}
```

### 3.修改应用配置
修改 bootstrap.properties，从而接入CSE服务中心，增加如下配置：

```yaml
cse.credentials.accessKey=your access key in CSE
cse.credentials.secretKey=your secret key in CSE
cse.credentials.akskCustomCipher=default
cse.credentials.project=cn-north-1
```
其中：

* cse.credentials.accessKey: 用户华为云账户AK
* cse.credentials.secretKey: 用户华为云账户SK
* cse.credentials.akskCustomCipher: 加密方式，默认不加密
* cse.credentials.project: 可选华北-北京（cn-north-1）、华南-广州（cn-south-1）、华东-上海二（cn-east-2），默认cn-north-1

### 4.启动服务配置中心

直接运行ConfigClientApplication的main函数，启动Config Client。

访问[ServiceStage](https://console.huaweicloud.com/servicestage/?region=cn-north-1#/cse/services/tab/services)或[CSE](https://console.huaweicloud.com/cse/?region=cn-north-1#/cse/services/tab/services)，切换到服务目录，查看服务实例是否注册成功

访问[http://localhost:7062/profile](http://localhost:7061/profile)，

访问[http://localhost:7062/profile2](http://localhost:7061/profile2)
