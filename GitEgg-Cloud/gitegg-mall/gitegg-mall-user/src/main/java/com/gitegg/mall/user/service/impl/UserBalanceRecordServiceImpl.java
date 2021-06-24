package com.gitegg.mall.user.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.mall.user.entity.UserBalanceRecord;
import com.gitegg.mall.user.mapper.UserBalanceRecordMapper;
import com.gitegg.mall.user.service.IUserBalanceRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.mall.user.dto.UserBalanceRecordDTO;
import com.gitegg.mall.user.dto.CreateUserBalanceRecordDTO;
import com.gitegg.mall.user.dto.UpdateUserBalanceRecordDTO;
import com.gitegg.mall.user.dto.QueryUserBalanceRecordDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2021-03-19
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserBalanceRecordServiceImpl extends ServiceImpl<UserBalanceRecordMapper, UserBalanceRecord> implements IUserBalanceRecordService {

    private final UserBalanceRecordMapper userBalanceRecordMapper;

    /**
    * 分页查询列表
    * @param page
    * @param queryUserBalanceRecordDTO
    * @return
    */
    @Override
    public Page<UserBalanceRecordDTO> queryUserBalanceRecordList(Page<UserBalanceRecordDTO> page, QueryUserBalanceRecordDTO queryUserBalanceRecordDTO) {
        Page<UserBalanceRecordDTO> userBalanceRecordInfoList = userBalanceRecordMapper.queryUserBalanceRecordList(page, queryUserBalanceRecordDTO);
        return userBalanceRecordInfoList;
    }

    /**
    * 查询详情
    * @param queryUserBalanceRecordDTO
    * @return
    */
    @Override
    public UserBalanceRecordDTO queryUserBalanceRecord(QueryUserBalanceRecordDTO queryUserBalanceRecordDTO) {
        UserBalanceRecordDTO userBalanceRecordDTO = userBalanceRecordMapper.queryUserBalanceRecord(queryUserBalanceRecordDTO);
        return userBalanceRecordDTO;
    }

    /**
    * 创建
    * @param userBalanceRecord
    * @return
    */
    @Override
    public boolean createUserBalanceRecord(CreateUserBalanceRecordDTO userBalanceRecord) {
        UserBalanceRecord userBalanceRecordEntity = BeanCopierUtils.copyByClass(userBalanceRecord, UserBalanceRecord.class);
        boolean result = this.save(userBalanceRecordEntity);
        return result;
    }

    /**
    * 更新
    * @param userBalanceRecord
    * @return
    */
    @Override
    public boolean updateUserBalanceRecord(UpdateUserBalanceRecordDTO userBalanceRecord) {
        UserBalanceRecord userBalanceRecordEntity = BeanCopierUtils.copyByClass(userBalanceRecord, UserBalanceRecord.class);
        QueryWrapper<UserBalanceRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("id", userBalanceRecordEntity.getId());
        boolean result = this.update(userBalanceRecordEntity, wrapper);
        return result;
    }

    /**
    * 删除
    * @param userBalanceRecordId
    * @return
    */
    @Override
    public boolean deleteUserBalanceRecord(Long userBalanceRecordId) {
        boolean result = this.removeById(userBalanceRecordId);
        return result;
    }

    /**
    * 批量删除
    * @param userBalanceRecordIds
    * @return
    */
    @Override
    public boolean batchDeleteUserBalanceRecord(List<Long> userBalanceRecordIds) {
        boolean result = this.removeByIds(userBalanceRecordIds);
        return result;
    }
}
