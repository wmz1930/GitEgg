package com.gitegg.service.extension.mail.controller;

/**
 * 邮件发送测试
 * @author GitEgg
 * @date 2022/6/28
 */

import com.gitegg.platform.base.result.Result;
import com.gitegg.service.extension.mail.dto.SendSimpleMailDTO;
import com.gitegg.service.extension.mail.service.IMailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.DependsOn;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 邮件渠道表 前端控制器
 * </p>
 *
 * @author GitEgg
 * @since 2022-06-24
 */
@RestController
@RequestMapping("/extension/mail/test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "MailChannelController|邮件渠道表前端控制器")
@RefreshScope
@ConditionalOnProperty(prefix = "spring.mail", name = "host")
public class MailTestController {

    private final IMailService mailService;

    /**
     * 测试发送简单邮件
     *
     * @param mailSendDTO
     * @return
     */
    @PostMapping("/send/simple")
    @ApiOperation(value = "测试发送简单邮件")
    public Result<?> sendSimpleMailTest(@RequestBody SendSimpleMailDTO mailSendDTO) {
        mailService.sendAsyncSimpleMail(mailSendDTO.getMailSubject(), mailSendDTO.getMailTo(), mailSendDTO.getMailContent(), false, mailSendDTO.getChannelCode());
        return Result.success();
    }

    /**
     * 测试发送Html邮件
     *
     * @param mailSendDTO
     * @return
     */
    @PostMapping("/send/html")
    @ApiOperation(value = "测试发送Html邮件")
    public Result<?> sendHtmlMailTest(@RequestBody SendSimpleMailDTO mailSendDTO) {
        String htmlContent = "<span style=\"color:red;\">这是html邮件内容，查看此段文字是否显示红色:</span>。";
        mailSendDTO.setMailContent(htmlContent);
        mailService.sendSimpleMail(mailSendDTO.getMailSubject(), mailSendDTO.getMailTo(), mailSendDTO.getMailContent(), true, mailSendDTO.getChannelCode());
        return Result.success();
    }

    /**
     * 测试发送模板邮件
     *
     * @param mailSendDTO
     * @return
     */
    @PostMapping("/send/template")
    @ApiOperation(value = "测试发送模板邮件")
    public Result<?> sendTemplateMailTest(@RequestBody SendSimpleMailDTO mailSendDTO) {
        mailSendDTO.setTemplateCode("register_code");
        Map<String,String> templateKeyValue = new HashMap<>();
        templateKeyValue.put("code", "测试Code");
        mailSendDTO.setTemplateKeyValue(templateKeyValue);
        mailService.sendTemplateMail(mailSendDTO);
        return Result.success();
    }

    /**
     * 测试发送带附件邮件
     *
     * @param mailSendDTO
     * @return
     */
    @PostMapping("/send/attachment")
    @ApiOperation(value = "测试发送带附件邮件")
    public Result<?> sendAttachmentMailTest(@RequestBody SendSimpleMailDTO mailSendDTO) {
        mailSendDTO.setAttachment("测试附件内容");
        mailService.sendAttachmentMail(mailSendDTO);
        return Result.success();
    }


    /**
     * 测试异步发送带附件邮件
     *
     * @param mailSendDTO
     * @return
     */
    @PostMapping("/send/async/attachment")
    @ApiOperation(value = "测试异步发送带附件邮件")
    public Result<?> sendAsyncAttachmentMailTest(@RequestBody SendSimpleMailDTO mailSendDTO) {
        mailSendDTO.setAttachment("测试附件内容");
        mailService.sendAsyncAttachmentMail(mailSendDTO);
        return Result.success();
    }

}
