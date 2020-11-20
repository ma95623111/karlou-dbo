package com.karlou.dbo.handle;

import com.karlou.dbo.route.DynamicDataSourceRoute;

/**
 * 自定义数据 提供外部调用接口
 */
public class DynamicDataSourceHandle {

    private static DynamicDataSourceRoute dynamicDataSourceRoute = DynamicDataSourceRoute.getInstance();

    public static boolean setTargetDataSource(Object object, Object dataSource) {
        dynamicDataSourceRoute.setTargetDataSources(object, dataSource);
        return true;
    }

    public static void removeTargetDataSource(Object object) {
        dynamicDataSourceRoute.removeDataSourceMap(object);
    }

    public static void setDefaultDataSource(Object defaultTargetDataSource) {
        dynamicDataSourceRoute.setDefaultTargetDataSource(defaultTargetDataSource);
    }
}
