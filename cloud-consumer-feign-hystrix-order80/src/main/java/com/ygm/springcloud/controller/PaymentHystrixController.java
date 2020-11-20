package com.ygm.springcloud.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.ygm.springcloud.service.PaymentHystrixService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;

@Service
public class PaymentHystrixController {

    @Resource
    private PaymentHystrixService paymentHystrixService;

    @GetMapping("/consumer/payment/hystrix/ok/{id}")
   public String paymentInfo_OK(@PathVariable("id") Integer id){
        String result =paymentHystrixService.paymentInfo_OK(id);
        return  result;
   }

   @GetMapping("/consumer/payment/hystrix/timeout/{id}")
   @HystrixCommand(fallbackMethod = "paymentTimeOutFallbackMethod",commandProperties = {
           @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "1500")
   })
   public String paymentInfo_TimeOut(@PathVariable("id") Integer id){

       String result = paymentHystrixService.paymentInfo_TimeOut(id);
       return result;
   }

   public String paymentTimeOutFallbackMethod(){
       return "消费者80，对方支付系统繁忙请稍后再试";
   }
}
