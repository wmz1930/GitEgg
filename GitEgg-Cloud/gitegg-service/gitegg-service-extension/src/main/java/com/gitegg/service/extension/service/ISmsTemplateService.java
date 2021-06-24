package com.gitegg.service.extension.service;

import java.util.List;

import com.gitegg.service.extension.entity.SmsTemplate;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.gitegg.service.extension.dto.SmsTemplateDTO;
import com.gitegg.service.extension.dto.CreateSmsTemplateDTO;
import com.gitegg.service.extension.dto.UpdateSmsTemplateDTO;
import com.gitegg.service.extension.dto.QuerySmsTemplateDTO;

/**
 * <p>
 * 短信配置表 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2021-01-26
 */
public interface ISmsTemplateService extends IService<SmsTemplate> {

    /**
    * 分页查询短信配置表列表
    * @param page
    * @param querySmsTemplateDTO
    * @return
    */
    Page<SmsTemplateDTO> querySmsTemplateList(Page<SmsTemplateDTO> page, QuerySmsTemplateDTO querySmsTemplateDTO);

    /**
    * 查询短信配置表详情
    * @param querySmsTemplateDTO
    * @return
    */
    SmsTemplateDTO querySmsTemplate(QuerySmsTemplateDTO querySmsTemplateDTO);

    /**
    * 创建短信配置表
    * @param smsTemplate
    * @return
    */
    boolean createSmsTemplate(CreateSmsTemplateDTO smsTemplate);

    /**
    * 更新短信配置表
    * @param smsTemplate
    * @return
    */
    boolean updateSmsTemplate(UpdateSmsTemplateDTO smsTemplate);

    /**
    * 删除短信配置表
    * @param smsTemplateId
    * @return
    */
    boolean deleteSmsTemplate(Long smsTemplateId);

    /**
    * 批量删除短信配置表
    * @param smsTemplateIds
    * @return
    */
    boolean batchDeleteSmsTemplate(List<Long> smsTemplateIds);
}
