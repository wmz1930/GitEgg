package com.gitegg.service.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.enums.ResultCodeEnum;
import com.gitegg.platform.base.result.PageResult;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.service.base.dto.*;
import com.gitegg.service.base.entity.Dict;
import com.gitegg.service.base.entity.DictBusiness;
import com.gitegg.service.base.service.IDictBusinessService;
import com.gitegg.service.base.service.IDictService;
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
 * 业务数据字典表 前端控制器
 * </p>
 *
 * @author GitEgg
 * @since 2018-10-28
 */
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "DictBusinessController|业务数据字典表前端控制器", tags = {"业务数据字典配置"})
@RefreshScope
@RequestMapping("/business/dict")
public class DictBusinessController {

    private final IDictBusinessService dictBusinessService;

    /**
     * 查询字典列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询字典列表")
    public Result<Page<DictBusinessDTO>> list(QueryDictBusinessDTO dict, Page<DictBusinessDTO> page) {
        Page<DictBusinessDTO> pageDictBusiness = dictBusinessService.selectDictBusinessList(page, dict);
        return Result.data(pageDictBusiness);
    }

    /**
     * 查询所有字典列表
     */
    @GetMapping("/list/all")
    @ApiOperation(value = "查询所有字典列表")
    public Result<?> listAll(QueryDictBusinessDTO dict) {
        List<DictBusinessDTO> dictDTOList = dictBusinessService.selectDictBusinessList(dict);
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
        List<DictBusiness> treeList = dictBusinessService.queryDictBusinessTreeByParentId(parentId);
        return Result.data(treeList);
    }

    /**
     * 添加字典
     */
    @PostMapping("/create")
    @ApiOperation(value = "添加字典")
    public Result<DictBusiness> create(@RequestBody CreateDictBusinessDTO dictBusinessDTO) {
        DictBusiness dictBusinessEntity = BeanCopierUtils.copyByClass(dictBusinessDTO, DictBusiness.class);
        boolean result = dictBusinessService.createDictBusiness(dictBusinessEntity);
        if (result) {
            return Result.data(dictBusinessEntity);
        } else {
            return Result.error("添加失败，请重试");
        }
    }

    /**
     * 修改字典
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新字典")
    public Result<DictBusiness> update(@RequestBody UpdateDictBusinessDTO dictBusinessDTO) {
        DictBusiness dictBusinessEntity = BeanCopierUtils.copyByClass(dictBusinessDTO, DictBusiness.class);
        boolean result = dictBusinessService.updateDictBusiness(dictBusinessEntity);
        if (result) {
            return Result.data(dictBusinessEntity);
        } else {
            return Result.error("修改失败");
        }
    }

    /**
     * 修改数据字典状态
     */
    @PostMapping("/status/{dictBusinessId}/{dictBusinessStatus}")
    @ApiOperation(value = "修改数据字典状态")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "dictBusinessId", value = "数据字典ID", required = true, dataTypeClass = Long.class, paramType = "path"),
        @ApiImplicitParam(name = "dictBusinessStatus", value = "数据字典状态", required = true, dataTypeClass = Integer.class,
            paramType = "path")})
    public Result<?> updateStatus(@PathVariable("dictBusinessId") Long dictBusinessId, @PathVariable("dictBusinessStatus") Integer dictBusinessStatus) {
        if (null == dictBusinessId || StringUtils.isEmpty(dictBusinessStatus)) {
            return Result.error("ID和状态不能为空");
        }
        DictBusiness dictBusiness = new DictBusiness();
        dictBusiness.setId(dictBusinessId);
        dictBusiness.setDictStatus(dictBusinessStatus);
        boolean result = dictBusinessService.updateById(dictBusiness);
        return Result.result(result);
    }

    /**
     * 删除字典
     */
    @PostMapping("/delete/{dictBusinessId}")
    @ApiOperation(value = "删除字典")
    @ApiImplicitParam(paramType = "path", name = "dictBusinessId", value = "字典ID", required = true, dataTypeClass = Integer.class)
    public Result<?> delete(@PathVariable("dictBusinessId") Long dictBusinessId) {
        boolean result = dictBusinessService.deleteDictBusiness(dictBusinessId);
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
    @ApiImplicitParam(name = "dictBusinessIds", value = "字典ID列表", required = true, dataTypeClass = List.class)
    public Result<?> batchDelete(@RequestBody List<Long> dictBusinessIds) {
        if (CollectionUtils.isEmpty(dictBusinessIds)) {
            return Result.error("字典ID列表不能为空");
        }
        boolean result = dictBusinessService.batchDeleteDictBusiness(dictBusinessIds);
        if (result) {
            return Result.success();
        } else {
            return Result.error(ResultCodeEnum.FAILED);
        }
    }

    /**
     * 查询字典里列表
     *
     * @param dictBusinessCode
     * @return
     */
    @PostMapping(value = "/query/{dictBusinessCode}")
    @ApiOperation(value = "查询字典列表", notes = "查询字典列表")
    @ApiImplicitParam(paramType = "query", name = "dictBusinessCode", value = "字典值", required = true, dataTypeClass = String.class)
    public Result<List<DictBusiness>> queryDictBusinessList(@PathVariable("dictBusinessCode") String dictBusinessCode) {
        List<DictBusiness> dictBusinessList = dictBusinessService.queryDictBusinessListByParentCode(dictBusinessCode);
        return Result.data(dictBusinessList);
    }

    /**
     * 批量查询查询字典里列表
     *
     * @param dictBusinessCodeList
     * @return
     */
    @PostMapping(value = "/batch/query")
    @ApiOperation(value = "批量查询字典列表", notes = "批量查询字典列表")
    @ApiImplicitParam(paramType = "query", name = "dictBusinessCodeList", value = "字典值", required = true, dataTypeClass = List.class)
    public Result<Map<String, List<DictBusiness>>> queryBatchDictList(@RequestBody List<String> dictBusinessCodeList) {
        Map<String, List<DictBusiness>> resultMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(dictBusinessCodeList))
        {
            dictBusinessCodeList.forEach(dictBusinessCode-> {
                List<DictBusiness> dictList = dictBusinessService.queryDictBusinessListByParentCode(dictBusinessCode);
                resultMap.put(dictBusinessCode, dictList);
            });
        }

        return Result.data(resultMap);
    }

    /**
     * 校验字典code是否存在
     *
     * @param dictBusinessDTO
     * @return
     */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验字典code是否存在", notes = "校验字典code是否存在")
    public Result<Boolean> checkUserExist(UpdateDictBusinessDTO dictBusinessDTO) {
        QueryWrapper<DictBusiness> dictBusinessQueryWrapper = new QueryWrapper<>();
        if (null != dictBusinessDTO && null != dictBusinessDTO.getId()) {
            dictBusinessQueryWrapper.ne("id", dictBusinessDTO.getId());
        }
        dictBusinessQueryWrapper.and(e -> e.eq("dict_code", dictBusinessDTO.getDictCode()).eq("parent_id", dictBusinessDTO.getParentId()));
        int count = dictBusinessService.count(dictBusinessQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count) {
            return Result.data(true);
        } else {
            return Result.data(false);
        }
    }

}
