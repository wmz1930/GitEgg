package com.gitegg.service.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.service.base.dto.DictBusinessDTO;
import com.gitegg.service.base.dto.QueryDictBusinessDTO;
import com.gitegg.service.base.entity.DictBusiness;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数据字典表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2018-10-28
 */
public interface DictBusinessMapper extends BaseMapper<DictBusiness> {

    /**
     * 分页查询数据字典
     *
     * @param page
     * @param dict
     * @return dictInfo
     */
    Page<DictBusinessDTO> selectDictBusinessList(Page<DictBusinessDTO> page, @Param("dict") QueryDictBusinessDTO dict);

    /**
     * 查询数据字典
     *
     * @param dict
     * @return dictInfo
     */
    List<DictBusinessDTO> selectDictBusinessList( @Param("dict") QueryDictBusinessDTO dict);

    /**
     * 查询字典树列表
     * 
     * @param dict
     * @return
     */
    List<DictBusiness> selectDictBusinessChildren(@Param("dict") QueryDictBusinessDTO dict);
}
