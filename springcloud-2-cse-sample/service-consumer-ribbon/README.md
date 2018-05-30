## 服务消费者接入CSE--使用Ribbon实现客户端负载均衡

Ribbon是一个客户端负载均衡的组件，它不像服务注册中心、配置中心、API网关等其他SpringCloud组件那样独立部署，Ribbon必须和微服务集成在一起运行。
 * 和Eureka整合，从Eureka注册中心中获取服务端列表
 * 支持多种协议-HTTP,TCP,UDP
 * caching/batching
 * built in failure resiliency
 
Spring Cloud有两种服务调用方式，一种是Ribbon + RestTemplate，另一种是Feign。这里我们使用Ribbon + Rest with Eureka来消费服务提供者的接口。

这里我们基于Eureka Client创建一个服务消费者服务，演示如何接入CSE。详细文档可参考[Spring Cloud应用接入CSE](https://support.huaweicloud.com/devg-cse/cse_03_0096.html)

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

### 2.自定义RibbonClient
采用CSE服务实例清单的维护机制，需要替代Ribbon默认的负载均衡策略，可以通过配置文件来自定义RibbonClient。
修改 application.properties或application.yaml，增加如下配置：

```
service-provider.ribbon.NIWSServerListClassName=org.apache.servicecomb.springboot.starter.discovery.ServiceCombServerList
```
其中：

* service-provider.ribbon.NIWSServerListClassName: RibbonClient的配置规则，<服务名>.ribbon.<类型>
* org.apache.servicecomb.springboot.starter.discovery.ServiceCombServerList: CSE服务实例清单的维护机制

### 3.修改应用配置
修改 application.properties或application.yaml，从而接入CSE服务中心，增加如下配置：

```yaml
cse.credentials.accessKey=your access key in CSE
cse.credentials.secretKey=your secret key in CSE
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

### 4.启动应用
直接运行ConsumerApplication的main函数

访问[ServiceStage](https://console.huaweicloud.com/servicestage/?region=cn-north-1#/cse/services/tab/services)或[CSE](https://console.huaweicloud.com/cse/?region=cn-north-1#/cse/services/tab/services)，切换到服务目录，查看服务实例是否注册成功

访问[http://localhost:7092/ribbon/hello/springcloud](http://localhost:7092/hello/springcloud)，同步方式调用服务/hello接口

访问[http://localhost:7092/ribbon/hello-async/springcloud](http://localhost:7092/ribbon/hello-async/springcloud)，异步方式调用服务/hello接口

