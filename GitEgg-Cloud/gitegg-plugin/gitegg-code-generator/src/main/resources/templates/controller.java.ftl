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
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
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
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询${table.comment!}列表")
    public PageResult<${entity}DTO> list(Query${entity}DTO query${entity}DTO, Page<${entity}DTO> page) {
        Page<${entity}DTO> page${entity} = ${table.entityPath}Service.query${entity}List(page, query${entity}DTO);
        return PageResult.data(page${entity}.getTotal(), page${entity}.getRecords());
    }

    /**
    * 查询${table.comment!}详情
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询${table.comment!}详情")
    public Result<?> query(Query${entity}DTO query${entity}DTO) {
        ${entity}DTO ${table.entityPath}DTO = ${table.entityPath}Service.query${entity}(query${entity}DTO);
        return Result.data(${table.entityPath}DTO);
    }

    /**
    * 添加${table.comment!}
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加${table.comment!}")
    public Result<?> create(@RequestBody Create${entity}DTO ${table.entityPath}) {
        boolean result = ${table.entityPath}Service.create${entity}(${table.entityPath});
        return Result.result(result);
    }

    /**
    * 修改${table.comment!}
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新${table.comment!}")
    public Result<?> update(@RequestBody Update${entity}DTO ${table.entityPath}) {
        boolean result = ${table.entityPath}Service.update${entity}(${table.entityPath});
        return Result.result(result);
    }

    /**
    * 删除${table.comment!}
    */
    @PostMapping("/delete/{${table.entityPath}Id}")
    @ApiOperation(value = "删除${table.comment!}")
    @ApiImplicitParam(paramType = "path", name = "${table.entityPath}Id", value = "${table.comment!}ID", required = true, dataType = "Long")
    public Result<?> delete(@PathVariable("${table.entityPath}Id") Long ${table.entityPath}Id) {
        if (null == ${table.entityPath}Id) {
            return new Result<>().error("ID不能为空");
        }
        boolean result = ${table.entityPath}Service.delete${entity}(${table.entityPath}Id);
        return Result.result(result);
    }

    /**
    * 批量删除${table.comment!}
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除${table.comment!}")
    @ApiImplicitParam(name = "${table.entityPath}Ids", value = "${table.comment!}ID列表", required = true, dataType = "List")
    public Result<?> batchDelete(@RequestBody List<Long> ${table.entityPath}Ids) {
        if (CollectionUtils.isEmpty(${table.entityPath}Ids)) {
            return new Result<>().error("${table.comment!}ID列表不能为空");
        }
        boolean result = ${table.entityPath}Service.batchDelete${entity}(${table.entityPath}Ids);
        return Result.result(result);
    }

    /**
    * 修改${table.comment!}状态
    */
    @PostMapping("/status/{${table.entityPath}Id}/{${table.entityPath}Status}")
    @ApiOperation(value = "修改${table.comment!}状态")
    @ApiImplicitParams({
    @ApiImplicitParam(name = "${table.entityPath}Id", value = "${table.comment!}ID", required = true, dataType = "Long", paramType = "path"),
    @ApiImplicitParam(name = "${table.entityPath}Status", value = "${table.comment!}状态", required = true, dataType = "Integer", paramType = "path") })
    public Result<?> updateStatus(@PathVariable("${table.entityPath}Id") Long ${table.entityPath}Id,
            @PathVariable("${table.entityPath}Status") Integer ${table.entityPath}Status) {
        if (null == ${table.entityPath}Id || StringUtils.isEmpty(${table.entityPath}Status)) {
            return Result.error("ID和状态不能为空");
        }
        Update${entity}DTO ${table.entityPath} = new Update${entity}DTO();
        ${table.entityPath}.setId(${table.entityPath}Id);
        ${table.entityPath}.set${entity}Status(${table.entityPath}Status);
        boolean result = ${table.entityPath}Service.update${entity}(${table.entityPath});
        return Result.result(result);
    }

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
 }
</#if>
