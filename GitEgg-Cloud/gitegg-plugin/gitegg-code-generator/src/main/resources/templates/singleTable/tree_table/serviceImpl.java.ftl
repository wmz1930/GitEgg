package ${package.ServiceImpl};

<#if config?? && config.queryReuse == true>
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.util.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.exception.BusinessException;
</#if>
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;

import com.gitegg.platform.base.util.BeanCopierUtils;
<#assign dtoPackage="${package.Entity}"/>
import ${dtoPackage?replace("entity","dto")}.${entity}DTO;
import ${dtoPackage?replace("entity","dto")}.Create${entity}DTO;
import ${dtoPackage?replace("entity","dto")}.Update${entity}DTO;
import ${dtoPackage?replace("entity","dto")}.Query${entity}DTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    private final ${entity}Mapper ${table.entityPath}Mapper;

     /**
     * query${entity}Tree
     * 
     * @Title: query${entity}List
     * @Description: 查询${table.comment!}树
     * @param query${entity}DTO
     * @return List<${entity}>
     */
    @Override
    public List<${entity}DTO> query${entity}Tree(Query${entity}DTO query${entity}DTO) {
        List<${entity}DTO> ${table.entityPath}s;
        try {
            if (null == query${entity}DTO.getParentId()) {
                query${entity}DTO.setParentId(GitEggConstant.PARENT_ID);
            }
            List<${entity}DTO> ${table.entityPath}List = ${table.entityPath}Mapper.select${entity}Children(query${entity}DTO);
            Map<Long, ${entity}DTO> ${table.entityPath}Map = new HashMap<>();
            ${table.entityPath}s = this.assemble${entity}Tree(${table.entityPath}List, ${table.entityPath}Map);
        } catch (Exception e) {
            log.error("查询${table.comment!}失败:", e);
            throw new BusinessException("查询${table.comment!}失败");
        }
        return ${table.entityPath}s;
    }

    /**
    * 分页查询${table.comment!}列表
    * @param page
    * @param query${entity}DTO
    * @return
    */
    @Override
    public Page<${entity}DTO> query${entity}List(Page<${entity}DTO> page, Query${entity}DTO query${entity}DTO) {
        Page<${entity}DTO> ${table.entityPath}InfoList = ${table.entityPath}Mapper.query${entity}List(page, query${entity}DTO);
        return ${table.entityPath}InfoList;
    }


    /**
    * query${entity}List
    * 
    * @Title: query${entity}List
    * @Description: 查询所有的${table.comment!}树
    * @param query${entity}DTO
    * @return List<${entity}>
    */
    @Override
    public List<${entity}DTO> query${entity}List(Query${entity}DTO query${entity}DTO) {
        List<${entity}DTO> ${table.entityPath}List;
        try {
            if (null == query${entity}DTO.getParentId()) {
               query${entity}DTO.setParentId(GitEggConstant.PARENT_ID);
            }
            ${table.entityPath}List = ${table.entityPath}Mapper.select${entity}Children(query${entity}DTO);
        } catch (Exception e) {
            log.error("查询${table.comment!}失败:", e);
            throw new BusinessException("查询${table.comment!}失败");
        }
        return ${table.entityPath}List;
    }

    <#if config?? && config.queryReuse == false>
    /**
    * 查询${table.comment!}详情
    * @param query${entity}DTO
    * @return
    */
    @Override
    public ${entity}DTO query${entity}(Query${entity}DTO query${entity}DTO) {
        ${entity}DTO ${table.entityPath}DTO = ${table.entityPath}Mapper.query${entity}(query${entity}DTO);
        return ${table.entityPath}DTO;
    }
    <#else>
    /**
    * 查询${table.comment!}详情
    * @param query${entity}DTO
    * @return
    */
    @Override
    public ${entity}DTO query${entity}(Query${entity}DTO query${entity}DTO) {
        Page<${entity}DTO> page = new Page<>();
        page.setSize(GitEggConstant.Number.ONE);
        Page<${entity}DTO> ${table.entityPath}InfoPage = this.query${entity}List(page, query${entity}DTO);
        if (!CollectionUtils.isEmpty(${table.entityPath}InfoPage.getRecords()))
        {
            return ${table.entityPath}InfoPage.getRecords().get(GitEggConstant.Number.ZERO);
        }
        return null;
    }
    </#if>

    /**
    * 创建${table.comment!}
    * @param ${table.entityPath}DTO
    * @return
    */
    @Override
    public boolean create${entity}(Create${entity}DTO ${table.entityPath}DTO) {
        ${entity} ${table.entityPath}Entity = BeanCopierUtils.copyByClass(${table.entityPath}DTO, ${entity}.class);

        LambdaQueryWrapper<${entity}> lew = new LambdaQueryWrapper<>();
        lew.eq(${entity}::get${entity}Name, ${table.entityPath}Entity.get${entity}Name()).or().eq(${entity}::get${entity}Key, ${table.entityPath}Entity.get${entity}Key());
        List<${entity}> ${table.entityPath}List = list(lew);
        if (!CollectionUtils.isEmpty(${table.entityPath}List)) {
            throw new BusinessException("名称或标识已经存在");
        }

        if(null != ${table.entityPath}Entity.getParentId() && ${table.entityPath}Entity.getParentId().longValue() != GitEggConstant.PARENT_ID.longValue())
        {
            ${entity} ${table.entityPath!}Parent = this.getById(${table.entityPath}Entity.getParentId());
            String parentAncestors = ${table.comment!}Parent.getAncestors();
            ${table.entityPath}Entity.setAncestors(parentAncestors + StrUtil.COMMA + ${table.entityPath}Entity.getParentId());
        }
        else
        {
            ${table.entityPath}Entity.setAncestors(GitEggConstant.PARENT_ID.toString());
        }

        boolean result = save(${table.entityPath}Entity);
        return result;
    }

    /**
    * 更新${table.comment!}
    * @param ${table.entityPath}DTO
    * @return
    */
    @Override
    public boolean update${entity}(Update${entity}DTO ${table.entityPath}DTO) {
        ${entity} ${table.entityPath}Entity = BeanCopierUtils.copyByClass(${table.entityPath}DTO, ${entity}.class);
        LambdaQueryWrapper<${entity}> ew = new LambdaQueryWrapper<>();
        ew.ne(${entity}::getId, ${table.entityPath}Entity.getId()).and(e -> e.eq(${entity}::get${entity}Name, ${table.entityPath}Entity.get${entity}Name()).or().eq(${entity}::get${entity}Key, ${table.entityPath}Entity.get${entity}Key()));
        List<${entity}> ${table.entityPath}List = list(ew);
        if (!CollectionUtils.isEmpty(${table.entityPath}List)) {
            throw new BusinessException("名称或标识已经存在");
        }

        //判断是否修改了父级ID，如果改了，那么需要修改本级及本级下所有的ancestors字段
        ${entity} ${table.entityPath}Old = this.getById(${table.entityPath}Entity.getId());
        if (null != ${table.entityPath}Old && null != ${table.entityPath}Old.getParentId()
                && null != ${table.entityPath}Entity.getParentId() && ${table.entityPath}Old.getParentId().longValue() != ${table.entityPath}Entity.getParentId().longValue())
        {
            ${entity} ${table.entityPath}ParentNew = this.getById(${table.entityPath}Entity.getParentId());
            //新的父级组合
            String ancestorsNew = null == ${table.entityPath}ParentNew ? GitEggConstant.PARENT_ID.toString() : ${table.entityPath}ParentNew.getAncestors() + StrUtil.COMMA + ${table.entityPath}Entity.getParentId();
            //设置组织新的父级组合
            ${table.entityPath}Entity.setAncestors(ancestorsNew);
            //旧的父级组合
            String ancestorsOld = ${table.entityPath}Old.getAncestors();

            Query${entity}DTO ${table.entityPath}Parent = new Query${entity}DTO();
            ${table.entityPath}Parent.setParentId(${table.entityPath}Entity.getId());
            //只查子节点
            ${table.entityPath}Parent.setIsLeaf(GitEggConstant.Number.ONE);
            List<${entity}> ${table.entityPath}ChildrenList = ${table.entityPath}Mapper.select${entity}Children(${table.entityPath}Parent);
            if (!CollectionUtils.isEmpty(${table.entityPath}ChildrenList)) {
                ${table.entityPath}ChildrenList = ${table.entityPath}ChildrenList.stream().map(${table.entityPath} -> {${table.entityPath}.setAncestors(${table.entityPath}.getAncestors().replaceFirst(ancestorsOld, ancestorsNew)); return ${table.entityPath};}).collect(Collectors.toList());
            }
            this.updateBatchById(${table.entityPath}ChildrenList);
        }

        boolean result = updateById(${table.entityPath}Entity);
        return result;
    }

    /**
    * 删除${table.comment!}
    * @param ${table.entityPath}Id
    * @return
    */
    @Override
    public boolean delete${entity}(Long ${table.entityPath}Id) {
        boolean result = false;
        if (null == ${table.entityPath}Id)
        {
            throw new BusinessException("请选择要删除的记录");
        }
        Query${entity}DTO ${table.entityPath}Parent = new Query${entity}DTO();
        ${table.entityPath}Parent.setParentId(${table.entityPath}Id);
        List<${entity}DTO> ${table.entityPath}ChildrenList = ${table.entityPath}Mapper.select${entity}Children(${table.entityPath}Parent);
        if (!CollectionUtils.isEmpty(${table.entityPath}ChildrenList))
        {
            List<Long> ${table.entityPath}Ids = ${table.entityPath}ChildrenList.stream().map(${entity}DTO::getId).collect(Collectors.toList());
            result = removeByIds(${table.entityPath}Ids);
        }
        return result;
    }

    /**
     * 组装子父级目录
     * @param ${table.entityPath}List
     * @param ${table.entityPath}Map
     * @return
     */
    private List<${entity}DTO> assemble${entity}Tree(List<${entity}DTO> ${table.entityPath}List, Map<Long, ${entity}DTO> ${table.entityPath}Map)
    {
        List<${entity}DTO> ${table.entityPath}s = new ArrayList<>();
        for (${entity}DTO ${table.entityPath} : ${table.entityPath}List) {
            ${table.entityPath}Map.put(${table.entityPath}.getId(), ${table.entityPath});
        }
        for (${entity}DTO ${table.entityPath} : ${table.entityPath}List) {
            Long treePId = ${table.entityPath}.getParentId();
            ${entity}DTO ${table.entityPath}Tree = ${table.entityPath}Map.get(treePId);
            if (null != ${table.entityPath}Tree && !${table.entityPath}.equals(${table.entityPath}Tree)) {
                List<${entity}DTO> nodes = ${table.entityPath}Tree.getChildren();
                if (null == nodes) {
                    nodes = new ArrayList<>();
                    ${table.entityPath}Tree.setChildren(nodes);
                }
                nodes.add(${table.entityPath});
            } else {
                ${table.entityPath}s.add(${table.entityPath});
            }
        }
        return ${table.entityPath}s;
    }
}
</#if>
