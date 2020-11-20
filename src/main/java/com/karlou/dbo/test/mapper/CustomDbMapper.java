package com.karlou.dbo.test.mapper;

import com.karlou.dbo.annotation.DataSource;
import com.karlou.dbo.test.pojo.CustomDb;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface CustomDbMapper {

    @Select("select * from karlou.k_custom_db")
    List<CustomDb> selectAll();

    @DataSource(name = "karlou")
    @Select("select * from k_custom_db")
    List<Map> selectCustAll();

    @Select("select * from karlou.k_custom_db")
//    @DataSource(name = "infocarriers")
    List<CustomDb> selectAllInfocarriers();


    @Insert({
            "<script>",
            "insert into table_name(column1, column2, column3) values ",
            "<foreach collection='testLists' item='item' index='index' separator=','>",
            "(#{item.实体属性1}, #{item.实体属性2}, #{item.实体属性3})",
            "</foreach>",
            "</script>"
    })
    void insertList(@Param(value = "testLists") List<CustomDb> testLists);
}