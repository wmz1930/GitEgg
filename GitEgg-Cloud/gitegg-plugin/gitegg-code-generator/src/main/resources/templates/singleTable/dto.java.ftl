<#assign dtoPackage="${package.Entity}"/>
package ${dtoPackage?replace("entity","dto")};

<#list table.importPackages as pkg>
    <#if !pkg?starts_with("com.baomidou.mybatisplus.annotation.") && !pkg?starts_with("java.io.Serializable")>
import ${pkg};
    </#if>
</#list>
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
<#if tableShowType?? && tableShowType == "tree_table">

import java.util.List;

</#if>
/**
 * <p>
 * ${table.comment!}
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
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
@ApiModel(value="${entity}DTO对象", description="${table.comment!}")
</#if>
<#if superEntityClass??>
public class ${entity}DTO extends ${superEntityClass}<#if activeRecord><${entity}></#if> {
<#elseif activeRecord>
public class ${entity}DTO extends Model<${entity}> {
<#else>
public class ${entity}DTO implements Serializable {
</#if>

<#if entitySerialVersionUID>
    private static final long serialVersionUID = 1L;
</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list fields as field>
  <#if field?? && !baseEntityFieldList?seq_contains(field.fieldName)>

    <#if field.comment!?length gt 0>
        <#if swagger>
    @ApiModelProperty(value = "${field.comment}")
        <#else>
    /**
    * ${field.comment}
    */
        </#if>
    </#if>
    private ${field.entityType} ${field.entityName};
  </#if>
</#list>
<#------------  END 字段循环遍历  ---------->
<#if tableShowType?? && tableShowType == "tree_table">

    @ApiModelProperty(value = "是否是叶子节点", notes = "查询时，如果此值为 1，则表示只查询子节点")
    private Integer isLeaf;

    @ApiModelProperty(value = "子节点")
    private List<${entity}DTO> children;

</#if>
}
