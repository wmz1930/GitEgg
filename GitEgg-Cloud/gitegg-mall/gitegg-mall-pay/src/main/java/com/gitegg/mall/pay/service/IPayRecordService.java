package com.gitegg.mall.pay.service;

import java.util.List;

import com.gitegg.mall.pay.entity.PayRecord;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.gitegg.mall.pay.dto.PayRecordDTO;
import com.gitegg.mall.pay.dto.CreatePayRecordDTO;
import com.gitegg.mall.pay.dto.UpdatePayRecordDTO;
import com.gitegg.mall.pay.dto.QueryPayRecordDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author GitEgg
 * @since 2021-03-19
 */
public interface IPayRecordService extends IService<PayRecord> {

    /**
    * 分页查询列表
    * @param page
    * @param queryPayRecordDTO
    * @return
    */
    Page<PayRecordDTO> queryPayRecordList(Page<PayRecordDTO> page, QueryPayRecordDTO queryPayRecordDTO);

    /**
    * 查询详情
    * @param queryPayRecordDTO
    * @return
    */
    PayRecordDTO queryPayRecord(QueryPayRecordDTO queryPayRecordDTO);

    /**
    * 创建
    * @param payRecord
    * @return
    */
    boolean createPayRecord(CreatePayRecordDTO payRecord);

    /**
    * 更新
    * @param payRecord
    * @return
    */
    boolean updatePayRecord(UpdatePayRecordDTO payRecord);

    /**
    * 删除
    * @param payRecordId
    * @return
    */
    boolean deletePayRecord(Long payRecordId);

    /**
    * 批量删除
    * @param payRecordIds
    * @return
    */
    boolean batchDeletePayRecord(List<Long> payRecordIds);
}
