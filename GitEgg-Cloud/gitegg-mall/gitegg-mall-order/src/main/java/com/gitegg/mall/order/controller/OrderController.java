package com.gitegg.mall.order.controller;


import java.util.List;

import com.gitegg.mall.order.dto.*;
import com.gitegg.platform.base.annotation.auth.CurrentUser;
import com.gitegg.platform.base.domain.GitEggUser;
import io.swagger.annotations.*;
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
import com.gitegg.mall.order.entity.Order;

import com.gitegg.mall.order.service.IOrderService;

import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Size;


/**
* <p>
*  前端控制器
* </p>
*
* @author GitEgg
* @since 2021-03-19
*/
@RestController
@RequestMapping("/mall/order")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "OrderController|前端控制器")
@RefreshScope
public class OrderController {

    private final IOrderService orderService;

    @PostMapping("/order")
    @ApiOperation(value = "下单")
    public Result<?> order(@Valid @Size(min = 1) @RequestBody List<OrderSkuDTO> orderSkuList
            , @ApiIgnore @CurrentUser GitEggUser user) {
        orderService.order(orderSkuList, user.getId());
        return Result.success();
    }



    /**
    * 查询列表
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询列表")
    public PageResult<OrderDTO> list(QueryOrderDTO queryOrderDTO, Page<OrderDTO> page) {
        Page<OrderDTO> pageOrder = orderService.queryOrderList(page, queryOrderDTO);
        return PageResult.data(pageOrder.getTotal(), pageOrder.getRecords());
    }

    /**
    * 查询详情
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询详情")
    public Result<?> query(QueryOrderDTO queryOrderDTO) {
        OrderDTO orderDTO = orderService.queryOrder(queryOrderDTO);
        return Result.data(orderDTO);
    }

    /**
    * 添加
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加")
    public Result<?> create(@RequestBody CreateOrderDTO order) {
        boolean result = orderService.createOrder(order);
        return Result.result(result);
    }

    /**
    * 修改
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新")
    public Result<?> update(@RequestBody UpdateOrderDTO order) {
        boolean result = orderService.updateOrder(order);
        return Result.result(result);
    }

    /**
    * 删除
    */
    @PostMapping("/delete/{orderId}")
    @ApiOperation(value = "删除")
    @ApiImplicitParam(paramType = "path", name = "orderId", value = "ID", required = true, dataType = "Long")
    public Result<?> delete(@PathVariable("orderId") Long orderId) {
        if (null == orderId) {
            return new Result<>().error("ID不能为空");
        }
        boolean result = orderService.deleteOrder(orderId);
        return Result.result(result);
    }

    /**
    * 批量删除
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除")
    @ApiImplicitParam(name = "orderIds", value = "ID列表", required = true, dataType = "List")
    public Result<?> batchDelete(@RequestBody List<Long> orderIds) {
        if (CollectionUtils.isEmpty(orderIds)) {
            return new Result<>().error("ID列表不能为空");
        }
        boolean result = orderService.batchDeleteOrder(orderIds);
        return Result.result(result);
    }

    /**
    * 修改状态
    */
    @PostMapping("/status/{orderId}/{orderStatus}")
    @ApiOperation(value = "修改状态")
    @ApiImplicitParams({
    @ApiImplicitParam(name = "orderId", value = "ID", required = true, dataType = "Long", paramType = "path"),
    @ApiImplicitParam(name = "orderStatus", value = "状态", required = true, dataType = "Integer", paramType = "path") })
    public Result<?> updateStatus(@PathVariable("orderId") Long orderId,
            @PathVariable("orderStatus") Integer orderStatus) {
        if (null == orderId || StringUtils.isEmpty(orderStatus)) {
            return Result.error("ID和状态不能为空");
        }
        UpdateOrderDTO order = new UpdateOrderDTO();
        order.setId(orderId);
        order.setStatus(orderStatus);
        boolean result = orderService.updateOrder(order);
        return Result.result(result);
    }

    /**
    * 校验是否存在
    *
    * @param order
    * @return
    */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验是否存在", notes = "校验是否存在")
    public Result<Boolean> checkOrderExist(CheckExistDTO order) {
        String field = order.getCheckField();
        String value = order.getCheckValue();
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq(field, value);
        if(null != order.getId()) {
            orderQueryWrapper.ne("id", order.getId());
        }
        int count = orderService.count(orderQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }
 }
