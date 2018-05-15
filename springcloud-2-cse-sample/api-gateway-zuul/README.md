## 服务网关API Gateway接入CSE

Zuul是Netflix基于JVM的路由和服务端负载均衡器，主要功能包括：反向代理，智能路由，权限校验等。Zuul默认集成了Ribbon来定位一个通过发现转发的实例

Spring Cloud微服务架构，客户端请求一般Ngnix --> Zuul -->微服务。这里我们基于Netflix Zuul创建一个服务网关。

这里我们基于Zuul创建一个服务网关，演示如何接入CSE。详细文档可参考[Spring Cloud应用接入CSE](https://support.huaweicloud.com/devg-cse/cse_03_0096.html)

### 1.修改pom文件

- 删除spring-cloud-starter-eureka-server依赖，并增加spring-boot-starter-web运行依赖

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
```
其中：

* cse.credentials.accessKey: 用户华为云账户AK
* cse.credentials.secretKey: 用户华为云账户SK
* cse.credentials.akskCustomCipher: 加密方式，默认不加密
* cse.credentials.project: 注册Region，默认为华北区cn-north-1

### 4.启动应用
直接运行ZuulApiGatewayApplication的main函数

访问[http://localhost:7071/](http://localhost:7071/)，可以看到Eureka Server自带的UI管理界面上新增一条SERVICE-CONSUMER服务实例记录

访问[http://localhost:8080/api/hello/springcloud](http://localhost:7091/hello-sync/springcloud)，同步方式调用服务/hello接口


