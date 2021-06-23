package com.gitegg.service.system.api.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ApiSystemDTO {

    @NotNull
    @Min(value = 10, message = "id必须大于10")
    @Max(value = 150, message = "id必须小于150")
    private Long id;

    @NotNull(message = "名称不能为空")
    @Size(min = 3, max = 20, message = "名称长度必须在3-20之间")
    private String name;
    
}
