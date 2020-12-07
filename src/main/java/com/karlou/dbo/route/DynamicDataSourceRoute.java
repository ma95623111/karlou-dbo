package com.karlou.dbo.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public final class DynamicDataSourceRoute extends AbstractRoutingDataSource {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceRoute.class);
    //---目标数据源集合
    private final Map<Object, Object> datasourceMap = new HashMap<Object, Object>();
    private Object defaultTargetDataSource;

    private DynamicDataSourceRoute() {
        LOGGER.info("DynamicDataSourceRoute Init ");
    }

    public static DynamicDataSourceRoute getInstance() {
        return DataSourceRouteHolder.instance;
    }

    private static class DataSourceRouteHolder {
        public static DynamicDataSourceRoute instance = new DynamicDataSourceRoute();
    }

    /**
     * 获取当前执行数据源
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
//        logger.info("DynamicDataSourceRoute 当前数据源 " + getDataSource());
        return getDataSource();
    }

    /**
     * 添加执行数据源
     *
     * @param dataSource
     */
    public static void setDataSource(String dataSource) {
        contextHolder.set(dataSource);
    }

    /**
     * 获取当前数据源执行名称
     *
     * @return
     */
    public static String getDataSource() {
        return contextHolder.get();
    }

    /**
     * 清楚当前数据源
     */
    public static void clearDataSource() {
        contextHolder.remove();
    }

    /**
     * 获取目标数据源集合
     *
     * @return
     */
    public Map<Object, Object> getTargetDataSources() {
        return datasourceMap;
    }

    /**
     * 添加单个自定义数据源
     *
     * @param object
     * @param dataSource
     */
    public void setTargetDataSources(Object object, Object dataSource) {
        Assert.notNull(dataSource, "datasource must not null");
        Assert.notNull(object, "you must specify datasource alias");
        if (this.datasourceMap.get(object) != null) logger.warn(String.valueOf(object) + " exist ");
        datasourceMap.put(object, dataSource);
        setTargetDataSources();
    }

    /**
     * 删除目标数据源集合中指定数据源配置
     *
     * @param key
     */
    public void removeDataSourceMap(Object key) {
        datasourceMap.remove(key);
        setTargetDataSources();
    }

    /**
     * 设置路由基本配置
     *
     * @param defaultTargetDataSource
     * @param map
     */
    public void setDefaultDataSource(Object defaultTargetDataSource, Map<Object, Object> map) {
        this.defaultTargetDataSource = defaultTargetDataSource;
        this.datasourceMap.putAll(map);
        setDefaultTargetDataSource();
    }

    public void setDefaultDataSource(Object defaultTargetDataSource) {
        this.defaultTargetDataSource = defaultTargetDataSource;
        setDefaultTargetDataSource();
    }

    private void setDefaultTargetDataSource() {
        this.setDefaultTargetDataSource(this.defaultTargetDataSource);
        this.setTargetDataSources(this.datasourceMap);
        afterPropertiesSet();
    }

    private void setTargetDataSources() {
        this.setTargetDataSources(this.datasourceMap);
        afterPropertiesSet();
    }

}
