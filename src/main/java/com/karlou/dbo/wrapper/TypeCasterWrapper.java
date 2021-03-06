package com.karlou.dbo.wrapper;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Java类型和mysql类型间的转换，仅创建数据表时使用
 *
 * @author mzc
 * @date 2020-09-10 09:08:43
 */
public class TypeCasterWrapper {
    /**
     * varchar/varbinary类型，允许最大长度为65535，在这里限制：如果超过3000，转换为text/blob
     */
    private static final int MAX = 3000;

    /**
     * TINYTEXT 	256 bytes
     * TEXT 	65,535 bytes 	~64kb
     * MEDIUMTEXT 	 16,777,215 bytes 	~16Mb
     * LONGTEXT 	4,294,967,295 bytes 	~4Gb
     */
    private static final int TEXT_MAX = 65535;

    /**
     * decimal类型的最大长度为65，根据平时使用的需要，设置为20，足够大多数场景使用了
     */
    private static final int DECIMAL_MAX = 20;
    private static Map<String, String> map = new HashMap<>(16);

    private static final String STRING = "string";
    private static final String INT = "int";
    private static final String INTEGER = "integer";
    private static final String LONG = "long";
    private static final String DATE = "date";
    private static final String BYTE_ARRAY = "byte[]";
    private static final String FLOAT = "float";
    private static final String DOUBLE = "double";

    static {
        map.put(STRING, "varchar(200)");
        map.put(INT, "int");
        map.put(INTEGER, "int");
        map.put(LONG, "bigint");
        map.put(DATE, "datetime");
        map.put(BYTE_ARRAY, "varbinary(200)");
        map.put(FLOAT, "decimal(10,2)");
        map.put(DOUBLE, "decimal(10,2)");
    }

    /**
     * 根据Java数据类型和设置的长度，转换为MySQL的数据类型
     *
     * @param key
     * @param length
     * @return
     */
    public static String getType(String key, int length) {
        if (!StringUtils.hasLength(key)) {
            return null;
        }

        if (length <= 0) {
            return map.get(key.toLowerCase());
        }

        /*
        float/Float/double/Double类型判断设置的长度是否符合规则，如果超长，将长度设置为允许的最大长度
         */
        if (FLOAT.equalsIgnoreCase(key)
                || DOUBLE.equalsIgnoreCase(key)) {
            length = length > DECIMAL_MAX ? DECIMAL_MAX : length;
            return "decimal(" + length + ",2)";
        }

        //String 根据长度，转换为 varchar 或 text
        if (STRING.equalsIgnoreCase(key)) {
            if (length < MAX) {
                return "varchar(" + length + ")";
            }
            if (length < TEXT_MAX) {
                return "text";
            }

            return "mediumtext";


        }

        //byte[] 根据长度，转换为 varbinary 或 blob
        if (BYTE_ARRAY.equalsIgnoreCase(key)) {
            if (length < MAX) {
                return "varbinary(" + length + ")";
            }
            return "blob";
        }

        return map.get(key.toLowerCase());
    }


}
