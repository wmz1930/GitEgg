package com.gitegg.code.generator.dict.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitegg.code.generator.dict.dto.GeneratorDictDTO;
import com.gitegg.code.generator.dict.dto.QueryGeneratorDictDTO;
import com.gitegg.code.generator.dict.entity.GeneratorDict;

import java.util.List;

/**
 * <p>
 * 数据字典表 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2018-10-28
 */
public interface IGeneratorDictService extends IService<GeneratorDict> {

    /**
     * 分页查询数据字典
     *
     * @param generatorDict
     * @param page
     * @return
     */
    Page<GeneratorDictDTO> selectGeneratorDictList(Page<GeneratorDictDTO> page, QueryGeneratorDictDTO generatorDict);

    /**
     * 分页查询数据字典
     *
     * @param generatorDict
     * @return
     */
    List<GeneratorDictDTO> selectGeneratorDictList(QueryGeneratorDictDTO generatorDict);

    /**
     * 创建数据字典
     * 
     * @param generatorDict
     * @param
     * @return
     */
    boolean createGeneratorDict(GeneratorDict generatorDict);

    /**
     * 更新数据字典
     * 
     * @param generatorDict
     * @param
     * @return
     */
    boolean updateGeneratorDict(GeneratorDict generatorDict);

    /**
     * 删除数据字典
     * 
     * @param generatorDictId
     * @return
     */
    boolean deleteGeneratorDict(Long generatorDictId);

    /**
     * 批量删除数据字典
     *
     * @param generatorDictIds
     * @return
     */
    boolean batchDeleteGeneratorDict(List<Long> generatorDictIds);

    /**
     * 查询字典列表值
     * 
     * @param generatorDictCode
     * @return
     */
    List<GeneratorDict> queryGeneratorDictListByParentCode(String generatorDictCode);

    /**
     * 查询字典列表树
     * 
     * @param parentId
     * @return
     */
    List<GeneratorDict> queryGeneratorDictTreeByParentId(Long parentId);
}
