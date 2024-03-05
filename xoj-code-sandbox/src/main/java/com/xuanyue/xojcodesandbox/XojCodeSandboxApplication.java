package com.xuanyue.xojcodesandbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan("com.xuanyue")
@EnableDiscoveryClient
public class XojCodeSandboxApplication {

    public static void main(String[] args) {
        SpringApplication.run(XojCodeSandboxApplication.class, args);
    }

}
