package com.gitegg.platform.boot.common.base;

import java.util.List;

import com.gitegg.platform.boot.common.enums.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: PageResult
 * @Description: 通用分页返回
 * @author GitEgg
 * @date
 * @param <T>
 */
@Data
@ApiModel("通用分页响应类")
public class PageResult<T> {

    @ApiModelProperty(value = "是否成功", required = true)
    private boolean success;

    @ApiModelProperty(value ="响应代码", required = true)
    private int code;

    @ApiModelProperty(value ="提示信息", required = true)
    private String msg;

    @ApiModelProperty(value ="总数量", required = true)
    private long count;

    @ApiModelProperty(value ="分页数据")
    private List<T> data;

    public PageResult(long total, List<T> rows) {
        this.count = total;
        this.data = rows;
        this.code = ResultCodeEnum.SUCCESS.code;
        this.msg = ResultCodeEnum.SUCCESS.msg;
    }
}
