<#assign dtoPackage="${package.Entity}"/>
package ${dtoPackage?replace("entity","dto")};

<#list table.importPackages as pkg>
    <#if !pkg?starts_with("com.baomidou.mybatisplus.annotation.") && !pkg?starts_with("java.io.Serializable") >
import ${pkg};
    </#if>
</#list>
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Pattern;
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
@ApiModel(value="${entity}对象", description="${table.comment!}")
</#if>
<#if superEntityClass??>
public class Update${entity}DTO extends ${superEntityClass}<#if activeRecord><${entity}></#if> {
<#elseif activeRecord>
public class Update${entity}DTO extends Model<${entity}> {
<#else>
public class Update${entity}DTO implements Serializable {
</#if>

<#if entitySerialVersionUID>
    private static final long serialVersionUID = 1L;
</#if>
<#--找出主键-->
<#list table.fields as field>
<#if field.keyFlag>
    <#assign keyPropertyName="${field.propertyName}"/>
    <#assign keyPropertyType="${field.propertyType}"/>
    <#assign keyPropertyComment="${field.comment}"/>
</#if>
</#list>
<#--判断是否存在主键-->
<#list fields as field>
<#if field?? && field.formEdit == true && field.entityName == keyPropertyName>
    <#assign hasKeyFlag=true/>
</#if>
</#list>
<#if !hasKeyFlag??>

    @ApiModelProperty(value = "${keyPropertyComment}")
    private ${keyPropertyType} ${keyPropertyName};
</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list fields as field>
    <#if field?? && field.formEdit == true && (config.tableType != " join" || (config.tableType == "join" && field.joinId == 0))>

    <#if field.comment!?length gt 0>
        <#if swagger>
    @ApiModelProperty(value = "${field.comment}")
        <#else>
    /**
     * ${field.comment}
     */
        </#if>
    </#if>
    <#if field.required == true>
    @NotBlank(message="${field.comment}不能为空")
    </#if>
    <#if field.min??>
    @Min(${field.min}L)
    </#if>
    <#if field.max??>
    @Max(${field.max}L)
    </#if>
    <#if field.maxLength??>
    @Length(min=${field.minLength},max=${field.maxLength})
    </#if>
    <#if field.validateValue??>
    @Pattern(regexp="${field.validateValue}",message="${field.comment}格式错误")
    </#if>
    <#if field.validateRegular??>
    @Pattern(regexp="${field.validateRegular}",message="${field.comment}格式错误")
    </#if>
    private ${field.entityType} ${field.entityName};
    </#if>
</#list>
<#------------  END 字段循环遍历  ---------->
}
