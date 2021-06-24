package com.gitegg.service.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.service.base.dto.DictDTO;
import com.gitegg.service.base.dto.QueryDictDTO;
import com.gitegg.service.base.entity.Dict;

/**
 * <p>
 * 数据字典表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2018-10-28
 */
public interface DictMapper extends BaseMapper<Dict> {

    /**
     * 分页查询数据字典
     *
     * @param page
     * @param dict
     * @return dictInfo
     */
    Page<DictDTO> selectDictList(Page<DictDTO> page, @Param("dict") QueryDictDTO dict);

    /**
     * 查询字典列表
     * 
     * @param parentId
     * @return
     */
    List<DictDTO> queryDictTreeProc(Integer parentId);
}
