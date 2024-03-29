package com.xuanyue.xojbackendjudgeservice;

import com.xuanyue.xojbackendjudgeservice.rabbitmq.InitRabbitMq;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.xuanyue")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.xuanyue.xojbackendserviceclient.service"})
public class XojBackendJudgeServiceApplication {

    public static void main(String[] args) {
        // 初始化消息队列
        InitRabbitMq.doInitCodeMq();
        SpringApplication.run(XojBackendJudgeServiceApplication.class, args);
    }

}
