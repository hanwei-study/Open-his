package com.hanwei.aspectj.annotation;

import com.hanwei.aspectj.enums.BusinessType;
import com.hanwei.aspectj.enums.OperatorType;
import java.lang.annotation.*;

/**
 * @author hanwei
 * @ClassName Log
 * @date 2020/10/2
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log
{
    /**
     * 模块
     */
    String title() default "";

    /**
     * 功能
     */
    BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类别
     */
    OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;
}

