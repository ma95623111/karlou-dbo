package com.karlou.dbo.injector;


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
    private Class type;
    //sql解析器 默认为Mysql解析器
    //数据库方言
    private String dataBaseId;
    //数据库
    private String dataSource;
    //数据库表名
    private String tableName;
    //所有列
    private List<AbstractFieldInfo> fields;
    //主键列
    private List<AbstractFieldInfo> keyFields;
    //是否创建索引
    private boolean isIndexes;


}
