package com.gitegg.service.base.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.service.base.dto.LogDTO;
import com.gitegg.service.base.dto.QueryLogDTO;
import com.gitegg.service.base.entity.Log;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2018-10-24
 */
public interface LogMapper extends BaseMapper<Log> {
    /**
     * 分页查询操作日志
     * 
     * @param page
     * @param log
     * @return logInfo
     */
    Page<LogDTO> selectLogList(Page<LogDTO> page, @Param("log") QueryLogDTO log);
}
