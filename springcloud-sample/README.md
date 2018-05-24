### 项目简介
一个基于SpringCloud的微服务完整示例

### 环境
JDK1.8

Maven 3.3+

Eclipse 或 IDEA

SpringCloud Edgware.SR2

### 技术栈
| 微服务工程名     | 描述              | 
| ----------------| ---------------- | 
| 配置服务         | Config Server    |
| 注册中心         | Eureka Server    |
| 服务发现         | Eureka Client    |
| 负载均衡         | Ribbon           |
| 弹性与熔断       | Hystrix          |
| API Gateway     | Zuul             |
| 调用连服务       | Zipkin           |

### 服务规划

![](https://github.com/cse-sample/springcloud-2-cse/blob/master/springcloud-sample/images/design.png)

| 微服务工程名                     | 描述                     | 端口号     | 接口                                      |
| ------------------------------- | ------------------------ | --------- | ----------------------------------------  |
| api-gateway-zuul                | 网关-API GateWay         | 8080      | http://127.0.0.1:8080/hello/xxx           |
| config-server                   | 配置服务中心              | 7061      | http://127.0.0.1:7061/hello/xxx           |
| config-client                   | 配置服务客户端            | 7092      | http://127.0.0.1:7062/profile             |
| eureka-server                   | 服务注册中心              | 7071      | http://127.0.0.1:7071/                    |
| service-provider                | 服务提供者                | 7081,7082 | http://127.0.0.1:7081/hello/xxx           |
| service-consume                 | 服务消费者                | 7091      | http://127.0.0.1:7091/consumer/hello/xxx  |
| service-consume-ribbon          | 服务消费者-Ribbon         | 7092      | http://127.0.0.1:7092/ribbon/hello/xxx      |
| service-consume-feign           | 服务消费者-Feign          | 7093      | http://127.0.0.1:7093/feign/hello/xxx          |
| service-consume-ribbon-hystrix  | 服务消费者-Ribbon-Hystrix | 7094      | http://127.0.0.1:7094/ribbon-hystrix/hello/xxx |
| service-consume-feign-hystrix   | 服务消费者-Feign-Hystrix  | 7095      | http://127.0.0.1:7095/feign-hystrix/hello/xxx  |
| zipkin-server                   | 调用链服务                | 7051      | http://127.0.0.1:7051/           |
| service-provider-zipkin          | 服务消费者-Zipkin        | 7052      | http://127.0.0.1:7052/hello/xxx  |
| service-consume-zipkin          | 服务消费者-Zipkin         | 7053      | http://127.0.0.1:7053/hello/xxx  |
 
 
