package com.karlou.dbo.base;

/**
 * @author mzc
 * @mail 95623111@qq.com
 * @date 2020/10/23 10:38
 */
public interface Constants {
    String DOT = ".";
    String NEWLINE = "\n";
    String QUOTE = "\"";
    String RIGHT_CHEV = ">";
    String HASH_LEFT_BRACE = "#{";
    String RIGHT_BRACE = "}";
    String DOLLAR_LEFT_BRACE = "${";
    String PREP = " AS ";
    String EQ = " = ";
    String CONJ = " and ";
    String NOFIELD = " 1=2 ";
    String NOEQUALS = " != ";
    String EMPTY = " null ";
    /**
     * columnMap
     */
    String COLUMN_MAP = "cm";
    /**
     * entity
     */
    String ENTITY = "et";
    /**
     * columnMap.isEmpty
     */
    String COLUMN_MAP_IS_EMPTY = COLUMN_MAP + DOT + "isEmpty";
}
