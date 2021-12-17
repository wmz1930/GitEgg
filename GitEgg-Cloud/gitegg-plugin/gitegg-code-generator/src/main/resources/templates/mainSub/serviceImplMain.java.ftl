package ${package.ServiceImpl};

import java.util.List;

<#if config?? && config.queryReuse == true>
import com.alibaba.excel.util.CollectionUtils;
import com.gitegg.platform.base.constant.GitEggConstant;
</#if>
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
<#assign dtoPackage="${package.Entity}"/>
import ${dtoPackage?replace("entity","dto")}.${entity}DTO;
import ${dtoPackage?replace("entity","dto")}.Create${entity}DTO;
import ${dtoPackage?replace("entity","dto")}.Update${entity}DTO;
import ${dtoPackage?replace("entity","dto")}.Query${entity}DTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    * 查询${table.comment!}列表
    * @param query${entity}DTO
    * @return
    */
    @Override
    public List<${entity}DTO> query${entity}List(Query${entity}DTO query${entity}DTO) {
        List<${entity}DTO> ${table.entityPath}InfoList = ${table.entityPath}Mapper.query${entity}List(query${entity}DTO);
        return ${table.entityPath}InfoList;
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
    * @param ${table.entityPath}
    * @return
    */
    @Override
    public ${entity} create${entity}(Create${entity}DTO ${table.entityPath}) {
        ${entity} ${table.entityPath}Entity = BeanCopierUtils.copyByClass(${table.entityPath}, ${entity}.class);
        this.save(${table.entityPath}Entity);
        return ${table.entityPath}Entity;
    }

    /**
    * 更新${table.comment!}
    * @param ${table.entityPath}
    * @return
    */
    @Override
    public boolean update${entity}(Update${entity}DTO ${table.entityPath}) {
        ${entity} ${table.entityPath}Entity = BeanCopierUtils.copyByClass(${table.entityPath}, ${entity}.class);
        boolean result = this.updateById(${table.entityPath}Entity);
        return result;
    }

    /**
    * 删除${table.comment!}
    * @param ${table.entityPath}Id
    * @return
    */
    @Override
    public boolean delete${entity}(Long ${table.entityPath}Id) {
        boolean result = this.removeById(${table.entityPath}Id);
        return result;
    }

    /**
    * 批量删除${table.comment!}
    * @param ${table.entityPath}Ids
    * @return
    */
    @Override
    public boolean batchDelete${entity}(List<Long> ${table.entityPath}Ids) {
        boolean result = this.removeByIds(${table.entityPath}Ids);
        return result;
    }
}
</#if>
