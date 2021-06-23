package com.gitegg.service.system.dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class SystemDTO {

    @NotNull
    @Min(value = 10, message = "id必须大于10")
    @Max(value = 150, message = "id必须小于150")
    private Long id;

    @NotNull(message = "名称不能为空")
    @Size(min = 3, max = 20, message = "名称长度必须在3-20之间")
    private String name;
}
