package com.gitegg.service.system.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.dto.CheckExistDTO;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.base.result.Result;
import com.gitegg.service.system.bo.RoleExportBO;
import com.gitegg.service.system.bo.RoleImportBO;
import com.gitegg.service.system.bo.UserExportBO;
import com.gitegg.service.system.bo.UserImportBO;
import com.gitegg.service.system.dto.*;
import com.gitegg.service.system.entity.Role;
import com.gitegg.service.system.entity.RoleResource;
import com.gitegg.service.system.service.IRoleResourceService;
import com.gitegg.service.system.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: RoleController
 * @Description: Role前端控制器
 * @author gitegg
 * @date 2018年5月18日 下午4:06:17
 */
@Slf4j
@RestController
@RequestMapping(value = "role")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "RoleController|角色相关的前端控制器", tags = {"角色配置"})
@RefreshScope
public class RoleController {

    private final IRoleService roleService;

    private final IRoleResourceService roleResourceService;

    /**
     * 查询角色列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询角色列表")
    public Result<Page<Role>> list(QueryRoleDTO role, Page<Role> page) {
        Page<Role> pageRole = roleService.selectRoleList(page, role);
        return Result.data(pageRole);
    }

    /**
     * 添加角色
     */
    @PostMapping("/create")
    @ApiOperation(value = "添加角色")
    public Result<?> create(@RequestBody CreateRoleDTO role) {
        boolean result = roleService.createRole(role);
        return Result.result(result);
    }

    /**
     * 修改角色
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新角色")
    public Result<?> update(@RequestBody @Valid UpdateRoleDTO role) {
        boolean result = roleService.updateRole(role);
        return Result.result(result);
    }

    /**
     * 删除角色
     */
    @PostMapping("/delete/{roleId}")
    @ApiOperation(value = "删除角色")
    @ApiImplicitParam(paramType = "path", name = "roleId", value = "角色ID", required = true, dataTypeClass = Long.class)
    public Result<?> delete(@PathVariable("roleId") Long roleId) {
        if (null == roleId) {
            return Result.error("ID不能为空");
        }
        boolean result = roleService.deleteRole(roleId);
        return Result.result(result);
    }

    /**
     * 批量删除角色
     */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除角色")
    @ApiImplicitParam(name = "roleIds", value = "角色ID列表", required = true, dataTypeClass = List.class)
    public Result<?> batchDelete(@RequestBody List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Result.error("角色ID列表不能为空");
        }
        boolean result = roleService.batchDeleteRole(roleIds);
        return Result.result(result);
    }

    /**
     * 修改角色状态
     */
    @PostMapping("/status/{roleId}/{roleStatus}")
    @ApiOperation(value = "修改角色状态")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataTypeClass = Long.class, paramType = "path"),
        @ApiImplicitParam(name = "roleStatus", value = "角色状态", required = true, dataTypeClass = Integer.class,
            paramType = "path")})
    public Result<?> updateStatus(@PathVariable("roleId") Long roleId,
        @PathVariable("roleStatus") Integer roleStatus) {
        if (null == roleId || StringUtils.isEmpty(roleStatus)) {
            return Result.error("ID和状态不能为空");
        }
        UpdateRoleDTO role = new UpdateRoleDTO();
        role.setId(roleId);
        role.setRoleStatus(roleStatus);
        boolean result = roleService.updateRole(role);
        return Result.result(result);
    }

    /**
     * 获取角色资源
     * 
     * @param roleId
     * @return
     */
    @GetMapping(value = "/resource/{roleId}")
    @ApiOperation(value = "获取角色的权限资源")
    @ApiImplicitParam(paramType = "path", name = "roleId", value = "角色ID", required = true, dataTypeClass = Integer.class)
    public Result<List<RoleResource>> queryRoleResource(@PathVariable("roleId") Integer roleId) {
        LambdaQueryWrapper<RoleResource> ew = new LambdaQueryWrapper<>();
        ew.eq(RoleResource::getRoleId, roleId);
        List<RoleResource> list = roleResourceService.list(ew);
        return Result.data(list);
    }

    /**
     * 修改角色资源
     * 
     * @param updateRoleResource
     * @return
     */
    @PostMapping(value = "/resource/update")
    @ApiOperation(value = "修改角色的权限资源")
    public Result<?> updateRoleResource(@RequestBody UpdateRoleResourceDTO updateRoleResource) {
        boolean result = roleResourceService.updateList(updateRoleResource);
        return Result.result(result);
    }

    /**
     * 查询所有角色列表
     * 
     * @return
     */
    @GetMapping(value = "/all")
    @ApiOperation(value = "查询所有角色列表")
    public Result<List<Role>> queryAll() {
        List<Role> result = roleService.list();
        return Result.data(result);
    }

    /**
     * 校验角色是否存在
     * 
     * @param role
     * @return
     */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验角色是否存在", notes = "校验角色是否存在")
    public Result<Boolean> checkRoleExist(@RequestBody CheckExistDTO role) {
        String field = role.getCheckField();
        String value = role.getCheckValue();
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq(field, value);
        if(null != role && null != role.getId()) {
            roleQueryWrapper.ne("id", role.getId());
        }
        int count = roleService.count(roleQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }
    
    /**
     * 下载角色导入数据模板
     * @param response
     */
    @GetMapping("/download/template")
    @ApiOperation("导出上传模板")
    public void downloadTemplate(HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = null;
        try {
            fileName = URLEncoder.encode("角色批量导入模板", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            String sheetName = "角色数据列表";
            EasyExcel.write(response.getOutputStream(), RoleImportBO.class).sheet(sheetName).doWrite(new ArrayList<>());
        } catch (Exception e) {
            log.error("下载角色批量模板时发生错误:{}", e);
            throw new BusinessException("下载批量模板失败:" + e);
        }
    }
    
    /**
     * 批量导出角色数据
     * @param response
     * @param role
     */
    @PostMapping("/export")
    @ApiOperation("导出数据")
    public void exportRoleList(HttpServletResponse response, @RequestBody QueryRoleDTO role) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = null;
        try {
            fileName = URLEncoder.encode("角色数据列表", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            List<RoleExportBO> roleExportList = roleService.exportRoleList(role);
            String sheetName = "角色数据列表";
            EasyExcel.write(response.getOutputStream(), RoleExportBO.class).sheet(sheetName).doWrite(roleExportList);
        } catch (Exception e) {
            log.error("批量导出角色数据时发生错误:{}", e);
            throw new BusinessException("批量导出失败:" + e);
        }
        
    }
    
    /**
     * 批量上传角色数据
     * @param file
     * @return
     */
    @PostMapping("/import")
    @ApiOperation("批量上传数据")
    public Result<?> importUserList(@RequestParam("uploadFile") MultipartFile file) {
        boolean importSuccess = roleService.importRoleList(file);
        return Result.result(importSuccess);
    }

}