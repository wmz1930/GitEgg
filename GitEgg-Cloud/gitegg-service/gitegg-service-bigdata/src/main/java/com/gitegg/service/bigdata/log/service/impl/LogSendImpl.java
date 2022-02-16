package com.gitegg.service.bigdata.log.service.impl;

import com.gitegg.service.bigdata.log.service.ILogSendService;
import com.gitegg.service.bigdata.source.LogSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author GitEgg
 */
@EnableBinding(value = { LogSource.class })
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LogSendImpl implements ILogSendService {

    private final LogSource logSource;

    @Override
    public void sendOperationLog() {
        log.info("推送操作日志-------开始------");
        logSource.outputOperationLog()
                .send(MessageBuilder.withPayload(UUID.randomUUID().toString()).build());
        log.info("推送操作日志-------结束------");
    }

    @Override
    public void sendApiLog() {
        log.info("推送API日志-------开始------");
        logSource.outputApiLog()
                .send(MessageBuilder.withPayload(UUID.randomUUID().toString()).build());
        log.info("推送API日志-------结束------");
    }
}
