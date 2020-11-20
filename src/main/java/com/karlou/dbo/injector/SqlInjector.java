package com.karlou.dbo.injector;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.Configuration;

import java.util.List;

/**
 * @author mzc
 * @mail 95623111@qq.com
 * @date 2020/9/24 17:23
 */
public interface SqlInjector<T> {
    //---------------- baseMapper

    /**
     * 查询数据
     * 根据当前class 查询所有数据
     * 根据当前entry主键 查询当前数据
     * 根据当前class 及 自定义条件 查询数据
     * 根据当前class 及 自定义条件 分页查询数据
     * 根据当前class 分页查询数据
     */
    void selectClassAll(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, @Param("cl") Class c);

    void selectEntryKey(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass);

    void selectClassDyn(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass);

    void selectClassDynPage(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass);

    void selectClassAllPage(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass);

    /**
     * 删除数据
     * 根据当前class 删除所有数据
     * 根据当前entry主键 删除指定数据
     * 根据当前class 及 自定义条件 删除指定数据
     */
    void deleteClassAll(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass);

    void deleteEntryKey(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass);

    void deleteClassDyn(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass);

    /**
     * 更新数据
     * 根据当前entry主键 更新指定数据
     * 根据当前class 及 自定义条件 更新指定数据
     */
    void updateEntryKey(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass);

    void updateClassDyn(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass);

    /**
     * 插入数据
     * 根据entry  插入指定数据
     * 根据List<entry> 插入批量数据
     * 根据自定义数据插入当前表
     */
    void insertEntry(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass);

    void insertListEntry(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass);

    void insertEntryDyn(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass);

    void injector(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass);
}
