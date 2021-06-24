package com.gitegg.platform.base.result;

import com.gitegg.platform.base.enums.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

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

    /**
     * 组装数据返回
     *
     * @param count  消息
     * @param data 数据
     * @param <T>  T 响应数据
     * @
     */
    public static <T> PageResult<T> data(long count, List<T> data) {
        return new PageResult(count, data);
    }
}
