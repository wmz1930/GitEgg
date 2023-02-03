package com.gitegg.code.generator.api.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 接口配置表
 * </p>
 *
 * @author GitEgg
 * @since 2022-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="GeneratorApi对象", description="接口配置表")
public class QueryGeneratorApiDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "接口名称")
    private String apiName;

    @ApiModelProperty(value = "接口方法")
    private String apiMethod;

    @ApiModelProperty(value = "状态")
    private Integer apiStatus;

    @ApiModelProperty(value = "批量查询")
    private List<Long> generatorApiIds;

    @ApiModelProperty(value = "开始时间")
    private String beginDateTime;

    @ApiModelProperty(value = "结束时间")
    private String endDateTime;

}