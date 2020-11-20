package com.karlou.dbo.injector;

import com.karlou.dbo.enums.IndexesTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 数据库字段代理对象
 *
 * @author mzc
 * @mail 95623111@qq.com
 * @date 2020/9/22 17:42
 */
@Getter
@Setter
public class AbstractFieldInfo {
    //列名
    protected String fieldName;
    //列类型
    protected String fieldType;
    //java属性名
    protected String jName;
    //jJava类型
    protected String jType;
    //是否主键
    protected boolean isKey;
    //是否创建索引
    protected boolean isIndexes;
    //索引类型
    protected IndexesTypeEnum indexesTypeEnum;
    //是否不为空
    protected boolean isNotNull;
    //是否设置默认值
    protected boolean isDefault;
    //默认值
    protected Object defaultValue;
    //是否自增
    protected boolean isAuto;
    //描述
    protected String comment;

    public AbstractFieldInfo() {
    }

    public AbstractFieldInfo(String fieldName, String fieldType, String jName, String jType) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.jName = jName;
        this.jType = jType;
    }

    public AbstractFieldInfo(String fieldName, String fieldType, String jName, String jType, boolean isKey) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.jName = jName;
        this.jType = jType;
        this.isKey = isKey;
    }
}
