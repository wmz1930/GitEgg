package com.gitegg.mall.order.controller;


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
import com.gitegg.mall.order.entity.OrderSku;
import com.gitegg.mall.order.dto.OrderSkuDTO;
import com.gitegg.mall.order.dto.CreateOrderSkuDTO;
import com.gitegg.mall.order.dto.UpdateOrderSkuDTO;
import com.gitegg.mall.order.dto.QueryOrderSkuDTO;

import com.gitegg.mall.order.service.IOrderSkuService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;


/**
* <p>
*  前端控制器
* </p>
*
* @author GitEgg
* @since 2021-03-19
*/
@RestController
@RequestMapping("/mall/order/sku")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "OrderSkuController|前端控制器")
@RefreshScope
public class OrderSkuController {

    private final IOrderSkuService orderSkuService;

    /**
    * 查询列表
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询列表")
    public PageResult<OrderSkuDTO> list(QueryOrderSkuDTO queryOrderSkuDTO, Page<OrderSkuDTO> page) {
        Page<OrderSkuDTO> pageOrderSku = orderSkuService.queryOrderSkuList(page, queryOrderSkuDTO);
        return PageResult.data(pageOrderSku.getTotal(), pageOrderSku.getRecords());
    }

    /**
    * 查询详情
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询详情")
    public Result<?> query(QueryOrderSkuDTO queryOrderSkuDTO) {
        OrderSkuDTO orderSkuDTO = orderSkuService.queryOrderSku(queryOrderSkuDTO);
        return Result.data(orderSkuDTO);
    }

    /**
    * 添加
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加")
    public Result<?> create(@RequestBody CreateOrderSkuDTO orderSku) {
        boolean result = orderSkuService.createOrderSku(orderSku);
        return Result.result(result);
    }

    /**
    * 修改
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新")
    public Result<?> update(@RequestBody UpdateOrderSkuDTO orderSku) {
        boolean result = orderSkuService.updateOrderSku(orderSku);
        return Result.result(result);
    }

    /**
    * 删除
    */
    @PostMapping("/delete/{orderSkuId}")
    @ApiOperation(value = "删除")
    @ApiImplicitParam(paramType = "path", name = "orderSkuId", value = "ID", required = true, dataType = "Long")
    public Result<?> delete(@PathVariable("orderSkuId") Long orderSkuId) {
        if (null == orderSkuId) {
            return new Result<>().error("ID不能为空");
        }
        boolean result = orderSkuService.deleteOrderSku(orderSkuId);
        return Result.result(result);
    }

    /**
    * 批量删除
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除")
    @ApiImplicitParam(name = "orderSkuIds", value = "ID列表", required = true, dataType = "List")
    public Result<?> batchDelete(@RequestBody List<Long> orderSkuIds) {
        if (CollectionUtils.isEmpty(orderSkuIds)) {
            return new Result<>().error("ID列表不能为空");
        }
        boolean result = orderSkuService.batchDeleteOrderSku(orderSkuIds);
        return Result.result(result);
    }

    /**
    * 校验是否存在
    *
    * @param orderSku
    * @return
    */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验是否存在", notes = "校验是否存在")
    public Result<Boolean> checkOrderSkuExist(CheckExistDTO orderSku) {
        String field = orderSku.getCheckField();
        String value = orderSku.getCheckValue();
        QueryWrapper<OrderSku> orderSkuQueryWrapper = new QueryWrapper<>();
        orderSkuQueryWrapper.eq(field, value);
        if(null != orderSku.getId()) {
            orderSkuQueryWrapper.ne("id", orderSku.getId());
        }
        int count = orderSkuService.count(orderSkuQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }
 }
