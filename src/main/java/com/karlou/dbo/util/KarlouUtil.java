package com.karlou.dbo.util;

import com.karlou.dbo.base.BaseMapper;
import com.karlou.dbo.exception.KarlouDboException;
import com.karlou.dbo.injector.SqlInjector;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author mzc
 * @mail 95623111@qq.com
 * @date 2020/9/14 09:11
 */
public class KarlouUtil {

    /**
     * 判断map是否存在数据，不存在则直接抛出异常
     *
     * @param map
     */
    public static void notNullForMap(Map map, String message) {
        if (map == null || map.size() == 0) {
            throw new KarlouDboException(message);
        }
    }

    /**
     * 判断map是否为空
     * 为空则 true；
     * 存在则 false；
     *
     * @param map
     */
    public static boolean isNullForMap(Map map) {
        if (map == null || map.size() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isBaseMapperSub(Class c) {
        return BaseMapper.class.isAssignableFrom(c);
    }

    public static boolean isBaseMapper(String s) {
        return BaseMapper.class.getName().equals(s);
    }

    /**
     * @see #isBlank(CharSequence)
     */
    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * 查询包路径下，指定父类下所有子类
     *
     * @param packages 路径
     * @param c        父类
     * @return
     */
    public static Set<Class> getPackpathClass(String packages, Class c) {
        Set<Class> res = new HashSet<>();

        String path = packages.replace(".", "/");
        URL url = Thread.currentThread().getContextClassLoader().getResource(path);
        if (url != null) {
            String protocol = url.getProtocol();
            if ("jar".equalsIgnoreCase(protocol)) {
                try {
                    res.addAll(getJarClasses(url, packages, c));
                } catch (IOException e) {
                    e.printStackTrace();
                    return res;
                }
            } else if ("file".equalsIgnoreCase(protocol)) {
                res.addAll(getFileClasses(url, packages, c));
            }
        }
        return res;
    }


    //获取file路径下的class文件
    private static Set<Class> getFileClasses(URL url, String packagePath, Class c) {
        Set<Class> res = new HashSet<>();
        String filePath = url.getFile();
        File dir = new File(filePath);
        String[] list = dir.list();
        if (list == null) return res;
        for (String classPath : list) {
            if (classPath.endsWith(".class")) {
                classPath = classPath.replace(".class", "");
                try {
                    Class<?> aClass = Class.forName(packagePath + "." + classPath);
//                    if (c.isAssignableFrom(aClass)&&!aClass.getName().equals(c.getName()))
                    if (c.isAssignableFrom(aClass) && !aClass.isInterface())
                        res.add(aClass);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                res.addAll(getPackpathClass(packagePath + "." + classPath, c));
            }
        }
        return res;
    }

    //使用JarURLConnection类获取路径下的所有类
    private static Set<Class> getJarClasses(URL url, String packagePath, Class c) throws IOException {
        Set<Class> res = new HashSet<>();
        JarURLConnection conn = (JarURLConnection) url.openConnection();
        if (conn != null) {
            JarFile jarFile = conn.getJarFile();
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String name = jarEntry.getName();
                if (name.contains(".class") && name.replaceAll("/", ".").startsWith(packagePath)) {
                    String className = name.substring(0, name.lastIndexOf(".")).replace("/", ".");
                    try {
                        Class clazz = Class.forName(className);
//                        if (c.isAssignableFrom(clazz)&&clazz.getName().equals(c.getName()))
                        if (c.isAssignableFrom(clazz) && !clazz.isInterface())
                            res.add(clazz);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return res;
    }

}
