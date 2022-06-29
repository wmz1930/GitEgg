package com.gitegg.service.extension.mail.service;

import java.util.List;

import com.gitegg.service.extension.mail.entity.MailChannel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.gitegg.service.extension.mail.dto.MailChannelDTO;
import com.gitegg.service.extension.mail.dto.CreateMailChannelDTO;
import com.gitegg.service.extension.mail.dto.UpdateMailChannelDTO;
import com.gitegg.service.extension.mail.dto.QueryMailChannelDTO;

/**
 * <p>
 * 邮件渠道表 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2022-06-24
 */
public interface IMailChannelService extends IService<MailChannel> {

    /**
    * 分页查询邮件渠道表列表
    * @param page
    * @param queryMailChannelDTO
    * @return
    */
    Page<MailChannelDTO> queryMailChannelList(Page<MailChannelDTO> page, QueryMailChannelDTO queryMailChannelDTO);

    /**
    * 查询邮件渠道表列表
    * @param queryMailChannelDTO
    * @return
    */
    List<MailChannelDTO> queryMailChannelList(QueryMailChannelDTO queryMailChannelDTO);

    /**
    * 查询邮件渠道表详情
    * @param queryMailChannelDTO
    * @return
    */
    MailChannelDTO queryMailChannel(QueryMailChannelDTO queryMailChannelDTO);

    /**
    * 创建邮件渠道表
    * @param mailChannel
    * @return
    */
    boolean createMailChannel(CreateMailChannelDTO mailChannel);

    /**
    * 更新邮件渠道表
    * @param mailChannel
    * @return
    */
    boolean updateMailChannel(UpdateMailChannelDTO mailChannel);

    /**
    * 删除邮件渠道表
    * @param mailChannelId
    * @return
    */
    boolean deleteMailChannel(Long mailChannelId);

    /**
    * 批量删除邮件渠道表
    * @param mailChannelIds
    * @return
    */
    boolean batchDeleteMailChannel(List<Long> mailChannelIds);

    /**
     * 初始化邮件配置
     */
    void initMailChannelList();
}
