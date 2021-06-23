package com.gitegg.service.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.platform.boot.common.exception.BusinessException;
import com.gitegg.service.system.entity.SystemTable;
import com.gitegg.service.system.mapper.SystemTableMapper;
import com.gitegg.service.system.service.ISystemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
@AllArgsConstructor
public class SystemServiceImpl implements ISystemService {

    private final SystemTableMapper systemTableMapper;

    @Override
    public List<SystemTable> list() {
        return systemTableMapper.list();
    }

    @Override
    public Page<SystemTable> page() {
        Page<SystemTable> page = new Page<>(1, 10);
        List<SystemTable> records = systemTableMapper.page(page);
        page.setRecords(records);
        return page;
    }

    @Override
    public String exception() {
        throw new BusinessException("自定义异常");
    }
}
