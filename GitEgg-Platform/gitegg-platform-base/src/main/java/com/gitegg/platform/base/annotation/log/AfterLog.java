package com.gitegg.platform.base.annotation.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @ClassName: AfterLog
 * @Description: 记录后置日志
 * @author gitegg
 * @date 2019年4月27日 下午3:36:29
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface AfterLog {

    String name() default "";
}
