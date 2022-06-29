package com.gitegg.service.extension.mail.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.service.extension.mail.entity.MailLog;
import com.gitegg.service.extension.mail.mapper.MailLogMapper;
import com.gitegg.service.extension.mail.service.IMailLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.service.extension.mail.dto.MailLogDTO;
import com.gitegg.service.extension.mail.dto.CreateMailLogDTO;
import com.gitegg.service.extension.mail.dto.UpdateMailLogDTO;
import com.gitegg.service.extension.mail.dto.QueryMailLogDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 邮件记录 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2022-06-24
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MailLogServiceImpl extends ServiceImpl<MailLogMapper, MailLog> implements IMailLogService {

    private final MailLogMapper mailLogMapper;

    /**
    * 分页查询邮件记录列表
    * @param page
    * @param queryMailLogDTO
    * @return
    */
    @Override
    public Page<MailLogDTO> queryMailLogList(Page<MailLogDTO> page, QueryMailLogDTO queryMailLogDTO) {
        Page<MailLogDTO> mailLogInfoList = mailLogMapper.queryMailLogList(page, queryMailLogDTO);
        return mailLogInfoList;
    }

    /**
    * 查询邮件记录列表
    * @param queryMailLogDTO
    * @return
    */
    @Override
    public List<MailLogDTO> queryMailLogList(QueryMailLogDTO queryMailLogDTO) {
        List<MailLogDTO> mailLogInfoList = mailLogMapper.queryMailLogList(queryMailLogDTO);
        return mailLogInfoList;
    }

    /**
    * 查询邮件记录详情
    * @param queryMailLogDTO
    * @return
    */
    @Override
    public MailLogDTO queryMailLog(QueryMailLogDTO queryMailLogDTO) {
        MailLogDTO mailLogDTO = mailLogMapper.queryMailLog(queryMailLogDTO);
        return mailLogDTO;
    }

    /**
    * 创建邮件记录
    * @param mailLog
    * @return
    */
    @Override
    public boolean createMailLog(CreateMailLogDTO mailLog) {
        MailLog mailLogEntity = BeanCopierUtils.copyByClass(mailLog, MailLog.class);
        boolean result = this.save(mailLogEntity);
        return result;
    }

    /**
    * 更新邮件记录
    * @param mailLog
    * @return
    */
    @Override
    public boolean updateMailLog(UpdateMailLogDTO mailLog) {
        MailLog mailLogEntity = BeanCopierUtils.copyByClass(mailLog, MailLog.class);
        boolean result = this.updateById(mailLogEntity);
        return result;
    }

    /**
    * 删除邮件记录
    * @param mailLogId
    * @return
    */
    @Override
    public boolean deleteMailLog(Long mailLogId) {
        boolean result = this.removeById(mailLogId);
        return result;
    }

    /**
    * 批量删除邮件记录
    * @param mailLogIds
    * @return
    */
    @Override
    public boolean batchDeleteMailLog(List<Long> mailLogIds) {
        boolean result = this.removeByIds(mailLogIds);
        return result;
    }
}
