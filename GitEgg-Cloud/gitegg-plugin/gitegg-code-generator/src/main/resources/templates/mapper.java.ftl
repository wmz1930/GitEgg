package ${package.Mapper};

import ${package.Entity}.${entity};
import ${superMapperClassPackage};

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

<#assign dtoPackage="${package.Entity}"/>
import ${dtoPackage?replace("entity","dto")}.${entity}DTO;
import ${dtoPackage?replace("entity","dto")}.Query${entity}DTO;

import java.util.List;

/**
 * <p>
 * ${table.comment!} Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

    /**
    * 分页查询${table.comment!}列表
    * @param page
    * @param ${table.entityPath}DTO
    * @return
    */
    Page<${entity}DTO> query${entity}List(Page<${entity}DTO> page, @Param("${table.entityPath}") Query${entity}DTO ${table.entityPath}DTO);

    /**
    * 查询${table.comment!}列表
    * @param ${table.entityPath}DTO
    * @return
    */
    List<${entity}DTO> query${entity}List(@Param("${table.entityPath}") Query${entity}DTO ${table.entityPath}DTO);

            <#if config?? && config.queryReuse == false>
    /**
    * 查询${table.comment!}信息
    * @param ${table.entityPath}DTO
    * @return
    */
    ${entity}DTO query${entity}(@Param("${table.entityPath}") Query${entity}DTO ${table.entityPath}DTO);
            </#if>
}
</#if>
