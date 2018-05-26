### 项目简介

基于SpringCloud技术栈的应用快速接入到[ServiceStage微服务云应用平台](https://servicestage.huaweicloud.com)

### 接入说明

![](https://github.com/cse-sample/springcloud-2-cse/blob/master/springcloud-2-cse-sample/images/design.png)

华为微服务服务引擎(Cloud Service Engine)兼容SpringCloud应用，使用SpringCloud技术栈开发的业务业务，在华为云上部署时，不需要部署eureka-server，config-server，zipkin-server等微服务运行所需基础支撑服务，只需要关注业务微服务本身即可，具体改造步骤如下：

#### 1.修改pom文件

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

- 增加CSE服务注册和发现依赖，最新版本从可以从这里[下载](https://console.huaweicloud.com/servicestage/?agencyId=df8004e6ccc14bb3b7935d5d6c6fa1c1&region=cn-north-1&locale=zh-cn#/cse/tools)

```xml
<dependency>
	<groupId>com.huawei.paas.cse</groupId>
	<artifactId>cse-solution-spring-cloud</artifactId>
	<version>2.3.19</version>
</dependency>
```

#### 2.使用华为自定义RibbonClient
采用CSE服务实例清单的维护机制，需要替代Ribbon默认的负载均衡策略。修改 application.properties或application.yaml，增加如下配置：

```
service-provider.ribbon.NIWSServerListClassName=org.apache.servicecomb.springboot.starter.discovery.ServiceCombServerList
```
其中：

* service-provider.ribbon.NIWSServerListClassName: RibbonClient的配置规则，<服务名>.ribbon.<类型>
* org.apache.servicecomb.springboot.starter.discovery.ServiceCombServerList: 从华为云微服务中心获取服务实例列表

说明：纯粹的服务消费者可以跳过第2步

#### 3.修改应用配置
修改 application.properties或application.yaml，配置接入地址和身份认证信息：

```yaml
cse.credentials.accessKey=your access key in CSE
cse.credentials.secretKey=your secret key in CSE
cse.credentials.akskCustomCipher=default

cse.credentials.project=cn-north-1
```
其中：

* cse.credentials.accessKey: 用户华为云账户AK，[获取](https://support.huaweicloud.com/api-iam/zh-cn_topic_0057845589.html)
* cse.credentials.secretKey: 用户华为云账户SK，[获取](https://support.huaweicloud.com/api-iam/zh-cn_topic_0057845589.html)
* cse.credentials.akskCustomCipher: 加密方式，默认不加密
* cse.credentials.project: 可选华北-北京（cn-north-1）、华南-广州（cn-south-1）、华东-上海二（cn-east-2），默认cn-north-1

说明：使用ServiceStage部署应用，可以跳过步骤3。应用在线下运行，连接华为云上服务注册、配置中心需要上面配置。

#### 4.启动应用

| 接入指导                     |        端口号     | 接口                                      |
| -------------------------------  | --------- | ----------------------------------------  |
| [api-gateway-zuul](./api-gateway-zuul)                 | 8080      | http://127.0.0.1:8080/hello/xxx           |
| config-server                          | 7061      | http://127.0.0.1:7061/config-client/dev   |
| config-client                             | 7092      | http://127.0.0.1:7062/profile             |
| eureka-server                             | 7071      | http://127.0.0.1:7071/                    |
| service-provider                               | 7081,7082 | http://127.0.0.1:7081/hello/xxx           |
| service-consume                               | 7091      | http://127.0.0.1:7091/consumer/hello/xxx  |
| service-consume-ribbon                  | 7092      | http://127.0.0.1:7092/ribbon/hello/xxx    |
| service-consume-feign                    | 7093      | http://127.0.0.1:7093/feign/hello/xxx          |
| service-consume-ribbon-hystrix  | 7094      | http://127.0.0.1:7094/ribbon-hystrix/hello/xxx |
| service-consume-feign-hystrix     | 7095      | http://127.0.0.1:7095/feign-hystrix/hello/xxx  |
| zipkin-server                    | 7051      | http://127.0.0.1:7051/           |
| zipkin-service-provider           | 7052      | http://127.0.0.1:7052/hello/xxx  |
| zipkin-service-consumer           | 7053      | http://127.0.0.1:7053/hello/xxx  |
