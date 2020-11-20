package com.ygm.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")
public class PaymentService {

    //正常访问OK的方法
    public String paymentInfo_ok(Integer id){
        return "线程池："+Thread.currentThread().getName()+"paymentinfo_ok,id:"+id+"\t"+"o(n_n)o哈哈~";
    }

    /***
     * 一旦调用服务方法失败并抛出了错误信息后，
     * 会自动调用@HystrixCommand标注好的fallbackMethod 调用类中的指定方法
     * @param id
     * @return
     * 规定5秒钟以内走正常的业务逻辑，超过5秒 走fallbackMethod 方法
     */
//    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler",commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000")
//    })
    @HystrixCommand
    public String paymentInfo_TimeOut(Integer id){
        int a=10/0; //by zero
        int timeNumber=3;
        try {
            TimeUnit.SECONDS.sleep(timeNumber);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return "线程池："+Thread.currentThread().getName()+"paymentInfo_TimeOut,id:"+id+"\t"+"耗时(秒)："+timeNumber;
    }

    public String paymentInfo_TimeOutHandler(Integer id){
        return "线程池："+Thread.currentThread().getName()+"paymentInfo_TimeOutHandler,id:"+id+"\t"+"o(T__T)o~8001系统繁忙";
    }

    //全局的fallback
    public String payment_Global_FallbackMethod(){
        return "系统异常，请联系管理员";
    }


    /////服务熔断

    /**
     * 在10秒钟中请求，10次访问，失败率超过60%熔断
     * @param id
     * @return
     */

    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",commandProperties = {
        @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),//是否开启断路器
        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),//请求次数
        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),//时间窗口期
        @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"),//失败率达到多少后跳闸
    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id){

        if(id<0){
            throw new RuntimeException("id 不能为负数");
        }
        String serialNumber= IdUtil.simpleUUID();
        return Thread.currentThread().getName()+"\t"+"调用成功："+serialNumber;
    }

    public String paymentCircuitBreaker_fallback(@PathVariable("id")Integer id){
        return "ID 不能为负数，请稍后再试"+id;
    }














}
