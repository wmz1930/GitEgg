package com.gitegg.service.system.controller;

import com.gitegg.service.system.service.ISystemService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "system")
@AllArgsConstructor
public class SystemController {

    private final ISystemService systemService;

    @GetMapping(value = "list")
    public Object list() {
        return systemService.list();
    }


    @GetMapping(value = "page")
    public Object page() {
        return systemService.page();
    }
}
