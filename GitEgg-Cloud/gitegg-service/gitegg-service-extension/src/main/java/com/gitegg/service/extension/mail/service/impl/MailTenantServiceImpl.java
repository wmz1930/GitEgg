package com.gitegg.service.extension.mail.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.email.enums.MailResultCodeEnum;
import com.gitegg.platform.email.factory.JavaMailSenderFactory;
import com.gitegg.platform.email.impl.GitEggJavaMailSenderImpl;
import com.gitegg.service.extension.mail.dto.CreateMailLogDTO;
import com.gitegg.service.extension.mail.dto.SendSimpleMailDTO;
import com.gitegg.service.extension.mail.entity.MailTemplate;
import com.gitegg.service.extension.mail.service.IMailLogService;
import com.gitegg.service.extension.mail.service.IMailService;
import com.gitegg.service.extension.mail.service.IMailTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 只有开启多租户时，才使用此邮件发送方法，如果没有开启多租户，请使用原生的springboot mail邮件发送方法
 * 这里只考虑系统发送通知邮件等基本常用接口，不是复杂的邮件系统，其他高级功能请自己定制
 * @author GitEgg
 * @date 2022/6/25
 */
@Slf4j
@Service
@EnableAsync
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ConditionalOnProperty( value = {"spring.mail.tenant", "tenant.enable"}, havingValue = "true")
public class MailTenantServiceImpl implements IMailService {

    private final JavaMailSenderFactory javaMailSenderFactory;

    /**
     * 邮件发送记录日志
     */
    private final IMailLogService mailLogService;

    /**
     * 查询模板
     */
    private final IMailTemplateService mailTemplateService;

    /**
     * 发送简单模板邮件 ，最简单的应用场景就是发送注册验证码、找回密码链接、邮箱验证等。
     * @param sendSimpleMailDTO
     */
    @Override
    public void sendTemplateMail(SendSimpleMailDTO sendSimpleMailDTO) {
        LambdaQueryWrapper<MailTemplate> templateQueryWrapper = new LambdaQueryWrapper();
        templateQueryWrapper.eq(MailTemplate::getTemplateCode, sendSimpleMailDTO.getTemplateCode());
        MailTemplate mailTemplate = mailTemplateService.getOne(templateQueryWrapper);
        String content = StrUtil.format( mailTemplate.getTemplateContent(), sendSimpleMailDTO.getTemplateKeyValue());
        this.sendSimpleMail(sendSimpleMailDTO.getMailSubject(), null, new String[]{sendSimpleMailDTO.getMailTo()}, content, sendSimpleMailDTO.getHtmlFlag());
    }

    /**
     * 发送普通邮件，不设置from，默认取系统账号邮箱
     * @param subject
     * @param to
     * @param content
     */
    @Override
    public void sendSimpleMail(String subject, String to, String content, boolean htmlFlag, String... channelCode) {
        this.sendSimpleMail(subject, null, new String[]{to}, content, htmlFlag, channelCode);
    }

    /**
     * 批量异步发送普通邮件，不设置from，默认取系统账号邮箱
     * @param subject
     * @param to
     * @param content
     */
    @Async("mailTaskExecutor")
    @Override
    public void sendAsyncSimpleMail(String subject, String to, String content, boolean htmlFlag, String... channelCode) {
        this.sendSimpleMail(subject, null, to, content, htmlFlag, channelCode);
    }

    /**
     * 批量发送普通邮件，不设置from，默认取系统账号邮箱
     * @param subject
     * @param to
     * @param content
     */
    @Override
    public void sendSimpleMail(String subject, String[] to, String content, boolean htmlFlag, String... channelCode) {
        this.sendSimpleMail(subject, null, to, content, htmlFlag, channelCode);
    }

    /**
     * 发送普通邮件， 设置from
     * @param subject
     * @param from
     * @param to
     * @param content
     */
    @Override
    public void sendSimpleMail(String subject, String from, String to, String content, boolean htmlFlag, String... channelCode) {
        this.sendSimpleMail(subject, from, new String[]{to}, content, htmlFlag, channelCode);

    }

    /**
     * 批量发送异步普通邮件，设置from
     * @param subject
     * @param from
     * @param to
     * @param content
     */
    @Async("mailTaskExecutor")
    @Override
    public void sendAsyncSimpleMail(String subject, String from, String[] to, String content, boolean htmlFlag, String... channelCode) {
        this.sendSimpleMail(subject, from, to, content, htmlFlag, channelCode);

    }

    /**
     * 批量发送普通邮件，设置from
     * @param subject
     * @param from
     * @param to
     * @param content
     * @param channelCode 通过可变参数来设置是否需要选择邮件渠道
     */
    @Override
    public void sendSimpleMail(String subject, String from, String[] to, String content, boolean htmlFlag, String... channelCode) {
        CreateMailLogDTO mailLog = new CreateMailLogDTO();
        try {
            JavaMailSenderImpl javaMailSender = javaMailSenderFactory.getMailSender(channelCode);
            if (StringUtils.isEmpty(from))
            {
                from = javaMailSender.getUsername();
            }
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, htmlFlag);
            javaMailSender.send(message);

            //设置日志记录参数，这里没有使用通用的切面日志，因为单独为mail设置的发送记录存储表，且没有必要单独再做切面日志
            mailLog.setMailFrom(from);
            mailLog.setChannelId(((GitEggJavaMailSenderImpl)javaMailSender).getId());
            mailLog.setMailTo(Arrays.toString(to));
            mailLog.setMailSubject(subject);
            mailLog.setMailContent(content);
            mailLog.setSendTime(LocalDateTime.now());
            mailLog.setSendResultCode(MailResultCodeEnum.SUCCESS.code);
            mailLog.setSendResultMsg(MailResultCodeEnum.SUCCESS.message);
        } catch (Exception e) {
            log.error("邮件发送异常:{}", e);
            mailLog.setSendResultCode(MailResultCodeEnum.ERROR.code);
            mailLog.setSendResultMsg(String.valueOf(e));
            throw new BusinessException("邮件发送异常:", e);
        } finally {
            // 日志记录异常，不影响正常业务逻辑
            try {
                mailLogService.createMailLog(mailLog);
            }catch (Exception e)
            {
                log.error("记录邮件发送日志异常，邮件主题:{}，异常信息:{}", mailLog.getMailSubject(), e);
            }
        }

    }

    /**
     * 异步发送带附件的邮件 ，这里以字节流的方式发送，实际复杂场景可自行扩展。
     * @param sendSimpleMailDTO
     */
    @Async("mailTaskExecutor")
    @Override
    public void sendAsyncAttachmentMail(SendSimpleMailDTO sendSimpleMailDTO) {
        this.sendAttachmentMail(sendSimpleMailDTO);
    }

    /**
     * 发送带附件的邮件 ，这里以字节流的方式发送，实际复杂场景可自行扩展。
     * @param sendSimpleMailDTO
     */
    @Override
    public void sendAttachmentMail(SendSimpleMailDTO sendSimpleMailDTO) {
        CreateMailLogDTO mailLog = new CreateMailLogDTO();
        try {
            JavaMailSenderImpl javaMailSender = javaMailSenderFactory.getMailSender(sendSimpleMailDTO.getChannelCode());
            String from = sendSimpleMailDTO.getMailFrom();
            if (StringUtils.isEmpty(from))
            {
                from = javaMailSender.getUsername();
            }
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(sendSimpleMailDTO.getMailTo());
            helper.setSubject(sendSimpleMailDTO.getMailSubject());
            helper.setText(sendSimpleMailDTO.getMailContent(), sendSimpleMailDTO.getHtmlFlag());

            // 将字符串转换为字节流作为附件发送
            byte[] byteArray =  ((String)sendSimpleMailDTO.getAttachment()).getBytes();
            if (byteArray != null){
                ByteArrayResource attachmentByte = new ByteArrayResource(byteArray);
                helper.addAttachment(sendSimpleMailDTO.getAttachmentName(), attachmentByte);
            }
            javaMailSender.send(message);
            //设置日志记录参数，这里没有使用通用的切面日志，因为单独为mail设置的发送记录存储表，且没有必要单独再做切面日志
            mailLog.setMailFrom(from);
            mailLog.setChannelId(((GitEggJavaMailSenderImpl)javaMailSender).getId());
            mailLog.setMailTo(sendSimpleMailDTO.getMailTo());
            mailLog.setMailSubject(sendSimpleMailDTO.getMailSubject());
            mailLog.setMailContent(sendSimpleMailDTO.getMailContent());
            mailLog.setSendTime(LocalDateTime.now());
            mailLog.setSendResultCode(MailResultCodeEnum.SUCCESS.code);
            mailLog.setSendResultMsg(MailResultCodeEnum.SUCCESS.message);
        } catch (Exception e) {
            log.error("邮件发送异常:{}", e);
            mailLog.setSendResultCode(MailResultCodeEnum.ERROR.code);
            mailLog.setSendResultMsg(String.valueOf(e));
            throw new BusinessException("邮件发送异常:", e);
        } finally {
            // 日志记录异常，不影响正常业务逻辑
            try {
                mailLogService.createMailLog(mailLog);
            }catch (Exception e)
            {
                log.error("记录邮件发送日志异常，邮件主题:{}，异常信息:{}", mailLog.getMailSubject(), e);
            }
        }
    }
}
