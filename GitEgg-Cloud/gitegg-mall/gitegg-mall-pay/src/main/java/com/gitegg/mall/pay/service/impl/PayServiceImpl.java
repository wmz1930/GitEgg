package com.gitegg.mall.pay.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.gitegg.mall.pay.entity.PayRecord;
import com.gitegg.mall.pay.mapper.PayRecordMapper;
import com.gitegg.mall.pay.service.IPayRecordService;
import com.gitegg.mall.pay.service.IPayService;
import com.gitegg.mall.user.client.feign.IMallUserFeign;
import com.gitegg.platform.base.constant.GitEggConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author GitEgg
 * @date 2021-03-19 15:04:41
 **/
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PayServiceImpl implements IPayService {

    private final IPayRecordService payRecordService;

    private final IMallUserFeign mallUserFeign;

    /**
     * 事务传播特性设置为 REQUIRES_NEW 开启新的事务    重要！！！！一定要使用REQUIRES_NEW
     */
    @DS("mall_pay")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Long pay(Long userId, BigDecimal payMoney) {

        //调用gitegg-mall-user的账户扣除余额接口
        mallUserFeign.accountDeduction(userId, payMoney);

        // 插入支付记录表
        PayRecord payRecord = new PayRecord();
        payRecord.setUserId(userId);
        payRecord.setAmount(payMoney);
        payRecord.setPayStatus(GitEggConstant.Number.ONE);
        payRecord.setPayType(GitEggConstant.Number.FIVE);
        payRecordService.save(payRecord);
        return payRecord.getId();
    }
}
