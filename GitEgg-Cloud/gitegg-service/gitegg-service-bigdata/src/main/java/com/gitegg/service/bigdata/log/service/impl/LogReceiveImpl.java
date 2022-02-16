package com.gitegg.service.bigdata.log.service.impl;

import com.gitegg.service.bigdata.log.service.ILogReceiveService;
import com.gitegg.service.bigdata.sink.LogSink;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

/**
 * @author GitEgg
 */
@Slf4j
@Component
@EnableBinding(value = { LogSink.class })
public class LogReceiveImpl implements ILogReceiveService {

    @StreamListener(LogSink.INPUT_OPERATION_LOG)
    @Override
    public synchronized <T> void receiveOperationLog(GenericMessage<T> msg) {
        log.info("接收到操作日志: " + msg.getPayload());
    }

    @StreamListener(LogSink.INPUT_API_LOG)
    @Override
    public synchronized <T> void receiveApiLog(GenericMessage<T> msg) {
        log.info("接收到API日志: " + msg.getPayload());
    }
}
