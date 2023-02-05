package com.gitegg.code.generator.dict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.code.generator.dict.dto.GeneratorDictDTO;
import com.gitegg.code.generator.dict.dto.QueryGeneratorDictDTO;
import com.gitegg.code.generator.dict.entity.GeneratorDict;
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
public interface GeneratorDictMapper extends BaseMapper<GeneratorDict> {

    /**
     * 分页查询数据字典
     *
     * @param page
     * @param dict
     * @return dictInfo
     */
    Page<GeneratorDictDTO> selectGeneratorDictList(Page<GeneratorDictDTO> page, @Param("dict") QueryGeneratorDictDTO dict);

    /**
     * 查询数据字典
     *
     * @param dict
     * @return dictInfo
     */
    List<GeneratorDictDTO> selectGeneratorDictList(@Param("dict") QueryGeneratorDictDTO dict);

    /**
     * 查询字典树列表
     * 
     * @param dict
     * @return
     */
    List<GeneratorDict> selectGeneratorDictChildren(@Param("dict") QueryGeneratorDictDTO dict);
}
