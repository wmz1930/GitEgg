package ${package.Controller};


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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
import ${package.Entity}.*;
<#assign dtoPackage="${package.Entity}"/>
import ${dtoPackage?replace("entity","dto")}.*;

import ${package.Service}.${table.serviceName};
import ${mainPackagePath}.dto.Create${mainDtoName};

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
<#if config.importFlag == true || config.exportFlag == true>

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
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "${table.controllerName}|${table.comment!}前端控制器", tags = {"${table.comment!}"})
@RefreshScope
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    private final ${table.serviceName} ${table.entityPath}Service;

     /**
     * 联合主表查询${table.comment!}列表
     *
     * @param query${entity}DTO
     * @param page
     * @return
     */
     @GetMapping("/main/list")
     @ApiOperation(value = "联合主表查询${table.comment!}列表")
     public Result<Page<${entity}DTO> mainList(Query${entity}DTO query${entity}DTO, Page<${entity}DTO> page) {
         Page<${entity}DTO> page${entity} = ${table.entityPath}Service.query${mainEntityName}${entity}List(page, query${entity}DTO);
         return Result.data(page${entity});
     }

     /**
     * 联合主表查询${table.comment!}详情
     *
     * @param query${entity}DTO
     * @return
     */
     @GetMapping("/main/query")
     @ApiOperation(value = "联合主表查询${table.comment!}详情")
     public Result<?> mainQuery(Query${entity}DTO query${entity}DTO) {
         ${entity}DTO ${table.entityPath}DTO = ${table.entityPath}Service.query${mainEntityName}${entity}(query${entity}DTO);
         return Result.data(${table.entityPath}DTO);
     }


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
    public Result<?> create(@RequestBody Create${entity}DTO ${table.entityPath}) {
        ${entity} result = ${table.entityPath}Service.create${entity}(${table.entityPath});
        return Result.data(result);
    }

    /**
    * 修改${table.comment!}
    *
    * @param ${table.entityPath}
    * @return
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新${table.comment!}")
    public Result<?> update(@RequestBody Update${entity}DTO ${table.entityPath}) {
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
    public Result<Boolean> check${entity}Exist(CheckExistDTO ${table.entityPath}) {
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
    * @throws IOException
    */
    @GetMapping("/download")
    @ApiOperation("导出数据")
    public void download(HttpServletResponse response, Query${entity}DTO query${entity}DTO) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("${table.comment!}数据列表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<${entity}DTO> ${table.entityPath}List = ${table.entityPath}Service.query${mainEntityName}${entity}List(query${entity}DTO);
        List<${entity}Export> ${table.entityPath}ExportList = new ArrayList<>();
        for (${entity}DTO ${table.entityPath}DTO : ${table.entityPath}List) {
           ${entity}Export ${table.entityPath}Export = BeanCopierUtils.copyByClass(${table.entityPath}DTO, ${entity}Export.class);
           ${table.entityPath}Export = BeanCopierUtils.copyByObject(${table.entityPath}DTO.get${mainEntityName}DTO(), ${table.entityPath}Export);
           ${table.entityPath}ExportList.add(${table.entityPath}Export);
        }
        String sheetName = "${table.comment!}数据列表";
        EasyExcel.write(response.getOutputStream(), ${entity}Export.class).sheet(sheetName).doWrite(${table.entityPath}ExportList);
    }
    </#if>
    <#if config.importFlag == true>

    /**
    * 批量上传${table.comment!}数据
    * @param file
    * @return
    * @throws IOException
    */
    @PostMapping("/upload")
    @ApiOperation("批量上传数据")
    public Result<?> upload(@RequestParam("uploadFile") MultipartFile file) throws IOException {
    List<${entity}Import> ${table.entityPath}ImportList =  EasyExcel.read(file.getInputStream(), ${entity}Import.class, null).sheet().doReadSync();
        if (!CollectionUtils.isEmpty(${table.entityPath}ImportList))
        {
            List<Create${entity}DTO> create${entity}DTOList = new ArrayList<>();
            ${table.entityPath}ImportList.stream().forEach(${table.entityPath}Import-> {
               Create${entity}DTO create${entity}DTO = BeanCopierUtils.copyByClass(${table.entityPath}Import, Create${entity}DTO.class);
               Create${mainEntityName}DTO create${mainEntityName}DTO = BeanCopierUtils.copyByClass(${table.entityPath}Import, Create${mainEntityName}DTO.class);
               create${entity}DTO.set${mainEntityName}DTO(create${mainEntityName}DTO);
               create${entity}DTOList.add(create${entity}DTO);
            });
            ${table.entityPath}Service.create${entity}Batch(create${entity}DTOList);
        }
        return Result.success();
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
        String fileName = URLEncoder.encode("${table.comment!}数据导入模板", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        String sheetName = "${table.comment!}数据列表";
        EasyExcel.write(response.getOutputStream(), ${entity}Import.class).sheet(sheetName).doWrite(new ArrayList<>());
    }
    </#if>
 }
</#if>
