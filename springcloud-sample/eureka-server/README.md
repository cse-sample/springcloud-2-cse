## 创建服务注册中心

Eureka提供云端服务发现，以实现云端中间层服务自动发现和故障转移。
Spring Cloud 集成了 Eureka，并提供了开箱即用的支持。Eureka可细分为 Eureka Server, Eureka Client。

这里我们基于Eureka Server实现一个服务注册中心。

### 从 Spring Initializr 进行项目的初始化

最简单的方式是访问http://start.spring.io/ 进行项目的初始化，Switch to the full version，选择创建Eureka Service工程，工程名称为eureka-service。

![](https://github.com/cse-sample/springcloud-2-cse/blob/master/springcloud-sample/images/Initializr_eureka_server.png)
