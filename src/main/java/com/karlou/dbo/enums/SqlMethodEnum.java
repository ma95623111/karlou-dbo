package com.karlou.dbo.enums;

/**
 * @author mzc
 * @mail 95623111@qq.com
 * @date 2020/9/22 09:15
 */
public enum SqlMethodEnum {
    /**
     * 创建表
     * 1 根据class反射 创建表
     * 2 根据proxy 表代理对象 创建表
     * 3 自定义sql创建表
     */
    CREATE_TABLE_CLASS("createByClass", "根据class反射 创建表", "<script>\n\n</script>"),
    CREATE_TABLE_PROXY("CreateByProxy", "根据proxy 表代理对象 创建表", "<script>\n\n</script>"),
    CREATE_TABLE_SQL("createBySql", "自定义sql创建表", "<script>\n\n</script>"),
    /**
     * 删除表
     * 1 根据class 删除表
     * 2 根据proxy 表代理对象 删除表
     * 3 自定义sql 删除表
     */
    DROP_TABLE_CLASS("dropByClass", "根据class 删除表", "<script>\n\n</script>"),
    DROP_TABLE_PROXY("dropByProxy", "根据proxy 表代理对象 删除表", "<script>\n\n</script>"),
    DROP_TABLE_SQL("dropBySql", "自定义sql 删除表", "<script>\n\n</script>"),
    /**
     * 增加列
     * 根据tableName 及 proxy列代理对象 增加列
     */
    ALTER_ADD_PROXY("alterAddByProxy", "根据tableName 及 proxy列代理对象 增加列", "<script>\n\n</script>"),
    /**
     * 删除列
     * 根据class 及proxy列代理对象 删除列
     * 根据tableName 及 proxy列代理对象 删除列
     * 自定义sql 删除列
     */
    ALTER_DROP_PROXY("alterDropByProxy", "根据tableName 及 proxy列代理对象 删除列", "<script>\n\n</script>"),
    /**
     * 修改列
     * 根据class 及 proxy 列代理对象 修改列
     * 根据tableName 及 proxy列代理对象 修改列
     * 自定义sql 修改列
     */
    ALTER_MODIFY_PROXY("alterModifyByProxy", "根据tableName 及 proxy列代理对象 修改列", "<script>\n\n</script>"),
    /**
     * 创建索引
     * 根据class 及 proxy索引代理对象 创建索引
     * 根据tableName 及 proxy索引代理对象 创建索引
     * 自定义sql 创建索引
     */
    INDEXES_ADD_CLASS("indexesAddByClass", "根据class 及 proxy索引代理对象 创建索引", "<script>\n\n</script>"),
    INDEXES_ADD_PROXY("indexesAddByProxy", "根据tableName 及 proxy索引代理对象 创建索引", "<script>\n\n</script>"),
    INDEXES_ADD_SQL("indexesAddBySql", "自定义sql 创建索引", "<script>\n\n</script>"),
    /**
     * 删除索引
     * 根据class 及 proxy索引代理对象 删除索引
     * 根据tableName 及 proxy索引代理对象删除索引
     * 自定义sql 删除索引
     */
    INDEXES_DROP_CLASS("indexesDropByClass", "根据class 及 proxy索引代理对象 删除索引", "<script>\n\n</script>"),
    INDEXES_DROP_PROXY("indexesDropByProxy", "根据tableName 及 proxy索引代理对象删除索引", "<script>\n\n</script>"),
    INDEXES_DROP_SQL("indexesDropBySql", "自定义sql 删除索引", "<script>\n\n</script>"),
    /**
     * 自定义sql
     * 自定义查询sql
     * 自定义删除sql
     * 自定义插入sql
     * 自定义更新sql
     */
    DYN_SELECT_SQL("dynSelectSql", "自定义查询sql", "<script>\n\n</script>"),
    DYN_DELETE_SQL("dynDeleteSql", "自定义删除sql", "<script>\n\n</script>"),
    DYN_INSERT_SQL("dynInsertSql", "自定义插入sql", "<script>\n\n</script>"),
    DYN_UPDATE_SQL("dynUpdateSql", "自定义更新sql", "<script>\n\n</script>"),
    //---------------- baseMapper
    /**
     * 查询数据
     * 根据当前class 查询所有数据
     * 根据当前entry主键 查询当前数据
     * 根据当前class 及 自定义条件 查询数据
     * 根据当前class 及 自定义条件 分页查询数据
     * 根据当前class 分页查询数据
     */
    SELECT_CLASS_ALL("selectAll", "根据当前class 查询所有数据", "<script>\nSELECT %s FROM %s \n</script>"),
    SELECT_ENTRY_KEY("selectKey", "根据当前entry主键 查询当前数据", "<script>\nSELECT %s FROM %s WHERE %s\n</script>"),
    SELECT_CLASS_DYN("selectDyn", "根据当前class 及 自定义条件 查询数据", "<script>\nSELECT %s FROM %s %s\n</script>"),
    SELECT_CLASS_DYN_PAGE("selectDynPage", "根据当前class 及 自定义条件 分页查询数据", "<script>\n\n</script>"),
    SELECT_CLASS_ALL_PAGE("selectAllPage", "根据当前class 分页查询数据", "<script>\n\n</script>"),
    /**
     * 删除数据
     * 根据当前class 删除所有数据
     * 根据当前entry主键 删除指定数据
     * 根据当前class 及 自定义条件 删除指定数据
     */
    DELETE_CLASS_ALL("deleteAll", "根据当前class 删除所有数据", "<script>\nDELETE FROM %s \n</script>"),
    DELETE_ENTRY_KEY("deleteKey", "根据当前entry主键 删除指定数据", "<script>\nDELETE FROM %s WHERE %s\n</script>"),
    DELETE_CLASS_DYN("deleteDyn", "根据当前class 及 自定义条件 删除指定数据", "<script>\nDELETE FROM %s %s\n</script>"),
    /**
     * 更新数据
     * 根据当前entry主键 更新指定数据
     * 根据当前class 及 自定义条件 更新指定数据
     */
    UPDATE_ENTRY_KEY("updateKey", "根据当前entry主键 更新指定数据", "<script>\nUPDATE %s %s WHERE %s\n</script>"),
    UPDATE_CLASS_DYN("updateDyn", "根据当前class 及 自定义条件 更新指定数据", "<script>\nUPDATE %s %s %s\n</script>"),
    /**
     * 插入数据
     * 根据entry  插入指定数据
     * 根据List<entry> 插入批量数据
     * 根据自定义条件插入数据
     */
    INSERT_ENTRY("insertEnt", "根据entry  插入指定数据", "<script>\nINSERT INTO %s %s VALUES%s\n</script>"),
    INSERT_LIST_ENTRY("insertList", "根据List<entry> 插入批量数据", "<script>\nINSERT INTO %s %s VALUES%s\n</script>"),
    INSERT_ENTRY_DYN("insertDyn", "根据entry  插入指定数据", "<script>\nINSERT INTO %s %s VALUES%s\n</script>");

    SqlMethodEnum(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

    private String method;
    private String desc;
    private String sql;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
