package com.runbo.education.easy.learn.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Created by Edison Xu on 2017/3/28.
 */
@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
public class GatewayServiceApplication {

    public static void main(String args[]){
        SpringApplication.run(GatewayServiceApplication.class, args);
    }
}
