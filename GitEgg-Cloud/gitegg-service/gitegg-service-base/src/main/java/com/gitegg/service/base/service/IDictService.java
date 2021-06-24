package com.gitegg.service.base.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitegg.service.base.dto.DictDTO;
import com.gitegg.service.base.dto.QueryDictDTO;
import com.gitegg.service.base.entity.Dict;

/**
 * <p>
 * 数据字典表 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2018-10-28
 */
public interface IDictService extends IService<Dict> {

    /**
     * 分页查询数据字典
     *
     * @param dict
     * @param
     * @return
     */
    Page<DictDTO> selectDictList(Page<DictDTO> page, QueryDictDTO dict);

    /**
     * 创建数据字典
     * 
     * @param dict
     * @param
     * @return
     */
    boolean createDict(Dict dict);

    /**
     * 更新数据字典
     * 
     * @param dict
     * @param
     * @return
     */
    boolean updateDict(Dict dict);

    /**
     * 删除数据字典
     * 
     * @param dictId
     * @return
     */
    boolean deleteDict(Long dictId);

    /**
     * 批量删除数据字典
     *
     * @param dictIds
     * @return
     */
    boolean batchDeleteDict(List<Long> dictIds);

    /**
     * 查询字典列表值
     * 
     * @param dictCode
     * @return
     */
    List<Dict> queryDictListByPanentCode(String dictCode);

    /**
     * 查询字典列表树
     * 
     * @param parentId
     * @return
     */
    List<DictDTO> queryDictTreeByPanentId(Integer parentId);
}
