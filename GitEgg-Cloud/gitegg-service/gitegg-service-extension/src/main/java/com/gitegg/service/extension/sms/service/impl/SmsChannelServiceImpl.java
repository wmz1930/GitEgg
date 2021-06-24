package com.gitegg.service.extension.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.service.extension.sms.dto.CreateSmsChannelDTO;
import com.gitegg.service.extension.sms.dto.QuerySmsChannelDTO;
import com.gitegg.service.extension.sms.dto.SmsChannelDTO;
import com.gitegg.service.extension.sms.dto.UpdateSmsChannelDTO;
import com.gitegg.service.extension.sms.entity.SmsChannel;
import com.gitegg.service.extension.sms.mapper.SmsChannelMapper;
import com.gitegg.service.extension.sms.service.ISmsChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 短信渠道表 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2021-01-26
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SmsChannelServiceImpl extends ServiceImpl<SmsChannelMapper, SmsChannel> implements ISmsChannelService {

    private final SmsChannelMapper smsChannelMapper;

    /**
    * 分页查询短信渠道表列表
    * @param page
    * @param querySmsChannelDTO
    * @return
    */
    @Override
    public Page<SmsChannelDTO> querySmsChannelList(Page<SmsChannelDTO> page, QuerySmsChannelDTO querySmsChannelDTO) {
        Page<SmsChannelDTO> smsChannelInfoList = smsChannelMapper.querySmsChannelList(page, querySmsChannelDTO);
        return smsChannelInfoList;
    }

    /**
    * 查询短信渠道表详情
    * @param querySmsChannelDTO
    * @return
    */
    @Override
    public SmsChannelDTO querySmsChannel(QuerySmsChannelDTO querySmsChannelDTO) {
        SmsChannelDTO smsChannelDTO = smsChannelMapper.querySmsChannel(querySmsChannelDTO);
        return smsChannelDTO;
    }

    /**
    * 创建短信渠道表
    * @param smsChannel
    * @return
    */
    @Override
    public boolean createSmsChannel(CreateSmsChannelDTO smsChannel) {
        SmsChannel smsChannelEntity = BeanCopierUtils.copyByClass(smsChannel, SmsChannel.class);
        boolean result = this.save(smsChannelEntity);
        return result;
    }

    /**
    * 更新短信渠道表
    * @param smsChannel
    * @return
    */
    @Override
    public boolean updateSmsChannel(UpdateSmsChannelDTO smsChannel) {
        SmsChannel smsChannelEntity = BeanCopierUtils.copyByClass(smsChannel, SmsChannel.class);
        QueryWrapper<SmsChannel> wrapper = new QueryWrapper<>();
        wrapper.eq("id", smsChannelEntity.getId());
        boolean result = this.update(smsChannelEntity, wrapper);
        return result;
    }

    /**
    * 删除短信渠道表
    * @param smsChannelId
    * @return
    */
    @Override
    public boolean deleteSmsChannel(Long smsChannelId) {
        boolean result = this.removeById(smsChannelId);
        return result;
    }

    /**
    * 批量删除短信渠道表
    * @param smsChannelIds
    * @return
    */
    @Override
    public boolean batchDeleteSmsChannel(List<Long> smsChannelIds) {
        boolean result = this.removeByIds(smsChannelIds);
        return result;
    }
}
