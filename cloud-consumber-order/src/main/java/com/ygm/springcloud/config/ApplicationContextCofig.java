package com.ygm.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationContextCofig {
    @Bean
//    @LoadBalanced //赋予RestTemplate 负载均衡 的能力
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
