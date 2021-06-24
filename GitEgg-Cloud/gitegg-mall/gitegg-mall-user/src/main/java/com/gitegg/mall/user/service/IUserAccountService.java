package com.gitegg.mall.user.service;

import java.math.BigDecimal;
import java.util.List;

import com.gitegg.mall.user.entity.UserAccount;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.gitegg.mall.user.dto.UserAccountDTO;
import com.gitegg.mall.user.dto.CreateUserAccountDTO;
import com.gitegg.mall.user.dto.UpdateUserAccountDTO;
import com.gitegg.mall.user.dto.QueryUserAccountDTO;
import com.gitegg.platform.base.domain.GitEggUser;

/**
 * <p>
 * 用户账户表 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2021-03-19
 */
public interface IUserAccountService extends IService<UserAccount> {


    /**
     * 扣款接口
     * @param userId
     * @param amountOfMoney
     */
    void deduction(Long userId, BigDecimal amountOfMoney);

    /**
    * 分页查询用户账户表列表
    * @param page
    * @param queryUserAccountDTO
    * @return
    */
    Page<UserAccountDTO> queryUserAccountList(Page<UserAccountDTO> page, QueryUserAccountDTO queryUserAccountDTO);

    /**
    * 查询用户账户表详情
    * @param queryUserAccountDTO
    * @return
    */
    UserAccountDTO queryUserAccount(QueryUserAccountDTO queryUserAccountDTO);

    /**
    * 创建用户账户表
    * @param userAccount
    * @return
    */
    boolean createUserAccount(CreateUserAccountDTO userAccount);

    /**
    * 更新用户账户表
    * @param userAccount
    * @return
    */
    boolean updateUserAccount(UpdateUserAccountDTO userAccount);

    /**
    * 删除用户账户表
    * @param userAccountId
    * @return
    */
    boolean deleteUserAccount(Long userAccountId);

    /**
    * 批量删除用户账户表
    * @param userAccountIds
    * @return
    */
    boolean batchDeleteUserAccount(List<Long> userAccountIds);
}
