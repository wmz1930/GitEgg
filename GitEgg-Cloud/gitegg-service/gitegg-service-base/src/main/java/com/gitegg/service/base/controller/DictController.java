package com.gitegg.service.base.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.enums.ResultCodeEnum;
import com.gitegg.platform.base.result.PageResult;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.service.base.dto.CreateDictDTO;
import com.gitegg.service.base.dto.DictDTO;
import com.gitegg.service.base.dto.QueryDictDTO;
import com.gitegg.service.base.dto.UpdateDictDTO;
import com.gitegg.service.base.entity.Dict;
import com.gitegg.service.base.service.IDictService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

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
@Api(tags = "gitegg-base")
@RefreshScope
@RequestMapping("/dict")
public class DictController {

    private final IDictService dictService;

    /**
     * 查询字典列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询字典列表")
    public PageResult<DictDTO> list(QueryDictDTO dict, Page<DictDTO> page) {
        Page<DictDTO> pageDict = dictService.selectDictList(page, dict);
        PageResult<DictDTO> pageResult = new PageResult<>(pageDict.getTotal(), pageDict.getRecords());
        return pageResult;
    }

    /**
     * 查询所有字典列表
     */
    @GetMapping("/list/all")
    @ApiOperation(value = "查询所有字典列表")
    public Result<?> listAll(QueryDictDTO dict) {
        List<DictDTO> dictDTOList = dictService.selectDictList(dict);
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
    @ApiImplicitParam(paramType = "query", name = "parentId", value = "父级ID", required = false, dataType = "Long")
    public Result<?> queryDictTree(Long parentId) {
        List<Dict> treeList = dictService.queryDictTreeByParentId(parentId);
        return Result.data(treeList);
    }

    /**
     * 添加字典
     */
    @PostMapping("/create")
    @ApiOperation(value = "添加字典")
    public Result<Dict> create(@RequestBody CreateDictDTO dictDTO) {
        Dict dictEntity = BeanCopierUtils.copyByClass(dictDTO, Dict.class);
        boolean result = dictService.createDict(dictEntity);
        if (result) {
            return Result.data(dictEntity);
        } else {
            return Result.error("添加失败，请重试");
        }
    }

    /**
     * 修改字典
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新字典")
    public Result<Dict> update(@RequestBody UpdateDictDTO dictDTO) {
        Dict dictEntity = BeanCopierUtils.copyByClass(dictDTO, Dict.class);
        boolean result = dictService.updateDict(dictEntity);
        if (result) {
            return Result.data(dictEntity);
        } else {
            return Result.error("修改失败");
        }
    }

    /**
     * 修改数据字典状态
     */
    @PostMapping("/status/{dictId}/{dictStatus}")
    @ApiOperation(value = "修改数据字典状态")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "dictId", value = "数据字典ID", required = true, dataType = "Long", paramType = "path"),
        @ApiImplicitParam(name = "dictStatus", value = "数据字典状态", required = true, dataType = "Integer",
            paramType = "path")})
    public Result<?> updateStatus(@PathVariable("dictId") Long dictId, @PathVariable("dictStatus") Integer dictStatus) {
        if (null == dictId || StringUtils.isEmpty(dictStatus)) {
            return Result.error("ID和状态不能为空");
        }
        Dict dict = new Dict();
        dict.setId(dictId);
        dict.setDictStatus(dictStatus);
        boolean result = dictService.updateById(dict);
        return Result.result(result);
    }

    /**
     * 删除字典
     */
    @PostMapping("/delete/{dictId}")
    @ApiOperation(value = "删除字典")
    @ApiImplicitParam(paramType = "path", name = "dictId", value = "字典ID", required = true, dataType = "Integer")
    public Result<?> delete(@PathVariable("dictId") Long dictId) {
        boolean result = dictService.deleteDict(dictId);
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
    @ApiImplicitParam(name = "dictIds", value = "字典ID列表", required = true, dataType = "List")
    public Result<?> batchDelete(@RequestBody List<Long> dictIds) {
        if (CollectionUtils.isEmpty(dictIds)) {
            return Result.error("字典ID列表不能为空");
        }
        boolean result = dictService.batchDeleteDict(dictIds);
        if (result) {
            return Result.success();
        } else {
            return Result.error(ResultCodeEnum.FAILED);
        }
    }

    /**
     * 查询字典里列表
     *
     * @param dictCode
     * @return
     */
    @PostMapping(value = "/query/{dictCode}")
    @ApiOperation(value = "查询字典列表", notes = "查询字典列表")
    @ApiImplicitParam(paramType = "query", name = "dictCode", value = "字典值", required = true, dataType = "String")
    public Result<List<Dict>> queryDictList(@PathVariable("dictCode") String dictCode) {
        List<Dict> dictList = dictService.queryDictListByParentCode(dictCode);
        return Result.data(dictList);
    }

    /**
     * 批量查询查询字典里列表
     *
     * @param dictCodeList
     * @return
     */
    @PostMapping(value = "/batch/query")
    @ApiOperation(value = "批量查询字典列表", notes = "批量查询字典列表")
    @ApiImplicitParam(paramType = "query", name = "dictCodeList", value = "字典值", required = true, dataType = "List")
    public Result<Map<String, List<Dict>>> queryBatchDictList(@RequestBody List<String> dictCodeList) {
        Map<String, List<Dict>> resultMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(dictCodeList))
        {
            dictCodeList.forEach(dictCode-> {
                List<Dict> dictList = dictService.queryDictListByParentCode(dictCode);
                resultMap.put(dictCode, dictList);
            });
        }

        return Result.data(resultMap);
    }

    /**
     * 校验字典code是否存在
     *
     * @param dictDTO
     * @return
     */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验字典code是否存在", notes = "校验字典code是否存在")
    public Result<Boolean> checkUserExist(UpdateDictDTO dictDTO) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        if (null != dictDTO && null != dictDTO.getId()) {
            dictQueryWrapper.ne("id", dictDTO.getId());
        }
        dictQueryWrapper.and(e -> e.eq("dict_code", dictDTO.getDictCode()).eq("parent_id", dictDTO.getParentId()));
        int count = dictService.count(dictQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count) {
            return Result.data(true);
        } else {
            return Result.data(false);
        }
    }

}
