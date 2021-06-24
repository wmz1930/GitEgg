package com.gitegg.mall.pay.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.mall.pay.entity.PayRecord;
import com.gitegg.mall.pay.mapper.PayRecordMapper;
import com.gitegg.mall.pay.service.IPayRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.mall.pay.dto.PayRecordDTO;
import com.gitegg.mall.pay.dto.CreatePayRecordDTO;
import com.gitegg.mall.pay.dto.UpdatePayRecordDTO;
import com.gitegg.mall.pay.dto.QueryPayRecordDTO;

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
public class PayRecordServiceImpl extends ServiceImpl<PayRecordMapper, PayRecord> implements IPayRecordService {

    private final PayRecordMapper payRecordMapper;

    /**
    * 分页查询列表
    * @param page
    * @param queryPayRecordDTO
    * @return
    */
    @Override
    public Page<PayRecordDTO> queryPayRecordList(Page<PayRecordDTO> page, QueryPayRecordDTO queryPayRecordDTO) {
        Page<PayRecordDTO> payRecordInfoList = payRecordMapper.queryPayRecordList(page, queryPayRecordDTO);
        return payRecordInfoList;
    }

    /**
    * 查询详情
    * @param queryPayRecordDTO
    * @return
    */
    @Override
    public PayRecordDTO queryPayRecord(QueryPayRecordDTO queryPayRecordDTO) {
        PayRecordDTO payRecordDTO = payRecordMapper.queryPayRecord(queryPayRecordDTO);
        return payRecordDTO;
    }

    /**
    * 创建
    * @param payRecord
    * @return
    */
    @Override
    public boolean createPayRecord(CreatePayRecordDTO payRecord) {
        PayRecord payRecordEntity = BeanCopierUtils.copyByClass(payRecord, PayRecord.class);
        boolean result = this.save(payRecordEntity);
        return result;
    }

    /**
    * 更新
    * @param payRecord
    * @return
    */
    @Override
    public boolean updatePayRecord(UpdatePayRecordDTO payRecord) {
        PayRecord payRecordEntity = BeanCopierUtils.copyByClass(payRecord, PayRecord.class);
        QueryWrapper<PayRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("id", payRecordEntity.getId());
        boolean result = this.update(payRecordEntity, wrapper);
        return result;
    }

    /**
    * 删除
    * @param payRecordId
    * @return
    */
    @Override
    public boolean deletePayRecord(Long payRecordId) {
        boolean result = this.removeById(payRecordId);
        return result;
    }

    /**
    * 批量删除
    * @param payRecordIds
    * @return
    */
    @Override
    public boolean batchDeletePayRecord(List<Long> payRecordIds) {
        boolean result = this.removeByIds(payRecordIds);
        return result;
    }
}
