package com.karlou.dbo.annotation;


import com.karlou.dbo.injector.SqlInjector;
import com.karlou.dbo.injector.impl.MySqlInjectorImpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表声明注解，作用在类上，如作用在字段上则失效 ceshi
 *
 * @author mzc sa
 * @mail 95623111@qq.com
 * @date 2020-09-09
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableAttribute {
    /**
     * 表名
     *
     * @return
     */
    String name();

    /**
     * 数据库别名，默认为当前数据库
     *
     * @return
     */
    String datasource() default "";

    /**
     * 描述
     *
     * @return
     */
    String comment() default "";

    /**
     * 公共mapper方法注入sql生成器
     */
    Class<? extends SqlInjector> sqlInjector() default MySqlInjectorImpl.class;
}
