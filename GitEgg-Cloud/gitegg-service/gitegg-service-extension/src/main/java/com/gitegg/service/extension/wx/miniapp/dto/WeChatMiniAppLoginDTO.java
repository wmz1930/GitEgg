package com.gitegg.service.extension.wx.miniapp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GitEgg
 */
@Data
@ApiModel(value = "WeChatMiniAppLoginDTO对象", description = "微信小程序登录结果")
public class WeChatMiniAppLoginDTO {
    
    @ApiModelProperty(value = "租户")
    private String openid;
    
    @ApiModelProperty(value = "租户")
    private String unionid;
    
    @ApiModelProperty(value = "是否已经获取到用户信息")
    private Boolean userInfoAlready;
    
    @ApiModelProperty(value = "是否已经绑定用户")
    private Boolean userBindAlready;
    
    @ApiModelProperty(value = "用户绑定的key, 有效期")
    private String bindKey;
}
