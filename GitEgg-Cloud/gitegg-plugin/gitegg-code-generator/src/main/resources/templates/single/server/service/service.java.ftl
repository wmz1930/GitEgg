package ${package.Service};

import java.util.List;

import ${package.Entity}.${entity};
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${superServiceClassPackage};

<#assign dtoPackage="${package.Entity}"/>
<#if config.exportFlag == true>
import ${dtoPackage?replace("entity","bo")}.${entity}ExportBO;
</#if>
import ${dtoPackage?replace("entity","dto")}.${entity}DTO;
import ${dtoPackage?replace("entity","dto")}.Create${entity}DTO;
import ${dtoPackage?replace("entity","dto")}.Update${entity}DTO;
import ${dtoPackage?replace("entity","dto")}.Query${entity}DTO;
<#if config.importFlag == true>

import org.springframework.web.multipart.MultipartFile;
</#if>

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

<#if tableShowType?? && tableShowType == "tree_table">
    /**
    * 查询${table.comment!}树
    * @param query${entity}DTO
    * @return
    */
    List<${entity}DTO> query${entity}Tree(Query${entity}DTO query${entity}DTO);

</#if>
    /**
    * 分页查询${table.comment!}列表
    * @param page
    * @param query${entity}DTO
    * @return
    */
    Page<${entity}DTO> query${entity}List(Page<${entity}DTO> page, Query${entity}DTO query${entity}DTO);

    /**
    * 查询${table.comment!}列表
    * @param query${entity}DTO
    * @return
    */
    List<${entity}DTO> query${entity}List(Query${entity}DTO query${entity}DTO);

    /**
    * 查询${table.comment!}详情
    * @param query${entity}DTO
    * @return
    */
    ${entity}DTO query${entity}(Query${entity}DTO query${entity}DTO);

    /**
    * 创建${table.comment!}
    * @param ${table.entityPath}
    * @return
    */
    boolean create${entity}(Create${entity}DTO ${table.entityPath});

    /**
    * 更新${table.comment!}
    * @param ${table.entityPath}
    * @return
    */
    boolean update${entity}(Update${entity}DTO ${table.entityPath});

    /**
    * 删除${table.comment!}
    * @param ${table.entityPath}Id
    * @return
    */
    boolean delete${entity}(Long ${table.entityPath}Id);

    /**
    * 批量删除${table.comment!}
    * @param ${table.entityPath}Ids
    * @return
    */
    boolean batchDelete${entity}(List<Long> ${table.entityPath}Ids);
    <#if config.exportFlag == true>

    /**
    * 导出${table.comment!}列表
    * @param query${entity}DTO
    * @return
    */
    List<${entity}ExportBO> export${entity}List(Query${entity}DTO query${entity}DTO);
    </#if>
    <#if config.importFlag == true>

    /**
    * 导入${table.comment!}列表
    * @param file
    * @return
    */
    boolean import${entity}List(MultipartFile file);
    </#if>
}
</#if>
