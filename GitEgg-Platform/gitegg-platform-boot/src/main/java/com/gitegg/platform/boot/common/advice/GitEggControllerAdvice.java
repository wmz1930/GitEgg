package com.gitegg.platform.boot.common.advice;

import com.gitegg.platform.boot.common.base.Result;
import com.gitegg.platform.boot.common.enums.ResultCodeEnum;
import com.gitegg.platform.boot.common.exception.BusinessException;
import com.gitegg.platform.boot.common.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GitEggControllerAdvice {

    /**
     * 服务名
     */
    @Value("${spring.application.name}")
    private String serverName;

    /**
     * 微服务系统标识
     */
    private String errorSystem;

    @PostConstruct
    public void init() {
        this.errorSystem = new StringBuffer()
                .append(this.serverName)
                .append(": ").toString();
    }

    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {

    }

    /**
     * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
     */
    @ModelAttribute
    public void addAttributes(Model model) {

    }

    /**
     * 全局异常捕捉处理
     */
    @ExceptionHandler(value = {Exception.class})
    public Result handlerException(Exception exception, HttpServletRequest request) {
        log.error("请求路径uri={},系统内部出现异常:{}", request.getRequestURI(), exception);
        Result result = Result.error(ResultCodeEnum.ERROR, errorSystem + exception.toString());
        return result;
    }

    /**
     * 非法请求异常
     */
    @ExceptionHandler(value = {
            HttpMediaTypeNotAcceptableException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpRequestMethodNotSupportedException.class,
            MissingServletRequestParameterException.class,
            NoHandlerFoundException.class,
            MissingPathVariableException.class,
            HttpMessageNotReadableException.class
    })
    public Result handlerSpringAOPException(Exception exception) {
        Result result = Result.error(ResultCodeEnum.ILLEGAL_REQUEST, errorSystem + exception.getMessage());
        return result;
    }

    /**
     * 非法请求异常-参数类型不匹配
     */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public Result handlerSpringAOPException(MethodArgumentTypeMismatchException exception) {
        Result result = Result.error(ResultCodeEnum.PARAM_TYPE_MISMATCH, errorSystem + exception.getMessage());
        return result;
    }

    /**
     * 非法请求-参数校验
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public Result handlerMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        //获取异常字段及对应的异常信息
        StringBuffer stringBuffer = new StringBuffer();
        methodArgumentNotValidException.getBindingResult().getFieldErrors().stream()
                .map(t -> t.getField()+"=>"+t.getDefaultMessage()+" ")
                .forEach(e -> stringBuffer.append(e));
        String errorMessage = stringBuffer.toString();
        Result result = Result.error(ResultCodeEnum.PARAM_VALID_ERROR, errorSystem + errorMessage);
        return result;
    }

    /**
     * 非法请求异常-参数校验
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public Result handlerConstraintViolationException(ConstraintViolationException constraintViolationException) {
        String errorMessage = constraintViolationException.getLocalizedMessage();
        Result result = Result.error(ResultCodeEnum.PARAM_VALID_ERROR, errorSystem + errorMessage);
        return result;
    }

    /**
     * 自定义业务异常-BusinessException
     */
    @ExceptionHandler(value = {BusinessException.class})
    public Result handlerCustomException(BusinessException exception) {
        String errorMessage = exception.getMsg();
        Result result = Result.error(exception.getCode(), errorSystem + errorMessage);
        return result;
    }

    /**
     * 自定义系统异常-SystemException
     */
    @ExceptionHandler(value = {SystemException.class})
    public Result handlerCustomException(SystemException exception) {
        String errorMessage = exception.getMsg();
        Result result = Result.error(exception.getCode(), errorSystem + errorMessage);
        return result;
    }

}
