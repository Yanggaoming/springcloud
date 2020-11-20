package com.ygm.springcloud.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
public class OrderZkController {

    protected static final Logger log = LoggerFactory.getLogger(OrderZkController.class);

    public static String INVOKE_URL ="http://cloud-provider-payment";

    @Resource
    private RestTemplate restTemplate;
    @GetMapping("/consumber/payment/zk")
    public String paymentInfo(){
        String result=restTemplate.getForObject(INVOKE_URL+"/payment/zk",String.class);
        return result;
    }
}
