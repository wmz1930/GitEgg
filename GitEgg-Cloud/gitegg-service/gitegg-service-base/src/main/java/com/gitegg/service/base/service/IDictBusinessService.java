package com.gitegg.service.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitegg.service.base.dto.DictBusinessDTO;
import com.gitegg.service.base.dto.DictDTO;
import com.gitegg.service.base.dto.QueryDictBusinessDTO;
import com.gitegg.service.base.dto.QueryDictDTO;
import com.gitegg.service.base.entity.Dict;
import com.gitegg.service.base.entity.DictBusiness;

import java.util.List;

/**
 * <p>
 * 数据字典表 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2018-10-28
 */
public interface IDictBusinessService extends IService<DictBusiness> {

    /**
     * 分页查询数据字典
     *
     * @param dictBusiness
     * @param page
     * @return
     */
    Page<DictBusinessDTO> selectDictBusinessList(Page<DictBusinessDTO> page, QueryDictBusinessDTO dictBusiness);

    /**
     * 分页查询数据字典
     *
     * @param dictBusiness
     * @return
     */
    List<DictBusinessDTO> selectDictBusinessList(QueryDictBusinessDTO dictBusiness);

    /**
     * 创建数据字典
     * 
     * @param dictBusiness
     * @param
     * @return
     */
    boolean createDictBusiness(DictBusiness dictBusiness);

    /**
     * 更新数据字典
     * 
     * @param dictBusiness
     * @param
     * @return
     */
    boolean updateDictBusiness(DictBusiness dictBusiness);

    /**
     * 删除数据字典
     * 
     * @param dictBusinessId
     * @return
     */
    boolean deleteDictBusiness(Long dictBusinessId);

    /**
     * 批量删除数据字典
     *
     * @param dictBusinessIds
     * @return
     */
    boolean batchDeleteDictBusiness(List<Long> dictBusinessIds);

    /**
     * 查询字典列表值
     * 
     * @param dictBusinessCode
     * @return
     */
    List<DictBusiness> queryDictBusinessListByParentCode(String dictBusinessCode);

    /**
     * 查询字典列表树
     * 
     * @param parentId
     * @return
     */
    List<DictBusiness> queryDictBusinessTreeByParentId(Long parentId);
    
    /**
     * 排除多租户，查询初始化业务数据字典
     * @param dict
     * @return
     */
    List<DictBusinessDTO> initDictBusinessList(QueryDictBusinessDTO dict);
    
    /**
     * 初始化业务数据词典配置
     * @return
     */
    void initDictBusinessConfig();
}
