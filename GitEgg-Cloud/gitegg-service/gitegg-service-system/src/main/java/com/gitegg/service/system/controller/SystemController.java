package com.gitegg.service.system.controller;

import com.gitegg.service.system.service.ISystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "system")
@AllArgsConstructor
@Api(tags = "gitegg-system")
public class SystemController {

    private final ISystemService systemService;

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
}
