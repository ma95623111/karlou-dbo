package com.karlou.dbo.annotation;

import com.karlou.dbo.enums.IndexesTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段名注解，用来表示此字段是数据库字段
 *
 * @author mzc
 * @date 2020-09-09
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldAttribute {

    /**
     * 数据库字段名
     *
     * @return
     */
    String name() default "";

    /**
     * 字段说明
     *
     * @return
     */
    String comments() default "";

    /**
     * 是否是主键
     *
     * @return
     */
    boolean iskey() default false;

    /**
     * 是否自增
     *
     * @return
     */
    boolean isautoIncr() default false;

    /**
     * 是否必填字段，默认不是必填
     *
     * @return
     */
    boolean notNull() default false;

    /**
     * 字段长度 ，仅可变长类型设置
     * String 、byte[] 类型分别对应 mysql 中 varchar、varbinary类型，需要设置长度，默认50
     *
     * @return
     */
    int length() default 0;

    /**
     * 索引类型
     *
     * @return
     */
    IndexesTypeEnum indexesType() default IndexesTypeEnum.NOTINDEXES;
}
