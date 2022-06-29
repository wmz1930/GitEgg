package com.gitegg.service.extension.mail.mapper;

import com.gitegg.service.extension.mail.entity.MailLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.service.extension.mail.dto.MailLogDTO;
import com.gitegg.service.extension.mail.dto.QueryMailLogDTO;

import java.util.List;

/**
 * <p>
 * 邮件记录 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2022-06-24
 */
public interface MailLogMapper extends BaseMapper<MailLog> {

    /**
    * 分页查询邮件记录列表
    * @param page
    * @param mailLogDTO
    * @return
    */
    Page<MailLogDTO> queryMailLogList(Page<MailLogDTO> page, @Param("mailLog") QueryMailLogDTO mailLogDTO);

    /**
    * 查询邮件记录列表
    * @param mailLogDTO
    * @return
    */
    List<MailLogDTO> queryMailLogList(@Param("mailLog") QueryMailLogDTO mailLogDTO);

    /**
    * 查询邮件记录信息
    * @param mailLogDTO
    * @return
    */
    MailLogDTO queryMailLog(@Param("mailLog") QueryMailLogDTO mailLogDTO);
}
