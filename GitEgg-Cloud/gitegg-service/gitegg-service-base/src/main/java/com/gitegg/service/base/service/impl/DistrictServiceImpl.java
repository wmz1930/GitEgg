package com.gitegg.service.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.service.base.entity.District;
import com.gitegg.service.base.mapper.DistrictMapper;
import com.gitegg.service.base.service.IDistrictService;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2018-05-26
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class DistrictServiceImpl extends ServiceImpl<DistrictMapper, District> implements IDistrictService {}
