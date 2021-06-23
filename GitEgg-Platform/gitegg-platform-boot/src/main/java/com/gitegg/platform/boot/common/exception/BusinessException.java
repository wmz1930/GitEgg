package com.gitegg.platform.boot.common.exception;

import com.gitegg.platform.boot.common.enums.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: BusinessException
 * @Description: 业务处理异常
 * @author GitEgg
 * @date
 */
@Getter
@Setter
public class BusinessException extends RuntimeException {

    private int code;

    private String msg;

    public BusinessException() {
        this.code = ResultCodeEnum.FAILED.code;
        this.msg = ResultCodeEnum.FAILED.msg;
    }

    public BusinessException(String message) {
        this.code = ResultCodeEnum.FAILED.code;
        this.msg = message;
    }

    public BusinessException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

}