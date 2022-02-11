package com.gitegg.gateway.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author GitEgg
 */
@Data
@ApiModel(value = "GatewayLog对象", description = "GatewayLog对象")
public class GatewayApiLog {

    @ApiModelProperty(value = "请求地址")
    private String requestUri;

    @ApiModelProperty(value = "GET请求查询参数")
    private String queryParams;

    @ApiModelProperty(value = "请求参数")
    private String requestBody;

    @ApiModelProperty(value = "返回码")
    private String responseCode;

    @ApiModelProperty(value = "返回参数")
    private String responseBody;

    @ApiModelProperty(value = "开始时间")
    private Long startTime;

    @ApiModelProperty(value = "结束时间")
    private Long endTime;

    @ApiModelProperty(value = "耗费时长")
    private Long duration;

    /**
     * HTTP OR HTTPS
     */
    @ApiModelProperty(value = "scheme")
    private String scheme;

    /**
     * POST OR GET
     */
    @ApiModelProperty(value = "method")
    private String method;

    @ApiModelProperty(value = "客户端HOST")
    private String clientHost;

    @ApiModelProperty(value = "客户端IP")
    private String clientIp;

}
