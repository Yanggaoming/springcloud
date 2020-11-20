package com.ygm.springcloud.controller;

import com.ygm.cloud.entity.CommonResult;
import com.ygm.cloud.entity.Payment;
import com.ygm.springcloud.lb.LoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

/**
 * RestTemplate 提供了多种便捷访问远程HTTP 服务的方法
 * 是一种简单便捷的访问restful 服务模板类，是spring提供的用于访问rest服务的客户端模板工具集
 *
 */
@RestController
@Slf4j
public class OrderController {

    public static final String PAYMENT_URL="http://CLOUD-PAYMENT-SERVICE";

    @Resource
    private DiscoveryClient discoveryClient;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private LoadBalancer loadBalancer;

    /***
     * 1:restTemplate.postForObject :返回对象为响应体中数据转换成的对象，基本上可以理解为Json
     * 2:restTemplate.getForEntity :返回对象为ResponseEntity 对象，包含了响应中的一些重要信息，比如响应头，响应状态码，响应体等
     * @param payment
     * @return
     */
    @GetMapping("/consumer/payment/create")
    public CommonResult<Payment> create(Payment payment){
        return restTemplate.postForObject(PAYMENT_URL+"/payment/create",payment,CommonResult.class);
    }

    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") Long id){
        return restTemplate.getForObject(PAYMENT_URL+"/payment/get/"+id,CommonResult.class);
    }

    @GetMapping(value = "/consumer/payent/lb")
    public String getPaymentLB(){
        List<ServiceInstance> instances =discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        if(instances==null && instances.size()<=0){
            return null;
        }
        ServiceInstance serviceInstance =loadBalancer.instance(instances);
        URI uri = serviceInstance.getUri();
        return restTemplate.getForObject(uri+"/payment/lb",String.class);
    }
}
