package com.gitegg.service.extension.mail.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.service.extension.mail.entity.MailTemplate;
import com.gitegg.service.extension.mail.mapper.MailTemplateMapper;
import com.gitegg.service.extension.mail.service.IMailTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.service.extension.mail.dto.MailTemplateDTO;
import com.gitegg.service.extension.mail.dto.CreateMailTemplateDTO;
import com.gitegg.service.extension.mail.dto.UpdateMailTemplateDTO;
import com.gitegg.service.extension.mail.dto.QueryMailTemplateDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 邮件模板 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2022-06-24
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MailTemplateServiceImpl extends ServiceImpl<MailTemplateMapper, MailTemplate> implements IMailTemplateService {

    private final MailTemplateMapper mailTemplateMapper;

    /**
    * 分页查询邮件模板列表
    * @param page
    * @param queryMailTemplateDTO
    * @return
    */
    @Override
    public Page<MailTemplateDTO> queryMailTemplateList(Page<MailTemplateDTO> page, QueryMailTemplateDTO queryMailTemplateDTO) {
        Page<MailTemplateDTO> mailTemplateInfoList = mailTemplateMapper.queryMailTemplateList(page, queryMailTemplateDTO);
        return mailTemplateInfoList;
    }

    /**
    * 查询邮件模板列表
    * @param queryMailTemplateDTO
    * @return
    */
    @Override
    public List<MailTemplateDTO> queryMailTemplateList(QueryMailTemplateDTO queryMailTemplateDTO) {
        List<MailTemplateDTO> mailTemplateInfoList = mailTemplateMapper.queryMailTemplateList(queryMailTemplateDTO);
        return mailTemplateInfoList;
    }

    /**
    * 查询邮件模板详情
    * @param queryMailTemplateDTO
    * @return
    */
    @Override
    public MailTemplateDTO queryMailTemplate(QueryMailTemplateDTO queryMailTemplateDTO) {
        MailTemplateDTO mailTemplateDTO = mailTemplateMapper.queryMailTemplate(queryMailTemplateDTO);
        return mailTemplateDTO;
    }

    /**
    * 创建邮件模板
    * @param mailTemplate
    * @return
    */
    @Override
    public boolean createMailTemplate(CreateMailTemplateDTO mailTemplate) {
        MailTemplate mailTemplateEntity = BeanCopierUtils.copyByClass(mailTemplate, MailTemplate.class);
        boolean result = this.save(mailTemplateEntity);
        return result;
    }

    /**
    * 更新邮件模板
    * @param mailTemplate
    * @return
    */
    @Override
    public boolean updateMailTemplate(UpdateMailTemplateDTO mailTemplate) {
        MailTemplate mailTemplateEntity = BeanCopierUtils.copyByClass(mailTemplate, MailTemplate.class);
        boolean result = this.updateById(mailTemplateEntity);
        return result;
    }

    /**
    * 删除邮件模板
    * @param mailTemplateId
    * @return
    */
    @Override
    public boolean deleteMailTemplate(Long mailTemplateId) {
        boolean result = this.removeById(mailTemplateId);
        return result;
    }

    /**
    * 批量删除邮件模板
    * @param mailTemplateIds
    * @return
    */
    @Override
    public boolean batchDeleteMailTemplate(List<Long> mailTemplateIds) {
        boolean result = this.removeByIds(mailTemplateIds);
        return result;
    }
}
