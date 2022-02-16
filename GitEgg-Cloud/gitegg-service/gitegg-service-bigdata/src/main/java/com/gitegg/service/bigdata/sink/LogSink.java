package com.gitegg.service.bigdata.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author GitEgg
 */
public interface LogSink {

    String INPUT_OPERATION_LOG = "output_operation_log";

    String INPUT_API_LOG = "output_api_log";

    /**
     * 操作日志自定义输入通道
     * @return
     */
    @Input(INPUT_OPERATION_LOG)
    SubscribableChannel inputOperationLog();

    /**
     * API日志自定义输入通道
     * @return
     */
    @Input(INPUT_API_LOG)
    SubscribableChannel inputApiLog();
}
