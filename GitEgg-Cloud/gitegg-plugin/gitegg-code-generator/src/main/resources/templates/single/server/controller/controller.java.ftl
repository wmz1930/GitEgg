package ${package.Controller};

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
<#if config.serviceType == "cloud">
import org.springframework.cloud.context.config.annotation.RefreshScope;
</#if>
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
<#if config.importFlag == true>
import org.springframework.web.multipart.MultipartFile;
</#if>

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.dto.CheckExistDTO;
<#if config.exportFlag == true || config.importFlag == true>
import com.gitegg.platform.base.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
</#if>
import ${package.Entity}.*;
<#assign dtoPackage="${package.Entity}"/>
import ${dtoPackage?replace("entity","dto")}.*;

import ${package.Service}.${table.serviceName};

import javax.validation.Valid;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
<#if config.importFlag == true || config.exportFlag == true>

import ${dtoPackage?replace("entity","bo")}.*;
import com.alibaba.excel.EasyExcel;
import com.gitegg.platform.base.util.BeanCopierUtils;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
</#if>

<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
* <p>
* ${table.comment!} 前端控制器
* </p>
*
* @author ${author}
* @since ${date}
*/
<#if config.exportFlag == true || config.importFlag == true>
@Slf4j
</#if>
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "${table.controllerName}|${table.comment!}前端控制器", tags = {"${table.comment!}"})
<#if config.serviceType == "cloud">
@RefreshScope
</#if>
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    private final ${table.serviceName} ${table.entityPath}Service;

<#if tableShowType?? && tableShowType == "tree_table">
    /**
    * 查询${table.comment!}树
    *
    * @param query${entity}DTO
    * @return
    */
    @GetMapping(value = "/tree")
    @ApiOperation(value = "查询${table.comment!}树", notes = "树状展示${table.comment!}信息")
    public Result<List<${entity}DTO>> queryTree(Query${entity}DTO query${entity}DTO) {
    List<${entity}DTO> treeList = ${table.entityPath}Service.query${entity}Tree(query${entity}DTO);
        return Result.data(treeList);
    }

</#if>
    /**
    * 查询${table.comment!}列表
    *
    * @param query${entity}DTO
    * @param page
    * @return
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询${table.comment!}列表")
    public Result<Page<${entity}DTO>> list(Query${entity}DTO query${entity}DTO, Page<${entity}DTO> page) {
        Page<${entity}DTO> page${entity} = ${table.entityPath}Service.query${entity}List(page, query${entity}DTO);
        return Result.data(page${entity});
    }

    /**
    * 查询${table.comment!}详情
    *
    * @param query${entity}DTO
    * @return
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询${table.comment!}详情")
    public Result<?> query(Query${entity}DTO query${entity}DTO) {
        ${entity}DTO ${table.entityPath}DTO = ${table.entityPath}Service.query${entity}(query${entity}DTO);
        return Result.data(${table.entityPath}DTO);
    }

    /**
    * 添加${table.comment!}
    *
    * @param ${table.entityPath}
    * @return
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加${table.comment!}")
    public Result<?> create(@RequestBody @Valid Create${entity}DTO ${table.entityPath}) {
        boolean result = ${table.entityPath}Service.create${entity}(${table.entityPath});
        return Result.result(result);
    }

    /**
    * 修改${table.comment!}
    *
    * @param ${table.entityPath}
    * @return
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新${table.comment!}")
    public Result<?> update(@RequestBody @Valid Update${entity}DTO ${table.entityPath}) {
        boolean result = ${table.entityPath}Service.update${entity}(${table.entityPath});
        return Result.result(result);
    }

    /**
    * 删除${table.comment!}
    *
    * @param ${table.entityPath}Id
    * @return
    */
    @PostMapping("/delete/{${table.entityPath}Id}")
    @ApiOperation(value = "删除${table.comment!}")
    @ApiImplicitParam(paramType = "path", name = "${table.entityPath}Id", value = "${table.comment!}ID", required = true, dataTypeClass = Long.class)
    public Result<?> delete(@PathVariable("${table.entityPath}Id") Long ${table.entityPath}Id) {
        if (null == ${table.entityPath}Id) {
            return Result.error("ID不能为空");
        }
        boolean result = ${table.entityPath}Service.delete${entity}(${table.entityPath}Id);
        return Result.result(result);
    }

    /**
    * 批量删除${table.comment!}
    *
    * @param ${table.entityPath}Ids
    * @return
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除${table.comment!}")
    @ApiImplicitParam(name = "${table.entityPath}Ids", value = "${table.comment!}ID列表", required = true, dataTypeClass = List.class)
    public Result<?> batchDelete(@RequestBody List<Long> ${table.entityPath}Ids) {
        if (CollectionUtils.isEmpty(${table.entityPath}Ids)) {
            return Result.error("${table.comment!}ID列表不能为空");
        }
        boolean result = ${table.entityPath}Service.batchDelete${entity}(${table.entityPath}Ids);
        return Result.result(result);
    }
    <#list table.fields as field>
      <#if field.keyFlag>
         <#assign keyPropertyType="${field.propertyType}"/>
         <#assign keyPropertyName="${field.propertyName}"/>
         <#assign keyPropertySet="set${field.capitalName}"/>
      </#if>
    </#list>
<#list table.fields as field>
    <#if field?? && field.annotationColumnName?ends_with("status") && config.statusHandling == true>
        <#assign hasStatus=true/>
        <#assign statusName=field.propertyName/>
        <#assign statusType=field.propertyType/>
        <#assign statusCapitalName=field.capitalName/>
    </#if>
</#list>
<#if hasStatus?? && hasStatus == true>
     /**
     * 修改${table.comment!}状态
     *
     * @param ${table.entityPath}Id
     * @param ${statusName}
     * @return
     */
     @PostMapping("/status/{${table.entityPath}Id}/{${statusName}}")
     @ApiOperation(value = "修改${table.comment!}状态")
     @ApiImplicitParams({
     @ApiImplicitParam(name = "${table.entityPath}Id", value = "${table.comment!}ID", required = true, dataType = "${keyPropertyType}", paramType = "path"),
     @ApiImplicitParam(name = "${statusName}", value = "${table.comment!}状态", required = true, dataType = "${statusType}", paramType = "path") })
     public Result<?> updateStatus(@PathVariable("${table.entityPath}Id") ${keyPropertyType} ${table.entityPath}Id,
         @PathVariable("${statusName}") ${statusType} ${statusName}) {

         if (null == ${table.entityPath}Id || StringUtils.isEmpty(${statusName})) {
           return Result.error("ID和状态不能为空");
         }
         Update${entity}DTO ${table.entityPath} = new Update${entity}DTO();
         ${table.entityPath}.${keyPropertySet}(${table.entityPath}Id);
         ${table.entityPath}.set${statusCapitalName}(${statusName});
         boolean result = ${table.entityPath}Service.update${entity}(${table.entityPath});
         return Result.result(result);
     }
</#if>

<#list fields as field>
    <#if field?? && field.fieldUnique == true>
        <#assign checkExist=true/>
    </#if>
</#list>
<#if checkExist?? && checkExist == true>
    /**
    * 校验${table.comment!}是否存在
    *
    * @param ${table.entityPath}
    * @return
    */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验${table.comment!}是否存在", notes = "校验${table.comment!}是否存在")
    public Result<Boolean> check${entity}Exist(@RequestBody @Valid CheckExistDTO ${table.entityPath}) {
        String field = ${table.entityPath}.getCheckField();
        String value = ${table.entityPath}.getCheckValue();
        QueryWrapper<${entity}> ${table.entityPath}QueryWrapper = new QueryWrapper<>();
        ${table.entityPath}QueryWrapper.eq(field, value);
        if(null != ${table.entityPath}.getId()) {
            ${table.entityPath}QueryWrapper.ne("id", ${table.entityPath}.getId());
        }
        int count = ${table.entityPath}Service.count(${table.entityPath}QueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }
</#if>

    <#if config.exportFlag == true>
    /**
     * 批量导出${table.comment!}数据
     * @param response
     * @param query${entity}DTO
     */
    @PostMapping("/export")
    @ApiOperation("导出数据")
    public void export${entity}List(HttpServletResponse response, @RequestBody Query${entity}DTO query${entity}DTO) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = null;
        try {
            fileName = URLEncoder.encode("用户数据列表", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            List<${entity}ExportBO> ${table.entityPath}ExportList = ${table.entityPath}Service.export${entity}List(query${entity}DTO);
            String sheetName = "用户数据列表";
            EasyExcel.write(response.getOutputStream(), ${entity}ExportBO.class).sheet(sheetName).doWrite(${table.entityPath}ExportList);
        } catch (Exception e) {
            log.error("批量导出用户数据时发生错误:{}", e);
            throw new BusinessException("批量导出失败:" + e);
        }

    }
    </#if>
    <#if config.importFlag == true>

    /**
     * 批量上传${table.comment!}数据
     * @param file
     * @return
     */
    @PostMapping("/import")
    @ApiOperation("批量上传${table.comment!}数据")
    public Result<?> import${entity}List(@RequestParam("uploadFile") MultipartFile file) {
        boolean importSuccess = ${table.entityPath}Service.import${entity}List(file);
        return Result.result(importSuccess);
    }

    /**
    * 下载${table.comment!}数据导入模板
    * @param response
    * @throws IOException
    */
    @GetMapping("/download/template")
    @ApiOperation("导出上传模板")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = null;
        try {
            fileName = URLEncoder.encode("${table.comment!}数据导入模板", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            String sheetName = "${table.comment!}数据列表";
            EasyExcel.write(response.getOutputStream(), ${entity}ImportBO.class).sheet(sheetName).doWrite(new ArrayList<>());
        } catch (Exception e) {
            log.error("下载${table.comment!}批量模板时发生错误:{}", e);
            throw new BusinessException("下载批量模板失败:" + e);
        }
    }
    </#if>
 }
</#if>
