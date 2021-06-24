package com.gitegg.service.extension.dfs.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 分布式存储配置表
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Dfs对象", description="分布式存储配置表")
public class UpdateDfsDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "分布式存储分类")
    private Long dfsType;

    @ApiModelProperty(value = "分布式存储编号")
    private String dfsCode;

    @ApiModelProperty(value = "文件访问地址前缀")
    private String accessUrlPrefix;

    @ApiModelProperty(value = "分布式存储上传地址")
    private String uploadUrl;

    @ApiModelProperty(value = "空间名称")
    private String bucket;

    @ApiModelProperty(value = "应用ID")
    private String appId;

    @ApiModelProperty(value = "区域")
    private String region;

    @ApiModelProperty(value = "accessKey")
    private String accessKey;

    @ApiModelProperty(value = "secretKey")
    private String secretKey;

    @ApiModelProperty(value = "是否默认存储 0否，1是")
    private Integer dfsDefault;

    @ApiModelProperty(value = "状态 0禁用，1 启用")
    private Integer dfsStatus;

    @ApiModelProperty(value = "状态 0私有，1公开")
    private Integer accessControl;

    @ApiModelProperty(value = "备注")
    private String comments;


}
