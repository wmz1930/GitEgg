package com.gitegg.service.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.service.system.entity.SystemTable;

import java.util.List;

public interface ISystemService {

    List<SystemTable> list();

    Page<SystemTable> page();

    String exception();
}
