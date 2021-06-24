package com.gitegg.service.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.service.base.dto.LogDTO;
import com.gitegg.service.base.dto.QueryLogDTO;
import com.gitegg.service.base.entity.Log;
import com.gitegg.service.base.mapper.LogMapper;
import com.gitegg.service.base.service.ILogService;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2018-10-24
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

    private final LogMapper logMapper;

    @Override
    public Page<LogDTO> selectLogList(Page<LogDTO> page, QueryLogDTO log) {
        Page<LogDTO> pageLogInfo = logMapper.selectLogList(page, log);
        return pageLogInfo;
    }
}
