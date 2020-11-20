package com.ygm.service;

import com.ygm.cloud.entity.Payment;
import com.ygm.dao.PaymentDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService{
    @Autowired
    private PaymentDao paymentDao;


    public  int create(Payment payment){
        return paymentDao.create(payment);
    }

    public  Payment getPaymentById(@Param("id") Long id){
        return  paymentDao.getPaymentById(id);
    }
}
