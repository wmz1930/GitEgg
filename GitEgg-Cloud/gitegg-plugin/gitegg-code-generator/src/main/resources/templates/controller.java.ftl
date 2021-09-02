package ${package.Controller};


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.result.PageResult;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.dto.CheckExistDTO;
import ${package.Entity}.${entity};
<#assign dtoPackage="${package.Entity}"/>
import ${dtoPackage?replace("entity","dto")}.${entity}DTO;
import ${dtoPackage?replace("entity","dto")}.Create${entity}DTO;
import ${dtoPackage?replace("entity","dto")}.Update${entity}DTO;
import ${dtoPackage?replace("entity","dto")}.Query${entity}DTO;

import ${package.Service}.${table.serviceName};

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

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
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")}<#else>${table.entityPath}</#if>")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "${table.controllerName}|${table.comment!}前端控制器")
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
    * 查询${table.comment!}列表
    *
    * @param query${entity}DTO
    * @param page
    * @return
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询${table.comment!}列表")
    public PageResult<${entity}DTO> list(Query${entity}DTO query${entity}DTO, Page<${entity}DTO> page) {
        Page<${entity}DTO> page${entity} = ${table.entityPath}Service.query${entity}List(page, query${entity}DTO);
        return PageResult.data(page${entity}.getTotal(), page${entity}.getRecords());
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
    @ApiImplicitParam(paramType = "path", name = "${table.entityPath}Id", value = "${table.comment!}ID", required = true, dataType = "Long")
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
    @ApiImplicitParam(name = "${table.entityPath}Ids", value = "${table.comment!}ID列表", required = true, dataType = "List")
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
     <#if field.annotationColumnName?contains("status")>
     /**
     * 修改${table.comment!}状态
     *
     * @param ${table.entityPath}Id
     * @param ${field.propertyName}
     * @return
     */
     @PostMapping("/status/{${table.entityPath}Id}/{${field.propertyName}}")
     @ApiOperation(value = "修改${table.comment!}状态")
     @ApiImplicitParams({
     @ApiImplicitParam(name = "${table.entityPath}Id", value = "${table.comment!}ID", required = true, dataType = "${keyPropertyType}", paramType = "path"),
     @ApiImplicitParam(name = "${field.propertyName}", value = "${table.comment!}状态", required = true, dataType = "${field.propertyType}", paramType = "path") })
     public Result<?> updateStatus(@PathVariable("${table.entityPath}Id") ${keyPropertyType} ${table.entityPath}Id,
         @PathVariable("${field.propertyName}") ${field.propertyType} ${field.propertyName}) {

         if (null == ${table.entityPath}Id || StringUtils.isEmpty(${field.propertyName}) {
           return Result.error("ID和状态不能为空");
         }
         Update${entity}DTO ${table.entityPath} = new Update${entity}DTO();
         ${table.entityPath}.${keyPropertySet}(${table.entityPath}Id);
         ${table.entityPath}.set${field.capitalName}(${field.propertyName});
         boolean result = ${table.entityPath}Service.update${entity}(${table.entityPath});
         return Result.result(result);
     }
     </#if>
     </#list>

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

    <#if exportFlag == 1>
    /**
    * 批量导出${table.comment!}数据
    * @param response
    * @param query${entity}DTO
    * @throws IOException
    */
    @GetMapping("/download")
    public void download(HttpServletResponse response, Query${entity}DTO query${entity}DTO) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("${table.comment!}数据列表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<${entity}DTO> ${table.entityPath}List = ${table.entityPath}Service.query${entity}DTOList(query${entity}DTO);
        List<${entity}Export> ${table.entityPath}ExportList = new ArrayList<>();
        for (${entity}DTO ${table.entityPath}DTO : ${table.entityPath}List) {
           ${entity}Export ${table.entityPath}Export = BeanCopierUtils.copyByClass(${table.entityPath}DTO, ${table.entityPath}Export.class);
           ${table.entityPath}ExportList.add(${table.entityPath}Export);
        }
        String sheetName = "${table.comment!}数据列表";
        EasyExcel.write(response.getOutputStream(), ${entity}Export.class).sheet(sheetName).doWrite(${table.entityPath}ExportList);
    }
    </#if>

    <#if importFlag == 1>
    /**
    * 批量上传${table.comment!}数据
    * @param file
    * @return
    * @throws IOException
    */
    @PostMapping("/upload")
    public Result<?> upload(@RequestParam("uploadFile") MultipartFile file) throws IOException {
    List<${entity}Import> ${table.entityPath}ImportList =  EasyExcel.read(file.getInputStream(), ${entity}Import.class, null).sheet().doReadSync();
        if (!CollectionUtils.isEmpty(${table.entityPath}ImportList))
        {
            List<${entity}> ${table.entityPath}List = new ArrayList<>();
            ${table.entityPath}ImportList.stream().forEach(${table.entityPath}Import-> {
               ${table.entityPath}ImportList.add(BeanCopierUtils.copyByClass(${table.entityPath}Import, ${entity}.class));
            });
            ${table.entityPath}Service.saveBatch(${table.entityPath}List);
        }
        return Result.success();
    }

    /**
    * 下载${table.comment!}数据导入模板
    * @param response
    * @throws IOException
    */
    @GetMapping("/download/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("${table.comment!}数据导入模板", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        String sheetName = "${table.comment!}数据列表";
        EasyExcel.write(response.getOutputStream(), ${entity}Import.class).sheet(sheetName).doWrite(null);
    }
    </#if>

 }
</#if>
