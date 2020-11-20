package com.ygm.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ygm.cloud.entity.CommonResult;
import com.ygm.cloud.entity.Payment;
import com.ygm.springcloud.myhandler.CustomerBlockHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimitController {

    @GetMapping("/byResouce")
    @SentinelResource(value = "byResouce",blockHandler = "handleException")
    public CommonResult byResouce(){

        return new CommonResult(200,"按资源名称限流测试OK,",new Payment(2020L,"serial001"));
    }

    public CommonResult handleException(BlockException exception){
        return new CommonResult(444,exception.getClass().getCanonicalName()+"\t服务不可用");
    }

    @GetMapping("/rateLimit/customerBlockHandler")
    @SentinelResource(value = "customerBlockHandler",blockHandlerClass = CustomerBlockHandler.class,blockHandler = "handlerException2")
    public CommonResult customerBlockHandler(){
        return  new CommonResult(200,"按客户自定义",new Payment(2020L,"serial002"));
    }
}

