package com.gitegg.service.extension.mail.service;

import java.util.List;

import com.gitegg.service.extension.mail.entity.MailLog;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.gitegg.service.extension.mail.dto.MailLogDTO;
import com.gitegg.service.extension.mail.dto.CreateMailLogDTO;
import com.gitegg.service.extension.mail.dto.UpdateMailLogDTO;
import com.gitegg.service.extension.mail.dto.QueryMailLogDTO;

/**
 * <p>
 * 邮件记录 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2022-06-24
 */
public interface IMailLogService extends IService<MailLog> {

    /**
    * 分页查询邮件记录列表
    * @param page
    * @param queryMailLogDTO
    * @return
    */
    Page<MailLogDTO> queryMailLogList(Page<MailLogDTO> page, QueryMailLogDTO queryMailLogDTO);

    /**
    * 查询邮件记录列表
    * @param queryMailLogDTO
    * @return
    */
    List<MailLogDTO> queryMailLogList(QueryMailLogDTO queryMailLogDTO);

    /**
    * 查询邮件记录详情
    * @param queryMailLogDTO
    * @return
    */
    MailLogDTO queryMailLog(QueryMailLogDTO queryMailLogDTO);

    /**
    * 创建邮件记录
    * @param mailLog
    * @return
    */
    boolean createMailLog(CreateMailLogDTO mailLog);

    /**
    * 更新邮件记录
    * @param mailLog
    * @return
    */
    boolean updateMailLog(UpdateMailLogDTO mailLog);

    /**
    * 删除邮件记录
    * @param mailLogId
    * @return
    */
    boolean deleteMailLog(Long mailLogId);

    /**
    * 批量删除邮件记录
    * @param mailLogIds
    * @return
    */
    boolean batchDeleteMailLog(List<Long> mailLogIds);
}
