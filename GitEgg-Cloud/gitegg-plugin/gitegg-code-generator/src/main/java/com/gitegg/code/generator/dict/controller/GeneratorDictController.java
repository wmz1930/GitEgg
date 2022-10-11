package com.gitegg.code.generator.dict.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.code.generator.dict.dto.CreateGeneratorDictDTO;
import com.gitegg.code.generator.dict.dto.GeneratorDictDTO;
import com.gitegg.code.generator.dict.dto.QueryGeneratorDictDTO;
import com.gitegg.code.generator.dict.dto.UpdateGeneratorDictDTO;
import com.gitegg.code.generator.dict.entity.GeneratorDict;
import com.gitegg.code.generator.dict.service.IGeneratorDictService;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.enums.ResultCodeEnum;
import com.gitegg.platform.base.result.PageResult;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.util.BeanCopierUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统数据字典表 前端控制器
 * </p>
 *
 * @author GitEgg
 * @since 2018-10-28
 */
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "GeneratorDictController|代码生成配置字典前端控制器", tags = {"代码生成配置"})
@RefreshScope
@RequestMapping("/code/generator/dict")
public class GeneratorDictController {

    private final IGeneratorDictService generatorDictService;

    /**
     * 查询字典列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询字典列表")
    public PageResult<GeneratorDictDTO> list(QueryGeneratorDictDTO dict, Page<GeneratorDictDTO> page) {
        Page<GeneratorDictDTO> pageGeneratorDict = generatorDictService.selectGeneratorDictList(page, dict);
        PageResult<GeneratorDictDTO> pageResult = new PageResult<>(pageGeneratorDict.getTotal(), pageGeneratorDict.getRecords());
        return pageResult;
    }

    /**
     * 查询所有字典列表
     */
    @GetMapping("/list/all")
    @ApiOperation(value = "查询所有字典列表")
    public Result<?> listAll(QueryGeneratorDictDTO dict) {
        List<GeneratorDictDTO> dictDTOList = generatorDictService.selectGeneratorDictList(dict);
        return Result.data(dictDTOList);
    }

    /**
     * 查询字典里列表
     * 
     * @param parentId
     * @return
     */
    @GetMapping(value = "/tree")
    @ApiOperation(value = "查询字典树列表", notes = "查询字典树列表")
    @ApiImplicitParam(paramType = "query", name = "parentId", value = "父级ID", required = false, dataTypeClass = Long.class)
    public Result<?> queryDictTree(Long parentId) {
        List<GeneratorDict> treeList = generatorDictService.queryGeneratorDictTreeByParentId(parentId);
        return Result.data(treeList);
    }

    /**
     * 添加字典
     */
    @PostMapping("/create")
    @ApiOperation(value = "添加字典")
    public Result<GeneratorDict> create(@RequestBody CreateGeneratorDictDTO generatorDictDTO) {
        GeneratorDict generatorDictEntity = BeanCopierUtils.copyByClass(generatorDictDTO, GeneratorDict.class);
        boolean result = generatorDictService.createGeneratorDict(generatorDictEntity);
        if (result) {
            return Result.data(generatorDictEntity);
        } else {
            return Result.error("添加失败，请重试");
        }
    }

    /**
     * 修改字典
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新字典")
    public Result<GeneratorDict> update(@RequestBody UpdateGeneratorDictDTO generatorDictDTO) {
        GeneratorDict generatorDictEntity = BeanCopierUtils.copyByClass(generatorDictDTO, GeneratorDict.class);
        boolean result = generatorDictService.updateGeneratorDict(generatorDictEntity);
        if (result) {
            return Result.data(generatorDictEntity);
        } else {
            return Result.error("修改失败");
        }
    }

    /**
     * 修改数据字典状态
     */
    @PostMapping("/status/{generatorDictId}/{generatorDictStatus}")
    @ApiOperation(value = "修改数据字典状态")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "generatorDictId", value = "数据字典ID", required = true, dataTypeClass = Long.class, paramType = "path"),
        @ApiImplicitParam(name = "generatorDictStatus", value = "数据字典状态", required = true, dataTypeClass = Integer.class,
            paramType = "path")})
    public Result<?> updateStatus(@PathVariable("generatorDictId") Long generatorDictId, @PathVariable("generatorDictStatus") Integer generatorDictStatus) {
        if (null == generatorDictId || StringUtils.isEmpty(generatorDictStatus)) {
            return Result.error("ID和状态不能为空");
        }
        GeneratorDict generatorDict = new GeneratorDict();
        generatorDict.setId(generatorDictId);
        generatorDict.setDictStatus(generatorDictStatus);
        boolean result = generatorDictService.updateById(generatorDict);
        return Result.result(result);
    }

    /**
     * 删除字典
     */
    @PostMapping("/delete/{generatorDictId}")
    @ApiOperation(value = "删除字典")
    @ApiImplicitParam(paramType = "path", name = "generatorDictId", value = "字典ID", required = true, dataTypeClass = Integer.class)
    public Result<?> delete(@PathVariable("generatorDictId") Long generatorDictId) {
        boolean result = generatorDictService.deleteGeneratorDict(generatorDictId);
        if (result) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * 批量删除字典
     */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除字典")
    @ApiImplicitParam(name = "generatorDictIds", value = "字典ID列表", required = true, dataTypeClass = List.class)
    public Result<?> batchDelete(@RequestBody List<Long> generatorDictIds) {
        if (CollectionUtils.isEmpty(generatorDictIds)) {
            return Result.error("字典ID列表不能为空");
        }
        boolean result = generatorDictService.batchDeleteGeneratorDict(generatorDictIds);
        if (result) {
            return Result.success();
        } else {
            return Result.error(ResultCodeEnum.FAILED);
        }
    }

    /**
     * 查询字典里列表
     *
     * @param generatorDictCode
     * @return
     */
    @PostMapping(value = "/query/{generatorDictCode}")
    @ApiOperation(value = "查询字典列表", notes = "查询字典列表")
    @ApiImplicitParam(paramType = "query", name = "generatorDictCode", value = "字典值", required = true, dataTypeClass = String.class)
    public Result<List<GeneratorDict>> queryGeneratorDictList(@PathVariable("generatorDictCode") String generatorDictCode) {
        List<GeneratorDict> generatorDictList = generatorDictService.queryGeneratorDictListByParentCode(generatorDictCode);
        return Result.data(generatorDictList);
    }

    /**
     * 批量查询查询字典里列表
     *
     * @param generatorDictCodeList
     * @return
     */
    @PostMapping(value = "/batch/query")
    @ApiOperation(value = "批量查询字典列表", notes = "批量查询字典列表")
    @ApiImplicitParam(paramType = "query", name = "generatorDictCodeList", value = "字典值", required = true, dataTypeClass = List.class)
    public Result<Map<String, List<GeneratorDict>>> queryBatchDictList(@RequestBody List<String> generatorDictCodeList) {
        Map<String, List<GeneratorDict>> resultMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(generatorDictCodeList))
        {
            generatorDictCodeList.forEach(generatorDictCode-> {
                List<GeneratorDict> dictList = generatorDictService.queryGeneratorDictListByParentCode(generatorDictCode);
                resultMap.put(generatorDictCode, dictList);
            });
        }

        return Result.data(resultMap);
    }

    /**
     * 校验字典code是否存在
     *
     * @param generatorDictDTO
     * @return
     */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验字典code是否存在", notes = "校验字典code是否存在")
    public Result<Boolean> checkUserExist(UpdateGeneratorDictDTO generatorDictDTO) {
        QueryWrapper<GeneratorDict> generatorDictQueryWrapper = new QueryWrapper<>();
        if (null != generatorDictDTO && null != generatorDictDTO.getId()) {
            generatorDictQueryWrapper.ne("id", generatorDictDTO.getId());
        }
        generatorDictQueryWrapper.and(e -> e.eq("dict_code", generatorDictDTO.getDictCode()).eq("parent_id", generatorDictDTO.getParentId()));
        int count = generatorDictService.count(generatorDictQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count) {
            return Result.data(true);
        } else {
            return Result.data(false);
        }
    }

}
