package com.gitegg.service.system.controller;

import com.gitegg.platform.boot.common.base.Result;
import com.gitegg.service.system.dto.SystemDTO;
import com.gitegg.service.system.service.ISystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "system")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(tags = "gitegg-system")
@RefreshScope
public class SystemController {

    private final ISystemService systemService;

    @Value("${spring.datasource.maxActive}")
    private String nacosMaxActiveType;

    @Value("${server.port}")
    private Integer serverPort;

    @GetMapping(value = "list")
    @ApiOperation(value = "system list接口")
    public Object list() {
        return systemService.list();
    }


    @GetMapping(value = "page")
    @ApiOperation(value = "system page接口")
    public Object page() {
        return systemService.page();
    }

    @GetMapping(value = "exception")
    @ApiOperation(value = "自定义异常及返回测试接口")
    public Result<String> exception() {
        return Result.data(systemService.exception());
    }

    @PostMapping(value = "valid")
    @ApiOperation(value = "参数校验测试接口")
    public Result<SystemDTO> valid(@Valid @RequestBody SystemDTO systemDTO) {
        return Result.data(systemDTO);
    }

    @PostMapping(value = "nacos")
    @ApiOperation(value = "Nacos读取配置文件测试接口")
    public Result<String> nacos() {
        return Result.data(nacosMaxActiveType);
    }

    @GetMapping(value = "api/by/id")
    @ApiOperation(value = "Fegin Get调用测试接口")
    public Result<Object> feginById(@RequestParam("id") String id) {
        return Result.data(systemService.list());
    }

    @PostMapping(value = "api/by/dto")
    @ApiOperation(value = "Fegin Post调用测试接口")
    public Result<Object> feginByDto(@Valid @RequestBody SystemDTO systemDTO) {
        return Result.data(systemDTO);
    }

    @GetMapping("/api/ribbon")
    @ApiOperation(value = "Ribbon调用测试接口")
    public Result<String> testRibbon() {
        return Result.data("现在访问的服务端口是:" + serverPort);
    }

    @ApiOperation(value = "限流测试")
    @GetMapping(value = "sentinel/protected")
    public Result<String> sentinelProtected() {
        return Result.data("访问的是限流测试接口");
    }
}
