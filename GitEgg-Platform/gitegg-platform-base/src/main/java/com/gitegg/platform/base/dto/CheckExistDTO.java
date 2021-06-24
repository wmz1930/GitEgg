
package com.gitegg.platform.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 校验某个对象字段是否存在
 * </p>
 *
 * @author gitegg
 * @since 2019-05-26
 */
@Data
@ApiModel(value = "CheckExist对象", description = "校验某个对象字段是否存在")
public class CheckExistDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "检查字段")
    private String checkField;

    @ApiModelProperty(value = "检查字段的值")
    private String checkValue;


}
