package com.gitegg.service.extension.mapper;

import com.gitegg.service.extension.entity.SmsChannel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.service.extension.dto.SmsChannelDTO;
import com.gitegg.service.extension.dto.QuerySmsChannelDTO;

/**
 * <p>
 * 短信渠道表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-01-26
 */
public interface SmsChannelMapper extends BaseMapper<SmsChannel> {

    /**
    * 查询短信渠道表列表
    * @param page
    * @param smsChannelDTO
    * @return
    */
    Page<SmsChannelDTO> querySmsChannelList(Page<SmsChannelDTO> page, @Param("smsChannel") QuerySmsChannelDTO smsChannelDTO);

    /**
    * 查询短信渠道表信息
    * @param smsChannelDTO
    * @return
    */
    SmsChannelDTO querySmsChannel(@Param("smsChannel") QuerySmsChannelDTO smsChannelDTO);
}
