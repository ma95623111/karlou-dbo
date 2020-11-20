package com.karlou.dbo.config;

import com.karlou.dbo.registry.KarlouMapperRegistry;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mzc
 * @mail 95623111@qq.com
 * @date 2020/9/18 11:19
 */
public class KarlouMybatisConfiguration extends Configuration {
    private final KarlouMapperRegistry mapperRegistry;

    public KarlouMybatisConfiguration() {
        super();
        this.mapperRegistry = new KarlouMapperRegistry(this);

    }

    @Override
    public MapperRegistry getMapperRegistry() {
        return this.mapperRegistry;
    }

    @Override
    public void addMappers(String packageName, Class<?> superType) {
        this.mapperRegistry.addMappers(packageName, superType);
    }

    @Override
    public void addMappers(String packageName) {
        this.mapperRegistry.addMappers(packageName);
    }

    @Override
    public <T> void addMapper(Class<T> type) {
        this.mapperRegistry.addMapper(type);
    }

    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return this.mapperRegistry.getMapper(type, sqlSession);
    }

    @Override
    public boolean hasMapper(Class<?> type) {
        return this.mapperRegistry.hasMapper(type);
    }

}
