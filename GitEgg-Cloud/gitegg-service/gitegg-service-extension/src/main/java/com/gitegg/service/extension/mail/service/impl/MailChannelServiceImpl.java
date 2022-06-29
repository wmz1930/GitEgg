package com.gitegg.service.extension.mail.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.platform.base.util.JsonUtils;
import com.gitegg.platform.email.constant.JavaMailConstant;
import com.gitegg.platform.email.props.GitEggMailProperties;
import com.gitegg.service.extension.mail.dto.CreateMailChannelDTO;
import com.gitegg.service.extension.mail.dto.MailChannelDTO;
import com.gitegg.service.extension.mail.dto.QueryMailChannelDTO;
import com.gitegg.service.extension.mail.dto.UpdateMailChannelDTO;
import com.gitegg.service.extension.mail.entity.MailChannel;
import com.gitegg.service.extension.mail.mapper.MailChannelMapper;
import com.gitegg.service.extension.mail.service.IMailChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 邮件渠道表 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2022-06-24
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MailChannelServiceImpl extends ServiceImpl<MailChannelMapper, MailChannel> implements IMailChannelService {

    private final MailChannelMapper mailChannelMapper;

    private final RedisTemplate redisTemplate;

    /**
     * 是否开启租户模式
     */
    @Value("${tenant.enable}")
    private Boolean enable;

    /**
    * 分页查询邮件渠道表列表
    * @param page
    * @param queryMailChannelDTO
    * @return
    */
    @Override
    public Page<MailChannelDTO> queryMailChannelList(Page<MailChannelDTO> page, QueryMailChannelDTO queryMailChannelDTO) {
        Page<MailChannelDTO> mailChannelInfoList = mailChannelMapper.queryMailChannelList(page, queryMailChannelDTO);
        return mailChannelInfoList;
    }

    /**
    * 查询邮件渠道表列表
    * @param queryMailChannelDTO
    * @return
    */
    @Override
    public List<MailChannelDTO> queryMailChannelList(QueryMailChannelDTO queryMailChannelDTO) {
        List<MailChannelDTO> mailChannelInfoList = mailChannelMapper.queryMailChannelList(queryMailChannelDTO);
        return mailChannelInfoList;
    }

    /**
    * 查询邮件渠道表详情
    * @param queryMailChannelDTO
    * @return
    */
    @Override
    public MailChannelDTO queryMailChannel(QueryMailChannelDTO queryMailChannelDTO) {
        MailChannelDTO mailChannelDTO = mailChannelMapper.queryMailChannel(queryMailChannelDTO);
        return mailChannelDTO;
    }

    /**
    * 创建邮件渠道表
    * @param mailChannel
    * @return
    */
    @Override
    public boolean createMailChannel(CreateMailChannelDTO mailChannel) {
        MailChannel mailChannelEntity = BeanCopierUtils.copyByClass(mailChannel, MailChannel.class);
        try {
            String mailChannelEntityStr = JsonUtils.objToJson(mailChannelEntity);
            mailChannelEntity.setMd5(SecureUtil.md5(mailChannelEntityStr));
        } catch (Exception e) {
            log.error("创建mail配置时，md5加密失败:{}", e);
            throw new BusinessException("创建mail配置时，md5加密失败:" + e);
        }
        boolean result = this.save(mailChannelEntity);
        if (result)
        {
            // 更新到缓存
            MailChannel mailChannelLocal = this.getById(mailChannelEntity.getId());
            MailChannelDTO mailChannelDTO = BeanCopierUtils.copyByClass(mailChannelLocal, MailChannelDTO.class);
            this.addOrUpdateMailChannelCache(mailChannelDTO);
        }
        return result;
    }

    /**
    * 更新邮件渠道表
    * @param mailChannel
    * @return
    */
    @Override
    public boolean updateMailChannel(UpdateMailChannelDTO mailChannel) {
        MailChannel mailChannelEntity = BeanCopierUtils.copyByClass(mailChannel, MailChannel.class);
        try {
            String mailChannelEntityStr = JsonUtils.objToJson(mailChannelEntity);
            mailChannelEntity.setMd5(SecureUtil.md5(mailChannelEntityStr));
        } catch (Exception e) {
            log.error("修改mail配置时，md5加密失败:{}", e);
            throw new BusinessException("修改mail配置时，md5加密失败:" + e);
        }
        boolean result = this.updateById(mailChannelEntity);
        if (result)
        {
            // 更新到缓存
            MailChannel mailChannelLocal = this.getById(mailChannelEntity.getId());
            MailChannelDTO mailChannelDTO = BeanCopierUtils.copyByClass(mailChannelLocal, MailChannelDTO.class);
            this.addOrUpdateMailChannelCache(mailChannelDTO);
        }
        return result;
    }

    /**
    * 删除邮件渠道表
    * @param mailChannelId
    * @return
    */
    @Override
    public boolean deleteMailChannel(Long mailChannelId) {
        // 从缓存删除
        MailChannel mailChannelDelete = this.getById(mailChannelId);
        MailChannelDTO mailChannelDTO = BeanCopierUtils.copyByClass(mailChannelDelete, MailChannelDTO.class);
        this.deleteMailChannelCache(mailChannelDTO);

        boolean result = this.removeById(mailChannelId);
        return result;
    }

    /**
    * 批量删除邮件渠道表
    * @param mailChannelIds
    * @return
    */
    @Override
    public boolean batchDeleteMailChannel(List<Long> mailChannelIds) {
        List<MailChannel> mailChannelDeleteList = this.listByIds(mailChannelIds);
        if (!CollectionUtils.isEmpty(mailChannelDeleteList))
        {
            for(MailChannel mailChannelDelete: mailChannelDeleteList)
            {
                MailChannelDTO mailChannelDTO = BeanCopierUtils.copyByClass(mailChannelDelete, MailChannelDTO.class);
                this.deleteMailChannelCache(mailChannelDTO);
            }
        }
        boolean result = this.removeByIds(mailChannelIds);
        return result;
    }

    /**
     * 初始化配置表列表
     * @return
     */
    @Override
    public void initMailChannelList() {
        QueryMailChannelDTO mailChannelDTO = new QueryMailChannelDTO();
        mailChannelDTO.setChannelStatus(GitEggConstant.ENABLE);
        List<MailChannelDTO> mailChannelInfoList = mailChannelMapper.initMailChannelList(mailChannelDTO);

        List<GitEggMailProperties> channelProperties = mailChannelInfoList.stream()
                .map(channelDTO -> {
                    GitEggMailProperties gitEggMailProperties = BeanCopierUtils.copyByClass(channelDTO, GitEggMailProperties.class);
                    Properties properties = this.loadProperties(channelDTO.getProperties());
                    Map<String, String> map = new HashMap<String, String>((Map) properties);
                    gitEggMailProperties.getProperties().putAll(map);
                    return gitEggMailProperties;
                })
                .collect(Collectors.toList());

        // 判断是否开启了租户模式，如果开启了，那么角色权限需要按租户进行分类存储
        if (enable) {
            Map<Long, List<GitEggMailProperties>> mailChannelListMap =
                    channelProperties.stream().collect(Collectors.groupingBy(GitEggMailProperties::getTenantId));
            mailChannelListMap.forEach((key, value) -> {
                String redisKey = JavaMailConstant.MAIL_TENANT_CONFIG_KEY + key;
                redisTemplate.delete(redisKey);
                addMailChannel(redisKey, value);
            });

        } else {
            redisTemplate.delete(JavaMailConstant.MAIL_CONFIG_KEY);
            addMailChannel(JavaMailConstant.MAIL_CONFIG_KEY, channelProperties);
        }
    }

    private void addMailChannel(String key, List<GitEggMailProperties> mailChannelList) {
        Map<String, String> mailChannelMap = new TreeMap<>();
        Optional.ofNullable(mailChannelList).orElse(new ArrayList<>()).forEach(channel -> {
            try {
                mailChannelMap.put(channel.getChannelCode(), JsonUtils.objToJson(channel));
                redisTemplate.opsForHash().putAll(key, mailChannelMap);
            } catch (Exception e) {
                log.error("初始化邮件配置失败：{}" , e);
            }
        });
    }

    private void addOrUpdateMailChannelCache(MailChannelDTO mailChannelDTO) {
        try {
            GitEggMailProperties gitEggMailProperties = BeanCopierUtils.copyByClass(mailChannelDTO, GitEggMailProperties.class);

            Properties properties = this.loadProperties(mailChannelDTO.getProperties());
            Map<String, String> map = new HashMap<String, String>((Map) properties);
            gitEggMailProperties.getProperties().putAll(map);

            String redisKey = JavaMailConstant.MAIL_CONFIG_KEY;
            if (enable) {
                redisKey = JavaMailConstant.MAIL_TENANT_CONFIG_KEY + gitEggMailProperties.getTenantId();
            }
            redisTemplate.opsForHash().put(redisKey, gitEggMailProperties.getChannelCode(), JsonUtils.objToJson(gitEggMailProperties));
        } catch (Exception e) {
            log.error("初始化邮件配置失败：{}" , e);
        }
    }

    private void deleteMailChannelCache(MailChannelDTO mailChannelDTO) {
        try {
            GitEggMailProperties gitEggMailProperties = BeanCopierUtils.copyByClass(mailChannelDTO, GitEggMailProperties.class);
            String redisKey = JavaMailConstant.MAIL_CONFIG_KEY;
            if (enable) {
                redisKey = JavaMailConstant.MAIL_TENANT_CONFIG_KEY + gitEggMailProperties.getTenantId();
            }
            redisTemplate.opsForHash().delete(redisKey, gitEggMailProperties.getChannelCode(), JsonUtils.objToJson(gitEggMailProperties));
        } catch (Exception e) {
            log.error("初始化邮件配置失败：{}" , e);
        }
    }



    private Properties loadProperties(String propertiesString) {
        if (StringUtils.isEmpty(propertiesString))
        {
            return null;
        }
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(propertiesString));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
