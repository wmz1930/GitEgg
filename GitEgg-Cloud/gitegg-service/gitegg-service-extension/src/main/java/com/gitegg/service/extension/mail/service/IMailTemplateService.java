package com.gitegg.service.extension.mail.service;

import java.util.List;

import com.gitegg.service.extension.mail.entity.MailTemplate;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.gitegg.service.extension.mail.dto.MailTemplateDTO;
import com.gitegg.service.extension.mail.dto.CreateMailTemplateDTO;
import com.gitegg.service.extension.mail.dto.UpdateMailTemplateDTO;
import com.gitegg.service.extension.mail.dto.QueryMailTemplateDTO;

/**
 * <p>
 * 邮件模板 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2022-06-24
 */
public interface IMailTemplateService extends IService<MailTemplate> {

    /**
    * 分页查询邮件模板列表
    * @param page
    * @param queryMailTemplateDTO
    * @return
    */
    Page<MailTemplateDTO> queryMailTemplateList(Page<MailTemplateDTO> page, QueryMailTemplateDTO queryMailTemplateDTO);

    /**
    * 查询邮件模板列表
    * @param queryMailTemplateDTO
    * @return
    */
    List<MailTemplateDTO> queryMailTemplateList(QueryMailTemplateDTO queryMailTemplateDTO);

    /**
    * 查询邮件模板详情
    * @param queryMailTemplateDTO
    * @return
    */
    MailTemplateDTO queryMailTemplate(QueryMailTemplateDTO queryMailTemplateDTO);

    /**
    * 创建邮件模板
    * @param mailTemplate
    * @return
    */
    boolean createMailTemplate(CreateMailTemplateDTO mailTemplate);

    /**
    * 更新邮件模板
    * @param mailTemplate
    * @return
    */
    boolean updateMailTemplate(UpdateMailTemplateDTO mailTemplate);

    /**
    * 删除邮件模板
    * @param mailTemplateId
    * @return
    */
    boolean deleteMailTemplate(Long mailTemplateId);

    /**
    * 批量删除邮件模板
    * @param mailTemplateIds
    * @return
    */
    boolean batchDeleteMailTemplate(List<Long> mailTemplateIds);
}
