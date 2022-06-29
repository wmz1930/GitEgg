package com.gitegg.service.extension.mail.mapper;

import com.gitegg.service.extension.mail.entity.MailTemplate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.service.extension.mail.dto.MailTemplateDTO;
import com.gitegg.service.extension.mail.dto.QueryMailTemplateDTO;

import java.util.List;

/**
 * <p>
 * 邮件模板 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2022-06-24
 */
public interface MailTemplateMapper extends BaseMapper<MailTemplate> {

    /**
    * 分页查询邮件模板列表
    * @param page
    * @param mailTemplateDTO
    * @return
    */
    Page<MailTemplateDTO> queryMailTemplateList(Page<MailTemplateDTO> page, @Param("mailTemplate") QueryMailTemplateDTO mailTemplateDTO);

    /**
    * 查询邮件模板列表
    * @param mailTemplateDTO
    * @return
    */
    List<MailTemplateDTO> queryMailTemplateList(@Param("mailTemplate") QueryMailTemplateDTO mailTemplateDTO);

    /**
    * 查询邮件模板信息
    * @param mailTemplateDTO
    * @return
    */
    MailTemplateDTO queryMailTemplate(@Param("mailTemplate") QueryMailTemplateDTO mailTemplateDTO);
}
