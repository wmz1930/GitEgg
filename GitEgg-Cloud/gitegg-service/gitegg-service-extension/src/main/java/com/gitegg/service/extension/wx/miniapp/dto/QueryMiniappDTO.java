package com.gitegg.service.extension.wx.miniapp.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 微信小程序配置
 * </p>
 *
 * @author GitEgg
 * @since 2023-07-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Miniapp对象", description="微信小程序配置")
public class QueryMiniappDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "小程序名称")
    private String miniappName;

    @ApiModelProperty(value = "appid")
    private String appid;

    @ApiModelProperty(value = "secret")
    private String secret;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "批量查询")
    private List<Long> miniappIds;

    @ApiModelProperty(value = "开始时间")
    private String beginDateTime;

    @ApiModelProperty(value = "结束时间")
    private String endDateTime;

}