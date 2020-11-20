package com.karlou.dbo.base;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

import static com.karlou.dbo.base.Constants.*;


/**
 * @author mzc
 * @mail 95623111@qq.com
 * @date 2020/9/24 17:58
 */
public interface BaseMapper<T> {
    /**
     * 查询数据
     * 根据当前class 查询所有数据
     */
    List<T> selectAll();

    /**
     * 查询数据
     * 根据当前entry主键 查询当前数据
     */
    T selectKey(@Param("et") T t);

    /**
     * 查询数据
     * 根据当前class 及 自定义条件 查询数据
     */
    List<T> selectDyn(@Param("cm") Map<String, Object> m);

    /**
     * 删除数据
     * 根据当前class 删除所有数据
     */
    int deleteAll();

    /**
     * 删除数据
     * 根据当前entry主键 删除指定数据
     */
    int deleteKey(@Param("et") T t);

    /**
     * 删除数据
     * 根据当前class 及 自定义条件 删除指定数据
     */
    int deleteDyn(@Param("cm") Map<String, Object> m);

    /**
     * 更新数据
     * 根据当前entry主键 更新指定数据
     */
    int updateKey(@Param("et") T t);

    /**
     * 更新数据
     * 根据当前class 及 自定义条件 更新指定数据
     */
    int updateDyn(@Param("et") T t, @Param("cm") Map<String, Object> m);

    /**
     * 插入数据
     * 根据entry  插入指定数据
     */
    int insertEnt(@Param("et") T t);

    /**
     * 插入数据
     * 根据List<entry> 插入批量数据
     */
    int insertList(List list);

    /**
     * 插入数据
     * 根据自定义条件插入数据
     */
    int insertDyn(@Param("cm") Map<String, Object> m);

}
