## 服务提供者接入CSE

这里我们基于Eureka Client创建一个服务提供者服务，演示如何接入CSE详细文档可参考[Spring Cloud应用接入CSE](https://support.huaweicloud.com/devg-cse/cse_03_0096.html)

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

### 2.启用服务注册和发现

**没有使用Registration对象的跳过这一步**

在原 ProviderController.java 中注释Registration自动装配，目前CSE暂不支持Registration。

```Java
//@Autowired
private Registration registration;
```

可以通过 `RegistryUtils.getMicroservice()` 获取微服务元信息，参考如下：
```Java
@RequestMapping("/instances")
public String instance(@RequestParam(value = "serviceId", required = false) String serviceId) {

    List<MicroserviceInstance> insts;
    Microservice microservice = RegistryUtils.getMicroservice();
    String versionRule = "0.0+";
    if (serviceId == null) {
        serviceId = microservice.getServiceName();
        versionRule = microservice.getVersion();
    }
    insts = RegistryUtils.findServiceInstance(microservice.getAppId(), serviceId, versionRule);


    if (insts == null || insts.isEmpty()) {
        LOGGER.warn(serviceId + " has no service instance.");
    }

    insts.forEach(serviceInst -> {
        LOGGER.info("service instance, host " + serviceInst.getEndpoints());
    });

    return serviceId + "  instance : " + insts.stream().map(inst -> inst.getEndpoints().toString()).collect(Collectors.toList());
}
```

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
* cse.credentials.project: 可选华北-北京（cn-north-1）、华南-广州（cn-south-1）、华东-上海二（cn-east-2），默认cn-north-1

### 4.启动服务
直接运行ProviderApplication的main函数

访问[http://localhost:7081/hello/springcloud](http://localhost:7081/hello/springcloud)，调用service-provider服务/hello接口
