package com.gitegg.service.bigdata.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 自定义Stream输出通道
 * @author GitEgg
 */
public interface LogSource {

    String OUTPUT_OPERATION_LOG = "input_operation_log";

    String OUTPUT_API_LOG = "input_api_log";

    /**
     * 操作日志自定义输出通道
     * @return
     */
    @Output(OUTPUT_OPERATION_LOG)
    MessageChannel outputOperationLog();

    /**
     * API日志自定义输出通道
     * @return
     */
    @Output(OUTPUT_API_LOG)
    MessageChannel outputApiLog();

}
