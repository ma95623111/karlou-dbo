package com.karlou.dbo.injector;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 数据库转换代理对象
 *
 * @author mzc
 * @mail 95623111@qq.com
 * @date 2020/9/22 17:34
 */
public abstract class AbstractTableInfo {
    //实体类
    @Getter
    private Class type;
    //sql解析器 默认为Mysql解析器
    //数据库方言
    @Getter
    @Setter
    private String dataBaseId;
    //数据库
    @Getter
    @Setter
    private String dataSource;
    //数据库表名
    @Getter
    @Setter
    private String tableName;
    //所有列
    @Getter
    @Setter
    private List<AbstractFieldInfo> fields;
    //主键列
    @Getter
    @Setter
    private List<AbstractFieldInfo> keyFields;
    //是否创建索引
    @Getter
    @Setter
    private boolean isIndexes;


}
