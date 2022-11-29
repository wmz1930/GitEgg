package com.gitegg.service.system.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 角色信息导出
 * </p>
 *
 * @author gitegg
 * @since 2019-10-24
 */
@HeadRowHeight(20)
@ContentRowHeight(15)
@Data
@ApiModel(value="RoleExportBO对象", description="角色信息数据导出")
public class RoleExportBO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色名称")
    @ExcelProperty(value = "角色名称" ,index = 0)
    @ColumnWidth(20)
    private String roleName;

    @ApiModelProperty(value = "角色标识")
    @ExcelProperty(value = "角色标识" ,index = 1)
    @ColumnWidth(20)
    private String roleKey;

    @ApiModelProperty(value = "角色级别")
    @ExcelProperty(value = "角色级别" ,index = 2)
    @ColumnWidth(20)
    private Integer roleLevel;

    @ApiModelProperty(value = "角色状态")
    @ExcelProperty(value = "角色状态" ,index = 3)
    @ColumnWidth(20)
    private Integer roleStatus;

    @ApiModelProperty(value = "角色数据权限")
    @ExcelProperty(value = "角色数据权限" ,index = 4)
    @ColumnWidth(20)
    private String dataPermissionType;

    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注" ,index = 5)
    @ColumnWidth(20)
    private String comments;
    
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "创建时间" ,index = 6)
    @ColumnWidth(20)
    private LocalDateTime createTime;

}
