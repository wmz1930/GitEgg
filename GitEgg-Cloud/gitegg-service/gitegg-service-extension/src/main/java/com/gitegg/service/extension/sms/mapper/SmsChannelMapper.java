package com.gitegg.service.extension.sms.mapper;

import com.gitegg.service.extension.sms.entity.SmsChannel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.service.extension.sms.dto.SmsChannelDTO;
import com.gitegg.service.extension.sms.dto.QuerySmsChannelDTO;

import java.util.List;

/**
 * <p>
 * 短信渠道表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-24
 */
public interface SmsChannelMapper extends BaseMapper<SmsChannel> {

    /**
    * 分页查询短信渠道表列表
    * @param page
    * @param smsChannelDTO
    * @return
    */
    Page<SmsChannelDTO> querySmsChannelList(Page<SmsChannelDTO> page, @Param("smsChannel") QuerySmsChannelDTO smsChannelDTO);

    /**
    * 查询短信渠道表列表
    * @param smsChannelDTO
    * @return
    */
    List<SmsChannelDTO> querySmsChannelList(@Param("smsChannel") QuerySmsChannelDTO smsChannelDTO);

    /**
    * 查询短信渠道表信息
    * @param smsChannelDTO
    * @return
    */
    SmsChannelDTO querySmsChannel(@Param("smsChannel") QuerySmsChannelDTO smsChannelDTO);
}
