package com.gitegg.service.bigdata.log.service;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.messaging.support.GenericMessage;

/**
 * @author GitEgg
 */
public interface ILogReceiveService {

    /**
     * 接收到操作日志消息
     * @param msg
     */
    <T> void receiveOperationLog(GenericMessage<T> msg);

    /**
     * 接收到API日志消息
     * @param msg
     */
    <T> void receiveApiLog(GenericMessage<T> msg);
}
