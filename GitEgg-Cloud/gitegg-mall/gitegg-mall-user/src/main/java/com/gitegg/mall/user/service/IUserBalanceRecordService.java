package com.gitegg.mall.user.service;

import java.util.List;

import com.gitegg.mall.user.entity.UserBalanceRecord;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.gitegg.mall.user.dto.UserBalanceRecordDTO;
import com.gitegg.mall.user.dto.CreateUserBalanceRecordDTO;
import com.gitegg.mall.user.dto.UpdateUserBalanceRecordDTO;
import com.gitegg.mall.user.dto.QueryUserBalanceRecordDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author GitEgg
 * @since 2021-03-19
 */
public interface IUserBalanceRecordService extends IService<UserBalanceRecord> {

    /**
    * 分页查询列表
    * @param page
    * @param queryUserBalanceRecordDTO
    * @return
    */
    Page<UserBalanceRecordDTO> queryUserBalanceRecordList(Page<UserBalanceRecordDTO> page, QueryUserBalanceRecordDTO queryUserBalanceRecordDTO);

    /**
    * 查询详情
    * @param queryUserBalanceRecordDTO
    * @return
    */
    UserBalanceRecordDTO queryUserBalanceRecord(QueryUserBalanceRecordDTO queryUserBalanceRecordDTO);

    /**
    * 创建
    * @param userBalanceRecord
    * @return
    */
    boolean createUserBalanceRecord(CreateUserBalanceRecordDTO userBalanceRecord);

    /**
    * 更新
    * @param userBalanceRecord
    * @return
    */
    boolean updateUserBalanceRecord(UpdateUserBalanceRecordDTO userBalanceRecord);

    /**
    * 删除
    * @param userBalanceRecordId
    * @return
    */
    boolean deleteUserBalanceRecord(Long userBalanceRecordId);

    /**
    * 批量删除
    * @param userBalanceRecordIds
    * @return
    */
    boolean batchDeleteUserBalanceRecord(List<Long> userBalanceRecordIds);
}
