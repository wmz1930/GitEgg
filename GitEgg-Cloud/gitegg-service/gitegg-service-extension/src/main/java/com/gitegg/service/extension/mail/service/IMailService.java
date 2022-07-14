package com.gitegg.service.extension.mail.service;

import com.gitegg.service.extension.mail.dto.SendSimpleMailDTO;
import org.springframework.scheduling.annotation.Async;

/**
 * @author GitEgg
 * @date 2022/6/25
 */
public interface IMailService {

    /**
     * 发送模板邮件
     * @param sendSimpleMailDTO
     */
    void sendTemplateMail(SendSimpleMailDTO sendSimpleMailDTO);

    /**
     * 发送普通邮件，不设置from，默认取系统账号邮箱
     * @param subject
     * @param to
     * @param content
     * @param htmlFlag
     */
    void sendSimpleMail(String subject, String to, String content, boolean htmlFlag, String... channelCode);

    /**
     * 批量异步发送普通邮件，不设置from，默认取系统账号邮箱
     * @param subject
     * @param to
     * @param content
     * @param htmlFlag
     */
    void sendAsyncSimpleMail(String subject, String to, String content, boolean htmlFlag, String... channelCode);

    /**
     * 批量发送普通邮件，不设置from，默认取系统账号邮箱
     * @param subject
     * @param to
     * @param content
     * @param htmlFlag
     */
    void sendSimpleMail(String subject, String[] to, String content, boolean htmlFlag, String... channelCode);

    /**
     * 发送普通邮件， 设置from
     * @param subject
     * @param from
     * @param to
     * @param content
     * @param htmlFlag
     */
    void sendSimpleMail(String subject, String from, String to, String content, boolean htmlFlag, String... channelCode);

    /**
     * 批量发送异步普通邮件，设置from
     * @param subject
     * @param from
     * @param to
     * @param content
     * @param htmlFlag
     */
    void sendAsyncSimpleMail(String subject, String from, String[] to, String content, boolean htmlFlag, String... channelCode);

    /**
     * 批量发送普通邮件，设置from
     * @param subject
     * @param from
     * @param to
     * @param content
     * @param htmlFlag
     * @param channelCode 邮件渠道
     */
    void sendSimpleMail(String subject, String from, String[] to, String content, boolean htmlFlag, String... channelCode);

    /**
     * 发送异步带附件的邮件 ，这里以字节流的方式发送，实际复杂场景可自行扩展。
     * @param sendSimpleMailDTO
     */
    @Async("AsyncSendMailTaskExecutor")
    void sendAsyncAttachmentMail(SendSimpleMailDTO sendSimpleMailDTO);

    /**
     * 发送带附件的邮件 ，这里以字节流的方式发送，实际复杂场景可自行扩展。
     * @param sendSimpleMailDTO
     */
    void sendAttachmentMail(SendSimpleMailDTO sendSimpleMailDTO);
}
