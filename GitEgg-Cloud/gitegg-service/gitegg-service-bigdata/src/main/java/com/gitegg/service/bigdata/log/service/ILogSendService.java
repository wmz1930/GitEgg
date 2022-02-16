package com.gitegg.service.bigdata.log.service;

/**
 * @author GitEgg
 */
public interface ILogSendService {

    /**
     * 发送操作日志消息
     * @return
     */
    void sendOperationLog();

    /**
     * 发送api日志消息
     * @return
     */
    void sendApiLog();
}
