package com.gitegg.service.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitegg.service.base.entity.District;
import org.springframework.cache.annotation.Cacheable;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2018-05-26
 */
public interface IDistrictService extends IService<District> {
    
    /**
     * 通过code查询区域名称
     * @param districtCode
     * @return
     */
    @Cacheable
    District queryDistrict(String districtCode);
}
