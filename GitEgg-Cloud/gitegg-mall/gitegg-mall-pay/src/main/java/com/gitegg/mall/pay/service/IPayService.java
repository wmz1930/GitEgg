package com.gitegg.mall.pay.service;

import java.math.BigDecimal;

/**
 * 支付接口
 */
public interface IPayService {

    Long pay(Long userId, BigDecimal payMoney);

}
