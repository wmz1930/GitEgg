package com.gitegg.service.system.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 角色查询条件
 * </p>
 *
 * @author gitegg
 * @since 2019-10-24
 */
@Data
@ApiModel(value="Role对象", description="角色表")
public class QueryRoleDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "父id")
    private Long parentId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色标识")
    private String roleKey;

    @ApiModelProperty(value = "角色级别")
    private Integer roleLevel;

    @ApiModelProperty(value = "1有效，0禁用")
    private Integer roleStatus;

    @ApiModelProperty(value = "角色数据权限")
    private String dataPermissionType;

    @ApiModelProperty(value = "备注")
    private String comments;
    
    @ApiModelProperty(value = "角色id批量查询用户")
    private List<Long> roleIds;

}
