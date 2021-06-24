package com.gitegg.mall.user.mapper;

import com.gitegg.mall.user.entity.UserBalanceRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.mall.user.dto.UserBalanceRecordDTO;
import com.gitegg.mall.user.dto.QueryUserBalanceRecordDTO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-03-19
 */
public interface UserBalanceRecordMapper extends BaseMapper<UserBalanceRecord> {

    /**
    * 查询列表
    * @param page
    * @param userBalanceRecordDTO
    * @return
    */
    Page<UserBalanceRecordDTO> queryUserBalanceRecordList(Page<UserBalanceRecordDTO> page, @Param("userBalanceRecord") QueryUserBalanceRecordDTO userBalanceRecordDTO);

    /**
    * 查询信息
    * @param userBalanceRecordDTO
    * @return
    */
    UserBalanceRecordDTO queryUserBalanceRecord(@Param("userBalanceRecord") QueryUserBalanceRecordDTO userBalanceRecordDTO);
}
