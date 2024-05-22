package com.example.common.config;

import java.lang.annotation.*;

//Java基础 注解 记录日志
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoLog {
    String value() default "";
}