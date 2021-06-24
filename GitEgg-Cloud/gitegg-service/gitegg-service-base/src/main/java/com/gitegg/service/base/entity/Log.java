package com.gitegg.service.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitegg.platform.mybatis.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author GitEgg
 * @since 2018-10-24
 */
@Data
@TableName("t_sys_log")
@ApiModel(value = "Log对象", description = "Log对象")
public class Log extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "接口名称")
    @TableField("method_name")
    private String methodName;

    @ApiModelProperty(value = "入参")
    @TableField("in_params")
    private String inParams;

    @ApiModelProperty(value = "出参")
    @TableField("out_params")
    private String outParams;

    @ApiModelProperty(value = "日志类型")
    @TableField("log_type")
    private String logType;

    @ApiModelProperty(value = "操作名称")
    @TableField("operation_name")
    private String operationName;

    @ApiModelProperty(value = "操作的IP")
    @TableField("operation_ip")
    private String operationIp;

}
