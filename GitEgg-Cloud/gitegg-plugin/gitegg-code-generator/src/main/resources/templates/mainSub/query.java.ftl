<#assign dtoPackage="${package.Entity}"/>
package ${dtoPackage?replace("entity","dto")};

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
<#if swagger>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#if entityLombokModel>
import lombok.Data;
</#if>
import java.io.Serializable;

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
    <#if chainModel>
@Accessors(chain = true)
    </#if>
</#if>
<#if swagger>
@ApiModel(value="${entity}对象", description="${table.comment!}")
</#if>
public class Query${entity} implements Serializable {

<#if entitySerialVersionUID>
    private static final long serialVersionUID = 1L;
</#if>

    @ApiModelProperty(value = "查询条件")
    private Query${entity}DTO query${entity}DTO;

    @ApiModelProperty(value = "分页信息")
    private Page<${entity}DTO> page;

}
