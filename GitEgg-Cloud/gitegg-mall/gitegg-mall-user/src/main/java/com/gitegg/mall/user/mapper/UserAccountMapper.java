package com.gitegg.mall.user.mapper;

import com.gitegg.mall.user.entity.UserAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.mall.user.dto.UserAccountDTO;
import com.gitegg.mall.user.dto.QueryUserAccountDTO;

import java.math.BigDecimal;

/**
 * <p>
 * 用户账户表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-03-19
 */
public interface UserAccountMapper extends BaseMapper<UserAccount> {

    /**
    * 查询用户账户表列表
    * @param page
    * @param userAccountDTO
    * @return
    */
    Page<UserAccountDTO> queryUserAccountList(Page<UserAccountDTO> page, @Param("userAccount") QueryUserAccountDTO userAccountDTO);

    /**
    * 查询用户账户表信息
    * @param userAccountDTO
    * @return
    */
    UserAccountDTO queryUserAccount(@Param("userAccount") QueryUserAccountDTO userAccountDTO);

    /**
     * 扣减账户余额
     * @param id
     * @param amountOfMoney
     * @return
     */
    int deductionById(@Param("id") Long id, @Param("amountOfMoney") BigDecimal amountOfMoney);

}
