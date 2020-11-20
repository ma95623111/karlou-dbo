package com.karlou.dbo.test.mapper;

import com.karlou.dbo.base.BaseMapper;
import com.karlou.dbo.test.pojo.CustomDb;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author mzc
 * @mail 95623111@qq.com
 * @date 2020/9/24 18:08
 */
@Mapper
public interface TesMapper extends BaseMapper<CustomDb> {
}
