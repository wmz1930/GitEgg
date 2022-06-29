package com.gitegg.service.extension.mail.domain;

import lombok.Data;
import org.springframework.core.io.InputStreamSource;

import java.io.File;

/**
 * @author GitEgg
 * @date 2022/6/25
 */
@Data
public class SendMailRequest {

    private String to;

    private String[] toList;

    private String cc;

    private String[] ccList;

    private String bcc;

    private String[] bccList;

    private String from;

    private String subject;

    private String text;

    private String attachmentFilename;

    private File file;

    private InputStreamSource inputStreamSource;

    private String contentType;
}
