
package com.gitegg.service.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 账号更新
 * </p>
 *
 * @author gitegg
 * @since 2019-05-26
 */
@Data
@ApiModel(value = "UpdateAccount对象", description = "更新账号时的对象")
public class UpdateAccountDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @NotNull(message="不能为空")
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "昵称")
    @Size(min=2,max=16,message="昵称长度不正确")
    private String nickname;

    @ApiModelProperty(value = "姓名")
    @Size(min=2,max=16,message="姓名长度不正确")
    private String realName;

    @ApiModelProperty(value = "1 : 男，0 : 女， 2: 未知")
    private String gender;

    @ApiModelProperty(value = "头像图片地址")
    @Pattern(regexp = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$", message="头像图片地址格式不正确")
    private String avatar;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String area;

    @ApiModelProperty(value = "用户地区ID数组")
    private List<String> areas;

    @ApiModelProperty(value = "街道详细地址")
    private String street;

    @ApiModelProperty(value = "备注")
    @Size(min=0,max=255,message="备注信息长度不正确")
    private String comments;

}
