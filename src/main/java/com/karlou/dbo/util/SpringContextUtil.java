package com.karlou.dbo.util;


import java.util.HashMap;
import java.util.Map;

public class SpringContextUtil {

    private final static Map<Class, Object> map = new HashMap<>();

    public static Map<Class, Object> getMap() {
        return map;
    }

    public static void addMap(Map addMap) {
        map.putAll(addMap);
    }

    public static void put(Class c, Object b) {
        map.put(c, b);
    }

    /**
     * 获取对象通过Class
     *
     * @param cls
     * @return Object
     */
    public static <C> Object getBean(Class<C> cls)  {
        return map.get(cls);
    }

}
