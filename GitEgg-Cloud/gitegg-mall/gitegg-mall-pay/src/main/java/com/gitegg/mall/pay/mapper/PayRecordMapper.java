package com.gitegg.mall.pay.mapper;

import com.gitegg.mall.pay.entity.PayRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.mall.pay.dto.PayRecordDTO;
import com.gitegg.mall.pay.dto.QueryPayRecordDTO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-03-19
 */
public interface PayRecordMapper extends BaseMapper<PayRecord> {

    /**
    * 查询列表
    * @param page
    * @param payRecordDTO
    * @return
    */
    Page<PayRecordDTO> queryPayRecordList(Page<PayRecordDTO> page, @Param("payRecord") QueryPayRecordDTO payRecordDTO);

    /**
    * 查询信息
    * @param payRecordDTO
    * @return
    */
    PayRecordDTO queryPayRecord(@Param("payRecord") QueryPayRecordDTO payRecordDTO);
}
