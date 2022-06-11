package com.gitegg.service.system.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 定时任务示例代码，其他更多示例请查看
 * https://www.xuxueli.com/xxl-job
 * @author GitEgg
 */
@Slf4j
@Component
public class SystemJobHandler {
    
    /**
     * 1、简单任务示例（Bean模式）不带返回值
     */
    @XxlJob("systemJobHandler")
    public void systemJobHandler() throws Exception {
        
        XxlJobHelper.log("不带返回值：XXL-JOB, Hello World.");
        for (int i = 0; i < 5; i++) {
            XxlJobHelper.log("beat at:" + i);
            TimeUnit.SECONDS.sleep(2);
        }
    }
    
    /**
     * 2、简单任务示例（Bean模式）带成功或失败返回值
     */
    @XxlJob("userJobHandler")
    public ReturnT<String> userJobHandler() throws Exception {
        
        XxlJobHelper.log("带返回值：XXL-JOB, Hello World.");
        for (int i = 0; i < 5; i++) {
            XxlJobHelper.log("beat at:" + i);
            TimeUnit.SECONDS.sleep(2);
        }
        return ReturnT.SUCCESS;
    }

}
