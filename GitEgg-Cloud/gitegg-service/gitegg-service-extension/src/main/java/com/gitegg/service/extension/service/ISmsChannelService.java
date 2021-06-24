package com.gitegg.service.extension.service;

import java.util.List;

import com.gitegg.service.extension.entity.SmsChannel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.gitegg.service.extension.dto.SmsChannelDTO;
import com.gitegg.service.extension.dto.CreateSmsChannelDTO;
import com.gitegg.service.extension.dto.UpdateSmsChannelDTO;
import com.gitegg.service.extension.dto.QuerySmsChannelDTO;

/**
 * <p>
 * 短信渠道表 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2021-01-26
 */
public interface ISmsChannelService extends IService<SmsChannel> {

    /**
    * 分页查询短信渠道表列表
    * @param page
    * @param querySmsChannelDTO
    * @return
    */
    Page<SmsChannelDTO> querySmsChannelList(Page<SmsChannelDTO> page, QuerySmsChannelDTO querySmsChannelDTO);

    /**
    * 查询短信渠道表详情
    * @param querySmsChannelDTO
    * @return
    */
    SmsChannelDTO querySmsChannel(QuerySmsChannelDTO querySmsChannelDTO);

    /**
    * 创建短信渠道表
    * @param smsChannel
    * @return
    */
    boolean createSmsChannel(CreateSmsChannelDTO smsChannel);

    /**
    * 更新短信渠道表
    * @param smsChannel
    * @return
    */
    boolean updateSmsChannel(UpdateSmsChannelDTO smsChannel);

    /**
    * 删除短信渠道表
    * @param smsChannelId
    * @return
    */
    boolean deleteSmsChannel(Long smsChannelId);

    /**
    * 批量删除短信渠道表
    * @param smsChannelIds
    * @return
    */
    boolean batchDeleteSmsChannel(List<Long> smsChannelIds);
}
