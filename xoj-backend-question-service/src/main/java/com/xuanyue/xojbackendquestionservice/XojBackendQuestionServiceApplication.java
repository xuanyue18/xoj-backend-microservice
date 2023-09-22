package com.xuanyue.xojbackendquestionservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@MapperScan("com.xuanyue.xojbackendquestionservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.xuanyue")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.xuanyue.xojbackendserviceclient.service"})
public class XojBackendQuestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(XojBackendQuestionServiceApplication.class, args);
    }

}
