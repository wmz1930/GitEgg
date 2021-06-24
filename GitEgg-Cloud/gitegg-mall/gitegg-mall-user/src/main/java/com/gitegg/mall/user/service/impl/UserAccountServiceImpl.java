package com.gitegg.mall.user.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.mall.user.entity.UserBalanceRecord;
import com.gitegg.mall.user.service.IUserBalanceRecordService;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.domain.GitEggUser;
import com.gitegg.platform.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.mall.user.entity.UserAccount;
import com.gitegg.mall.user.mapper.UserAccountMapper;
import com.gitegg.mall.user.service.IUserAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.mall.user.dto.UserAccountDTO;
import com.gitegg.mall.user.dto.CreateUserAccountDTO;
import com.gitegg.mall.user.dto.UpdateUserAccountDTO;
import com.gitegg.mall.user.dto.QueryUserAccountDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户账户表 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2021-03-19
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements IUserAccountService {

    private final UserAccountMapper userAccountMapper;

    private final IUserBalanceRecordService userBalanceRecordService;

    /**
     * 事务传播特性设置为 REQUIRES_NEW 开启新的事务    重要！！！！一定要使用REQUIRES_NEW
     */
    @DS("mall_user")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void deduction(Long userId, BigDecimal amountOfMoney) {
        //查看账户余额是否满足扣款
        QueryUserAccountDTO queryUserAccountDTO = new QueryUserAccountDTO();
        queryUserAccountDTO.setUserId(userId);
        UserAccountDTO userAccount = this.queryUserAccount(queryUserAccountDTO);

        if(userAccount == null) {
            throw new BusinessException("用户未开通个人账户");
        }

        if(amountOfMoney.compareTo(userAccount.getBalance()) > GitEggConstant.Number.ZERO) {
            throw new BusinessException("账户余额不足");
        }
        //执行扣款
        userAccountMapper.deductionById(userAccount.getId(), amountOfMoney);

        //加入账户变动记录
        UserBalanceRecord userBalanceRecord = new UserBalanceRecord();
        userBalanceRecord.setUserId(userId);
        userBalanceRecord.setAmount(amountOfMoney);
        userBalanceRecord.setType(GitEggConstant.Number.TWO);
        userBalanceRecordService.save(userBalanceRecord);
    }

    /**
    * 分页查询用户账户表列表
    * @param page
    * @param queryUserAccountDTO
    * @return
    */
    @Override
    public Page<UserAccountDTO> queryUserAccountList(Page<UserAccountDTO> page, QueryUserAccountDTO queryUserAccountDTO) {
        Page<UserAccountDTO> userAccountInfoList = userAccountMapper.queryUserAccountList(page, queryUserAccountDTO);
        return userAccountInfoList;
    }

    /**
    * 查询用户账户表详情
    * @param queryUserAccountDTO
    * @return
    */
    @Override
    public UserAccountDTO queryUserAccount(QueryUserAccountDTO queryUserAccountDTO) {
        UserAccountDTO userAccountDTO = userAccountMapper.queryUserAccount(queryUserAccountDTO);
        return userAccountDTO;
    }

    /**
    * 创建用户账户表
    * @param userAccount
    * @return
    */
    @Override
    public boolean createUserAccount(CreateUserAccountDTO userAccount) {
        UserAccount userAccountEntity = BeanCopierUtils.copyByClass(userAccount, UserAccount.class);
        boolean result = this.save(userAccountEntity);
        return result;
    }

    /**
    * 更新用户账户表
    * @param userAccount
    * @return
    */
    @Override
    public boolean updateUserAccount(UpdateUserAccountDTO userAccount) {
        UserAccount userAccountEntity = BeanCopierUtils.copyByClass(userAccount, UserAccount.class);
        QueryWrapper<UserAccount> wrapper = new QueryWrapper<>();
        wrapper.eq("id", userAccountEntity.getId());
        boolean result = this.update(userAccountEntity, wrapper);
        return result;
    }

    /**
    * 删除用户账户表
    * @param userAccountId
    * @return
    */
    @Override
    public boolean deleteUserAccount(Long userAccountId) {
        boolean result = this.removeById(userAccountId);
        return result;
    }

    /**
    * 批量删除用户账户表
    * @param userAccountIds
    * @return
    */
    @Override
    public boolean batchDeleteUserAccount(List<Long> userAccountIds) {
        boolean result = this.removeByIds(userAccountIds);
        return result;
    }
}
