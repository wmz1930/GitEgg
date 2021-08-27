package com.gitegg.code.generator.datasource.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据源配置表
 * </p>
 *
 * @author GitEgg
 * @since 2021-08-18 16:39:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="CodeGenerationDatasource对象", description="数据源配置表")
public class QueryCodeGenerationDatasourceDTO extends BaseEntity {


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "数据源名称")
    private String datasourceName;

    @ApiModelProperty(value = "连接地址")
    private String url;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "数据库驱动")
    private String driver;

    @ApiModelProperty(value = "数据库类型")
    private String dbType;

    @ApiModelProperty(value = "备注")
    private String comments;

    @ApiModelProperty(value = "开始时间")
    private String beginDateTime;

    @ApiModelProperty(value = "结束时间")
    private String endDateTime;

}
