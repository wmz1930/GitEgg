package com.gitegg.service.extension.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.service.extension.entity.SmsTemplate;
import com.gitegg.service.extension.mapper.SmsTemplateMapper;
import com.gitegg.service.extension.service.ISmsTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.service.extension.dto.SmsTemplateDTO;
import com.gitegg.service.extension.dto.CreateSmsTemplateDTO;
import com.gitegg.service.extension.dto.UpdateSmsTemplateDTO;
import com.gitegg.service.extension.dto.QuerySmsTemplateDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 短信配置表 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2021-01-26
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SmsTemplateServiceImpl extends ServiceImpl<SmsTemplateMapper, SmsTemplate> implements ISmsTemplateService {

    private final SmsTemplateMapper smsTemplateMapper;

    /**
    * 分页查询短信配置表列表
    * @param page
    * @param querySmsTemplateDTO
    * @return
    */
    @Override
    public Page<SmsTemplateDTO> querySmsTemplateList(Page<SmsTemplateDTO> page, QuerySmsTemplateDTO querySmsTemplateDTO) {
        Page<SmsTemplateDTO> smsTemplateInfoList = smsTemplateMapper.querySmsTemplateList(page, querySmsTemplateDTO);
        return smsTemplateInfoList;
    }

    /**
    * 查询短信配置表详情
    * @param querySmsTemplateDTO
    * @return
    */
    @Override
    public SmsTemplateDTO querySmsTemplate(QuerySmsTemplateDTO querySmsTemplateDTO) {
        SmsTemplateDTO smsTemplateDTO = smsTemplateMapper.querySmsTemplate(querySmsTemplateDTO);
        return smsTemplateDTO;
    }

    /**
    * 创建短信配置表
    * @param smsTemplate
    * @return
    */
    @Override
    public boolean createSmsTemplate(CreateSmsTemplateDTO smsTemplate) {
        SmsTemplate smsTemplateEntity = BeanCopierUtils.copyByClass(smsTemplate, SmsTemplate.class);
        boolean result = this.save(smsTemplateEntity);
        return result;
    }

    /**
    * 更新短信配置表
    * @param smsTemplate
    * @return
    */
    @Override
    public boolean updateSmsTemplate(UpdateSmsTemplateDTO smsTemplate) {
        SmsTemplate smsTemplateEntity = BeanCopierUtils.copyByClass(smsTemplate, SmsTemplate.class);
        QueryWrapper<SmsTemplate> wrapper = new QueryWrapper<>();
        wrapper.eq("id", smsTemplateEntity.getId());
        boolean result = this.update(smsTemplateEntity, wrapper);
        return result;
    }

    /**
    * 删除短信配置表
    * @param smsTemplateId
    * @return
    */
    @Override
    public boolean deleteSmsTemplate(Long smsTemplateId) {
        boolean result = this.removeById(smsTemplateId);
        return result;
    }

    /**
    * 批量删除短信配置表
    * @param smsTemplateIds
    * @return
    */
    @Override
    public boolean batchDeleteSmsTemplate(List<Long> smsTemplateIds) {
        boolean result = this.removeByIds(smsTemplateIds);
        return result;
    }
}
