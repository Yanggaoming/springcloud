package com.ygm.controller;
import com.ygm.cloud.entity.CommonResult;
import com.ygm.cloud.entity.Payment;
import com.ygm.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class PaymentController {


    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PaymentService paymentService;

    @Resource
    private DiscoveryClient discoveryClient;


    @Value("${server.port}")
    private String serverPort;
    @PostMapping(value = "/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int result = paymentService.create(payment);
        if (result>0){
            return new CommonResult(200,"插入数据库成功---serverPort:"+serverPort,result);
        }else {
            return new CommonResult(444,"插入数据库失败",null);
        }
    }

    @GetMapping(value = "/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id){
        Payment paymentById = paymentService.getPaymentById(id);
        if (paymentById!=null){
            return new CommonResult(200,"查询成功---serverPort"+serverPort,paymentById);
        }else{
            return new CommonResult(444,"没有对应记录",null);
        }
    }

    //获取主机信息
    @GetMapping(value = "/payment/discovery")
    public Object discovery(){
        List<String> services = discoveryClient.getServices();
        for(String element: services){
            logger.info("------element:"+element);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances("");
        for (ServiceInstance instance:instances){
            logger.info(instance.getServiceId()+"\t" +instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());

        }
        return this.discoveryClient;
    }

    @GetMapping(value = "/payment/lb")
    public String getPaymentLB(){
        return serverPort;
    }


    @GetMapping(value = "/payment/feign/timeout")
        public String paymentFeignTimeout(){
        try {
            TimeUnit.SECONDS.sleep(3);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return serverPort;
    }
    
}
