package com.gitegg.service.extension.dfs.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 分布式存储文件记录表
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="DfsFile对象", description="分布式存储文件记录表")
public class QueryDfsFileDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "分布式存储配置id")
    private Long dfsId;

    @ApiModelProperty(value = "文件访问地址")
    private String accessUrl;

    @ApiModelProperty(value = "原文件名")
    private String originalName;

    @ApiModelProperty(value = "存储文件名")
    private String fileName;

    @ApiModelProperty(value = "文件类型")
    private String fileExtension;

    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    @ApiModelProperty(value = "状态 0上传成功失败，1 上传成功")
    private Integer fileStatus;

    @ApiModelProperty(value = "备注")
    private String comments;


    @ApiModelProperty(value = "开始时间")
    private String beginDateTime;

    @ApiModelProperty(value = "结束时间")
    private String endDateTime;

}
