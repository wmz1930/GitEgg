package com.gitegg.service.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitegg.service.base.dto.LogDTO;
import com.gitegg.service.base.dto.QueryLogDTO;
import com.gitegg.service.base.entity.Log;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2018-10-24
 */
public interface ILogService extends IService<Log> {

    /**
     * 查询日志列表
     * 
     * @param page
     * @param log
     * @return
     */
    Page<LogDTO> selectLogList(Page<LogDTO> page, QueryLogDTO log);

}
