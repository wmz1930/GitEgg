package ${package.Entity};

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
<#list table.importPackages as pkg>
    <#if !pkg?starts_with("com.baomidou.mybatisplus.annotation.") >
import ${pkg};
    </#if>
</#list>
import com.gitegg.platform.boot.excel.LocalDateTimeConverter;
<#if swagger>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#if entityLombokModel>
import lombok.Data;
import lombok.EqualsAndHashCode;
    <#if chainModel>
import lombok.experimental.Accessors;
    </#if>
</#if>

/**
 * <p>
 * ${table.comment!}
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@HeadRowHeight(20)
@ContentRowHeight(15)
<#if entityLombokModel>
@Data
    <#if superEntityClass??>
@EqualsAndHashCode(callSuper = true)
    <#else>
@EqualsAndHashCode(callSuper = false)
    </#if>
    <#if chainModel>
@Accessors(chain = true)
    </#if>
</#if>
<#if swagger>
@ApiModel(value="${entity}对象", description="${table.comment!}数据导出")
</#if>
public class ${entity}Export {

<#if entitySerialVersionUID>
    private static final long serialVersionUID = 1L;
</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#assign i=0/>
<#list fields as field>
    ${field}
<#--    <#if field?? && field.exportFlag == true>-->
<#--    <#if field?? && field.comment!?length gt 0>-->
<#--        <#if swagger>-->
<#--    @ApiModelProperty(value = "${field.comment}")-->
<#--        <#else>-->
<#--    /**-->
<#--     * ${field.comment}-->
<#--     */-->
<#--        </#if>-->
<#--    </#if>-->
<#--    @ExcelProperty(value = "${field.comment}" ,index = ${i}<#if field?? && field.propertyType == "LocalDateTime">, converter = LocalDateTimeConverter.class</#if>)-->
<#--    @ColumnWidth(20)-->
<#--    <#if field?? && field.propertyType == "LocalDateTime">@DateTimeFormat("yyyy-MM-dd HH:mm:ss")</#if>-->
<#--    private ${field.entityType} ${field.entityName};-->
<#--    <#assign i=i+1/>-->
<#--    </#if>-->
</#list>
<#------------  END 字段循环遍历  ---------->
}
